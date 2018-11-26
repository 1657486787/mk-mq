/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.replayto <br>
 *
 * @author mk <br>
 * Date:2018-11-26 15:25 <br>
 */

package com.suns.replayto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.*;

/**
 * ClassName: ProducerR <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-26 15:25 <br>
 * @version
 */
@Service
public class ProducerR {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private ProducerGetResponse producerGetResponse;

    public void sendMessage(Destination destination, final String message){
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(message);

                //配置，告诉消费者如何应答
//                Destination temporaryQueue = session.createTemporaryQueue();
                Destination responseQueue = session.createQueue("springboot.produceR-responseQueue");

                MessageConsumer responseConsumer = session.createConsumer(responseQueue);
//                responseConsumer.setMessageListener(producerGetResponse);//生产者监听结果ProducerGetResponse
                textMessage.setJMSReplyTo(responseQueue);

                String uid = "UID:"+System.currentTimeMillis();
                textMessage.setJMSCorrelationID(uid);
                return textMessage;
            }
        });
    }
}
