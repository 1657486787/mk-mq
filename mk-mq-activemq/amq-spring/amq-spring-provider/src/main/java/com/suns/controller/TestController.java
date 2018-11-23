/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.controller <br>
 *
 * @author mk <br>
 * Date:2018-11-23 16:23 <br>
 */

package com.suns.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ClassName: TestController <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-23 16:23 <br>
 * @version
 */
@Controller
public class TestController {


    @RequestMapping("/test.do")
    @ResponseBody
    public String test(){
        System.out.println("test....");
        return "test..";
    }
}
