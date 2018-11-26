/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.normal.queue <br>
 *
 * @author mk <br>
 * Date:2018-11-26 14:30 <br>
 */

package com.suns.normal.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * ClassName: ProducerQueue <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-26 14:30 <br>
 * @version
 */
@Service
public class ProducerQueue {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;//JmsMessagingTemplate 包含了jmsTemplate
    @Autowired
    private JmsTemplate jmsTemplate;//更灵活

    // 发送消息，destination是发送到的队列，message是待发送的消息
    public void sendMessage(Destination destination, final String message){
        jmsMessagingTemplate.convertAndSend(destination,message);
        /*jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(message);
            }
        });*/
    }

    public void sendMessage(String queueName, final String message){
        jmsTemplate.send(queueName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(message);
            }
        });
    }
}
