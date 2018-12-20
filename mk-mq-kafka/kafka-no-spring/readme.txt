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
        代码：详见包com.suns.selfserial

