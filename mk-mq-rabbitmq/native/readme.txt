RabbitMQ使用原生客户端
1.增加依赖
    <dependency>
        <groupId>com.rabbitmq</groupId>
        <artifactId>amqp-client</artifactId>
        <version>5.1.2</version>
    </dependency>
2.运行RabbitMQ
3.最简单的入门例子
    Send:消息发送者
    Recv:消息接受者
    代码：详见包com.suns.studyfromgw，是从官网复制的例子：https://www.rabbitmq.com/tutorials/tutorial-one-java.html

4.RabbitMQ的Exchange有四种类型
    共有四种direct,fanout,topic,headers，其种headers(几乎和direct一样)不实用，可以忽略
    4.1Direct
        路由键完全匹配，消息被投递到对应的队列，每个amqp的实现都必须有一个direct交换器，包含一个空白字符串名称的默认交换器。声明一个队列时，会自动绑定到默认交换器，并且以队列名称作为路由键：channel->basic_public($msg,’ ’,’queue-name’)
        代码：详见包com.suns.exchange.direct
            DirectProducer：direct类型交换器的生产者
            NormalConsumer：普通的消费者
            MulitBindConsumer：队列绑定到交换器上时，是允许绑定多个路由键的，也就是多重绑定
            MulitChannelConsumer：一个连接下允许有多个信道
            MulitConsumerOneQueue：一个队列多个消费者，则会表现出消息在消费者之间的轮询发送
    4.2Fanout
        消息广播到绑定的队列
        代码：详见com.suns.exchange.fanout
            FanoutProducer:生产者,1.设置exchange的类型为BuiltinExchangeType.FANOUT,2.设置三种路由键error,info,warning
            Consumer1:消费者,1.设置exchange的类型为BuiltinExchangeType.FANOUT,2.设置三种路由键error,info,warning
            Consumer2:消费者,1.设置exchange的类型为BuiltinExchangeType.FANOUT,2.设置一种路由键error（或者一个不存在的路由键）
            结果：生产者发送三种路由键error,info,warning，每种一条消息，Consumer1和Consumer2 都能受到
    4.3Topic
        通配符*和# 与ActiveMQ的通配符订阅类似
        "*" 用于匹配路径上的任何名字    "#" 用于递归地匹配任何字符
        代码：详见com.suns.exchange.topic

4.RabbitMQ,消息发布时的权衡
    （性能）快，（消息可靠性）低<-------------------------------------------------------------------------->（性能）慢，（消息可靠性）高
    无保障，失败通知，发布者确认，备用交换器，高可用队列，事务，事务+高可用队列，消息的持久化
    4.1无保障
    4.2失败通知
        代码：详见com.suns.exchange.mandatory
        生产者：MandatoryProducer中增加
            //失败通知
            channel.addReturnListener(new ReturnListener() {
                @Override
                public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body,"UTF-8");
                    System.out.println("replyCode["+replyCode+"],replyText["+replyText+"],exchange["+exchange+"],routingKey["+routingKey+"],body["+msg+"]");
                }
            });
            //设置mandatory为true
            channel.basicPublish(exchange_name,routKey,true,null,message.getBytes());
            System.out.println("send 路由键["+routKey+"] msg:"+message);
            Thread.sleep(200);//休眠，为了有足够时间监听channel.addReturnListener
        消费者：MandatoryConsume不需要改动
    4.3事务
        事务的实现主要是对信道（Channel）的设置，主要的方法有三个：
        1.	channel.txSelect()声明启动事务模式；
        2.	channel.txComment()提交事务；
        3.	channel.txRollback()回滚事务；
        在发送消息之前，需要声明channel为事务模式，提交或者回滚事务即可。
        开启事务后，客户端和RabbitMQ之间的通讯交互流程：
            客户端发送给服务器Tx.Select(开启事务模式)
            服务器端返回Tx.Select-Ok（开启事务模式ok）
            推送消息
            客户端发送给事务提交Tx.Commit
            服务器端返回Tx.Commit-Ok
        以上就完成了事务的交互流程，如果其中任意一个环节出现问题，就会抛出IoException移除，这样用户就可以拦截异常进行事务回滚，或决定要不要重复消息。
        那么，既然已经有事务了，为何还要使用发送方确认模式呢，原因是因为事务的性能是非常差的。根据相关资料，事务会降低2~10倍的性能
        代码：详见com.suns.transaction
    4.4发布者确认
        启用消息确认模式：channel.confirmSelect();
        Confirm的三种实现方式：
        方式一：channel.waitForConfirms()普通发送方确认模式；消息到达交换器，就会返回true。
        方式二：channel.waitForConfirmsOrDie()批量确认模式；使用同步方式等所有的消息发送之后才会执行后面代码，只要有一个消息未到达交换器就会抛出IOException异常。
        方式三：channel.addConfirmListener()异步监听发送方确认模式；
        代码：详见com.suns.producerconfirm
    4.5备用交换器
        在第一次声明交换器时被指定，用来提供一种预先存在的交换器，如果主交换器无法路由消息，那么消息将被路由到这个新的备用交换器。
        如果发布消息时同时设置了mandatory会发生什么？如果主交换器无法路由消息，RabbitMQ并不会通知发布者，因为，向备用交换器发送消息，表示消息已经被路由了。注意，新的备用交换器就是普通的交换器，没有任何特殊的地方。
        使用备用交换器，向往常一样，声明Queue和备用交换器，把Queue绑定到备用交换器上。然后在声明主交换器时，通过交换器的参数，alternate-exchange,，将备用交换器设置给主交换器。
        建议备用交换器设置为faout类型，Queue绑定时的路由键设置为“#”
        代码：详见com.suns.backupexchange

