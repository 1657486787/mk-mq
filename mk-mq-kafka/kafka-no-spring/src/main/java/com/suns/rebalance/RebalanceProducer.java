/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.rebalance <br>
 *
 * @author mk <br>
 * Date:2018-12-21 15:13 <br>
 */

package com.suns.rebalance;

import com.suns.config.BusiConst;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: RebalanceProducer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-21 15:13 <br>
 * @version
 */
public class RebalanceProducer {

    public static KafkaProducer<String, String> producer = null;
    public static void main(String[] args) {

        try{
            Properties properties = new Properties();
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
            producer = new KafkaProducer<>(properties);

            final String id = Thread.currentThread().getId()+"-"+System.identityHashCode(producer);
            for(int i=0;i<50;i++){
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>(BusiConst.REBALANCE_TOPIC, "测试分区再平衡key"+i, "测试分区再平衡value");
                producer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception exception) {
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
