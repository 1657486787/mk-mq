/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service.fanout <br>
 *
 * @author mk <br>
 * Date:2018-12-8 17:35 <br>
 */

package com.suns.service.fanout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * ClassName: H3_Service <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-8 17:35 <br>
 * @version
 */
public class H3_Service implements MessageListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onMessage(Message message) {
        logger.info("Get message: "+new String( message.getBody()));
    }
}