5.RabbitMQ,消息消费时的权衡
    5.1消息的获得方式
        5.1.1推送consume(之前的代码都是用这个方式)，如com.suns.exchange.direct
        5.1.2拉取Get
            属于一种轮询模型，发送一次get请求，获得一个消息。如果此时RabbitMQ中没有消息，会获得一个表示空的回复。总的来说，这种方式性能比较差，很明显，每获得一条消息，都要和RabbitMQ进行网络通信发出请求。而且对RabbitMQ来说，RabbitMQ无法进行任何优化，因为它永远不知道应用程序何时会发出请求。对我们实现者来说，要在一个循环里，不断去服务器get消息
            生产者：不变
            消费者：由原来的new DefaultConsumer()及channel.basicConsume()，改为channel.basicGet()
            代码：详见com.suns.getmessage
    5.2消息的应答
        前面说过，消费者收到的每一条消息都必须进行确认。消息确认后，RabbitMQ才会从队列删除这条消息，RabbitMQ不会为未确认的消息设置超时时间，它判断此消息是否需要重新投递给消费者的唯一依据是消费该消息的消费者连接是否已经断开。这么设计的原因是RabbitMQ允许消费者消费一条消息的时间可以很久很久
        5.2.1自动确认及手动确认
        自动确认：消费者在声明队列时，可以指定autoAck参数，当autoAck=true时，一旦消费者接收到了消息，就视为自动确认了消息。如果消费者在处理消息的过程中，出了错，就没有什么办法重新处理这条消息，所以我们很多时候，需要在消息处理成功后，再确认消息，这就需要手动确认
        手动确认：当autoAck=false时，RabbitMQ会等待消费者显式发回ack信号后才从内存(和磁盘，如果是持久化消息的话)中移去消息。否则，RabbitMQ会在队列中消息被消费后立即删除它。
             采用消息确认机制后，只要令autoAck=false，消费者就有足够的时间处理消息(任务)，不用担心处理消息过程中消费者进程挂掉后消息丢失的问题，因为RabbitMQ会一直持有消息直到消费者显式调用basicAck为止。
             当autoAck=false时，对于RabbitMQ服务器端而言，队列中的消息分成了两部分：一部分是等待投递给消费者的消息；一部分是已经投递给消费者，但是还没有收到消费者ack信号的消息。如果服务器端一直没有收到消费者的ack信号，并且消费此消息的消费者已经断开连接，则服务器端会安排该消息重新进入队列，等待投递给下一个消费者（也可能还是原来的那个消费者）。
             通过运行程序，启动两个消费者A、B，都可以收到消息，但是其中有一个消费者A不会对消息进行确认，当把这个消费者A关闭后，消费者B又会收到本来发送给消费者A的消息。所以我们一般使用手动确认的方法是，将消息的处理放在try/catch语句块中，成功处理了，就给RabbitMQ一个确认应答，如果处理异常了，就在catch中，进行消息的拒绝，如何拒绝，参考《消息的拒绝》章节
        代码：详见com.suns.ackfalse

    5.3消息的拒绝
        Reject和Nack
        消息确认可以让RabbitMQ知道消费者已经接受并处理完消息。但是如果消息本身或者消息的处理过程出现问题怎么办？需要一种机制，通知RabbitMQ，这个消息，我无法处理，请让别的消费者处理。这里就有两种机制，Reject和Nack。
        Reject在拒绝消息时，可以使用requeue标识，告诉RabbitMQ是否需要重新发送给别的消费者。不重新发送，一般这个消息就会被RabbitMQ丢弃。Reject一次只能拒绝一条消息。
        Nack则可以一次性拒绝多个消息。这是RabbitMQ对AMQP规范的一个扩展。
        生产者：RejectProducer，和普通的一样发十条路由键为error的消息
        消费者：NormalComsumerA，消息队列为focuserror，绑定路由键为error。采用手工确认消息机制channel.basicConsume(queueName,false,consumer);
        消费者：NormalComsumerB，消息队列为focuserror，绑定路由键为error。采用手工确认消息机制channel.basicConsume(queueName,false,consumer);
        消费者：RejectRequeueConsumer，消息队列为focuserror，绑定路由键为error。采用手工确认消息机制channel.basicConsume(queueName,false,consumer); 手工抛异常
        通过RejectRequeuConsumer可以看到当requeue参数设置为true时，消息发生了重新投递，如果设置为false,那么就不会进行重新投递。

        reject和nack用法一样
         channel.basicReject(envelope.getDeliveryTag(),false);
         channel.basicNack(envelope.getDeliveryTag(),false,true);

        代码：详见com.suns.rejectmsg

