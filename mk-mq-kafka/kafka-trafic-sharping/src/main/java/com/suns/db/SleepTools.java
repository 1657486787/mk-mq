/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.db <br>
 *
 * @author mk <br>
 * Date:2018-12-24 17:19 <br>
 */

package com.suns.db;

import java.util.concurrent.TimeUnit;

/**
 * ClassName: SleepTools <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-24 17:19 <br>
 * @version
 */
public class SleepTools {

    /**
     * 按秒休眠
     * @param seconds 秒数
     */
    public static final void second(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
        }
    }

    /**
     * 按毫秒数休眠
     * @param seconds 毫秒数
     */
    public static final void ms(int seconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(seconds);
        } catch (InterruptedException e) {
        }
    }
}
