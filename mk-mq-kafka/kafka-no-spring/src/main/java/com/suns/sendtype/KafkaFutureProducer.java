/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.sendtype <br>
 *
 * @author mk <br>
 * Date:2018-12-17 11:02 <br>
 */

package com.suns.sendtype;

import com.suns.config.BusiConst;
import com.suns.config.KafkaConst;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.concurrent.Future;

/**
 * ClassName: KafkaFutureProducer <br>
 * Description: 发送消息--未来某个时候get发送结果 <br>
 * @author mk
 * @Date 2018-12-17 11:02 <br>
 * @version
 */
public class KafkaFutureProducer {

    private static KafkaProducer producer = null;
    public static final String kafka_key="kafka-key-001";
    public static final String kafka_value="hello,卡夫卡";

    public static void main(String[] args) {

        /*消息生产者*/
        producer = new KafkaProducer(KafkaConst.producerConfig(StringSerializer.class,StringSerializer.class));

        try {
            ProducerRecord<String,String> producerRecord = new ProducerRecord<>(BusiConst.HELLO_TOPIC,kafka_key,kafka_value);
            Future<RecordMetadata> future = producer.send(producerRecord);
            RecordMetadata recordMetadata = future.get();
            System.out.println(String.format("kafka发送消息成功！主题topic:%s,分区partition:%d,偏移量offset:%d",
                    recordMetadata.topic(),recordMetadata.partition(),recordMetadata.offset()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }

    }
}
