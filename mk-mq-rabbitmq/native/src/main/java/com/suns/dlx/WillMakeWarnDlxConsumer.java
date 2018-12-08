/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.dlx <br>
 *
 * @author mk <br>
 * Date:2018-12-8 11:02 <br>
 */

package com.suns.dlx;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: WillMakeWarnDlxConsumer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-8 11:02 <br>
 * @version
 */
public class WillMakeWarnDlxConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        /*创建信道*/
        final Channel channel = connection.createChannel();
        /*创建交换器*/
        channel.exchangeDeclare(DlxProducer.EXCHANGE_NAME,BuiltinExchangeType.TOPIC);

        /*声明一个队列*/
        final String queueName = "dlx_warn_make";

        Map argMap = new HashMap<>();
        argMap.put("x-dead-letter-exchange",DlxProcessWarnConsumer.EXCHANGE_NAME);
        argMap.put("x-dead-letter-routing-key",DlxProcessWarnConsumer.ROUTE_KEY);
        channel.queueDeclare(queueName,false,false, false,argMap);
        /*绑定，将队列和交换器通过路由键进行绑定*/
        channel.queueBind(queueName,DlxProducer.EXCHANGE_NAME,"#");

        System.out.println("waiting for message........");

        /*声明了一个消费者*/
        final Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body,"UTF-8");
                if("error".equals(envelope.getRoutingKey())){
                    channel.basicAck(envelope.getDeliveryTag(),false);
                    System.out.println("basicAck======>queueName["+queueName+"],receive consumerTag["+consumerTag+"],路由键["+envelope.getRoutingKey()+"] msg:"+msg);
                }else{
                    channel.basicReject(envelope.getDeliveryTag(),false);
                    System.out.println("basicReject======>queueName["+queueName+"],receive consumerTag["+consumerTag+"],路由键["+envelope.getRoutingKey()+"] msg:"+msg);
                }
            }
        };

        /*消费者正式开始在指定队列上消费消息*/
        channel.basicConsume(queueName,false,consumer);
    }
}
