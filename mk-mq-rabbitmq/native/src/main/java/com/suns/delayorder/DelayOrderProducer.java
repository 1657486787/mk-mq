/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.delayorder <br>
 *
 * @author mk <br>
 * Date:2018-12-8 17:54 <br>
 */

package com.suns.delayorder;

import com.rabbitmq.client.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * ClassName: DelayOrderProducer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-8 17:54 <br>
 * @version
 */
public class DelayOrderProducer {

    public final static String EXCHANGE_NAME = "delay_exchange_8";
    public static SimpleDateFormat sdf = new SimpleDateFormat();

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
//            AMQP.BasicProperties properties = new AMQP.BasicProperties()
//                    .builder()
//                    .expiration("20181208200600")//设置消息的过期时间为60秒
//                    .build();
//            channel.basicPublish(EXCHANGE_NAME,servieity,properties,message.getBytes());
            System.out.println("发送时间："+new Date()+",send 路由键["+servieity+"] msg:"+message);
        }
        //关闭资源
        channel.close();
        connection.close();
    }
}
