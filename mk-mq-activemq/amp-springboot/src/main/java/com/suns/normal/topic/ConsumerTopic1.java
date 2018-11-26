/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.normal.topic <br>
 *
 * @author mk <br>
 * Date:2018-11-26 15:10 <br>
 */

package com.suns.normal.topic;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * ClassName: ConsumerTopic1 <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-26 15:10 <br>
 * @version
 */
@Component
public class ConsumerTopic1 {

    @JmsListener(destination = "springboot.topic",containerFactory = "jmsListenerContainerFactory")
    public void receivv(String text){
        System.out.println(this.getClass().getName() + " receive:"+text);
    }
}
