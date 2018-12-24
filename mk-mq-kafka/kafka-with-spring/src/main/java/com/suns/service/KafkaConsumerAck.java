/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service <br>
 *
 * @author mk <br>
 * Date:2018-12-24 11:00 <br>
 */

package com.suns.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

/**
 * ClassName: KafkaConsumerAck <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-24 11:00 <br>
 * @version
 */
public class KafkaConsumerAck implements AcknowledgingMessageListener<String,String> {

    @Override
    public void onMessage(ConsumerRecord<String, String> data, Acknowledgment acknowledgment) {
        System.out.println(String.format(Thread.currentThread().getId()+"-,KafkaConsumerAck接受消息,主题：%s,分区：%s,偏移量：%s,key:%s,value:%s",
                data.topic(),data.partition(),data.offset(),data.key(),data.value()));

        //手工确认
        acknowledgment.acknowledge();
        System.out.println(String.format(Thread.currentThread().getId()+"-,KafkaConsumerAck手工确认..."));
    }
}
