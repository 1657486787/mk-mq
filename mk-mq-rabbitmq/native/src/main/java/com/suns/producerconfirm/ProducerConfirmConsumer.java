/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.producerconfirm <br>
 *
 * @author mk <br>
 * Date:2018-12-5 14:40 <br>
 */

package com.suns.producerconfirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: ProducerConfirmConsumer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-5 14:40 <br>
 * @version
 */
public class ProducerConfirmConsumer {

//    public static final String exchange_name = ProducerConfirm.exchange_name;//对应生产者单条确认ProducerConfirm
//    public static final String exchange_name = ProducerBatchConfirm.exchange_name;//对应生产者批量确认确认ProducerBatchConfirm
    public static final String exchange_name = ProducerConfirmAsync.exchange_name;//对应生产者异步确认ProducerConfirmAysnc

    public static void main(String[] argv)
            throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        // 打开连接和创建频道，与发送端一样
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchange_name, BuiltinExchangeType.DIRECT);

        String queueName = exchange_name;
        channel.queueDeclare(queueName,false,false,
                false,null);

        //只关注error级别的日志
        String severity="error";
        channel.queueBind(queueName, exchange_name, severity);

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
