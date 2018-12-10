/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.fanout <br>
 *
 * @author mk <br>
 * Date:2018-12-10 15:32 <br>
 */

package com.suns.fanout;

import com.suns.RmConst;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ClassName: FanoutSender <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-10 15:32 <br>
 * @version
 */
@Component
public class FanoutSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String msg) {
        String sendMsg = msg +"---"+ System.currentTimeMillis();;
        System.out.println("FanoutSender : " + sendMsg);
        this.rabbitTemplate.convertAndSend(RmConst.EXCHANGE_FANOUT, "",sendMsg);
    }
}
