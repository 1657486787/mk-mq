/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.exchange.direct <br>
 *
 * @author mk <br>
 * Date:2018-12-4 15:51 <br>
 */

package com.suns.exchange.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: MulitChannelConsumer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-4 15:51 <br>
 * @version
 */
public class MulitChannelConsumer {


    public static class ConsumerWorker implements Runnable{

        private Connection connection;
        public ConsumerWorker(Connection connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                Channel channel = connection.createChannel();
                channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME,BuiltinExchangeType.DIRECT);

                // 声明一个随机队列
                String queueName = channel.queueDeclare().getQueue();
                //消费者名字，打印输出用
                final String consumerName =  Thread.currentThread().getName()+"-all";

                //所有日志严重性级别
                String[] severities={"error","info","warning"};
                for (String severity : severities) {
                    //关注所有级别的日志（多重绑定）
                    channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME, severity);
                }
                System.out.println("["+consumerName+"] Waiting for messages:");


                Consumer consumer = new DefaultConsumer(channel){
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String msg = new String(body,"UTF-8");
                        System.out.println("receive 路由键["+envelope.getRoutingKey()+"] msg:"+msg);
                    }
                };

                channel.basicConsume(queueName,true,consumer);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();

        for(int i=0;i<3;i++){
            new Thread(new ConsumerWorker(connection)).start();
        }
    }
}
