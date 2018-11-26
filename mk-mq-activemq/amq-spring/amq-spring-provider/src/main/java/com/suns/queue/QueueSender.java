/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.queue <br>
 *
 * @author mk <br>
 * Date:2018-11-23 18:36 <br>
 */

package com.suns.queue;

import com.suns.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.jms.*;

/**
 * ClassName: QueueSender <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-23 18:36 <br>
 * @version
 */
@Component("queueSender")
public class QueueSender {

    @Autowired
    @Qualifier("queueJmsTemplate")
    private JmsTemplate jmsTemplate;


    public void send(String queueName, final String message,String msgType){
        //消息类型：文本，map，对象，字节，Stream流
        if(StringUtils.isEmpty(msgType)){
            sendByText(queueName,message);
            return;
        }
        if("textMessage".equals(msgType)){
            sendByText(queueName,message);
            return;
        }
        if("mapMessage".equals(msgType)){
            sendByMap(queueName,message);
            return;
        }
        if("objectMessage".equals(msgType)){
            sendByObject(queueName,message);
            return;
        }
        if("byteMessage".equals(msgType)){
            sendByByte(queueName,message);
            return;
        }
        if("streamMessage".equals(msgType)){
            sendByStream(queueName,message);
            return;
        }
    }


    public void sendByText(String queueName, final String message){
        //消息类型：文本，map，对象，字节，Stream流
        //1.文本
        jmsTemplate.send(queueName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(message);
            }
        });
    }

    public void sendByMap(String queueName, final String message){
        //消息类型：文本，map，对象，字节，Stream流
        //2.map
        jmsTemplate.send(queueName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("mapMessage_String_key","测试mapMessage:"+message);
                mapMessage.setInt("mapMessage_int_key",12);
                return mapMessage;
            }
        });
    }
    public void sendByObject(String queueName, final String message){
        //3.对象,发送ObjectMessage，被发送的实体类必须实现Serializable 接口
        jmsTemplate.send(queueName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                User user = new User();
                user.setName("张三:"+message);
                user.setSex('1');
                user.setAge(18);
                ObjectMessage objectMessage = session.createObjectMessage(user);
                return objectMessage;
            }
        });
    }
    public void sendByByte(String queueName, final String message){
        //4.字节
        jmsTemplate.send(queueName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {

                BytesMessage bytesMessage = session.createBytesMessage();
                bytesMessage.setStringProperty("测试bytesMessage",message);
                return bytesMessage;
            }
        });
    }
    public void sendByStream(String queueName, final String message){
        //5.Stream流
        jmsTemplate.send(queueName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                StreamMessage streamMessage = session.createStreamMessage();
                streamMessage.writeString("111:"+message);
                streamMessage.writeString("222:"+message);
                return streamMessage;
            }
        });
    }
}
