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
    public String receive(String text){
        System.out.println(this.getClass().getName() + " receive:"+text);
        return "ConsumerRAnnotation I want you.......";
    }
}
