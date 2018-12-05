/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.exchange.fanout <br>
 *
 * @author mk <br>
 * Date:2018-12-5 9:53 <br>
 */

package com.suns.exchange.fanout;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: Consumer2 <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-5 9:53 <br>
 * @version
 */
public class Consumer2 {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        //队列，路由键，绑定，交换器
        channel.exchangeDeclare(FanoutProducer.exchange_name,BuiltinExchangeType.FANOUT);
        final String queueName = channel.queueDeclare().getQueue();//随机队列

        //只关注error路由键（或者绑定一个不存在的路由键）,交换器的类型为FANOUT,那么也会受到所有路由键的消息
        String routKey = "error";//error123
        channel.queueBind(queueName,FanoutProducer.exchange_name,routKey);

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
