/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service <br>
 *
 * @author mk <br>
 * Date:2018-12-10 9:30 <br>
 */

package com.suns.service;

import com.suns.rpc.DepotService;
import com.suns.rpc.RpcProxy;
import com.suns.vo.GoodTransferVo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

/**
 * ClassName: RpcMode <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-10 9:30 <br>
 * @version
 */
//@Service("rpc")
@Service
@Qualifier("rpc")
public class RpcMode implements IProDepot{

    private static final String IP = "127.0.0.1";
    private static final int PORT = 10002;

    public void processDepot(String goodsId, int amount) {

        DepotService service = RpcProxy.getRmoteProxyObj(DepotService.class,
                new InetSocketAddress(IP,PORT));
        GoodTransferVo goodTransferVo = new GoodTransferVo();
        goodTransferVo.setGoodsId(goodsId);
        goodTransferVo.setChangeAmount(amount);
        goodTransferVo.setInOrOut(true);
        service.changeDepot(goodTransferVo);
    }

}
