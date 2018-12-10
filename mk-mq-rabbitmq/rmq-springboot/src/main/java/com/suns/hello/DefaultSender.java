/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.hello <br>
 *
 * @author mk <br>
 * Date:2018-12-10 15:30 <br>
 */

package com.suns.hello;

import com.suns.RmConst;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ClassName: DefaultSender <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-10 15:30 <br>
 * @version
 */
@Component
public class DefaultSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void send(String msg) {
        String sendMsg = msg +"---"+ System.currentTimeMillis();
        System.out.println("Sender : " + sendMsg);
        this.rabbitTemplate.convertAndSend(RmConst.QUEUE_HELLO, sendMsg);
        this.rabbitTemplate.convertAndSend(RmConst.QUEUE_USER, sendMsg);
    }
}
