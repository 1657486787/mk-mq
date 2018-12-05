/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.getmessage <br>
 *
 * @author mk <br>
 * Date:2018-12-5 16:12 <br>
 */

package com.suns.getmessage;

import com.rabbitmq.client.*;
import com.suns.exchange.direct.DirectProducer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: GetMessageConsumer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-5 16:12 <br>
 * @version
 */
public class GetMessageConsumer {

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        /*创建信道*/
        Channel channel = connection.createChannel();
        /*创建交换器*/
        channel.exchangeDeclare(GetMessageProducer.EXCHANGE_NAME,BuiltinExchangeType.DIRECT);

        /*声明一个队列*/
        final String queueName = "focuserror";
        channel.queueDeclare(queueName,false,false, false,null);

        /*绑定，将队列和交换器通过路由键进行绑定*/
        String routekey = "error";/*表示只关注error级别的日志消息*/
        channel.queueBind(queueName,GetMessageProducer.EXCHANGE_NAME,routekey);

        System.out.println("waiting for message........");

        while(true){
            GetResponse getResponse = channel.basicGet(queueName, true);

            if(null != getResponse){
                Envelope envelope = getResponse.getEnvelope();
                String msg = new String(getResponse.getBody(),"UTF-8");
                System.out.println("queueName["+queueName+"],receive consumerTag["+envelope.getDeliveryTag()+"],路由键["+envelope.getRoutingKey()+"] msg:"+msg);
            }

            Thread.sleep(1000);//1秒钟拉取一次消息
        }

    }
}
