/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.exchange.fanout <br>
 *
 * @author mk <br>
 * Date:2018-12-5 9:31 <br>
 */

package com.suns.exchange.fanout;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: Consumer1 <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-5 9:31 <br>
 * @version
 */
public class Consumer1 {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        //队列，路由键，绑定，交换器
        channel.exchangeDeclare(FanoutProducer.exchange_name,BuiltinExchangeType.FANOUT);
        final String queueName = channel.queueDeclare().getQueue();//随机队列

        //所有日志严重性级别
        String [] routKeys = {"error","info","warning"};
        for(int i=0;i<3;i++){
            String routKey = routKeys[i%3];
            channel.queueBind(queueName,FanoutProducer.exchange_name,routKey);
        }

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
