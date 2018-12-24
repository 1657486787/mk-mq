/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.vo <br>
 *
 * @author mk <br>
 * Date:2018-12-24 10:43 <br>
 */

package com.suns.vo;

/**
 * ClassName: User <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-24 10:43 <br>
 * @version
 */
public class User {

    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
