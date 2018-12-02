/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service.delay <br>
 * @author mk <br>
 * Date:2018-11-30 15:17 <br>
 */
 
package com.suns.service.delay;

import com.suns.model.OrderExp;

/**
 * Name: IDelayOrder <br>
 * Description:  <br>
 * @author mk <br>
 * @Date 2018-11-30 15:17 <br>
 * @version
 */
public interface IDelayOrder {

    /**
     * 进行延时处理的方法
     * @param orderExp 要进行延时处理的订单
     * @param expireTime 延时时长，单位秒
     */
    void orderDelay(OrderExp orderExp,long expireTime);
}
