/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.virtualtopic <br>
 *
 * @author mk <br>
 * Date:2018-11-29 17:55 <br>
 */

package com.suns.virtualtopic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * ClassName: VirutalConsumerB <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-29 17:55 <br>
 * @version
 */
public class VirutalConsumerB {

    //默认连接用户名
    private static final String USERNAME
            = ActiveMQConnection.DEFAULT_USER;
    //默认连接密码
    private static final String PASSWORD
            = ActiveMQConnection.DEFAULT_PASSWORD;
    //默认连接地址
    private static final String BROKEURL
            = ActiveMQConnection.DEFAULT_BROKER_URL;
    //发送的消息数量
    private static final int SENDNUM = 10;

    public static void main(String[] args) {
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;
        MessageConsumer consumer;

        connectionFactory = new ActiveMQConnectionFactory(USERNAME,PASSWORD,BROKEURL);
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("Consumer.B.VirtualTopic.vtgroup");//注意，这里是createQueue
            consumer = session.createConsumer(destination);

            final Connection finalConnection = connection;
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        TextMessage textMessage = (TextMessage) message;
                        System.out.println("receive ["+textMessage.getText()+"] from ["+ finalConnection.getClientID()+"]   success...");
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
