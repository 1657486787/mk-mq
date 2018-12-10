/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.delayorder <br>
 *
 * @author mk <br>
 * Date:2018-12-8 17:55 <br>
 */

package com.suns.delayorder;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: DelayOrderConsumer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-8 17:55 <br>
 * @version
 */
public class DelayOrderConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        /*创建信道*/
        final Channel channel = connection.createChannel();
        /*创建交换器*/
        channel.exchangeDeclare(DelayOrderProducer.EXCHANGE_NAME,BuiltinExchangeType.DIRECT);

        /*声明一个队列*/
        final String queueName = "queue:"+DelayOrderProducer.EXCHANGE_NAME;
        Map argMap = new HashMap<>();
        argMap.put("x-message-ttl",5000);
        argMap.put("x-dead-letter-exchange",DlxProcessConsumer.EXCHANGE_NAME);
        channel.queueDeclare(queueName,false,false,false,argMap);


        /*绑定，将队列和交换器通过路由键进行绑定*/
        String routekey = "error";/*表示只关注error级别的日志消息*/
        channel.queueBind(queueName,DelayOrderProducer.EXCHANGE_NAME,routekey);

        System.out.println("waiting for message........");

       /* *//*声明了一个消费者*//*
        final Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body,"UTF-8");
                channel.basicReject(envelope.getDeliveryTag(),false);
                System.out.println("basicReject======>queueName["+queueName+"],receive consumerTag["+consumerTag+"],路由键["+envelope.getRoutingKey()+"] msg:"+msg);
            }
        };

        *//*消费者正式开始在指定队列上消费消息*//*
        channel.basicConsume(queueName,false,consumer);*/
    }
}
