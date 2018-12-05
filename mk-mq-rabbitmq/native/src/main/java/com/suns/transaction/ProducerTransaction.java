/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.transaction <br>
 *
 * @author mk <br>
 * Date:2018-12-5 15:11 <br>
 */

package com.suns.transaction;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * ClassName: ProducerTransaction <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-5 15:11 <br>
 * @version
 */
public class ProducerTransaction {

    public final static String EXCHANGE_NAME = "producer_transaction";

    public static void main(String[] args) throws Exception{
        /* 创建连接,连接到RabbitMQ*/
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        /*创建信道*/
        Channel channel = connection.createChannel();
        /*创建交换器*/
        channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.DIRECT);

        //开启事务
        channel.txSelect();

        try{
            /*日志消息级别，作为路由键使用*/
            String[] serverities = {"error","info","warning"};
            for(int i=0;i<3;i++){
                String servieity = serverities[i%3];
                String message = "你好Hellol,RabbitMq"+(i+1);
                /*发布消息，需要参数：交换器，路由键，其中以日志消息级别为路由键*/
                channel.basicPublish(EXCHANGE_NAME,servieity,null,message.getBytes());
                System.out.println("send 路由键["+servieity+"] msg:"+message);
            }

            //提交事务
            channel.txCommit();
        }catch (Exception e){
            e.printStackTrace();

            //回滚
            channel.txRollback();
        }
        //关闭资源
        channel.close();
        connection.close();
    }
}
