/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.topic <br>
 *
 * @author mk <br>
 * Date:2018-11-24 9:49 <br>
 */

package com.suns.topic;

import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * ClassName: TopicReceiver1 <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-24 9:49 <br>
 * @version
 */
@Component("topicReceiver1")
public class TopicReceiver1 implements MessageListener {


    @Override
    public void onMessage(Message message) {
        try {
            String msg = ((TextMessage) message).getText();
            System.out.println(" topicReceiver1 接收消息："+msg);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
