/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns <br>
 *
 * @author mk <br>
 * Date:2018-12-24 17:12 <br>
 */

package com.suns;

import com.suns.service.SpringBeanUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * ClassName: KafkaTraficSharpingApplication <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-24 17:12 <br>
 * @version
 */
@SpringBootApplication
public class KafkaTraficSharpingApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(KafkaTraficSharpingApplication.class, args);
        SpringBeanUtils.setApplicationContext(applicationContext);
    }
}
