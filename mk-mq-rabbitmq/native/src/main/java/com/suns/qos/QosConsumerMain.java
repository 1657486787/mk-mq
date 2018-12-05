/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.qos <br>
 *
 * @author mk <br>
 * Date:2018-12-5 16:46 <br>
 */

package com.suns.qos;

import com.rabbitmq.client.*;
import com.suns.exchange.direct.DirectProducer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: QosConsumerMain <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-5 16:46 <br>
 * @version
 */
public class QosConsumerMain {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        /*创建信道*/
        final Channel channel = connection.createChannel();
        /*创建交换器*/
        channel.exchangeDeclare(QosProducer.EXCHANGE_NAME,BuiltinExchangeType.DIRECT);

        /*声明一个队列*/
        final String queueName = "focuserror";
        channel.queueDeclare(queueName,false,false, false,null);
        /*绑定，将队列和交换器通过路由键进行绑定*/
        String routekey = "error";/*表示只关注error级别的日志消息*/
        channel.queueBind(queueName,QosProducer.EXCHANGE_NAME,routekey);

        System.out.println("waiting for message........");

        /*声明了一个消费者*/
        final Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body,"UTF-8");
                System.out.println("queueName["+queueName+"],receive consumerTag["+consumerTag+"],路由键["+envelope.getRoutingKey()+"] msg:"+msg);

                channel.basicAck(envelope.getDeliveryTag(),true);
            }
        };
        channel.basicQos(150,true);

        /*消费者正式开始在指定队列上消费消息*/
        channel.basicConsume(queueName,false,consumer);
        BatchAckConsumer batchAckConsumer = new BatchAckConsumer(channel);
        channel.basicConsume(queueName,false,batchAckConsumer);

    }
}
