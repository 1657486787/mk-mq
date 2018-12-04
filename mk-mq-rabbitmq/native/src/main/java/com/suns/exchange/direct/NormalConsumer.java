/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.exchange.direct <br>
 *
 * @author mk <br>
 * Date:2018-12-4 14:54 <br>
 */

package com.suns.exchange.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: NormalConsumer <br>
 * Description: direct类型交换器的消费者  <br>
 * @author mk
 * @Date 2018-12-4 14:54 <br>
 * @version
 */
public class NormalConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        /*创建信道*/
        Channel channel = connection.createChannel();
        /*创建交换器*/
        channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME,BuiltinExchangeType.DIRECT);

        /*声明一个队列*/
        final String queueName = "focuserror";
        channel.queueDeclare(queueName,false,false, false,null);
        /*绑定，将队列和交换器通过路由键进行绑定*/
        String routekey = "error";/*表示只关注error级别的日志消息*/
        channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME,routekey);

        System.out.println("waiting for message........");

        /*声明了一个消费者*/
        final Consumer consumer = new DefaultConsumer(channel){
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
