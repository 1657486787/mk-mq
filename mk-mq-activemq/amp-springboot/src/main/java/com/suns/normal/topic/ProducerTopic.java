/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.normal.topic <br>
 *
 * @author mk <br>
 * Date:2018-11-26 15:04 <br>
 */

package com.suns.normal.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;

/**
 * ClassName: ProducerTopic <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-26 15:04 <br>
 * @version
 */
@Component
public class ProducerTopic {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    public void sendMessage(Destination destination, final String message){
        jmsMessagingTemplate.convertAndSend(destination,message);
    }
}
