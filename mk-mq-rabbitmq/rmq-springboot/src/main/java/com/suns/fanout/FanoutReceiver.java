/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.fanout <br>
 *
 * @author mk <br>
 * Date:2018-12-10 15:49 <br>
 */

package com.suns.fanout;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * ClassName: FanoutReceiver <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-10 15:49 <br>
 * @version
 */
@Component
@RabbitListener(queues = "sb.fanout.A")
public class FanoutReceiver {

    @RabbitHandler
    public void process(String hello) {
        System.out.println("FanoutReceiver : " + hello);
    }
}
