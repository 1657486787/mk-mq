/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service.mq.impl <br>
 *
 * @author mk <br>
 * Date:2018-12-2 10:55 <br>
 */

package com.suns.service.mq.impl;

import com.google.gson.Gson;
import com.suns.model.OrderExp;
import com.suns.service.busi.DlyOrderProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * ClassName: MqConsumer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-2 10:55 <br>
 * @version
 */
@Service
public class MqConsumer implements MessageListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DlyOrderProcess dlyOrderProcess;

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            logger.info("接收到消息队列发出消息："+text);
            Gson gson = new Gson();
            OrderExp orderExp = gson.fromJson(text, OrderExp.class);
            dlyOrderProcess.checkDelayOrder(orderExp);
        } catch (JMSException e) {
            e.printStackTrace();
            logger.error("处理消费异常！",e);
        }
    }
}
