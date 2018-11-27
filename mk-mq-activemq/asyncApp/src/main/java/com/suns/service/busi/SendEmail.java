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
 * ClassName: SendEmail <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-27 10:34 <br>
 * @version
 */
@Service
public class SendEmail {

    public void sendEmail(String email){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("-------------Already Send email to " + email);
    }
}
