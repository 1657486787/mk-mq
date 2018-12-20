/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.consumergroup <br>
 *
 * @author mk <br>
 * Date:2018-12-20 15:07 <br>
 */

package com.suns.consumergroup;

import com.suns.config.BusiConst;
import com.suns.config.KafkaConst;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * ClassName: TestGroupProducer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-20 15:07 <br>
 * @version
 */
public class TestGroupProducer {

    public static KafkaProducer<String, String> producer = null;
    public static void main(String[] args) {

        try{
            producer = new KafkaProducer<>(KafkaConst.producerConfig(StringSerializer.class, StringSerializer.class));

            //注意生产者发送的消息，根据key的不同分配到不同的分区上，如果key一样的话，那么会分配到一样的分区上面
            for(int i=0;i<20;i++){
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>(BusiConst.CONSUMER_GROUP_TOPIC, "消费者群组key"+i, "消费者群组key");
                producer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception exception) {
                        final String id = Thread.currentThread().getId()+"-"+System.identityHashCode(producer);
                        System.out.println(String.format(id+",kafka发送消息成功！主题topic:%s,分区partition:%d,偏移量offset:%d",
                                recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset()));
                    }
                });
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            producer.close();
        }
    }
}
