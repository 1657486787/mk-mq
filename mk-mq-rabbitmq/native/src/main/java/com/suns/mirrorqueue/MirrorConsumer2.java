/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.mirrorqueue <br>
 *
 * @author mk <br>
 * Date:2018-12-12 14:54 <br>
 */

package com.suns.mirrorqueue;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: MirrorConsumer2 <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-12 14:54 <br>
 * @version
 */
public class MirrorConsumer2 {

    public static void main(String[] argv)
            throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("47.107.146.57");
        connectionFactory.setPort(5673);
        connectionFactory.setUsername("user001");
        connectionFactory.setPassword("123456");
        connectionFactory.setVirtualHost("vhost001");

        // 打开连接和创建频道，与发送端一样
        Connection connection = connectionFactory.newConnection();
        final Channel channel = connection.createChannel();
        channel.exchangeDeclare(MirrorProducer.EXCHANGE_NAME,
                "fanout");

        /*声明一个队列*/
        String queueName = "mirror_queue_test3";
        Map<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("x-ha-policy","nodes");
        arguments.put("x-ha-nodes","[rabbit@node1]");
        channel.queueDeclare(queueName,false,false,
                false,null);

        /*绑定，将队列和交换器通过路由键进行绑定*/
        String routekey = "error";/*表示只关注error级别的日志消息*/
        channel.queueBind(queueName,MirrorProducer.EXCHANGE_NAME,routekey);

        System.out.println("waiting for message........");

        /*声明了一个消费者*/
        final Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received["+envelope.getRoutingKey()
                        +"]"+message);
            }
        };
        /*消费者正式开始在指定队列上消费消息*/
        channel.basicConsume(queueName,true,consumer);


    }
}
