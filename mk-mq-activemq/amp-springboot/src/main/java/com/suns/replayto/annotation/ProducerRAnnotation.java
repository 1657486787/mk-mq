/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.replayto.annotation <br>
 *
 * @author mk <br>
 * Date:2018-11-28 9:00 <br>
 */

package com.suns.replayto.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;

/**
 * ClassName: ProducerR <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-28 9:00 <br>
 * @version
 */
@Service
public class ProducerRAnnotation {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    // 发送消息，destination是发送到的队列，message是待发送的消息
    public void sendMessage(Destination destination, final String message){
        jmsMessagingTemplate.convertAndSend(destination,message);
    }

    @JmsListener(destination = "springboot.replayto.queue.annotation.resp")
    public void receive(String text){
        System.out.println(this.getClass().getName() + " receive:"+text);
    }

}
