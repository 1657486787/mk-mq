/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.dlq <br>
 *
 * @author mk <br>
 * Date:2018-11-29 11:33 <br>
 */

package com.suns.dlq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.broker.region.policy.RedeliveryPolicyMap;
import org.apache.activemq.command.ActiveMQDestination;

import javax.jms.*;

/**
 * ClassName: ProcessDlqConsumer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-29 11:33 <br>
 * @version
 */
public class ProcessDlqConsumer {

    private static final String USERNAME
            = ActiveMQConnection.DEFAULT_USER;//默认连接用户名
    private static final String PASSWORD
            = ActiveMQConnection.DEFAULT_PASSWORD;//默认连接密码
    private static final String BROKEURL
            = ActiveMQConnection.DEFAULT_BROKER_URL;//默认连接地址

    public static void main(String[] args) {
        ActiveMQConnectionFactory connectionFactory;
        ActiveMQConnection connection = null;
        Session session;
        ActiveMQDestination destination;

        MessageConsumer messageConsumer;//消息的消费者

        //实例化连接工厂
        connectionFactory = new ActiveMQConnectionFactory(USERNAME,PASSWORD,BROKEURL);

        try {
            //通过连接工厂获取连接
            connection = (ActiveMQConnection) connectionFactory.createConnection();
            //启动连接
            connection.start();
            //创建session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = (ActiveMQDestination) session.createQueue("mk.DLQ.>");//默认死信队列的名称为ActiveMQ.DLQ，可以通过activemq.xml自定义自己的前缀
            //创建消息消费者
            messageConsumer = session.createConsumer(destination);
            messageConsumer.setMessageListener(new MessageListener() {
                public void onMessage(Message message) {
                    try {
                        System.out.println("Accept msg : " +((TextMessage)message).getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
//                    throw new RuntimeException("test");
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
