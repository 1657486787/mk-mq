/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.replayto <br>
 *
 * @author mk <br>
 * Date:2018-11-26 16:39 <br>
 */

package com.suns.replayto;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * ClassName: ProducerGetResponse <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-26 16:39 <br>
 * @version
 */
@Component
public class ProducerGetResponse{

    @JmsListener(destination = "springboot.produceR-responseQueue")
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage)message;
            System.out.println(this.getClass().getName() + " receive:"+textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
