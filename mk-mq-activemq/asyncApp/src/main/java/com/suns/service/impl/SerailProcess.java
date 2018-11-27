/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service.impl <br>
 *
 * @author mk <br>
 * Date:2018-11-27 10:42 <br>
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
 * ClassName: SerailProcess <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-27 10:42 <br>
 * @version
 */
@Service
@Qualifier("serial")
public class SerailProcess implements IUserReg {

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
            sendEmail.sendEmail(user.getEmail());
            sendSms.sendSms(user.getPhoneNumber());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}
