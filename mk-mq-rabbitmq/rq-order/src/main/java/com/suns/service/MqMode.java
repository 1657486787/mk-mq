/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service <br>
 *
 * @author mk <br>
 * Date:2018-12-10 9:59 <br>
 */

package com.suns.service;

import com.google.gson.Gson;
import com.suns.vo.GoodTransferVo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * ClassName: MqMode <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-10 9:59 <br>
 * @version
 */
@Service
@Qualifier("mq")
public class MqMode implements IProDepot {

    private final static String DEPOT_RK = "amount.depot";
    private final static String DEPOT_EXCHANGE = "depot-amount-exchange";
    private static Gson gson = new Gson();

    @Autowired
    RabbitTemplate rabbitTemplate;


    @Override
    public void processDepot(String goodsId, int amount) {
        GoodTransferVo goodTransferVo = new GoodTransferVo();
        goodTransferVo.setGoodsId(goodsId);
        goodTransferVo.setChangeAmount(amount);
        goodTransferVo.setInOrOut(false);
        String goods = gson.toJson(goodTransferVo);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);//消息持久化：1.消息本身需要持久化2.交换器需要持久化3.队列需要持久化，三者缺一不可
        rabbitTemplate.send(DEPOT_EXCHANGE, DEPOT_RK,
                new Message(goods.getBytes(), messageProperties));

        rabbitTemplate.send(DEPOT_EXCHANGE, "ErrorRoute",//故意发送一个不存在的路由键
                new Message(goods.getBytes(), messageProperties));
    }
}
