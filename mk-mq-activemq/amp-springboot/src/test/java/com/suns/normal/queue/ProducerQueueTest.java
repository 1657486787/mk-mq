package com.suns.normal.queue;

import com.suns.normal.topic.ProducerTopic;
import com.suns.replayto.ProducerR;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.Destination;
import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProducerQueueTest{


    @Autowired
    private ProducerQueue producerQueue;

    @Test
    public void testNormalQueue() throws IOException {
        Destination destination = new ActiveMQQueue("springboot.queue");
        for(int i=0;i<10;i++){
//        producerQueue.sendMessage("springboot.queue","test...");
            producerQueue.sendMessage(destination,"测试springboot.queue,i="+i);
            System.out.println("testNormalQueue send,i="+i);
        }
    }


    @Autowired
    private ProducerTopic producerTopic;
    @Test
    public void testNormalTopic() throws IOException {
        Destination destination = new ActiveMQTopic("springboot.topic");
        for(int i=0;i<10;i++){
            producerTopic.sendMessage(destination,"测试springboot.topic,i="+i);
            System.out.println("testNormalTopic send,i="+i);
        }
    }


    @Autowired
    private ProducerR producerR;
    @Test
    public void testReplayTo() throws IOException {
        Destination destination = new ActiveMQQueue("springboot.replayto.queue");
        for(int i=0;i<1;i++){
            producerR.sendMessage(destination,"测试请求响应springboot.replayto.queue,i="+i);
            System.out.println("testReplayTo send,i="+i);
        }
        System.in.read();
    }

}