/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.usemq <br>
 *
 * @author mk <br>
 * Date:2018-11-23 11:20 <br>
 */

package com.suns.usemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * ClassName: JSMProducer <br>
 * Description: ActiveMq生产者 <br>
 *   使用原生ActiveMQ的API编程展示
 *   1.引入pom
 *          <dependency>
 *             <groupId>org.apache.activemq</groupId>
 *             <artifactId>activemq-all</artifactId>
 *             <version>5.9.0</version>
 *         </dependency>
 *   2.创建连接工厂，创建连接，启动连接，创建会话，创建消息目的地，创建生产者，组装好消息，然后发送
 *   3.消息有两种模式，点对点p2p和发布订阅(广播)pub/sub模式，在创建消息目的地时指定
 *   点对点p2p模式：一个消息只有一个消费者，默认会持久化，就是说重启activeMq，消息会自动保存
 *   发布订阅(广播)pub/sub模式：一个消息有多个消费者消费，默认不会持久化，就是说重启activeMq，消息会丢失，而且如果是生产者先发送消息之后，消费者才启动，那么消费者是获取不到消息的
 * @author mk
 * @Date 2018-11-23 11:20 <br>
 * @version
 */
public class JSMProducer {


    public static String ACTIVEMQ_BROKERURL = ActiveMQConnection.DEFAULT_BROKER_URL;/*默认连接用户名*/
    public static String ACTIVEMQ_USERNAME = ActiveMQConnection.DEFAULT_USER;/*默认连接用户名*/
    public static String ACTIVEMQ_PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD ;/*默认连接密码*/
    public static String HELLO_ACTIVEMQ_QUEUE = AMQConstant.HELLO_ACTIVEMQ_QUEUE;//消息模式：点对点模式
    public static String HELLO_ACTIVEMQ_TOPIC = AMQConstant.HELLO_ACTIVEMQ_TOPIC;//消息模式：发布订阅(广播)pub/sub模式

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
//            Destination destination = session.createTopic(HELLO_ACTIVEMQ_TOPIC);//发布订阅(广播)模式
            Destination destination  = session.createQueue(HELLO_ACTIVEMQ_QUEUE);//点对点模式
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