6.死信交换器Dlx
    RabbitMQ对AMQP规范的一个扩展。被投递消息被拒绝后的一个可选行为，往往用在对问题消息的诊断上。
    消息变成死信一般是以下几种情况：
    a.消息被拒绝，并且设置 requeue 参数为 false
    b.消息过期
    c.队列达到最大长度
    死信交换器仍然只是一个普通的交换器，创建时并没有特别要求和操作。在创建队列的时候，声明该交换器将用作保存被拒绝的消息即可，相关的参数是x-dead-letter-exchange。
    代码：详见com.suns.dlx
    通过运行程序可以看到，生产者产生了3条消息，分别是error,info,warn，两个消费者WillMakeDlxConsumer和WillMakeWarnDlxConsumer都拒绝了两条消息，而投送到死信队列后，可以发现根据投送死信时的路由键，不同的消费者有些可以接受到消息，有些则不行。

    和备用交换器的区别
    1、备用交换器是主交换器无法路由消息，那么消息将被路由到这个新的备用交换器，而死信交换器则是接收过期或者被拒绝的消息。
    2、备用交换器是在声明主交换器时发生联系，而死信交换器则声明队列时发生联系

7.控制队列
    7.1临时队列
        7.1.1自动删除队列
            自动删除队列和普通队列在使用上没有什么区别，唯一的区别是，当消费者断开连接时，队列将会被删除。自动删除队列允许的消费者没有限制，也就是说当这个队列上最后一个消费者断开连接才会执行删除。
            自动删除队列只需要在声明队列时，设置属性auto-delete标识为true即可。系统声明的随机队列，缺省就是自动删除的
            channel.queueDeclare(queueName,false,false, true,null);
        7.1.2单消费队列
            普通队列允许的消费者没有限制，多个消费者绑定到多个队列时，RabbitMQ会采用轮询进行投递。如果需要消费者独占队列，在队列创建的时候，设定属性exclusive为true。(如果exclusive为true，有多个消费者，启动就会报错了)
            channel.queueDeclare(queueName,false,true, false,null);
        7.1.3自动过期队列
            指队列在超过一定时间没使用，会被从RabbitMQ中被删除。什么是没使用？
            一定时间内没有Get操作发生
            没有Consumer连接在队列上
            特别的：就算一直有消息进入队列，也不算队列在被使用。
            通过声明队列时，设定x-expires参数即可，单位毫秒

            map.put("x-expires",10*60);
    7.2永久队列
        7.2.1队列的持久性
            持久化队列和非持久化队列的区别是，持久化队列会被保存在磁盘中，固定并持久的存储，当Rabbit服务重启后，该队列会保持原来的状态在RabbitMQ中被管理，而非持久化队列不会被保存在磁盘中，Rabbit服务重启后队列就会消失。
            非持久化比持久化的优势就是，由于非持久化不需要保存在磁盘中，所以使用速度就比持久化队列快。即是非持久化的性能要高于持久化。而持久化的优点就是会一直存在，不会随服务的重启或服务器的宕机而消失。
            在声明队列时，将属性durable设置为“false”，则该队列为非持久化队列，设置成“true”时，该队列就为持久化队列
            channel.queueDeclare(queueName,true,false, false,null);
        7.2.2队列级别消息过期
            就是为每个队列设置消息的超时时间。只要给队列设置x-message-ttl 参数，就设定了该队列所有消息的存活时间，时间单位是毫秒。如果声明队列时指定了死信交换器，则过期消息会成为死信消息

    7.3队列保留参数列表
        参数名 	目的
        x-dead-letter-exchange 	死信交换器
        x-dead-letter-routing-key 	死信消息的可选路由键
        x-expires 	队列在指定毫秒数后被删除
        x-ha-policy 	创建HA队列
        x-ha-nodes 	HA队列的分布节点
        x-max-length 	队列的最大消息数
        x-message-ttl 	毫秒为单位的消息过期时间，队列级别
        x-max-priority 	最大优先值为255的队列优先排序功能
    7.4消息的属性
        属性名	用处
        content-type	消息体的MIME类型，如application/json
        content-encoding	消息的编码类型，如是否压缩
        message-id	消息的唯一性标识，由应用进行设置
        correlation-id	一般用做关联消息的message-id，常用于消息的响应
        timestamp	消息的创建时刻，整形，精确到秒
        expiration	消息的过期时刻， 字符串，但是呈现格式为整型，精确到秒
        delivery-mode	消息的持久化类型，1为非持久化，2为持久化，性能影响巨大
        app-id	应用程序的类型和版本号
        user-id	标识已登录用户，极少使用
        type	消息类型名称，完全由应用决定如何使用该字段
        reply-to	构建回复消息的私有响应队列
        headers	键/值对表，用户自定义任意的键和值
        priority	指定队列中消息的优先级

    7.5 request-response模式
        在发送消息时，我们还可以对消息的属性做更细微的控制，比如构建Request-Response模式
        代码：详见com.suns.setmsg

    7.6消息存活时间
        当队列消息的TTL 和消息TTL都被设置，时间短的TTL设置生效。如果将一个过期消息发送给RabbitMQ，该消息不会路由到任何队列，而是直接丢弃。
        为消息设置TTL有一个问题：RabbitMQ只对处于队头的消息判断是否过期（即不会扫描队列），所以，很可能队列中已存在死消息，但是队列并不知情。这会影响队列统计数据的正确性，妨碍队列及时释放资源

    7.7消息的持久化
        默认情况下，队列和交换器在服务器重启后都会消失，消息当然也是。将队列和交换器的durable属性设为true，缺省为false，但是消息要持久化还不够，还需要将消息在发布前，将投递模式设置为2。消息要持久化，必须要有持久化的队列、交换器和投递模式都为2。
        消息属性的设置方法，包括如何将消息的持久化
        消息的持久化必须 设置交换器，队列都为持久化才行，三者缺一不可
        channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.DIRECT,true);
        channel.queueDeclare(queueName,true,false, false,null);
        channel.basicPublish(EXCHANGE_NAME,servieity,MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
        代码：详见com.suns.durablemsg中

    7.8作业：用rabbitmq实现限时订单
        利用ttl（x-message-ttl）和死信队列（x-dead-letter-exchange）配合使用
        代码：详见com.suns.delayorder