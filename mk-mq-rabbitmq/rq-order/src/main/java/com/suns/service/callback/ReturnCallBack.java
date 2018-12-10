/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service.callback <br>
 *
 * @author mk <br>
 * Date:2018-12-10 10:12 <br>
 */

package com.suns.service.callback;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * ClassName: ReturnCallBack <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-10 10:12 <br>
 * @version
 */
@Service
public class ReturnCallBack implements RabbitTemplate.ReturnCallback {
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        try {
            String msg = new String(message.getBody(),"UTF-8");
            System.out.println("replyCode["+replyCode+"],replyText["+replyText+"],exchange["+exchange+"],routingKey["+routingKey+"],body["+msg+"]");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

}
