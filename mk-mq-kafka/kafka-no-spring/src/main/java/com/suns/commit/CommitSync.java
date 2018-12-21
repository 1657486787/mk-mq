/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.commit <br>
 *
 * @author mk <br>
 * Date:2018-12-21 10:06 <br>
 */

package com.suns.commit;

import com.suns.config.BusiConst;
import com.suns.config.KafkaConst;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Map;

/**
 * ClassName: CommitSync <br>
 * Description: 手工提交偏移量，同步提交 <br>
 * @author mk
 * @Date 2018-12-21 10:06 <br>
 * @version
 */
public class CommitSync {


    public static KafkaConsumer<Object, Object> consumer = null;
    public static void main(String[] args) {
        try{

            Map<String, Object> properties = KafkaConst.consumerConfigMap("commitsync-001", StringDeserializer.class, StringDeserializer.class);
            /*取消自动提交*/
            properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,false);
            consumer = new KafkaConsumer<>(properties);
            consumer.subscribe(Collections.singletonList(BusiConst.CONSUMER_COMMIT_TOPIC));

            String id = Thread.currentThread().getId() + "-" + System.identityHashCode(consumer);
            System.out.println(id+",kafka准备接收消息");

            while(true){
                ConsumerRecords<Object, Object> records = consumer.poll(500);
                for(ConsumerRecord record:records){
                    System.out.println(id+",kafka接收消息："+record);
                }
                //同步提交偏移量
                consumer.commitSync();
            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            consumer.close();
        }
    }
}
