RabbitMQ
1.安装(需要先安装erlang，而且对版本号有要求)
    1.1windows下安装RabbitMQ（erlang版本号19.2，RabbitMQ版本号v3.6.6）
        1.1.1下载并安装erlang
            http://www.erlang.org/downloads/19.2
        1.1.2下载并安装windows版本的RabbitMQ
            http://www.rabbitmq.com/releases/rabbitmq-server/v3.6.6/rabbitmq-server-3.6.6.exe
        1.1.3配置环境变量
            增加变量ERLANG_HOME    E:\Program Files\erl8.2
            path下添加   %ERLANG_HOME%\bin;
            增加变量RABBITMQ_BASE  E:\Program Files\RabbitMQ Server\rabbitmq_server-3.6.6
            path下添加  %RABBITMQ_BASE%\sbin;%RABBITMQ_BASE%\ebin;
        1.1.4启动
            在开始菜单中启动服务RabbitMQ service - start 或者在命令行中执行rabbitmq-service.bat start
        1.1.5检查运行是否成功
            可以在安装目录的sbin下运行rabbitmqctl.bat status检测是否安装成功
        问题：
        1.Rabbit安装完后，启动rabbitmq-service start出现闪退问题解决:
            首先在命令行输入：rabbitmq-service stop，
            接着输入rabbitmq-service remove，
            再接着输入rabbitmq-service install，
            接着输入rabbitmq-service start
            然后运行rabbitmqctl.bat status检测是否安装成功
    1.2linux下安装RabbitMQ

2.概念
    2.1AMQP
        是应用层协议的一个开放标准,为面向消息的中间件设计。基于此协议的客户端与消息中间件可传递消息，并不受客户端/中间件不同产品，不同的开发语言等条件的限制。目标是实现一种在全行业广泛使用的标准消息中间件技术，以便降低企业和系统集成的开销，并且向大众提供工业级的集成服务。主要实现有 RabbitMQ
        RabbitMQ就是对AMQP的一种实现
    2.2包含的要素
        包括：生产者，消费者，消息，信道，交换器，队列，绑定，路由键
        生产者：消息的创建者，发送到rabbitmq
        消费者：连接到rabbitmq，订阅到队列上，消费消息，持续订阅(basicConsumer)和单条订阅(basicGet)
        消息：包含有效载荷和标签，有效载荷指要传输的数据，，标签描述了有效载荷，并且rabbitmq用它来决定谁获得消息，消费者只能拿到有效载荷，并不知道生产者是谁
        信道：信道是生产消费者与rabbit通信的渠道，生产者publish或是消费者subscribe一个队列都是通过信道来通信的。信道是建立在TCP连接上的虚拟连接，什么意思呢？就是说rabbitmq在一条TCP上建立成百上千个信道来达到多个线程处理，这个TCP被多个线程共享，每个线程对应一个信道，信道在rabbit都有唯一的ID ,保证了信道私有性，对应上唯一的线程使用。
              ==》疑问：为什么不建立多个TCP连接呢？原因是rabbit保证性能，系统为每个线程开辟一个TCP是非常消耗性能，每秒成百上千的建立销毁TCP会严重消耗系统。所以rabbitmq选择建立多个信道（建立在tcp的虚拟连接）连接到rabbit上
        交换器，队列，绑定，路由键：
        队列通过路由键（routing  key，某种确定的规则）绑定到交换器，生产者将消息发布到交换器，交换器根据绑定的路由键将消息路由到特定队列，然后由订阅这个队列的消费者进行接收

        常见问题
        如果消息达到无人订阅的队列会怎么办？消息会一直在队列中等待，RabbitMq默认队列是无限长度的。
        多个消费者订阅到同一队列怎么办？消息以循环的方式发送给消费者，每个消息只会发送给一个消费者。
        消息路由到了不存在的队列怎么办？一般情况下，凉拌，RabbitMq会忽略，当这个消息不存在，也就是这消息丢了。

    2.3消息的确认
        消费者收到的每一条消息都必须进行确认（自动确认和自行确认）。
        消费者在声明队列时，可以指定autoAck参数，当autoAck=false时，RabbitMQ会等待消费者显式发回ack信号后才从内存(和磁盘，如果是持久化消息的话)中移去消息。否则，RabbitMQ会在队列中消息被消费后立即删除它。
        采用消息确认机制后，只要令autoAck=false，消费者就有足够的时间处理消息(任务)，不用担心处理消息过程中消费者进程挂掉后消息丢失的问题，因为RabbitMQ会一直持有消息直到消费者显式调用basicAck为止。
        当autoAck=false时，对于RabbitMQ服务器端而言，队列中的消息分成了两部分：一部分是等待投递给消费者的消息；一部分是已经投递给消费者，但是还没有收到消费者ack信号的消息。如果服务器端一直没有收到消费者的ack信号，并且消费此消息的消费者已经断开连接，则服务器端会安排该消息重新进入队列，等待投递给下一个消费者（也可能还是原来的那个消费者）。
        RabbitMQ不会为未ack的消息设置超时时间，它判断此消息是否需要重新投递给消费者的唯一依据是消费该消息的消费者连接是否已经断开。这么设计的原因是RabbitMQ允许消费者消费一条消息的时间可以很久很久

    2.4交换器类型
        共有四种direct,fanout,topic,headers，其种headers(几乎和direct一样)不实用，可以忽略
        Direct
            路由键完全匹配，消息被投递到对应的队列，每个amqp的实现都必须有一个direct交换器，包含一个空白字符串名称的默认交换器。声明一个队列时，会自动绑定到默认交换器，并且以队列名称作为路由键：channel->basic_public($msg,’ ’,’queue-name’)
        Fanout
            消息广播到绑定的队列
        Topic
            通过使用“*”和“#”，使来自不同源头的消息到达同一个队列，”.”将路由键分为了几个标识符，“*”匹配1个，“#”匹配一个或多个。例如日志处理：
            假设有交换器log-exchange，
            日志级别有error,info,warning，
            应用模块有user,order,email，
            服务器有 A、B、C、D
            路由键的规则为 服务器+“.”+日志级别+“.”+应用模块名，如：A. info .email。
            1、要关注A服务器发送的所有应用错误的消息，怎么做？
            声明队列名称为“a-app-error-queue”并绑定到交换器上：channel. queueBind (‘a-app-error-queue’,’logs-change’,’A.error.*’)
            2、关注B服务器发送的的所有日志，怎么办？
            声明队列名称为“b-all-queue”并绑定到交换器上：channel. queueBind (b-all-queue’,’logs-change’,’ B.#’)或channel. queueBind (b-all-queue’,’logs-change’,’ B.*.*’)
            3、关注所有服务器发送的email的所有日志，怎么办？
            声明队列名称为“email-all-queue”并绑定到交换器上：channel. queueBind (email -all-queue’,’logs-change’,’ *.*.emal’)
            4、想要接收所有日志：channel->queue_bind(‘all-log’,’logs-change’,’#’)

    2.5虚拟主机
        虚拟消息服务器，vhost，本质上就是一个mini版的mq服务器，有自己的队列、交换器和绑定，最重要的，自己的权限机制。Vhost提供了逻辑上的分离，可以将众多客户端进行区分，又可以避免队列和交换器的命名冲突。Vhost必须在连接时指定，rabbitmq包含缺省vhost：“/”，通过缺省用户和口令guest进行访问。
        rabbitmq里创建用户，必须要被指派给至少一个vhost，并且只能访问被指派内的队列、交换器和绑定。Vhost必须通过rabbitmq的管理控制工具创建

3.使用：使用原生客户端
    原生客户端：详见项目native