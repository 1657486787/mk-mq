/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.replayto <br>
 *
 * @author mk <br>
 * Date:2018-11-26 15:29 <br>
 */

package com.suns.replayto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;

/**
 * ClassName: ConsumerR <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-26 15:29 <br>
 * @version
 */
@Component
public class ConsumerR {

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = "springboot.replayto.queue")
    public void receive(final Message message, final String text) throws JMSException {
        System.out.println(this.getClass().getName() + " receive:"+text);

        Destination jmsReplyTo = message.getJMSReplyTo();//响应目的地
        jmsTemplate.send(jmsReplyTo, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage("consumerR响应结果："+text);
                return textMessage;
            }
        });
    }
}
