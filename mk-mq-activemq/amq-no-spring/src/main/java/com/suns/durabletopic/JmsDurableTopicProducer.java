/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.durabletopic <br>
 *
 * @author mk <br>
 * Date:2018-11-28 16:31 <br>
 */

package com.suns.durabletopic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * ClassName: JmsDurableTopicProducer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-28 16:31 <br>
 * @version
 */
public class JmsDurableTopicProducer {

    public static String ACTIVEMQ_BROKERURL = ActiveMQConnection.DEFAULT_BROKER_URL;/*默认连接用户名*/
    public static String ACTIVEMQ_USERNAME = ActiveMQConnection.DEFAULT_USER;/*默认连接用户名*/
    public static String ACTIVEMQ_PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD ;/*默认连接密码*/

    public static void main(String[] args) {
        Connection connection = null;
        try {
            //连接工厂
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_USERNAME,ACTIVEMQ_PASSWORD,ACTIVEMQ_BROKERURL);
            //创建连接
            connection = connectionFactory.createConnection();
            //启动连接
            connection.start();
            //会话，第一个参数表示是否使用事务，第二次参数表示是否自动确认
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //消息的目的地
            Destination destination = session.createTopic("DurableTopic1");//发布订阅(广播)模式

            //消息的生产者
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);//默认是持久化的DeliveryMode.PERSISTENT
            //发送
            for(int i=0;i<2;i++){
                //消息
                String msg = "发送消息："+i;
                TextMessage textMessage = session.createTextMessage(msg);
                producer.send(textMessage);
                System.out.println("send ["+textMessage.getText()+"] to ["+connection.getClientID()+"]   success...");
            }
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if(null != connection){
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
