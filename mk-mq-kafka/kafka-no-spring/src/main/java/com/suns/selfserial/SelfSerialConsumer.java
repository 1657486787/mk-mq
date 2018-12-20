/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.selfserial <br>
 *
 * @author mk <br>
 * Date:2018-12-20 10:53 <br>
 */

package com.suns.selfserial;

import com.suns.config.BusiConst;
import com.suns.config.KafkaConst;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

/**
 * ClassName: SelfSerialConsumer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-20 10:53 <br>
 * @version
 */
public class SelfSerialConsumer {

    public static void main(String[] args) {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(KafkaConst.consumerConfig("selfseria_group_id",StringDeserializer.class,SelfDeserializer.class));
        consumer.subscribe(Collections.singletonList(BusiConst.SELF_PARTITION_TOPIC));
        String id = Thread.currentThread().getId() + "-" + System.identityHashCode(consumer);
        System.out.println(id+",kafka准备接收消息");
        while(true){
            ConsumerRecords<String, String> consumerRecords = consumer.poll(500);
            for(ConsumerRecord record : consumerRecords){
                System.out.println(String.format(
                        "主题：%s，分区：%d，偏移量：%d，key：%s，value：%s",
                        record.topic(),record.partition(),record.offset(),
                        record.key(),record.value()));
            }
        }


    }
}
