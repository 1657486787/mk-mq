/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service.impl <br>
 *
 * @author mk <br>
 * Date:2018-11-27 11:41 <br>
 */

package com.suns.service.impl;

import com.suns.service.IUserReg;
import com.suns.service.busi.SaveUser;
import com.suns.service.busi.SendEmail;
import com.suns.service.busi.SendSms;
import com.suns.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.*;

/**
 * ClassName: MQProcess <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-27 11:41 <br>
 * @version
 */
@Service
@Qualifier("mq")
public class MQProcess implements IUserReg {

    @Autowired
    private SaveUser saveUser;

    @Autowired
    @Qualifier("queueJmsTemplate")
    private JmsTemplate jmsTemplate;
    @Autowired
    @Qualifier("topicJmsTemplate")
    private JmsTemplate topicJmsTemplate;

    @Autowired
    private GetConsumerResp getConsumerResp;

    @Override
    public boolean userRegister(final User user) {
        try{
            saveUser.saveUser(user);

            //queue
            sendMsg(jmsTemplate,"asyncApp.queue.email",user.getEmail());
            sendMsg(jmsTemplate,"asyncApp.queue.sms",user.getPhoneNumber(),true);

            //topic
            sendMsg(topicJmsTemplate,"asyncApp.topic",user.toString());

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void sendMsg(JmsTemplate jmsTemplate, String queueName, final String msg) throws JMSException {
        sendMsg(jmsTemplate,queueName,msg,false);
    }

    private void sendMsg(JmsTemplate jmsTemplate, String queueName, final String msg, final boolean needReplayTo) throws JMSException {
        jmsTemplate.send(queueName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(msg);

                //配置消费者应答相关内容
                if(needReplayTo){
                    System.out.println("Ready send msg:[" + msg + "]," + "the msg need reply.");

                    //临时队列或者queue
                    Destination temporaryQueue = session.createTemporaryQueue();
                    MessageConsumer consumer = session.createConsumer(temporaryQueue);
                    //监听消费者应答的监听器
                    consumer.setMessageListener(getConsumerResp);
//                    Destination temporaryQueue = session.createQueue("asyncApp.queue.sms.resp");
                    textMessage.setJMSReplyTo(temporaryQueue);
                    //消费者应答的id，发送出的消息和应答消息进行匹配,就是唯一的订单号
                    String jMSCorrelationID = System.currentTimeMillis()+"";
                    textMessage.setJMSCorrelationID(jMSCorrelationID);
                    System.out.println("jMSCorrelationID===>"+jMSCorrelationID);
                }
                return textMessage;
            }
        });

        System.out.println(this.getClass().getName()+" send msg:[" + msg + "] to queueName:"+queueName);
    }
}
