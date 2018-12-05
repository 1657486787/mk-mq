/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.ackfalse <br>
 *
 * @author mk <br>
 * Date:2018-12-5 16:31 <br>
 */

package com.suns.ackfalse;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: AckFalseConsumerB <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-5 16:31 <br>
 * @version
 */
public class AckFalseConsumerB {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        /*创建信道*/
        final Channel channel = connection.createChannel();
        /*创建交换器*/
        channel.exchangeDeclare(AckFalseProducer.EXCHANGE_NAME,BuiltinExchangeType.DIRECT);

        /*声明一个队列*/
        final String queueName = "focuserror";
        channel.queueDeclare(queueName,false,false, false,null);
        /*绑定，将队列和交换器通过路由键进行绑定*/
        String routekey = "error";/*表示只关注error级别的日志消息*/
        channel.queueBind(queueName,AckFalseProducer.EXCHANGE_NAME,routekey);

        System.out.println("waiting for message........");

        /*声明了一个消费者*/
        final Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                try{
                    String msg = new String(body,"UTF-8");
                    System.out.println("queueName["+queueName+"],receive consumerTag["+consumerTag+"],路由键["+envelope.getRoutingKey()+"] msg:"+msg);
                    //消费者消息确认
                    channel.basicAck(envelope.getDeliveryTag(),false);
                    System.out.println("消费者消息确认,"+envelope.getDeliveryTag());
                }catch (Exception e){
                    e.printStackTrace();
                    //消费者消息拒绝
                    channel.basicNack(envelope.getDeliveryTag(),false,true);
                    System.out.println("消费者消息拒绝,"+envelope.getDeliveryTag());
                }
            }
        };


        /*消费者正式开始在指定队列上消费消息*/
        channel.basicConsume(queueName,false,consumer);
    }
}
