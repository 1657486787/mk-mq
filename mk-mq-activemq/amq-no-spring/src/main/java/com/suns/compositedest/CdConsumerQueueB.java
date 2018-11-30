/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.compositedest <br>
 *
 * @author mk <br>
 * Date:2018-11-30 9:35 <br>
 */

package com.suns.compositedest;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * ClassName: CdConsumerQueueB <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-30 9:35 <br>
 * @version
 */
public class CdConsumerQueueB {


    public static String ACTIVEMQ_BROKERURL = ActiveMQConnection.DEFAULT_BROKER_URL;/*默认连接用户名*/
    public static String ACTIVEMQ_USERNAME = ActiveMQConnection.DEFAULT_USER;/*默认连接用户名*/
    public static String ACTIVEMQ_PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD ;/*默认连接密码*/

    public static void main(String[] args) {
        Connection connection = null;
        try {
            //连接工厂
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_USERNAME, ACTIVEMQ_PASSWORD, ACTIVEMQ_BROKERURL);
            //连接
            connection = connectionFactory.createConnection();
            //启动连接
            connection.start();
            //会话
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //目的地
            Destination destination = session.createQueue("cd.queue");//点对点模式
            //消费者
            MessageConsumer consumer = session.createConsumer(destination);
            //消息
            Message message = null;
            while (null != (message = consumer.receive())) {
                TextMessage textMessage = (TextMessage) message;
                System.out.println("receive [" + textMessage.getText() + "] from [" + connection.getClientID() + "]   success...");
            }
            final Connection finalConnection = connection;
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        TextMessage textMessage = (TextMessage) message;
                        System.out.println("receive [" + textMessage.getText() + "] from [" + finalConnection.getClientID() + "]   success...");
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
