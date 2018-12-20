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
        代码：详见包com.suns.concurrent