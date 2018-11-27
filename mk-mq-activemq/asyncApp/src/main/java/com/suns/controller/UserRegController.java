/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.controller <br>
 *
 * @author mk <br>
 * Date:2018-11-27 10:23 <br>
 */

package com.suns.controller;

import com.suns.service.IUserReg;
import com.suns.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ClassName: UserRegController <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-27 10:23 <br>
 * @version
 */
@Controller
public class UserRegController {

    @Autowired
//    @Qualifier("serial")//按顺序调用1.用户信息，2.发送邮件，3.发送短信==>SerailProcess
//    @Qualifier("parallel")//通过多线程的方式==>ParallelProcess
    @Qualifier("mq")//使用Mq==>MqProcess
    private IUserReg userReg;

    @RequestMapping("/index")
    public String userReg() {
        return "userReg";
    }

    @RequestMapping("/saveUser")
    @ResponseBody
    public String saveUser(@RequestParam("name") String name,@RequestParam("email")String email,
                           @RequestParam("number")String phoneNumber,@RequestParam("address")String address){
        if(userReg.userRegister(new User(name,email,phoneNumber,address)))
            return "suc";
        else
            return "fail";
    }
}
