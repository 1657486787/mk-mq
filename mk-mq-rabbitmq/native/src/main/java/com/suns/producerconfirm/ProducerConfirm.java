/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.producerconfirm <br>
 *
 * @author mk <br>
 * Date:2018-12-5 14:13 <br>
 */

package com.suns.producerconfirm;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * ClassName: ProducerConfirm <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-5 14:13 <br>
 * @version
 */
public class ProducerConfirm {

    public static final String exchange_name = "producer_confirm";
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

            //启用消息确认模式
            channel.confirmSelect();

            //所有日志严重性级别
            for(int i=0;i<2;i++) {
                String message = "测试生产者消息确认,RabbitMq" + (i + 1);
                /*发布消息，需要参数：交换器，路由键，其中以日志消息级别为路由键*/
                //设置mandatory为true
                channel.basicPublish(exchange_name, routKey, true, null, message.getBytes());
                System.out.println("send 路由键[" + routKey + "] msg:" + message);

                //一条一条确认，返回true表示发送成功
                if(channel.waitForConfirms()){
                    System.out.println("send success...");
                }else{
                    System.out.println("send fail....");
                }
            }

            // 关闭频道和连接
            channel.close();
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
