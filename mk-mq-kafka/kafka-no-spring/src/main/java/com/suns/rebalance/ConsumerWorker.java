/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.rebalance <br>
 *
 * @author mk <br>
 * Date:2018-12-21 15:38 <br>
 */

package com.suns.rebalance;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * ClassName: ConsumerWorker <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-21 15:38 <br>
 * @version
 */
public class ConsumerWorker implements Runnable {



    private KafkaConsumer<String,String> consumer;
    /*用来保存每个消费者当前读取分区的偏移量*/
    private final Map<TopicPartition, OffsetAndMetadata> currOffsets;

    private String topic;
    private Properties properties;
    boolean isStop;
    public ConsumerWorker(String topic, boolean isStop){
        this.topic = topic;
        this.isStop = isStop;
        properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"rebalance_group_id");


        currOffsets = new HashMap<>();
        consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Collections.singletonList(topic), new HandlerRebalance(currOffsets,consumer));
        //        consumer.subscribe(Collections.singletonList(topic));
    }

    @Override
    public void run() {

        int count = 0;
        long offset = 0;
        try{
            String id = Thread.currentThread().getId() + "-" + System.identityHashCode(consumer);
            System.out.println(id+",kafka准备接收消息");

            while(true){
                ConsumerRecords<String, String> records = consumer.poll(500);
                for(ConsumerRecord record : records){
                    System.out.println(id+"|kafka接收消息："+String.format(
                            "处理主题：%s，分区：%d，偏移量：%d，" +
                                    "key：%s，value：%s",
                            record.topic(),record.partition(),
                            record.offset(),record.key(),record.value()));
                    offset = record.offset()+1;
                    currOffsets.put(new TopicPartition(topic,record.partition()),new OffsetAndMetadata(offset,"no metadata"));
                    count++;
                    //执行业务sql...
                }

                if(currOffsets.size()>0){
                    for(TopicPartition topicPartition : currOffsets.keySet()){
                        HandlerRebalance.partitionOffsetMap.put(topicPartition,currOffsets.get(topicPartition).offset());
                    }
                    //提交事务,同时将业务和偏移量入库
                }

                //消费者推出，模拟再均衡
                if(isStop && count >= 5){
                    System.out.println(id+"-将关闭，当前偏移量为："+currOffsets);
                    consumer.commitSync();
                    break;
                }

                consumer.commitSync();
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println(Thread.currentThread().getId() + "-" + " consumer.close()....");
            consumer.close();
        }

    }
}
