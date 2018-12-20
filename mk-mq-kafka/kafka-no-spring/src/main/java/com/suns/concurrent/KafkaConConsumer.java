/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.concurrent <br>
 *
 * @author mk <br>
 * Date:2018-12-17 11:35 <br>
 */

package com.suns.concurrent;

import com.suns.config.BusiConst;
import com.suns.config.KafkaConst;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName: KafkaConConsumer <br>
 * Description: 多线程下使用消费者 <br>
 * @author mk
 * @Date 2018-12-17 11:35 <br>
 * @version
 */
public class KafkaConConsumer {

    static KafkaConsumer<Object, Object> consumer = null;

    public static ExecutorService executorService = Executors.newFixedThreadPool(BusiConst.CONCURRENT_PARTITIONS_COUNT);


    public static class ConsumerWorker implements Runnable{

        private KafkaConsumer consumer;

        public ConsumerWorker(Map<String,Object> map, String topic) {
            Properties properties = new Properties();
            properties.putAll(map);
            this.consumer = new KafkaConsumer(properties);
            consumer.subscribe(Collections.singletonList(topic));
        }

        @Override
        public void run() {

            String id = Thread.currentThread().getId() + "-" + System.identityHashCode(consumer);
            System.out.println(id+",kafka准备接收消息");

            try{
                while(true){
                    ConsumerRecords<Object, Object> records = consumer.poll(100);
                    for(ConsumerRecord record : records){
                        System.out.println(id+",kafka接收消息："+record);
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                consumer.close();
            }
        }
    }

    public static void main(String[] args) {
        Map<String, Object> map = KafkaConst.consumerConfigMap("concurrent", StringDeserializer.class, StringDeserializer.class);
        for(int i=0;i<BusiConst.CONCURRENT_PARTITIONS_COUNT;i++){
            executorService.submit(new ConsumerWorker(map,BusiConst.CONCURRENT_USER_INFO_TOPIC));
        }
    }

}
