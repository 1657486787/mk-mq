kafka
1.什么是kafka
    kafka最初是LinkedIn的一个内部基础设施系统。最初开发的起因是，LinkedIn虽然有了数据库和其他系统可以用来存储数据，但是缺乏一个可以帮助处理持续数据流的组件。所以在设计理念上，开发者不想只是开发一个能够存储数据的系统，如关系数据库、Nosql数据库、搜索引擎等等，更希望把数据看成一个持续变化和不断增长的流，并基于这样的想法构建出一个数据系统，一个数据架构。
    Kafka可以看成一个流平台，这个平台上可以发布和订阅数据流，并把他们保存起来，进行处理。Kafka有点像消息系统，允许发布和订阅消息流，但是它和传统的消息系统有很大的差异，首先，Kafka是个现代分布式系统，以集群的方式运行，可以自由伸缩。其次，Kafka可以按照要求存储数据，保存多久都可以，第三，流式处理将数据处理的层次提示到了新高度，消息系统只会传递数据，Kafka的流式处理能力可以让我们用很少的代码就能动态地处理派生流和数据集。所以Kafka不仅仅是个消息中间件。
    同时在大数据领域，Kafka还可以看成实时版的Hadoop，Hadoop可以存储和定期处理大量的数据文件，往往以TB计数，而Kafka可以存储和持续处理大型的数据流。Hadoop主要用在数据分析上，而Kafka因为低延迟，更适合于核心的业务应用上。
    本次课程，将会以kafka_2.11-0.10.1.1版本为主，其余版本不予考虑
2.基本概念
    消息，批次，主题，分区，生产者，消费者，偏移量，消费者群组，broker和集群，保留消息
3.为什么选择kafka
    多生产者和多消费者
    基于磁盘的数据存储，换句话说，Kafka的数据天生就是持久化的。
    高伸缩性，Kafka一开始就被设计成一个具有灵活伸缩性的系统，对在线集群的伸缩丝毫不影响整体系统的可用性。
    高性能，结合横向扩展生产者、消费者和broker，Kafka可以轻松处理巨大的信息流，同时保证亚秒级的消息延迟
4.使用场景
    活动跟踪：跟踪网站用户和前端应用发生的交互，比如页面访问次数和点击，将这些信息作为消息发布到一个或者多个主题上，这样就可以根据这些数据为机器学习提供数据，更新搜素结果等等
    传递消息：标准消息中间件的功能
    收集指标和日志：收集应用程序和系统的度量监控指标，或者收集应用日志信息，通过Kafka路由到专门的日志搜索系统，比如ES
    提交日志：收集其他系统的变动日志，比如数据库。可以把数据库的更新发布到Kafka上，应用通过监控事件流来接收数据库的实时更新，或者通过事件流将数据库的更新复制到远程系统。
            还可以当其他系统发生了崩溃，通过重放日志来恢复系统的状态
    流处理：操作实时数据流，进行统计、转换、复杂计算等等。随着大数据技术的不断发展和成熟，无论是传统企业还是互联网公司都已经不再满足于离线批处理，实时流处理的需求和重要性日益增长。
            近年来业界一直在探索实时流计算引擎和API，比如这几年火爆的Spark Streaming、Kafka Streaming、Beam和Flink，其中阿里双11会场展示的实时销售金额，就用的是流计算，是基于Flink，然后阿里在其上定制化的Blink

5.下载安装
    预备环境：Kafka是Java生态圈下的一员，用Scala编写，运行在Java虚拟机上，所以安装运行和普通的Java程序并没有什么区别。
             安装Kafka官方说法，Java环境推荐Java8。
             Kafka需要Zookeeper保存集群的元数据信息和消费者信息。Kafka一般会自带Zookeeper，但是从稳定性考虑，应该使用单独的Zookeeper，而且构建Zookeeper集群
    下载：从官网http://kafka.apache.org/downloads上寻找合适的版本下载，我们这里选用的是kafka_2.11-0.10.1.1，下载完成后解压到本地目录
    安装：
        在windows/linux中安装，windows执行.bat，linux执行sh
        1.配置server.properties
            将zookeeper.connect=localhost:2181 修改为 zookeeper.connect=localhost:2181/kafka
        2.进入到cd D:\lurenz\springboot\mq\kafka\kafka_2.11-0.10.1\kafka_2.11-0.10.1.1\bin\windows
        3.运行zookeeper
            zookeeper-server-start.bat ../../config/zookeeper.properties
        4.运行kafka
            kafka-server-start.bat ../../config/server.properties
        5.创建topic(名为test)
            kafka-topics.bat --create --zookeeper localhost:2181/kafka --replication-factor 1 --partitions 1 --topic test
        6.基本的操作和管理
            ##列出所有主题
            kafka-topics.bat --zookeeper localhost:2181/kafka --list
            ##列出所有主题的详细信息
            kafka-topics.bat --zookeeper localhost:2181/kafka --describe
            ##创建主题 主题名 my-topic，1副本，8分区
            kafka-topics.bat --zookeeper localhost:2181/kafka --create --topic my-topic --replication-factor 1 --partitions 8
            ##增加分区，注意：分区无法被删除
            kafka-topics.bat --zookeeper localhost:2181/kafka --alter --topic my-topic --partitions 16
            ##删除主题
            kafka-topics.bat --zookeeper localhost:2181/kafka --delete --topic my-topic
            ##列出消费者群组（仅Linux）
            kafka-topics.sh --new-consumer --bootstrap-server localhost:9092/kafka --list
            ##列出消费者群组详细信息（仅Linux）
            kafka-topics.sh --new-consumer --bootstrap-server localhost:9092/kafka --describe --group 群组名
        7.常规配置
            broker.id
                在单机时无需修改，但在集群下部署时往往需要修改。它是个每一个broker在集群中的唯一表示，要求是正数。当该服务器的IP地址发生改变时，broker.id没有变化，则不会影响consumers的消息情况
            listeners
                监听列表(以逗号分隔 不同的协议(如plaintext,trace,ssl、不同的IP和端口)),hostname如果设置为0.0.0.0则绑定所有的网卡地址；如果hostname为空则绑定默认的网卡。如果
                没有配置则默认为java.net.InetAddress.getCanonicalHostName()。
                如：PLAINTEXT://myhost:9092,TRACE://:9091或 PLAINTEXT://0.0.0.0:9092,
            zookeeper.connect
                zookeeper集群的地址，可以是多个，多个之间用逗号分割
            log.dirs
                Kafka把所有的消息都保存在磁盘上，存放这些数据的目录通过log.dirs指定
6.使用：
    6.1原生客户端：kafka-no-spring
    6.2kafka与spring整合：kafka-with-spring
    6.3kafka与springboot整合：kafka-with-spring-boot
    6.4kafka与流式计算：kafka-stream
    6.5实战：削峰填谷：
        （削峰填谷）流量整形的服务端:kafka-trafic-sharping
        （削峰填谷）流量整形的客户端:kafka-trafic-sharping-client
            http://localhost:8090/buyTicket
            http://localhost:8080