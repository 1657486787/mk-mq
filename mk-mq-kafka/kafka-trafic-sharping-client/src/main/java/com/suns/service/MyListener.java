/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service <br>
 *
 * @author mk <br>
 * Date:2018-12-24 17:28 <br>
 */

package com.suns.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * ClassName: MyListener <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-24 17:28 <br>
 * @version
 */
public class MyListener {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    @KafkaListener(topics = {"traffic-shaping-result"})
    public void listen(ConsumerRecord<?, ?> record) {
        logger.info("收到服务器的应答: " + record.value().toString());
    }
}
