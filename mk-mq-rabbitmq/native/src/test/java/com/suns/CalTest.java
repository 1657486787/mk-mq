/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns <br>
 *
 * @author mk <br>
 * Date:2018-12-11 11:06 <br>
 */

package com.suns;

/**
 * ClassName: CalTest <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-11 11:06 <br>
 * @version
 */
public class CalTest {

    public static void main(String[] args) throws InterruptedException {
        cycle();

    }

    private static void cycle() throws InterruptedException {
        for(int i=0;i<1000;i++){
            int result = add(i, (i + 1));
            System.out.println(i+"+"+(i+1) +"="+ result);
            Thread.sleep(5000);
        }
    }

    private static int add(int i, int j) {
        return i+j;
    }

    private static int mul(int i, int j) {
        int result = (i+j)*i;
        return result;
    }
}
