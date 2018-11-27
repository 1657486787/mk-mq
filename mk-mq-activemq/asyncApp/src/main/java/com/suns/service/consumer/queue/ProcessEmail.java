/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service.consumer.queue <br>
 *
 * @author mk <br>
 * Date:2018-11-27 10:20 <br>
 */

package com.suns.service.consumer.queue;

import com.suns.service.busi.SendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * ClassName: ProcessEmail <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-27 10:20 <br>
 * @version
 */
@Component
public class ProcessEmail implements MessageListener {

    @Autowired
    private SendEmail sendEmail;

    @Override
    public void onMessage(Message message) {
       TextMessage textMessage = (TextMessage) message;
        try {
            String msg = textMessage.getText();
            sendEmail.sendEmail(msg);
            System.out.println("processEmail receive msg:"+msg);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
