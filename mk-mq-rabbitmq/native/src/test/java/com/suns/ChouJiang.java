/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns <br>
 *
 * @author mk <br>
 * Date:2018-12-13 15:16 <br>
 */

package com.suns;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * ClassName: ChouJiang <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-13 15:16 <br>
 * @version
 */
public class ChouJiang {

    private static String users [] = {"num0","num1","num2","num3","num4","num5","num6","num7","num8","num9","num10",
            "num11","num12","num13","num14","num15","num16","num17","num18","num19","num20",
            "num21","num22","num23","num24","num25","num26","num27","num28","num29","num30",
            "num31","num32","num33","num34","num35","num36","num37","num38","num39","num40",
            "num41","num42","num43","num44","num45","num46","num47","num48","num49"};

    public static void main(String[] args) {
        Set set = new HashSet();
        int num = 5;
        for(;;){
            if(set.size() == num){
                break;
            }
            int i = new SecureRandom().nextInt(users.length);
            set.add(users[i]);
        }
        System.out.println("中奖人员："+set);
    }
}
