/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.exchange.direct <br>
 *
 * @author mk <br>
 * Date:2018-12-4 16:19 <br>
 */

package com.suns.exchange.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: MultiConsumerOneQueue <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-4 16:19 <br>
 * @version
 */
public class MultiConsumerOneQueue {



    public static class ConsumerWorker implements Runnable{

        private Connection connection;
        private String queueNameStr;

        public ConsumerWorker(Connection connection, String queueNameStr) {
            this.connection = connection;
            this.queueNameStr = queueNameStr;
        }

        @Override
        public void run() {
            try {
                Channel channel = connection.createChannel();
                channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME,BuiltinExchangeType.DIRECT);

                String queueName = null;
                if(null == queueNameStr || "".equals(queueNameStr)){
                    // 声明一个随机队列
                    queueName = channel.queueDeclare().getQueue();

                }else{
                    queueName = queueNameStr;
                    channel.queueDeclare(queueName,false,false,false,null);
                }

                //消费者名字，打印输出用
                final String consumerName =  Thread.currentThread().getName();

                //所有日志严重性级别
                String[] severities={"error","info","warning"};
                for (String severity : severities) {
                    //关注所有级别的日志（多重绑定）
                    channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME, severity);
                }
                System.out.println("["+consumerName+"] Waiting for messages:");


                final String finalQueueName = queueName;
                Consumer consumer = new DefaultConsumer(channel){
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String msg = new String(body,"UTF-8");
                        System.out.println("consumerName["+consumerName+"],queueName["+ finalQueueName +"],receive consumerTag["+consumerTag+"],路由键["+envelope.getRoutingKey()+"] msg:"+msg);
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

        //3个线程，线程之间共享队列,一个队列多个消费者
        String queueName = "focusAll";//
//        String queueName = null;//多个对个多个消费者
        for(int i=0;i<3;i++){
            new Thread(new ConsumerWorker(connection,queueName)).start();
        }
    }
}
