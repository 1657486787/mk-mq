/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.replayto.annotation <br>
 *
 * @author mk <br>
 * Date:2018-11-28 9:02 <br>
 */

package com.suns.replayto.annotation;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * ClassName: ConsumerR <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-28 9:02 <br>
 * @version
 */
@Service
public class ConsumerRAnnotation {

    // 使用JmsListener配置消费者监听的队列，其中text是接收到的消息
    @JmsListener(destination = "springboot.replayto.queue.annotation")
    @SendTo("springboot.replayto.queue.annotation.resp")
    public String receive(Message message,String text){
        try {
            System.out.println(this.getClass().getName() + " receive:"+text+",JMSCorrelationID="+message.getJMSCorrelationID());
            return "ConsumerRAnnotation I want you.......,JMSCorrelationID="+message.getJMSCorrelationID();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return "ConsumerRAnnotation I want you.......";
    }
}
