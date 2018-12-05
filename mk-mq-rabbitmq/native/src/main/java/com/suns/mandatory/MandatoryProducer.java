/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.mandatory <br>
 *
 * @author mk <br>
 * Date:2018-12-5 11:17 <br>
 */

package com.suns.mandatory;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: MandatoryProducer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-5 11:17 <br>
 * @version
 */
public class MandatoryProducer {

    public static final String exchange_name = "producer_confirm";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
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

        //所有日志严重性级别
        String [] routKeys = {"error","info","warning"};
        for(int i=0;i<3;i++){
            String routKey = routKeys[i%3];
            String message = "测试失败通知,RabbitMq"+(i+1);
            /*发布消息，需要参数：交换器，路由键，其中以日志消息级别为路由键*/
            //设置mandatory为true
            channel.basicPublish(exchange_name,routKey,true,null,message.getBytes());
            System.out.println("send 路由键["+routKey+"] msg:"+message);
            Thread.sleep(200);//休眠，为了有足够时间监听channel.addReturnListener
        }

        // 关闭频道和连接
        channel.close();
        connection.close();
    }
}
