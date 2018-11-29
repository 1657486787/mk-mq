/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.dlq <br>
 *
 * @author mk <br>
 * Date:2018-11-29 11:27 <br>
 */

package com.suns.dlq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.broker.region.policy.RedeliveryPolicyMap;
import org.apache.activemq.command.ActiveMQDestination;

import javax.jms.*;

/**
 * ClassName: DlqConsumer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-29 11:27 <br>
 * @version
 */
public class DlqConsumer {

    private static final String USERNAME
            = ActiveMQConnection.DEFAULT_USER;//默认连接用户名
    private static final String PASSWORD
            = ActiveMQConnection.DEFAULT_PASSWORD;//默认连接密码
    private static final String BROKEURL
            = ActiveMQConnection.DEFAULT_BROKER_URL;//默认连接地址

    public static void main(String[] args) {
        ActiveMQConnectionFactory connectionFactory;
        ActiveMQConnection connection = null;
        final Session session;
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
            destination = (ActiveMQDestination) session.createQueue("TestDlq2");

            //限制了重发次数策略
            RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
            redeliveryPolicy.setMaximumRedeliveries(1);
            // 拿到消费者端重复策略map
            RedeliveryPolicyMap redeliveryPolicyMap = connection.getRedeliveryPolicyMap();
            // 将消费者端重发策略配置给消费者
            redeliveryPolicyMap.put(destination,redeliveryPolicy);

            //创建消息消费者
            messageConsumer = session.createConsumer(destination);
            messageConsumer.setMessageListener(new MessageListener() {
                public void onMessage(Message message) {
                    try {
                        System.out.println("Accept msg : " +((TextMessage)message).getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                    throw new RuntimeException("test");
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
