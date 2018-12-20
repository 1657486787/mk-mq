/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.selfserial <br>
 *
 * @author mk <br>
 * Date:2018-12-20 10:20 <br>
 */

package com.suns.selfserial;

import com.suns.config.BusiConst;
import com.suns.config.KafkaConst;
import com.suns.vo.DemoUser;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * ClassName: SelfSerialProducer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-20 10:20 <br>
 * @version
 */
public class SelfSerialProducer {

    public static KafkaProducer<String,DemoUser> producer = null;
    public static void main(String[] args) {

        try {
            producer = new KafkaProducer<>(KafkaConst.producerConfig(StringSerializer.class,SelfSerializer.class));
            ProducerRecord<String,DemoUser> producerRecord =
                    new ProducerRecord<>(BusiConst.SELF_PARTITION_TOPIC, "自定义序列key",new DemoUser(1,"自定义序列Value") );

            producer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    String id = Thread.currentThread().getId() + "-" + System.identityHashCode(producer);

                    if (null != e) {
                        e.printStackTrace();
                    }
                    if (null != recordMetadata) {
                        System.out.println(String.format(id + ",kafka发送消息成功！主题topic:%s,分区partition:%d,偏移量offset:%d",
                                recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset()));
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            producer.close();
        }
    }

}
