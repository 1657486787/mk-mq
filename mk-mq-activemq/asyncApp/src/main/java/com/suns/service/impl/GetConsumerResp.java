/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service.impl <br>
 *
 * @author mk <br>
 * Date:2018-11-27 14:14 <br>
 */

package com.suns.service.impl;

import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * ClassName: GetConsumerResp <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-27 14:14 <br>
 * @version
 */
@Component
public class GetConsumerResp implements MessageListener {

    @Override
    public void onMessage(Message message) {

        TextMessage textMessage = (TextMessage) message;
        try {
            String msg = textMessage.getText();
            System.out.println("GetConsumerResp receive msg:"+msg +", and JMSCorrelationID = "+message.getJMSCorrelationID());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
