/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.vo <br>
 *
 * @author mk <br>
 * Date:2018-11-30 15:21 <br>
 */

package com.suns.vo;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: ItemVo <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-30 15:21 <br>
 * @version
 */
public class ItemVo<T> implements Delayed {

    //到期时间,但传入的数值代表过期的时长，传入单位毫秒
    private long activeTime;
    private T data;//业务数据，泛型

    public ItemVo(long activeTime,T data){
        super();
        this.activeTime = activeTime+System.currentTimeMillis();
        this.data = data;
    }

    public long getActiveTime(){
        return this.activeTime;
    }

    public T getData(){
        return this.data;
    }

    /*
     * 这个方法返回到激活日期的剩余时间，时间单位由单位参数指定。
     */
    @Override
    public long getDelay(TimeUnit unit) {
        long d = unit.convert(this.activeTime - System.currentTimeMillis(),unit);
        return d;
    }

    /*
     *Delayed接口继承了Comparable接口，按剩余时间排序，实际计算考虑精度为纳秒数
     */
    @Override
    public int compareTo(Delayed o) {
        long d = (getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS));
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }
}
