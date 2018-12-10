/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.hello <br>
 *
 * @author mk <br>
 * Date:2018-12-10 15:37 <br>
 */

package com.suns.hello;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * ClassName: HelloReceiver <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-10 15:37 <br>
 * @version
 */
@Component
@RabbitListener(queues = "sb.hello")
public class HelloReceiver {

    @RabbitHandler
    public void process(String msg){
        System.out.println("HelloReceiver : " + msg);
    }
}
