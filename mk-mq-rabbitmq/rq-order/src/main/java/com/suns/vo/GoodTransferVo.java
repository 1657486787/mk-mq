/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.vo <br>
 *
 * @author mk <br>
 * Date:2018-12-10 9:33 <br>
 */

package com.suns.vo;

import java.io.Serializable;

/**
 * ClassName: GoodTransferVo <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-10 9:33 <br>
 * @version
 */
public class GoodTransferVo implements Serializable {

    private String goodsId;
    private int changeAmount;
    private boolean inOrOut;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public int getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(int changeAmount) {
        this.changeAmount = changeAmount;
    }

    public boolean isInOrOut() {
        return inOrOut;
    }

    public void setInOrOut(boolean inOrOut) {
        this.inOrOut = inOrOut;
    }

}
