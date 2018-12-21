/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.independconsumer <br>
 *
 * @author mk <br>
 * Date:2018-12-21 14:19 <br>
 */

package com.suns.independconsumer;

import com.suns.config.BusiConst;
import com.suns.config.KafkaConst;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.concurrent.TimeUnit;

/**
 * ClassName: NormalProducer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-21 14:19 <br>
 * @version
 */
public class NormalProducer {

    public static KafkaProducer<String, String> producer = null;
    public static void main(String[] args) {
        try{
            producer = new KafkaProducer<>(KafkaConst.producerConfig(StringSerializer.class, StringSerializer.class));

            for(int i=0;i<10;i++){
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>(BusiConst.INDEPENDENCE_CONSUMER_TOPIC, "测试独立消费者key"+i, "测试独立消费者value");
                producer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception exception) {
                        final String id = Thread.currentThread().getId()+"-"+System.identityHashCode(producer);
                        System.out.println(String.format(id+",kafka发送消息成功！主题topic:%s,分区partition:%d,偏移量offset:%d",
                                recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset()));
                    }
                });

                TimeUnit.SECONDS.sleep(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            producer.close();
        }

    }
}
