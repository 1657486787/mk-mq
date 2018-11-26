/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.queue <br>
 *
 * @author mk <br>
 * Date:2018-11-24 9:55 <br>
 */

package com.suns.queue;

import com.suns.vo.User;
import org.springframework.stereotype.Component;

import javax.jms.*;

/**
 * ClassName: QueueReceiver1 <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-24 9:55 <br>
 * @version
 */
@Component
public class QueueReceiver1 implements MessageListener {
    @Override
    public void onMessage(Message message) {
        //消息类型：文本，map，对象，字节，Stream流
        try {
            if(message instanceof TextMessage){
                String msg = ((TextMessage) message).getText();
                System.out.println(" QueueReceiver1 接收消息TextMessage："+msg);
            }
            if(message instanceof MapMessage){
                MapMessage mapMessage = ((MapMessage) message);
                System.out.println(" QueueReceiver1 接收消息MapMessage："+mapMessage.getString("mapMessage_String_key")+","+mapMessage.getInt("mapMessage_int_key") );
            }
            if(message instanceof ObjectMessage){
                User user = (User) ((ObjectMessage) message).getObject();
                System.out.println(" QueueReceiver1 接收消息ObjectMessage："+user);
            }
            if(message instanceof BytesMessage){
                String bytesMessage = ((BytesMessage) message).getStringProperty("测试bytesMessage");
                System.out.println(" QueueReceiver1 接收消息BytesMessage："+bytesMessage);
            }
            if(message instanceof StreamMessage){
                StreamMessage streamMessage = ((StreamMessage) message);
                System.out.println(" QueueReceiver1 接收消息StreamMessage："+streamMessage.readString());
                System.out.println(" QueueReceiver1 接收消息StreamMessage："+streamMessage.readString());
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
