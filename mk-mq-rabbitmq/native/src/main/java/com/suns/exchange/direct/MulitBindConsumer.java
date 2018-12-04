/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.exchange.direct <br>
 *
 * @author mk <br>
 * Date:2018-12-4 15:16 <br>
 */

package com.suns.exchange.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: MulitBindConsumer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-4 15:16 <br>
 * @version
 */
public class MulitBindConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        //队列，路由键，绑定，交换器
        channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME,BuiltinExchangeType.DIRECT);
        //声明一个随机队列
        final String queueName = channel.queueDeclare().getQueue();
        String[] serverities = {"error","info","warning"};
        for(int i=0;i<serverities.length;i++){
            String servieity = serverities[i];
            channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME,servieity);
        }

        System.out.println("waiting for message........");

        //创建消费者
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body,"UTF-8");
                System.out.println("queueName["+queueName+"],receive consumerTag["+consumerTag+"],路由键["+envelope.getRoutingKey()+"] msg:"+msg);
            }
        };
        /*消费者正式开始在指定队列上消费消息*/
        channel.basicConsume(queueName,true,consumer);

    }
}
