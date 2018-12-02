/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.controller <br>
 *
 * @author mk <br>
 * Date:2018-11-30 11:42 <br>
 */

package com.suns.controller;

import com.suns.service.busi.SaveOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ClassName: OrderController <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-30 11:42 <br>
 * @version
 */
@Controller
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SaveOrder saveOrder;

    @RequestMapping("/index")
    public String index(){
        return "order";
    }

    @RequestMapping("/submitOrder")
    public String submitOrder(@RequestParam("orderNumber") int orderNumber){
        logger.info("orderNumber{}",orderNumber);
        saveOrder.insertOrders(orderNumber);
        return "suc";
    }
}
