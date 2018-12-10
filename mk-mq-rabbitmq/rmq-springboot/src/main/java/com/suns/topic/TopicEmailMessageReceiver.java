/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.topic <br>
 *
 * @author mk <br>
 * Date:2018-12-10 15:51 <br>
 */

package com.suns.topic;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * ClassName: TopicEmailMessageReceiver <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-10 15:51 <br>
 * @version
 */
@Component
@RabbitListener(queues = "sb.info.email")
public class TopicEmailMessageReceiver {

    @RabbitHandler
    public void process(String msg){
        System.out.println("TopicEmailMessageReceiver  : " +msg);
    }
}
