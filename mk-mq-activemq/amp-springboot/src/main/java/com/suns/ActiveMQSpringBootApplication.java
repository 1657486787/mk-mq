/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns <br>
 *
 * @author mk <br>
 * Date:2018-11-26 11:45 <br>
 */

package com.suns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ClassName: ActiveMQSpringBootApplication <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-26 11:45 <br>
 * @version
 */
@SpringBootApplication
public class ActiveMQSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActiveMQSpringBootApplication.class,args);
        System.out.println(" ActiveMQSpringBootApplication start....");
    }
}
