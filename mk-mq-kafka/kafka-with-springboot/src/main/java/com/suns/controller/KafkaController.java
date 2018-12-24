/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.controller <br>
 *
 * @author mk <br>
 * Date:2018-12-24 14:18 <br>
 */

package com.suns.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: KafkaController <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-24 14:18 <br>
 * @version
 */
@RestController
@RequestMapping("/kafka-springboot")
public class KafkaController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @RequestMapping("/send")
    public String send(@RequestParam("key") String key,@RequestParam("value")String value){
        try{
            logger.info("kafka发送参数{}-{}",key,value);
            kafkaTemplate.send("springboot-topic-001", key,value);
            logger.info("发送kafka成功,key="+key+",value="+value);
            return "发送kafka成功,key="+key+",value="+value;

        }catch (Exception e){
            e.printStackTrace();
            return "发送kafka异常！"+e.getCause().toString();
        }
    }

    @RequestMapping("/sendAck")
    public String sendAck(@RequestParam("key") String key,@RequestParam("value")String value){
        try{
            logger.info("kafka发送参数{}-{}",key,value);
            kafkaTemplate.send("springboot-topic-ack", key,value);
            logger.info("发送kafka成功,key="+key+",value="+value);
            return "发送kafka成功,key="+key+",value="+value;

        }catch (Exception e){
            e.printStackTrace();
            return "发送kafka异常！"+e.getCause().toString();
        }
    }

}
