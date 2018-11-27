/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service.consumer.topic <br>
 *
 * @author mk <br>
 * Date:2018-11-27 10:22 <br>
 */

package com.suns.service.consumer.topic;

import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * ClassName: DataCenter <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-27 10:22 <br>
 * @version
 */
@Component
public class DataCenter implements MessageListener {
    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String msg = textMessage.getText();
            System.out.println(this.getClass().getName()+" receive msg:"+msg);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
