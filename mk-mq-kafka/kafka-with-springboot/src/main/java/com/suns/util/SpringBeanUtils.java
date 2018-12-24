/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.config <br>
 *
 * @author mk <br>
 * Date:2018-12-24 14:54 <br>
 */

package com.suns.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * ClassName: SpringBeanUtils <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-24 14:54 <br>
 * @version
 */
@Component
public class SpringBeanUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
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
