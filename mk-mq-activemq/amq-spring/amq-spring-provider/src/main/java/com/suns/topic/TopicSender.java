/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.topic <br>
 *
 * @author mk <br>
 * Date:2018-11-24 9:28 <br>
 */

package com.suns.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * ClassName: TopicSender <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-24 9:28 <br>
 * @version
 */
@Component("topicSender")
public class TopicSender {

    @Autowired
    @Qualifier("topicJmsTemplate")
    private JmsTemplate jmsTemplate;

    public void send(String topicName, final String message){
        jmsTemplate.send(topicName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(message);
                return textMessage;
            }
        });
    }
}
