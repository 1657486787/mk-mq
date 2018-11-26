/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.embed <br>
 *
 * @author mk <br>
 * Date:2018-11-26 18:05 <br>
 */

package com.suns.embed;

import com.suns.usemq.AMQConstant;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * ClassName: EmbedJmsProducer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-26 18:05 <br>
 * @version
 */
public class EmbedJmsProducer {

    public static String ACTIVEMQ_BROKERURL = "tcp://localhost:61000";/*自定义端口*/
    public static String ACTIVEMQ_USERNAME = ActiveMQConnection.DEFAULT_USER;/*默认连接用户名*/
    public static String ACTIVEMQ_PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD ;/*默认连接密码*/
    public static String HELLO_ACTIVEMQ_QUEUE = AMQConstant.HELLO_ACTIVEMQ_QUEUE;//消息模式：点对点模式

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_USERNAME,ACTIVEMQ_PASSWORD,ACTIVEMQ_BROKERURL);
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        TextMessage textMessage = session.createTextMessage("内嵌activemq EmbedJmsProducer");
        Destination queue = session.createQueue("embed.queue");
        MessageProducer producer = session.createProducer(queue);
        producer.send(queue,textMessage);
        System.out.println("send ["+textMessage.getText()+"] to ["+connection.getClientID()+"]   success...");
    }
}
