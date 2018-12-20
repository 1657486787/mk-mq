/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.selfpartition <br>
 *
 * @author mk <br>
 * Date:2018-12-20 14:26 <br>
 */

package com.suns.selfpartition;

import com.suns.config.BusiConst;
import com.suns.config.KafkaConst;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * ClassName: SelfPartitionProducer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-20 14:26 <br>
 * @version
 */
public class SelfPartitionProducer {

    public static KafkaProducer<String, String> producer = null;
    public static void main(String[] args) {

        try {
        Properties properties = KafkaConst.producerConfig(StringSerializer.class, StringSerializer.class);
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,SelfPartition.class);//设置自定义分区器

        producer = new KafkaProducer<>(properties);
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
