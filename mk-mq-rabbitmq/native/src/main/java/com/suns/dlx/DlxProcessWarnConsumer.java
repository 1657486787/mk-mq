/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.dlx <br>
 *
 * @author mk <br>
 * Date:2018-12-8 11:03 <br>
 */

package com.suns.dlx;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: DlxProcessWarnConsumer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-8 11:03 <br>
 * @version
 */
public class DlxProcessWarnConsumer {

    public final static String EXCHANGE_NAME = "dlx_warn_accept";
    public final static String ROUTE_KEY = "dlx_warn";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        /*创建信道*/
        Channel channel = connection.createChannel();
        /*创建交换器*/
        channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.TOPIC);

        /*声明一个队列*/
        final String queueName = "dlx_warn_accept";
        channel.queueDeclare(queueName,false,false, false,null);
        /*绑定，将队列和交换器通过路由键进行绑定*/
        channel.queueBind(queueName,EXCHANGE_NAME,ROUTE_KEY);

        System.out.println("waiting for message........");

        /*声明了一个消费者*/
        final Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body,"UTF-8");
                System.out.println("queueName["+queueName+"],receive consumerTag["+consumerTag+"],路由键["+envelope.getRoutingKey()+"] msg:"+msg);
            }
        };

        /*消费者正式开始在指定队列上消费消息*/
        channel.basicConsume(queueName,true,consumer);
    }
}
