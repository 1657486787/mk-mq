/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.controller <br>
 *
 * @author mk <br>
 * Date:2018-11-24 9:06 <br>
 */

package com.suns.controller;

import com.suns.queue.QueueSender;
import com.suns.topic.TopicSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ClassName: ActiveMQController <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-24 9:06 <br>
 * @version
 */
@Controller
@RequestMapping("/activemq")
public class ActiveMQController {

    @Autowired
    private QueueSender queueSender;
    @Autowired
    private TopicSender topicSender;

    @RequestMapping("/queueSender")
    @ResponseBody
    public String queueSender(@RequestParam("message") String msg,@RequestParam("msgType") String msgType){
        String result = "";
        try{
            queueSender.send("test.queue",msg,msgType);
            result = "suc";
        }catch (Exception e){
            e.printStackTrace();
            result = e.getCause().toString();
        }
        System.out.println("queueSender result="+result);
        return result;
    }

    @RequestMapping("/topicSender")
    @ResponseBody
    public String topicSender(@RequestParam("message") String msg){
        String result = "";
        try{
            topicSender.send("test.topic",msg);
            result = "suc";
        }catch (Exception e){
            e.printStackTrace();
            result = e.getCause().toString();
        }
        System.out.println("topicSender result="+result);
        return result;
    }
}
