/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service.busi <br>
 *
 * @author mk <br>
 * Date:2018-12-2 9:44 <br>
 */

package com.suns.service.busi;

import com.suns.dao.OrderExpDao;
import com.suns.model.OrderExp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ClassName: DlyOrderProcess <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-2 9:44 <br>
 * @version
 */
@Service
public class DlyOrderProcess {

    public final static short UNPAY = 0;
    public final static short PAYED = 1;
    public final static short EXPIRED = -1;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderExpDao orderExpDao;

    public void checkDelayOrder(OrderExp orderExp){
        OrderExp orderExp1 = orderExpDao.selectByPrimaryKey(orderExp.getId());
        if(UNPAY == orderExp1.getOrderStatus()){
            //过期订单未支付，则更新为已过期
            orderExpDao.updateExpireOrder(orderExp.getId());
            logger.info("过期订单未支付，则更新为已过期:{}",orderExp1);
        }else{
            //过期订单已支付，就不用做什么事情了
            logger.info("过期订单已支付，就不用做什么事情了:{}",orderExp1);

        }

    }

}
