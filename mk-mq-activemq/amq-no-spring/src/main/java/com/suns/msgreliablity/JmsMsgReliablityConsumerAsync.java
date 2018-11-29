/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.msgreliablity <br>
 *
 * @author mk <br>
 * Date:2018-11-29 9:46 <br>
 */

package com.suns.msgreliablity;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.management.JMException;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: JmsMsgReliablityConsumerAsync <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-29 9:46 <br>
 * @version
 */
public class JmsMsgReliablityConsumerAsync {

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
            //启动连接
            connection.start();
            //会话

            //Session.AUTO_ACKNOWLEDGE自动确认
            //Session.CLIENT_ACKNOWLEDGE客户端确认和message.acknowledge();结合使用
            //Session.DUPS_OK_ACKNOWLEDGE类似于AUTO_ACK确认机制，为自动批量确认而生，而且具有“延迟”确认的特点
            //Session.SESSION_TRANSACTED事务,必须要session.commit()才会真实消费消息， 还有第一个参数必须设置为true,否则抛异常javax.jms.JMSException: acknowledgeMode SESSION_TRANSACTED cannot be used for an non-transacted Session
            final Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
            //目的地
            Destination destination  = session.createQueue("MsgReliability");//点对点模式
            //消费者
            MessageConsumer consumer = session.createConsumer(destination);
            //消息
            final Connection finalConnection = connection;
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        TextMessage textMessage = (TextMessage) message;
//                        message.acknowledge();
                        System.out.println("receive ["+textMessage.getText()+"] from ["+ finalConnection.getClientID()+"]   success...");

                        session.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    throw new RuntimeException("模拟业务抛异常，测试重发，默认重发6次");
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
