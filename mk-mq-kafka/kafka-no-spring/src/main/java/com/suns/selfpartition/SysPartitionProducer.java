/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.selfpartition <br>
 *
 * @author mk <br>
 * Date:2018-12-20 14:20 <br>
 */

package com.suns.selfpartition;

import com.suns.config.BusiConst;
import com.suns.config.KafkaConst;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * ClassName: SysPartitionProducer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-20 14:20 <br>
 * @version
 */
public class SysPartitionProducer {


    public static KafkaProducer<String, String> producer = null;
    public static void main(String[] args) {

        try {

            producer = new KafkaProducer<>(KafkaConst.producerConfig(StringSerializer.class, StringSerializer.class));
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(BusiConst.SELF_PARTITION_TOPIC, "自定义分区key", "自定义分区value");
            producer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    final String id = Thread.currentThread().getId()+"-"+System.identityHashCode(producer);
                    System.out.println(String.format(id+",kafka发送消息成功！主题topic:%s,分区partition:%d,偏移量offset:%d",
                            recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset()));
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            producer.close();
        }
    }
}
