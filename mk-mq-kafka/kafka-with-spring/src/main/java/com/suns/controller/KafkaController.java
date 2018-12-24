/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.controller <br>
 *
 * @author mk <br>
 * Date:2018-12-24 10:42 <br>
 */

package com.suns.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: KafkaController <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-24 10:42 <br>
 * @version
 */
@RestController
@RequestMapping("/kafka")
public class KafkaController {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @RequestMapping("/spring")
    public String queueSender(@RequestParam("message")String message){
        String opt="";
        try{
            kafkaTemplate.send("kafka-spring-topic", message);
            System.out.println(message + " is send...");
            opt = "suc";
        }catch (Exception e){
            e.printStackTrace();
            opt = e.getCause().toString();
        }
        return opt;
    }

    @RequestMapping("/springb")
    public String queueSenderB(@RequestParam("message")String message){
        String opt="";
        try{
            kafkaTemplate.send("kafka-spring-topic-b", message);
            System.out.println(message + " is send...");
            opt = "suc";
        }catch (Exception e){
            e.printStackTrace();
            opt = e.getCause().toString();
        }
        return opt;
    }

}
