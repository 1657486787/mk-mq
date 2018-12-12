/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.cluster <br>
 *
 * @author mk <br>
 * Date:2018-12-12 14:00 <br>
 */

package com.suns.cluster;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: TestProducer <br>
 *
 *1.新建虚拟主机（如：名称为vhost001）
 * rabbitmqctl add_vhost vhost001
 * 查看：
 * rabbitmqctl list_vhosts vhost00
 *
 * 2.新建用户（如：名称为user001,密码为123456）
 * rabbitmqctl add_user user001 123456
 * 查看
 * rabbitmqctl list_users
 *
 * 3.给用户设置用户权限（指的是用户给哪个虚拟主机设置权限）
 * rabbitmqctl set_permissions -p vhost001 user001 '.*' '.*' '.*'
 *
 * 4.给用户设置用户角色(user为用户名， Tag为角色名：administrator，monitoring，policymaker，management)
 * rabbitmqctl set_user_tags user001 administrato
 *
 *
 * Description:  <br>
 * @author mk
 * @Date 2018-12-12 14:00 <br>
 * @version
 */
public class TestProducer {

    public final static String EXCHANGE_NAME = "cluster_direct";
    public final static String EXCHANGE_NAME_FANOUT = "direct_fanout";

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
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");
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
