/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.queue <br>
 *
 * @author mk <br>
 * Date:2018-11-23 18:36 <br>
 */

package com.suns.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * ClassName: QueueSender <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-23 18:36 <br>
 * @version
 */
@Component("queueSender")
public class QueueSender {

    @Autowired
    @Qualifier("queueJmsTemplate")
    private JmsTemplate jmsTemplate;


    public void send(String queueName, final String message){
        jmsTemplate.send(queueName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(message);
            }
        });
    }
}
