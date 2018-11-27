/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service.consumer.queue <br>
 *
 * @author mk <br>
 * Date:2018-11-27 14:18 <br>
 */

package com.suns.service.consumer.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;

/**
 * ClassName: ReplayToProducer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-27 14:18 <br>
 * @version
 */
@Component
public class ReplayToProducer {

    @Autowired
    @Qualifier("queueJmsTemplate")
    private JmsTemplate queueJmsTemplate;

    public void send(final Message message, final String checkCode){
        try {
            Thread.sleep(100);
            Destination jmsReplyTo = message.getJMSReplyTo();


            queueJmsTemplate.send(jmsReplyTo, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    TextMessage textMessage = session.createTextMessage("checkCode=" + checkCode);
                    textMessage.setJMSCorrelationID(message.getJMSCorrelationID());
                    System.out.println("-------------ReplayToProducer,send " + checkCode);
                    return textMessage;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
