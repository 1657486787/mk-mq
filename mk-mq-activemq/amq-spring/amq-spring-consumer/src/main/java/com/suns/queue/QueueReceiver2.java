/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.queue <br>
 *
 * @author mk <br>
 * Date:2018-11-24 9:57 <br>
 */

package com.suns.queue;

import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * ClassName: QueueReceiver2 <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-24 9:57 <br>
 * @version
 */
@Component
public class QueueReceiver2 implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            String msg = ((TextMessage) message).getText();
            System.out.println(" QueueReceiver2 接收消息："+msg);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
