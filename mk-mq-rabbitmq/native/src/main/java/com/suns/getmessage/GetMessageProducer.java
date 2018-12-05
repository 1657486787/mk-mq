/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.getmessage <br>
 *
 * @author mk <br>
 * Date:2018-12-5 16:12 <br>
 */

package com.suns.getmessage;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * ClassName: GetMessageProducer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-5 16:12 <br>
 * @version
 */
public class GetMessageProducer {

    public final static String EXCHANGE_NAME = "get_message_producer";

    public static void main(String[] args) throws Exception{
        /* 创建连接,连接到RabbitMQ*/
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        /*创建信道*/
        Channel channel = connection.createChannel();
        /*创建交换器*/
        channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.DIRECT);
        /*日志消息级别，作为路由键使用*/
        for(int i=0;i<3;i++){
            String servieity = "error";
            String message = "你好Hellol,RabbitMq"+(i+1);
            /*发布消息，需要参数：交换器，路由键，其中以日志消息级别为路由键*/
            channel.basicPublish(EXCHANGE_NAME,servieity,null,message.getBytes());
            System.out.println("send 路由键["+servieity+"] msg:"+message);
        }
        //关闭资源
        channel.close();
        connection.close();
    }
}
