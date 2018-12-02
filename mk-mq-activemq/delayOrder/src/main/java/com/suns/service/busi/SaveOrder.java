/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service.busi <br>
 *
 * @author mk <br>
 * Date:2018-11-30 11:42 <br>
 */

package com.suns.service.busi;

import com.suns.dao.OrderExpDao;
import com.suns.model.OrderExp;
import com.suns.service.delay.IDelayOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * ClassName: SaveOrder <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-30 11:42 <br>
 * @version
 */
@Service
public class SaveOrder {

    private Logger logger = LoggerFactory.getLogger(SaveOrder.class);

    public final static short UNPAY = 0;
    public final static short PAYED = 1;
    public final static short EXPIRED = -1;

    @Autowired
    private OrderExpDao orderExpDao;

//    @Autowired
//    @Qualifier("dq")//jdk自带的DelayeQueue
//    private IDelayOrder delayOrder;

    @Autowired
    @Qualifier("mq")//使用消息队列
    private IDelayOrder delayOrder;

    public void insertOrders(int orderNumber){
        Random random = new Random();
        for(int i=0;i<orderNumber;i++){
            long expireTime = random.nextInt(20)+5;//订单的超时时长，单位秒
            String orderNo = "D_"+expireTime+"S";
            OrderExp orderExp = new OrderExp();
            orderExp.setOrderNo(orderNo);
            orderExp.setOrderStatus(UNPAY);
            orderExp.setOrderNote("订单-"+orderNo);
            orderExpDao.insertDelayOrder(orderExp,expireTime);
//            logger.info("保存到订单{}-{}",orderNo,orderExp);

            //延时处理订单的的接口
            delayOrder.orderDelay(orderExp,expireTime);
        }
    }


//    1.当服务挂掉或重启之后，保存在DelayQueue中的数据会丢失（是保存在内存中的）。
//    解决办法：需要服务启动的时候去扫描订单表，查询未支付的订单做如下处理：如果已过期未支付，那么更新状态为已过期，了如果未过期未支付，就再次放入DelayQueue中
//    @PostConstruct
//    public void initDelayOrder() {
//        logger.info("系统启动，扫描表中过期未支付的订单并处理.........");
//
//        int counts = orderExpDao.updateExpireOrders();
//        logger.info("系统启动，处理了表中["+counts+"]个过期未支付的订单！");
//
//        List<OrderExp> orderList = orderExpDao.selectUnPayOrders();
//        logger.info("系统启动，发现了表中还有["+orderList.size()
//                +"]个未到期未支付的订单！推入检查队列准备到期检查....");
//        for(OrderExp order:orderList) {
//            //long expireTime = 10;//方便测试使用
//            long expireTime = order.getExpireTime().getTime()-(new Date().getTime());//剩余多久到期
//            delayOrder.orderDelay(order, expireTime);
//
//        }
//    }


}
