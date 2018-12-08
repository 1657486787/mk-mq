/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.setmsg <br>
 *
 * @author mk <br>
 * Date:2018-12-8 11:59 <br>
 */

package com.suns.setmsg;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;

/**
 * ClassName: ReplayToProducer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-8 11:59 <br>
 * @version
 */
public class ReplayToProducer {

    public final static String EXCHANGE_NAME = "replayto";

    public static void main(String[] args) throws Exception{
        /* 创建连接,连接到RabbitMQ*/
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        /*创建信道*/
        Channel channel = connection.createChannel();
        /*创建交换器*/
        channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.DIRECT,false);
        /*日志消息级别，作为路由键使用*/
        String servieity = "test_replayto";
        String message = "测试request-response";
        /*发布消息，需要参数：交换器，路由键，其中以日志消息级别为路由键*/


        //响应QueueName ，消费者将会把要返回的信息发送到该Queue
        final String responseQueue = channel.queueDeclare().getQueue();
        //消息的唯一id
        String msgId = UUID.randomUUID().toString();
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .replyTo(responseQueue)
                .messageId(msgId)
                .build();

        channel.basicPublish(EXCHANGE_NAME,servieity,properties,message.getBytes());
        System.out.println("send 路由键["+servieity+"] msg:"+message);


        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body,"UTF-8");
                System.out.println("queueName["+responseQueue+"],receive consumerTag["+consumerTag+"],路由键["+envelope.getRoutingKey()+"] msg:"+msg);
            }
        };
        channel.basicConsume(responseQueue,true,consumer);
    }
}
