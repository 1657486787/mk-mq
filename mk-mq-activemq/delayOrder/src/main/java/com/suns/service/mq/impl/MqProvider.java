/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service.mq.impl <br>
 *
 * @author mk <br>
 * Date:2018-12-2 10:54 <br>
 */

package com.suns.service.mq.impl;

import com.google.gson.Gson;
import com.suns.model.OrderExp;
import com.suns.service.delay.IDelayOrder;
import org.apache.activemq.ScheduledMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * ClassName: MqProvider <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-2 10:54 <br>
 * @version
 */
@Service
@Qualifier("mq")
public class MqProvider implements IDelayOrder {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JmsTemplate jmsTemplate;


    @Override
    public void orderDelay(OrderExp orderExp, long expireTime) {
        //注意expiredTime要乘以1000
        jmsTemplate.send("order.delay", new CreateOrderMessage(orderExp,expireTime*1000));
        logger.info("订单[超时时长："+expireTime+"秒]被推入mq，订单详情："+orderExp);
    }


    public class CreateOrderMessage implements MessageCreator{

        private OrderExp orderExp;
        private long expireTime;
        public CreateOrderMessage(OrderExp orderExp, long expireTime) {
            this.orderExp = orderExp;
            this.expireTime = expireTime;
        }

        @Override
        public Message createMessage(Session session) throws JMSException {
            Gson gson = new Gson();
            String textMsg = gson.toJson(orderExp);
            TextMessage textMessage = session.createTextMessage(textMsg);

            //一定要加上这句：
            textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,expireTime);
            return textMessage;
        }
    }
}
