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

import java.util.Collections;
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
        public ConsumerWorker(KafkaConsumer consumer) {
            this.consumer = consumer;
        }

        @Override
        public void run() {

            String id = Thread.currentThread().getId() + "-" + System.identityHashCode(consumer);
            System.out.println(id+",kafka准备接收消息");
            while(true){
                ConsumerRecords<Object, Object> records = consumer.poll(100);
                for(ConsumerRecord record : records){
                    System.out.println(id+",kafka接收消息："+record);
                }
            }
        }
    }

    public static void main(String[] args) {
        consumer = new KafkaConsumer<>(KafkaConst.consumerConfig("concurrent123", StringDeserializer.class, StringDeserializer.class));
        consumer.subscribe(Collections.singletonList(BusiConst.HELLO_TOPIC));
        for(int i=0;i<BusiConst.CONCURRENT_PARTITIONS_COUNT;i++){
            executorService.submit(new ConsumerWorker(consumer));
        }
    }

}
