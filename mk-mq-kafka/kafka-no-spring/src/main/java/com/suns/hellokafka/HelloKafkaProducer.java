/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.hellokafka <br>
 *
 * @author mk <br>
 * Date:2018-12-17 9:31 <br>
 */

package com.suns.hellokafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * ClassName: HelloKafkaProducer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-17 9:31 <br>
 * @version
 */
public class HelloKafkaProducer {

    public static final String kafka_topic="kafka-hello-topic-001";
    public static final String kafka_key="kafka-key-001";
    public static final String kafka_value="hello,卡夫卡";

    public static void main(String[] args) {
        KafkaProducer<String, String> producer = null;
        try{
            Properties properties = new Properties();
            properties.put("bootstrap.servers","127.0.0.1:9092");
            properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
            properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
            producer = new KafkaProducer<>(properties);
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(kafka_topic,kafka_key,kafka_value);
            producer.send(producerRecord);
            System.out.println("kafka发送消息成功！");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            producer.close();//一定要关闭，否则消费者收不到消息
        }


    }

}
