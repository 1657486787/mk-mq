/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.durabletopic <br>
 *
 * @author mk <br>
 * Date:2018-11-29 8:58 <br>
 */

package com.suns.durabletopic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * ClassName: JmsDurableTopicConsumerOther <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-29 8:58 <br>
 * @version
 */
public class JmsDurableTopicConsumerOther {

    public static String ACTIVEMQ_BROKERURL = ActiveMQConnection.DEFAULT_BROKER_URL;/*默认连接用户名*/
    public static String ACTIVEMQ_USERNAME = ActiveMQConnection.DEFAULT_USER;/*默认连接用户名*/
    public static String ACTIVEMQ_PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD ;/*默认连接密码*/

    public static void main(String[] args) {
        Connection connection = null;
        try {
            //连接工厂
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_USERNAME,ACTIVEMQ_PASSWORD,ACTIVEMQ_BROKERURL);
            //连接
            connection = connectionFactory.createConnection();

            //需要有个id
            connection.setClientID("DurableTopic____2");

            //启动连接
            connection.start();
            //会话
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //目的地
            Topic destination = session.createTopic("DurableTopic1");//发布订阅(广播)模式

            //消费者创建时变为session.createDurableSubscriber(destination,"任意名字，代表订阅名 ");
            TopicSubscriber durableSubscriber = session.createDurableSubscriber(destination,"durable_test");

            //设置监听，监听消息
            final Connection finalConnection = connection;
            durableSubscriber.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("receive ["+textMessage.getText()+"] from ["+ finalConnection.getClientID()+"]   success...");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        } /*finally {
            if(null != connection){
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }
}
