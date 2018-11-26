/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.normal.topic <br>
 *
 * @author mk <br>
 * Date:2018-11-26 15:20 <br>
 */

package com.suns.normal.topic;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * ClassName: ConsumerTopic2 <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-26 15:20 <br>
 * @version
 */
@Component
public class ConsumerTopic2 {

    @JmsListener(destination = "springboot.topic",containerFactory = "jmsListenerContainerFactory")
    public void receiv(String text){
        System.out.println(this.getClass().getName() + " receive:"+text);
    }
}
