/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.embed <br>
 *
 * @author mk <br>
 * Date:2018-11-26 18:10 <br>
 */

package com.suns.embed;

import com.suns.usemq.AMQConstant;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * ClassName: EmbedJmsConsumer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-26 18:10 <br>
 * @version
 */
public class EmbedJmsConsumer {

    public static String ACTIVEMQ_BROKERURL = "tcp://localhost:61000";/*自定义端口*/
    public static String ACTIVEMQ_USERNAME = ActiveMQConnection.DEFAULT_USER;/*默认连接用户名*/
    public static String ACTIVEMQ_PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD ;/*默认连接密码*/
    public static String HELLO_ACTIVEMQ_QUEUE = AMQConstant.HELLO_ACTIVEMQ_QUEUE;//消息模式：点对点模式

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_USERNAME,ACTIVEMQ_PASSWORD,ACTIVEMQ_BROKERURL);
        final Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("embed.queue");
        MessageConsumer consumer = session.createConsumer(destination);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    TextMessage textMessage = (TextMessage)message;
                    System.out.println("receive ["+textMessage.getText()+"] from ["+connection.getClientID()+"]   success...");
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
