/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.rebalance <br>
 *
 * @author mk <br>
 * Date:2018-12-21 15:30 <br>
 */

package com.suns.rebalance;

import com.suns.config.BusiConst;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: RebalanceConsumer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-21 15:30 <br>
 * @version
 */
public class RebalanceConsumer {


    private static int threadCount = Runtime.getRuntime().availableProcessors();
    private static ExecutorService executorService = Executors.newFixedThreadPool(2);


    public static void main(String[] args) throws InterruptedException {

        for(int i=0;i<threadCount;i++){
            executorService.submit(new ConsumerWorker(BusiConst.REBALANCE_TOPIC,false));
        }

        TimeUnit.SECONDS.sleep(5);
        new Thread(new ConsumerWorker(BusiConst.REBALANCE_TOPIC,true)).start();

    }
}
