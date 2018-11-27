/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service.busi <br>
 *
 * @author mk <br>
 * Date:2018-11-27 10:34 <br>
 */

package com.suns.service.busi;

import org.springframework.stereotype.Service;

/**
 * ClassName: SendSms <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-27 10:34 <br>
 * @version
 */
@Service
public class SendSms {

    public String sendSms(String phoneNumber){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String checkCode = (int)((Math.random()*9+1)*100000) + "";//6位随机数
        System.out.println("-------------Already Send sms["+checkCode+"] to " + phoneNumber);
        return checkCode;
    }

}
