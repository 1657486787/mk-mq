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
import com.suns.vo.DemoUser;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.concurrent.*;

/**
 * ClassName: KafkaConProducer <br>
 * Description: 多线程下使用生产者 <br>
 * @author mk
 * @Date 2018-12-17 11:35 <br>
 * @version
 */
public class KafkaConProducer {

    private static final int MSG_SIZE = 10;

    public static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static CountDownLatch cdl = new CountDownLatch(MSG_SIZE);

    public static class ProduceWorker implements Runnable{

        private KafkaProducer producer;
        private ProducerRecord producerRecord;
        public ProduceWorker(KafkaProducer producer, ProducerRecord<String,String> producerRecord) {
            this.producer = producer;
            this.producerRecord = producerRecord;
        }

        @Override
        public void run() {

            final String id = Thread.currentThread().getId()+"-"+System.identityHashCode(producer);

            producer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {

                    if (null != e) {
                        e.printStackTrace();
                    }
                    if (null != recordMetadata) {
                        System.out.println(String.format(id+",kafka发送消息成功！主题topic:%s,分区partition:%d,偏移量offset:%d",
                                recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset()));
                    }

                }
            });

            System.out.println(id+":数据["+producerRecord+"]已发送。");
            cdl.countDown();

        }
    }

    private static DemoUser makeUser(int id){
        DemoUser demoUser = new DemoUser(id);
        String userName = "mk_"+id;
        demoUser.setName(userName);
        return demoUser;
    }

    public static void main(String[] args) {

        /*消息生产者*/
        KafkaProducer producer = new KafkaProducer(KafkaConst.producerConfig(StringSerializer.class,StringSerializer.class));
        try {
            for(int i=0;i<MSG_SIZE;i++){
                DemoUser demoUser = makeUser(i);
                ProducerRecord<String,String> producerRecord = new ProducerRecord<>(BusiConst.CONCURRENT_USER_INFO_TOPIC,null,
                        System.currentTimeMillis(),String.valueOf(demoUser.getId()),demoUser.toString());
                executorService.submit(new ProduceWorker(producer,producerRecord));
            }
            cdl.await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
            executorService.shutdown();
        }
    }
}
