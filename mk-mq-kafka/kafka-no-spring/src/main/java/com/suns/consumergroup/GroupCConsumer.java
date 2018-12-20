/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.consumergroup <br>
 *
 * @author mk <br>
 * Date:2018-12-20 15:55 <br>
 */

package com.suns.consumergroup;

import com.suns.config.BusiConst;
import com.suns.config.KafkaConst;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;

/**
 * ClassName: GroupCConsumer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-20 15:55 <br>
 * @version
 */
public class GroupCConsumer {

    public static KafkaConsumer<Object, Object> consumer = null;
    public static void main(String[] args) {
        try{
            consumer = new KafkaConsumer<>(KafkaConst.consumerConfig(BusiConst.CONSUMER_GROUP_C, StringDeserializer.class, StringDeserializer.class));
            consumer.subscribe(Collections.singletonList(BusiConst.CONSUMER_GROUP_TOPIC));
            String id = Thread.currentThread().getId() + "-" + System.identityHashCode(consumer);
            System.out.println(id+",kafka准备接收消息");

            while(true){
                ConsumerRecords<Object, Object> records = consumer.poll(500);
                for(ConsumerRecord record : records){
                    System.out.println(id+",kafka接收消息："+record);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            consumer.close();
        }

    }
}
