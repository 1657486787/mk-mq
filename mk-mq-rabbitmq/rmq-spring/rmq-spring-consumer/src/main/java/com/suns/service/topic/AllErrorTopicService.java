/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service.topic <br>
 *
 * @author mk <br>
 * Date:2018-12-8 17:39 <br>
 */

package com.suns.service.topic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

/**
 * ClassName: AllErrorTopicService <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-8 17:39 <br>
 * @version
 */
@Component
public class AllErrorTopicService implements MessageListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onMessage(Message message) {
        logger.info("Get message: "+new String( message.getBody()));
    }
}
