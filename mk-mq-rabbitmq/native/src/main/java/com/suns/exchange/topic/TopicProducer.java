/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.exchange.topic <br>
 *
 * @author mk <br>
 * Date:2018-12-5 10:12 <br>
 */

package com.suns.exchange.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: TopicProducer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-5 10:12 <br>
 * @version
 */
public class TopicProducer {

    public static final String exchange_name = "exchange_topic";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exchange_name,BuiltinExchangeType.TOPIC);
        //所有日志严重性级别"error","info","warning"
        String [] levelKeys = {"error","info","warning"};
        String [] busKeys = {"user","order","email"}; //业务类型"user","order","email"
        String [] jqKeys = {"A","B","C"};//服务器"A","B","C"
        for(int i=0;i<3;i++){

            String levelKey = levelKeys[i%3];
            for(int j=0;j<3;j++){

                String busKey = busKeys[j%3];
                for(int k=0;k<3;k++){

                    String jqKey = jqKeys[k%3];
                   String routKey = levelKey+"."+busKey+"."+jqKey;

                   String message = "测试Topic,["+i+","+j+","+k+"]";
                   /*发布消息，需要参数：交换器，路由键，其中以日志消息级别为路由键*/
                   channel.basicPublish(exchange_name,routKey,null,message.getBytes());
                   System.out.println("send 路由键["+routKey+"] msg:"+message);
               }

           }

        }
        channel.close();
        connection.close();
    }
}
