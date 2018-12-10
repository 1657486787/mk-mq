/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service <br>
 *
 * @author mk <br>
 * Date:2018-12-10 9:29 <br>
 */

package com.suns.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * ClassName: ProcessOrder <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-10 9:29 <br>
 * @version
 */
@Service
public class ProcessOrder {

    private Logger logger = LoggerFactory.getLogger(ProcessOrder.class);

    @Autowired
//    @Qualifier("rpc")//rpc调用
    @Qualifier("mq")//mq
    private IProDepot proDepot;

    public void processOrder(String goodsId,int amount){
        try {
            Thread.sleep(80);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("--------------------["+goodsId+"]订单入库完成，准备变动库存！");
        proDepot.processDepot(goodsId,amount);

    }
}
