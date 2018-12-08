/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.rejectmsg <br>
 *
 * @author mk <br>
 * Date:2018-12-8 9:50 <br>
 */

package com.suns.rejectmsg;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * ClassName: RejectProducer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-8 9:50 <br>
 * @version
 */
public class RejectProducer {

    public final static String EXCHANGE_NAME = "direct_logs";

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
        String servieity = "error";
        for(int i=0;i<10;i++){
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
