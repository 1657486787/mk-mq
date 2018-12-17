/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.hellokafka <br>
 *
 * @author mk <br>
 * Date:2018-12-17 11:23 <br>
 */

package com.suns.sendtype;

import com.suns.config.BusiConst;
import com.suns.config.KafkaConst;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * ClassName: KafkaAsyncProducer <br>
 * Description: 发送消息--异步模式 <br>
 * @author mk
 * @Date 2018-12-17 11:23 <br>
 * @version
 */
public class KafkaAsyncProducer {

    static KafkaProducer<String,String> producer = null;
    public static final String kafka_key="kafka-key-001";
    public static final String kafka_value="hello,卡夫卡";
    public static void main(String[] args) {


        producer = new KafkaProducer<>(KafkaConst.producerConfig(StringSerializer.class, StringSerializer.class));
        try {
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(BusiConst.HELLO_TOPIC, kafka_key, kafka_value);
            producer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (null != e) {
                        e.printStackTrace();
                    }
                    if (null != recordMetadata) {
                        System.out.println(String.format("kafka发送消息成功！主题topic:%s,分区partition:%d,偏移量offset:%d",
                                recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset()));
                    }
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }

    }
}
