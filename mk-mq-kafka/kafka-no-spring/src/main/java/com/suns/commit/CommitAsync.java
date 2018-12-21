/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.commit <br>
 *
 * @author mk <br>
 * Date:2018-12-21 10:54 <br>
 */

package com.suns.commit;

import com.suns.config.BusiConst;
import com.suns.config.KafkaConst;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Map;

/**
 * ClassName: CommitAsync <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-21 10:54 <br>
 * @version
 */
public class CommitAsync {

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
                //异步提交偏移量
//                consumer.commitAsync();

                //异步提交偏移量，并获得回调
                consumer.commitAsync(new OffsetCommitCallback() {
                    @Override
                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                        if (null != exception) {
                            exception.printStackTrace();
                        }
                        if (null != offsets) {
                            for(TopicPartition topicPartition : offsets.keySet()){
                                OffsetAndMetadata offsetAndMetadata = offsets.get(topicPartition);
                                System.out.println(String.format("kafka异步提交偏移量成功！主题topic:%s,分区partition:%d,偏移量offset:%d,消息:%s",
                                        topicPartition.topic(), topicPartition.partition(), offsetAndMetadata.offset(),offsetAndMetadata.metadata()));

                            }


                        }
                    }
                });
            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            consumer.close();
        }
    }
}
