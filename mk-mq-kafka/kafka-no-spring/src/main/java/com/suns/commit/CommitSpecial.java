/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.commit <br>
 *
 * @author mk <br>
 * Date:2018-12-21 11:10 <br>
 */

package com.suns.commit;

import com.suns.config.BusiConst;
import com.suns.config.KafkaConst;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: CommitSpecial <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-21 11:10 <br>
 * @version
 */
public class CommitSpecial {

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

            Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
            int count = 0;
            while(true){
                ConsumerRecords<Object, Object> records = consumer.poll(500);
                for(ConsumerRecord record:records){
                    System.out.println(id+",kafka接收消息："+record);

                    currentOffsets.put(new TopicPartition(record.topic(),record.partition()),new OffsetAndMetadata(record.offset()+1,""));
                    if(count % 10 == 0){//每100条就提交一次
                        consumer.commitAsync(currentOffsets,null);
                        System.out.println("===================每100条提交一次偏移量==============");
                    }
                    count ++;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            consumer.close();
        }
    }

}
