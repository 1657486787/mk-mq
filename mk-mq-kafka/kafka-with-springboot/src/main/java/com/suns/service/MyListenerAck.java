/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service <br>
 *
 * @author mk <br>
 * Date:2018-12-24 16:07 <br>
 */

package com.suns.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;


/**
 * ClassName: MyListenerAck <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-24 16:07 <br>
 * @version
 */
@Component
public class MyListenerAck {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @KafkaListener(topics = {"springboot-topic-ack"},containerFactory = "kafkaListenerContainerFactoryAck")
    public void listener(ConsumerRecord<String, Object> data, Acknowledgment acknowledgment) {
        logger.info(String.format(Thread.currentThread().getId()+"-,MyListenerAck接受消息,主题：%s,分区：%s,偏移量：%s,key:%s,value:%s",
                data.topic(),data.partition(),data.offset(),data.key(),data.value()));
        acknowledgment.acknowledge();
        logger.info("自动确认完毕...");
    }
}
