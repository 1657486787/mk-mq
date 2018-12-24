/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service <br>
 *
 * @author mk <br>
 * Date:2018-12-24 17:36 <br>
 */

package com.suns.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * ClassName: KafkaSender <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-24 17:36 <br>
 * @version
 */
@Component
public class KafkaSender {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public void messageSender(String tpoic,String key,String message){
        try {
            System.out.println("准备发送..."+tpoic+","+","+key);
            kafkaTemplate.send(tpoic,key,message);
            System.out.println("已发送");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
