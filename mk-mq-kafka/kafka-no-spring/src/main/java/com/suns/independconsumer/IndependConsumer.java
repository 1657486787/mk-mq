/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.independconsumer <br>
 *
 * @author mk <br>
 * Date:2018-12-21 14:23 <br>
 */

package com.suns.independconsumer;

import com.suns.config.BusiConst;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * ClassName: IndependConsumer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-21 14:23 <br>
 * @version
 */
public class IndependConsumer {

    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
//        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"independence_group_id");
        KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(properties);
//        consumer.subscribe(Collections.singletonList(BusiConst.INDEPENDENCE_CONSUMER_TOPIC));

        //独立消费者
        List<TopicPartition> list = new ArrayList<>();
        List<PartitionInfo> partitionInfos = consumer.partitionsFor(BusiConst.INDEPENDENCE_CONSUMER_TOPIC);
        if(null != partitionInfos){
            for(PartitionInfo partitionInfo : partitionInfos){
                System.out.printf("主题：%s,分区：%s",partitionInfo.topic(),partitionInfo.partition()).println();
                TopicPartition topicPartition = new TopicPartition(partitionInfo.topic(), partitionInfo.partition());
                list.add(topicPartition);
            }
        }
        consumer.assign(list);

        String id = Thread.currentThread().getId() + "-" + System.identityHashCode(consumer);
        System.out.println(id+",kafka准备接收消息");
        while(true){
            ConsumerRecords<Object, Object> records = consumer.poll(500);
            for(ConsumerRecord record : records){
                System.out.println(id+",kafka接收消息："+record);
            }
        }
    }
}
