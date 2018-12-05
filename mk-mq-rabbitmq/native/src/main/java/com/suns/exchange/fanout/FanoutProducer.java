/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.exchange.fanout <br>
 *
 * @author mk <br>
 * Date:2018-12-5 9:17 <br>
 */

package com.suns.exchange.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: FanoutProducer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-5 9:17 <br>
 * @version
 */
public class FanoutProducer {

    public static final String exchange_name = "exchange_fanout";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exchange_name,BuiltinExchangeType.FANOUT);

        //所有日志严重性级别
        String [] routKeys = {"error","info","warning"};
        for(int i=0;i<3;i++){
            String routKey = routKeys[i%3];
            String message = "测试Fanout,RabbitMq"+(i+1);
            /*发布消息，需要参数：交换器，路由键，其中以日志消息级别为路由键*/
            channel.basicPublish(exchange_name,routKey,null,message.getBytes());
            System.out.println("send 路由键["+routKey+"] msg:"+message);
        }
        // 关闭频道和连接
        channel.close();
        connection.close();
    }
}
