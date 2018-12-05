/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.producerconfirm <br>
 *
 * @author mk <br>
 * Date:2018-12-5 14:59 <br>
 */

package com.suns.producerconfirm;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * ClassName: ProducerConfirmAsync <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-5 14:59 <br>
 * @version
 */
public class ProducerConfirmAsync {

    public static final String exchange_name = "producer_async_confirm";
    private final static String routKey = "error";

    public static void main(String[] args) {
        try{
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchange_name,BuiltinExchangeType.DIRECT);

            //失败通知
            channel.addReturnListener(new ReturnListener() {
                @Override
                public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body,"UTF-8");
                    System.out.println("replyCode["+replyCode+"],replyText["+replyText+"],exchange["+exchange+"],routingKey["+routingKey+"],body["+msg+"]");
                }
            });

            //生产者消息确认 异步模式
            channel.addConfirmListener(new ConfirmListener() {
                @Override
                public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("handleAck.......deliveryTag["+deliveryTag+"],multiple["+multiple+"]");
                }

                @Override
                public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("handleNack.......deliveryTag["+deliveryTag+"],multiple["+multiple+"]");
                }
            });

            //启用消息确认模式
            channel.confirmSelect();

            //所有日志严重性级别
            String[] severities={"error","warning"};
            for(int i=0;i<100;i++) {
                String severity = severities[i%2];
                String message = "测试生产者消息确认,RabbitMq" + (i + 1);
                /*发布消息，需要参数：交换器，路由键，其中以日志消息级别为路由键*/
                //设置mandatory为true
                channel.basicPublish(exchange_name, severity, true, null, message.getBytes());
                System.out.println("send 路由键[" + severity + "] msg:" + message);
            }

            // 关闭频道和连接
            channel.close();
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
