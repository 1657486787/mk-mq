/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.qos <br>
 *
 * @author mk <br>
 * Date:2018-12-5 16:46 <br>
 */

package com.suns.qos;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * ClassName: QosProducer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-5 16:46 <br>
 * @version
 */
public class QosProducer {


    public final static String EXCHANGE_NAME = "qos_producer";

    public static void main(String[] args) throws Exception{
        /* 创建连接,连接到RabbitMQ*/
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        /*创建信道*/
        Channel channel = connection.createChannel();
        /*创建交换器*/
        channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.DIRECT);


        //发送210条消息，其中第210条消息表示本批次消息的结束
        for(int i=0;i<210;i++){
            // 发送的消息
            String message = "Hello World_"+(i+1);
            if(i==209){
                message = "stop";
            }
            //参数1：exchange name
            //参数2：routing key
            channel.basicPublish(EXCHANGE_NAME, "error",
                    null, message.getBytes());
            System.out.println(" [x] Sent 'error':'"
                    + message + "'");
        }


        //关闭资源
        channel.close();
        connection.close();
    }
}
