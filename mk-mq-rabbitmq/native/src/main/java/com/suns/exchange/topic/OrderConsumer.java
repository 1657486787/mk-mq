/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.exchange.topic <br>
 *
 * @author mk <br>
 * Date:2018-12-5 10:29 <br>
 */

package com.suns.exchange.topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: OrderConsumer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-5 10:29 <br>
 * @version
 */
public class OrderConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        //队列，路由键，绑定，交换器
        channel.exchangeDeclare(TopicProducer.exchange_name,BuiltinExchangeType.TOPIC);
        final String queueName = channel.queueDeclare().getQueue();//随机队列

        //监听业务为order的所有消息
        String routKey = "#.order.#";//*.order.*或者#.order.#
        channel.queueBind(queueName,TopicProducer.exchange_name,routKey);

        System.out.println("waiting for message........");

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body,"UTF-8");
                System.out.println("queueName["+queueName+"],receive consumerTag["+consumerTag+"],路由键["+envelope.getRoutingKey()+"] msg:"+msg);
            }
        };

        channel.basicConsume(queueName,true,consumer);
    }
}
