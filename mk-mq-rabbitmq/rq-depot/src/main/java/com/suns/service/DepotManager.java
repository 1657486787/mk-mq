/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service <br>
 *
 * @author mk <br>
 * Date:2018-12-10 9:44 <br>
 */

package com.suns.service;

import com.suns.vo.GoodTransferVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ClassName: DepotManager <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-10 9:44 <br>
 * @version
 */
@Service
public class DepotManager {

    @Autowired
    private Depot depot;

    public void operDepot(GoodTransferVo goodTransferVo){
        if(goodTransferVo.isInOrOut()){
            depot.inDepot(goodTransferVo.getGoodsId(),goodTransferVo.getChangeAmount());
        }else{
            depot.outDepot(goodTransferVo.getGoodsId(),goodTransferVo.getChangeAmount());
        }
    }

//    public boolean isEmpty(String goodsId){
//        return depot.getGoodsAmount(goodsId)==0?true:false;
//    }
}
