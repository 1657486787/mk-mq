/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service.impl <br>
 *
 * @author mk <br>
 * Date:2018-11-27 11:12 <br>
 */

package com.suns.service.impl;

import com.suns.service.IUserReg;
import com.suns.service.busi.SaveUser;
import com.suns.service.busi.SendEmail;
import com.suns.service.busi.SendSms;
import com.suns.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * ClassName: ParallelProcess <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-27 11:12 <br>
 * @version
 */
@Service
@Qualifier("parallel")
public class ParallelProcess implements IUserReg {

    @Autowired
    private SaveUser saveUser;
    @Autowired
    private SendEmail sendEmail;
    @Autowired
    private SendSms sendSms;

    @Override
    public boolean userRegister(User user) {
        try{
            saveUser.saveUser(user);
            new Thread(new SendEmailRunnable(sendEmail,user)).start();
            new Thread(new SendSmsRunnable(sendSms,user)).start();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    class SendEmailRunnable implements Runnable{

        private SendEmail sendEmail;
        private User user;
        public SendEmailRunnable(SendEmail sendEmail,User user){
            this.sendEmail = sendEmail;
            this.user = user;
        }

        @Override
        public void run() {
            sendEmail.sendEmail(user.getEmail());
        }
    }

    class SendSmsRunnable implements Runnable{

        private SendSms sendSms;
        private User user;
        public SendSmsRunnable(SendSms sendSms,User user){
            this.sendSms = sendSms;
            this.user = user;
        }

        @Override
        public void run() {
            System.out.println("sendSms,checkCode:"+sendSms.sendSms(user.getPhoneNumber()));
        }
    }

}
