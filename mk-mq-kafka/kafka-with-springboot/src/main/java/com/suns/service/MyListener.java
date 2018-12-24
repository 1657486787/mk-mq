/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service <br>
 *
 * @author mk <br>
 * Date:2018-12-24 15:11 <br>
 */

package com.suns.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * ClassName: MyListener <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-24 15:11 <br>
 * @version
 */
@Component
public class MyListener{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @KafkaListener(topics = {"springboot-topic-001"})
    public void listener(ConsumerRecord<String, Object> data) {
        logger.info(String.format(Thread.currentThread().getId()+"-,MyListener接受消息,主题：%s,分区：%s,偏移量：%s,key:%s,value:%s",
                data.topic(),data.partition(),data.offset(),data.key(),data.value()));
    }

}
