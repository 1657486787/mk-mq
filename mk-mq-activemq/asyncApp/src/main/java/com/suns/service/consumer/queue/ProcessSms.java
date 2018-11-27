/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service.consumer.queue <br>
 *
 * @author mk <br>
 * Date:2018-11-27 10:21 <br>
 */

package com.suns.service.consumer.queue;

import com.suns.service.busi.SendSms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * ClassName: ProcessSms <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-27 10:21 <br>
 * @version
 */
@Component
public class ProcessSms implements MessageListener {

    @Autowired
    private SendSms sendSms;
    @Autowired
    private ReplayToProducer replayToProducer;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String msg = textMessage.getText();
            String checkCode = sendSms.sendSms(msg);
            System.out.println("ProcessSms receive msg:"+msg +" ,checkCode="+checkCode+", and JMSCorrelationID = "+message.getJMSCorrelationID());

            //相应
            replayToProducer.send(message,checkCode);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
