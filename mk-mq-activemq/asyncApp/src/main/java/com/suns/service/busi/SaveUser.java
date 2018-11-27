/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service.busi <br>
 *
 * @author mk <br>
 * Date:2018-11-27 10:31 <br>
 */

package com.suns.service.busi;

import com.suns.vo.User;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: SaveUser <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-27 10:31 <br>
 * @version
 */
@Service
public class SaveUser {

    //用一个Map作为内存数据库，保存用户注册的信息
    private ConcurrentHashMap<String,User> userData = new ConcurrentHashMap();

    public void saveUser(User user){
        try {
            Thread.sleep(50);//模拟业务操作
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        userData.putIfAbsent(user.getName(),user);
    }
}
