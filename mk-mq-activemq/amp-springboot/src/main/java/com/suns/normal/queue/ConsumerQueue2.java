/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.normal.queue <br>
 *
 * @author mk <br>
 * Date:2018-11-26 14:48 <br>
 */

package com.suns.normal.queue;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * ClassName: ConsumerQueue2 <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-26 14:48 <br>
 * @version
 */
@Component
public class ConsumerQueue2 {

    // 使用JmsListener配置消费者监听的队列，其中text是接收到的消息
    @JmsListener(destination = "springboot.queue")
    public void rec(String text){
        System.out.println(this.getClass().getName() + " receive:"+text);
    }
}
