/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.setmsg <br>
 *
 * @author mk <br>
 * Date:2018-12-8 11:59 <br>
 */

package com.suns.setmsg;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: ReplayToConsumer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-8 11:59 <br>
 * @version
 */
public class ReplayToConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        /*创建信道*/
        final Channel channel = connection.createChannel();
        /*创建交换器*/
        channel.exchangeDeclare(ReplayToProducer.EXCHANGE_NAME,BuiltinExchangeType.DIRECT);

        /*声明一个队列*/
        final String queueName = "queue_replayto";
        channel.queueDeclare(queueName,false,false, false,null);
        /*绑定，将队列和交换器通过路由键进行绑定*/
        String routekey = "test_replayto";
        channel.queueBind(queueName,ReplayToProducer.EXCHANGE_NAME,routekey);

        System.out.println("waiting for message........");

        /*声明了一个消费者*/
        final Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body,"UTF-8");
                System.out.println("queueName["+queueName+"],receive consumerTag["+consumerTag+"],路由键["+envelope.getRoutingKey()+"] msg:"+msg);


                //接收到消息之后，获取replayto，然后发送消息
                String replyTo = properties.getReplyTo();
                String messageId = properties.getMessageId();
                String responseMsg = "hi:"+msg;

                AMQP.BasicProperties properties_resp = new AMQP.BasicProperties().builder()
                    .replyTo(replyTo)
                    .correlationId(messageId)
                    .build();
                channel.basicPublish("",replyTo,properties,responseMsg.getBytes());

            }
        };

        /*消费者正式开始在指定队列上消费消息*/
        channel.basicConsume(queueName,true,consumer);
    }
}
