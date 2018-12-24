Kafka使用原生客户端
1.增加依赖
    <dependency>
        <groupId>org.apache.kafka</groupId>
        <artifactId>kafka-clients</artifactId>
        <version>0.10.1.1</version>
    </dependency>
2.运行Kafka
3.最简单的入门例子
    HelloKafkaProducer:消息发送者
    HelloKafkaConsumer:消息接受者
    代码：详见包com.suns.hellokafka
4.kafka使用
    4.1消息的发送模式
        发送消息拿结果：kafkaFutureProducer
        异步获取消息:kafkaAsyncProducer
        代码：详见包com.suns.sendtype
    4.2多线程下使用
        生产者是线程安全的,不同线程可以共用KafkaProducer
        消费者是非线程安全的，每个线程需要new KafkaConsumer
        注意：测试消费者多线程接收消息的时候，需要手工对主题的分区进行设定个数（因为1个分区只能被一个消费者群组中的一个消费者消费），命令如下：（其中concurrenttest为主题，2为分区的个数）
            .\kafka-topics.bat --zookeeper localhost:2181/kafka --alter --topic concurrenttest --partitions 2
        代码：详见包com.suns.concurrent
    4.3自定义序列器（一般情况下不会使用）
        自定义序列器SelfSerializer 实现接口 org.apache.kafka.common.serialization.Serializer
        自定义反序列器SelfDeserializer 实现接口 org.apache.kafka.common.serialization.Deserializer
        生产者：设置value.serializer 为自定义的序列器SelfSerializer
        消费者：设置value.deserailizer 为自定义的反序列器SelfDeserializer

        自定义序列化需要考虑的问题
            自定义序列化容易导致程序的脆弱性。举例，在我们上面的实现里，我们有多种类型的消费者，每个消费者对实体字段都有各自的需求，比如，有的将字段变更为long型，有的会增加字段，这样会出现新旧消息的兼容性问题。特别是在系统升级的时候，经常会出现一部分系统升级，其余系统被迫跟着升级的情况。
            解决这个问题，可以考虑使用自带格式描述以及语言无关的序列化框架。比如Protobuf，或者Kafka官方推荐的Apache Avro。
            Avro会使用一个JSON文件作为schema来描述数据，Avro在读写时会用到这个schema，可以把这个schema内嵌在数据文件中。这样，不管数据格式如何变动，消费者都知道如何处理数据。
            但是内嵌的消息，自带格式，会导致消息的大小不必要的增大，消耗了资源。我们可以使用schema注册表机制，将所有写入的数据用到的schema保存在注册表中，然后在消息中引用schema的标识符，而读取的数据的消费者程序使用这个标识符从注册表中拉取schema来反序列化记录。
            注意：Kafka本身并不提供schema注册表，需要借助第三方，现在已经有很多的开源实现，比如Confluent Schema Registry，可以从GitHub上获取。如何使用参考如下网址：
            https://cloud.tencent.com/developer/article/1336568

        代码：详见包com.suns.selfserial
    4.4自定义分区器
            我们在新增ProducerRecord对象中可以看到，ProducerRecord包含了目标主题，键和值，Kafka的消息都是一个个的键值对。键可以设置为默认的null。
            键的主要用途有两个：一，用来决定消息被写往主题的哪个分区，拥有相同键的消息将被写往同一个分区，二，还可以作为消息的附加消息。
            如果键值为null，并且使用默认的分区器，分区器使用轮询算法将消息均衡地分布到各个分区上。
            如果键不为空，并且使用默认的分区器，Kafka对键进行散列（Kafka自定义的散列算法，具体算法原理不知），然后根据散列值把消息映射到特定的分区上。很明显，同一个键总是被映射到同一个分区。但是只有不改变主题分区数量的情况下，键和分区之间的映射才能保持不变，一旦增加了新的分区，就无法保证了，所以如果要使用键来映射分区，那就要在创建主题的时候把分区规划好，而且永远不要增加新分区。
        自定义分区器
            某些情况下，数据特性决定了需要进行特殊分区，比如电商业务，北京的业务量明显比较大，占据了总业务量的20%，我们需要对北京的订单进行单独分区处理，默认的散列分区算法不合适了， 我们就可以自定义分区算法，对北京的订单单独处理，其他地区沿用散列分区算法。或者某些情况下，我们用value来进行分区。
            1.先创建一个4分区的主题self-partition（一定要多个分区，要不然看不出效果）
            2.SelfPartition 实现 Partitioner（具体的分区逻辑在partition方法中实现）
            3.在生产者中，增加properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,SelfPartition.class);//设置自定义分区器
        代码：详见包com.suns.selfpartition
    4.5消费者群组
        消费者的含义，同一般消息中间件中消费者的概念。在高并发的情况下，生产者产生消息的速度是远大于消费者消费的速度，单个消费者很可能会负担不起，此时有必要对消费者进行横向伸缩，于是我们可以使用多个消费者从同一个主题读取消息，对消息进行分流
        Kafka里消费者从属于消费者群组，一个群组里的消费者订阅的都是同一个主题，每个消费者接收主题一部分分区的消息：
        a.如果一个主题有4个分区，群组中只有一个消费者，那么消费者将收到4个分区的消息
        b.如果一个主题有4个分区，群组中有两个消费者，那么每个消费者将收到2个分区的消息
        c.如果一个主题有4个分区，群组中有三个消费者，那么有一个消费者将受到两个分区的消息，另外两个消费者将各收到一个分区的消息
        d.如果一个主题有4个分区，群组中有4个消费者，那么每个消费者将各自收到一个分区的消息
        e.如果一个主题有4个分区，群组中的消费者大于分区数，那么有4个消费者将收到消息，其余的闲置

        1.先创建一个3分区的主题consumer-group-002（一定要多个分区，要不然看不出效果）
        2.运行消费者，生产者（其中GroupAConsumer1,GroupAConsumer2,GroupAConsumer3为同个群组，GroupBConsumer1,GroupBConsumer2为同个群组，GroupCConsumer为一个群组）
        代码：详见包com.suns.consumergroup

        分区再均衡
            当消费者群组里的消费者发生变化，或者主题里的分区发生了变化，都会导致再均衡现象的发生。从前面的知识中，我们知道，Kafka中，存在着消费者对分区所有权的关系，
            这样无论是消费者变化，比如增加了消费者，新消费者会读取原本由其他消费者读取的分区，消费者减少，原本由它负责的分区要由其他消费者来读取，增加了分区，哪个消费者来读取这个新增的分区，这些行为，都会导致分区所有权的变化，这种变化就被称为再均衡。
            再均衡对Kafka很重要，这是消费者群组带来高可用性和伸缩性的关键所在。不过一般情况下，尽量减少再均衡，因为再均衡期间，消费者是无法读取消息的，会造成整个群组一小段时间的不可用。
            消费者通过向称为群组协调器的broker（不同的群组有不同的协调器）发送心跳来维持它和群组的从属关系以及对分区的所有权关系。如果消费者长时间不发送心跳，群组协调器认为它已经死亡，就会触发一次再均衡。
            在0.10.1及以后的版本中，心跳由单独的线程负责，相关的控制参数为max.poll.interval.ms。
            消费者分区分配的过程
                消费者要加入群组时，会向群组协调器发送一个JoinGroup请求，第一个加入群主的消费者成为群主，群主会获得群组的成员列表，并负责给每一个消费者分配分区。分配完毕后，群组把分配情况发送给群组协调器，协调器再把这些信息发送给所有的消费者，每个消费者只能看到自己的分配信息，只有群主知道群组里所有消费者的分配信息。这个过程在每次再均衡时都会发生
    4.6手工提交偏移量
        1.同步提交：CommitSync
        2.异步提交：CommitAsync
        3.同步和异步提交结合使用：SyncAndAsync
        4.提交特定的偏移量：CommitSpecial
        代码：详见包com.suns.commit

    4.7分区再均衡
        在提交偏移量一节中提到过,消费者在退出和进行分区再均衡之前,会做一些清理工作比如，提交偏移量、关闭文件句柄、数据库连接等。
        在为消费者分配新分区或移除旧分区时,可以通过消费者API执行一些应用程序代码，在调用 subscribe()方法时传进去一个 ConsumerRebalancelistener实例就可以了。
        ConsumerRebalancelistener有两个需要实现的方法。
        1) public void onPartitionsRevoked( Collection< TopicPartition> partitions)方法会在
            再均衡开始之前和消费者停止读取消息之后被调用。如果在这里提交偏移量，下一个接管分区的消费者就知道该从哪里开始读取了
        2) public void onPartitionsAssigned( Collection< TopicPartition> partitions)方法会在重新分配分区之后和消费者开始读取消息之前被调用。
            具体使用，我们先创建一个3分区的主题，然后实验一下
        kafka-topic.bat --zookeeper localhost:2181/kafka --alter --topic rebalance-topic-001 --partitions 3

        代码：详见包com.suns.rebalance
    4.8独立消费者
        1.生产者不用变
        2.消费者:不指定groupId和subscribe
        //properties.put(ConsumerConfig.GROUP_ID_CONFIG,"independence_group_id");
        //consumer.subscribe(Collections.singletonList(BusiConst.INDEPENDENCE_CONSUMER_TOPIC));

        List<TopicPartition> list = new ArrayList<>();
        List<PartitionInfo> partitionInfos = consumer.partitionsFor(BusiConst.INDEPENDENCE_CONSUMER_TOPIC);
        if(null != partitionInfos){
            for(PartitionInfo partitionInfo : partitionInfos){
                System.out.printf("主题：%s,分区：%s",partitionInfo.topic(),partitionInfo.partition()).println();
                TopicPartition topicPartition = new TopicPartition(partitionInfo.topic(), partitionInfo.partition());
                list.add(topicPartition);
            }
        }
        consumer.assign(list);
        注意：独立消费者会接收到主题下的所有分区的消息，如果动态新增分区后，需要重启消费者才会接受新的分区上的内容。

        代码：详见包com.suns.independconsumer