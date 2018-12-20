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

