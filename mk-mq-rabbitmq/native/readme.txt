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
    4.4发布者确认
        启用消息确认模式：channel.confirmSelect();
        Confirm的三种实现方式：
        方式一：channel.waitForConfirms()普通发送方确认模式；消息到达交换器，就会返回true。
        方式二：channel.waitForConfirmsOrDie()批量确认模式；使用同步方式等所有的消息发送之后才会执行后面代码，只要有一个消息未到达交换器就会抛出IOException异常。
        方式三：channel.addConfirmListener()异步监听发送方确认模式；
        代码：详见com.suns.producerconfirm
