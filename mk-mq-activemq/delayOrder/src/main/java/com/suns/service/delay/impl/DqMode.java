/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service.delay.impl <br>
 *
 * @author mk <br>
 * Date:2018-11-30 15:19 <br>
 */

package com.suns.service.delay.impl;

import com.suns.model.OrderExp;
import com.suns.service.busi.DlyOrderProcess;
import com.suns.service.delay.IDelayOrder;
import com.suns.vo.ItemVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.DelayQueue;

/**
 * ClassName: DqMode <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-30 15:19 <br>
 * @version
 */
@Service
@Qualifier("dq")
public class DqMode implements IDelayOrder {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static DelayQueue<ItemVo> delayQueue = new DelayQueue();

    @Autowired
    private DlyOrderProcess dlyOrderProcess;
    private Thread takeOrder;


    @Override
    public void orderDelay(OrderExp orderExp, long expireTime) {
        ItemVo itemVo = new ItemVo(expireTime*1000,orderExp);
        delayQueue.put(itemVo);
        logger.info("订单[超时时长："+expireTime+"秒]被推入检查队列，订单详情："+orderExp);
    }


    public class TakeOrder implements Runnable{

        private DlyOrderProcess dlyOrderProcess;

        public TakeOrder(DlyOrderProcess dlyOrderProcess) {
            this.dlyOrderProcess = dlyOrderProcess;
        }

        @Override
        public void run() {

            logger.info("处理到期订单线程已经启动！");
            while(!Thread.currentThread().isInterrupted()){
                try {
                    ItemVo<OrderExp> itemVo = delayQueue.take();
                    dlyOrderProcess.checkDelayOrder( itemVo.getData());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            logger.info("处理到期订单线程准备关闭......");
        }
    }

    //初始化完成之后就会调用该方法，用户监听Delayed的消息是否已经到期
//    @PostConstruct
//    public void init(){
//        takeOrder = new Thread(new TakeOrder(dlyOrderProcess));
//        takeOrder.start();
//    }

//    @PreDestroy
//    public void destory(){
//        takeOrder.interrupt();
//    }
}
