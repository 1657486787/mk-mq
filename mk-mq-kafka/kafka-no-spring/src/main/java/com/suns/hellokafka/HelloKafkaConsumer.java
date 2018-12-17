/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.hellokafka <br>
 *
 * @author mk <br>
 * Date:2018-12-17 9:31 <br>
 */

package com.suns.hellokafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

/**
 * ClassName: HelloKafkaConsumer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-17 9:31 <br>
 * @version
 */
public class HelloKafkaConsumer {

    public static void main(String[] args) {
        KafkaConsumer<String, String> consumer = null;
        try{
            Properties properties = new Properties();
            properties.put("bootstrap.servers","127.0.0.1:9092");
            properties.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
            properties.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
            properties.put("group.id","test");
            consumer = new KafkaConsumer<>(properties);
            consumer.subscribe(Collections.singletonList(HelloKafkaProducer.kafka_topic));
            System.out.println("kafka准备接收消息");
            while(true){
                ConsumerRecords<String, String> records = consumer.poll(500);
                for(ConsumerRecord<String,String> record : records){
                    System.out.println("kafka接收消息："+record);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            consumer.close();
        }

    }
}
