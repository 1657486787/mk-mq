/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.jq <br>
 *
 * @author mk <br>
 * Date:2018-12-2 12:04 <br>
 */

package com.suns.jq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * ClassName: JMSJQProducer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-2 12:04 <br>
 * @version
 */
public class JMSJQProducer {

    public static String ACTIVEMQ_BROKERURL = "failover:(tcp://localhost:61616,tcp://localhost:61617,tcp://localhost:61618)";
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
            Destination destination  = session.createQueue("jq.queue");//点对点模式
            //消息的生产者
            MessageProducer producer = session.createProducer(destination);
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
