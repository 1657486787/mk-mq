/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.topic <br>
 *
 * @author mk <br>
 * Date:2018-11-24 9:52 <br>
 */

package com.suns.topic;

import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * ClassName: TopicReceiver2 <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-24 9:52 <br>
 * @version
 */
@Component("topicReceiver2")
public class TopicReceiver2 implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            String msg = ((TextMessage) message).getText();
            System.out.println(" topicReceiver2 接收消息："+msg);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
