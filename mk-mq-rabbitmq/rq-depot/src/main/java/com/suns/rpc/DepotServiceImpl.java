/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.rpc <br>
 *
 * @author mk <br>
 * Date:2018-12-10 9:44 <br>
 */

package com.suns.rpc;

import com.suns.service.DepotManager;
import com.suns.vo.GoodTransferVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ClassName: DepotServiceImpl <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-10 9:44 <br>
 * @version
 */
@Service
public class DepotServiceImpl implements DepotService {

    @Autowired
    private DepotManager depotManager;

    @Override
    public void changeDepot(GoodTransferVo goodTransferVo) {
        depotManager.operDepot(goodTransferVo);
    }
}
