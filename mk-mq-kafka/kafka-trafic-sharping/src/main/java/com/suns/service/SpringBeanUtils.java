/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.config <br>
 *
 * @author mk <br>
 * Date:2018-12-24 14:54 <br>
 */

package com.suns.service;

import org.springframework.context.ApplicationContext;


/**
 * ClassName: SpringBeanUtils <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-24 14:54 <br>
 * @version
 */
public class SpringBeanUtils {

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext)  {
        SpringBeanUtils.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static <T> T getBean(String name){
        if(null == applicationContext){
            return null;
        }
        return (T)applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz){
        if(null == applicationContext){
            return null;
        }
        return applicationContext.getBean(clazz);
    }


}
