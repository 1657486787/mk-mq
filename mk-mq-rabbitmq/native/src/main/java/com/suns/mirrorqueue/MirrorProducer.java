/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.mirrorqueue <br>
 *
 * @author mk <br>
 * Date:2018-12-12 14:51 <br>
 */

package com.suns.mirrorqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: MirrorProducer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-12 14:51 <br>
 * @version
 */
public class MirrorProducer {

    public final static String EXCHANGE_NAME = "mirror_queue_test";

    public static void main(String[] args)
            throws IOException, TimeoutException {
        /* 创建连接,连接到RabbitMQ*/
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("47.107.146.57");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("user001");
        connectionFactory.setPassword("123456");
        connectionFactory.setVirtualHost("vhost001");
        Connection connection = connectionFactory.newConnection();

        /*创建信道*/
        Channel channel = connection.createChannel();
        /*创建交换器*/
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.DIRECT);

        /*日志消息级别，作为路由键使用*/
        String[] serverities = {"error","info","warning"};
        for(int i=0;i<3;i++){
            String severity = serverities[i%3];
            String msg = "Hellol,RabbitMq"+(i+1);
            /*发布消息，需要参数：交换器，路由键，其中以日志消息级别为路由键*/
            channel.basicPublish(EXCHANGE_NAME,severity,null,
                    msg.getBytes());
            System.out.println("Sent "+severity+":"+msg);

        }
        channel.close();
        connection.close();

    }
}
