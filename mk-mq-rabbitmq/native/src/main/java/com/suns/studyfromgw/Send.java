package com.suns.studyfromgw;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.concurrent.TimeoutException;

public class Send {
  private final static String QUEUE_NAME = "hello";

  public static void main(String[] argv) throws java.io.IOException {
      try {
          ConnectionFactory connectionFactory = new ConnectionFactory();
          connectionFactory.setHost("localhost");
          Connection connection = connectionFactory.newConnection();
          Channel channel = connection.createChannel();
          channel.queueDeclare(QUEUE_NAME, false, false, false, null);
          String message = "Hello World";
          channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
          System.out.println(" [x] Sent '" + message + "'");

          channel.close();
          connection.close();
      } catch (TimeoutException e) {
          e.printStackTrace();
      }
  }
} 