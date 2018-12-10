/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.topic <br>
 *
 * @author mk <br>
 * Date:2018-12-10 15:52 <br>
 */

package com.suns.topic;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * ClassName: TopicUserMessageReceiver <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-10 15:52 <br>
 * @version
 */
@Component
@RabbitListener(queues = "sb.info.user")
public class TopicUserMessageReceiver {

    @RabbitHandler
    public void process(String msg){
        System.out.println("TopicUserMessageReceiver  : " +msg);
    }
}
