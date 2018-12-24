/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service <br>
 *
 * @author mk <br>
 * Date:2018-12-24 10:59 <br>
 */

package com.suns.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

/**
 * ClassName: KafkaConsumer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-24 10:59 <br>
 * @version
 */
public class KafkaConsumer implements MessageListener<String,String> {

    @Override
    public void onMessage(ConsumerRecord<String, String> data) {
        System.out.println(String.format(Thread.currentThread().getId()+"-,KafkaConsumer接受消息,主题：%s,分区：%s,偏移量：%s,key:%s,value:%s",
                data.topic(),data.partition(),data.offset(),data.key(),data.value()));
    }
}
