/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.transaction <br>
 *
 * @author mk <br>
 * Date:2018-12-5 15:12 <br>
 */

package com.suns.transaction;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: ConsumerProducerTransaction <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-5 15:12 <br>
 * @version
 */
public class ConsumerProducerTransaction {

    public static void main(String[] argv)
            throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        // 打开连接和创建频道，与发送端一样
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare(ProducerTransaction.EXCHANGE_NAME,
                BuiltinExchangeType.DIRECT);

        String queueName = "producer_confirm";
        channel.queueDeclare(queueName,false,false,
                false,null);

        //只关注error级别的日志
        String severity="error";
        channel.queueBind(queueName, ProducerTransaction.EXCHANGE_NAME,
                severity);

        System.out.println(" [*] Waiting for messages......");

        // 创建队列消费者
        final Consumer consumerB = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                //记录日志到文件：
                System.out.println( "Received ["+ envelope.getRoutingKey()
                        + "] "+message);
            }
        };
        channel.basicConsume(queueName, true, consumerB);
    }
}
