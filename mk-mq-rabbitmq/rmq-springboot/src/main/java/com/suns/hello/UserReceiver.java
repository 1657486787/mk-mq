/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.fanout <br>
 *
 * @author mk <br>
 * Date:2018-12-10 15:38 <br>
 */

package com.suns.hello;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * ClassName: UserReceiver <br>
 * Description: RabbitMQ手工确认消息 <br>
 * @author mk
 * @Date 2018-12-10 15:38 <br>
 * @version
 */
@Component
public class UserReceiver implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message) {
        String msg = new String(message.getBody());
        System.out.println("UserReceiver1>>>>>>>接收到消息:"+msg);
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            String msg = new String(message.getBody());
            System.out.println("UserReceiver>>>>>>>接收到消息:"+msg);
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),
                        false);
                System.out.println("UserReceiver>>>>>>消息已消费");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),
                        false,true);
                System.out.println("UserReceiver>>>>>>拒绝消息，要求Mq重新派发");
                throw e;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
