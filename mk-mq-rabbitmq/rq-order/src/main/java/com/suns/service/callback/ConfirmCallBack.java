/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service.callback <br>
 *
 * @author mk <br>
 * Date:2018-12-10 10:11 <br>
 */

package com.suns.service.callback;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.stereotype.Service;

/**
 * ClassName: ConfirmCallBack <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-10 10:11 <br>
 * @version
 */
@Service
public class ConfirmCallBack implements RabbitTemplate.ConfirmCallback {

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if(ack){
            System.out.println(" ack....");
        }else{
            System.out.println(" not ack..."+cause);
        }

    }
}
