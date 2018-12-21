/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.commit <br>
 *
 * @author mk <br>
 * Date:2018-12-21 10:08 <br>
 */

package com.suns.commit;

import com.suns.config.BusiConst;
import com.suns.config.KafkaConst;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * ClassName: ProducerCommit <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-21 10:08 <br>
 * @version
 */
public class ProducerCommit {


    public static KafkaProducer<String, String> producer = null;
    public static void main(String[] args) {

        try{
            producer = new KafkaProducer<>(KafkaConst.producerConfig(StringSerializer.class, StringSerializer.class));
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(BusiConst.CONSUMER_COMMIT_TOPIC, "测试手工提交偏移量key", "测试手工提交偏移量value");

            for(int i=0;i<100;i++){
                producer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception exception) {
                        final String id = Thread.currentThread().getId()+"-"+System.identityHashCode(producer);
                        System.out.println(String.format(id+",kafka发送消息成功！主题topic:%s,分区partition:%d,偏移量offset:%d",
                                recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset()));
                    }
                });
                Thread.sleep(1);
            }
        }catch (Exception e ){
            e.printStackTrace();
        }finally {
            producer.close();
        }
    }
}
