/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.vo <br>
 *
 * @author mk <br>
 * Date:2018-12-17 14:13 <br>
 */

package com.suns.vo;

/**
 * ClassName: DemoUser <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-17 14:13 <br>
 * @version
 */
public class DemoUser {

    private int id;
    private String name;

    public DemoUser(int id) {
        this.id = id;
    }

    public DemoUser(int id, String name) {
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

    @Override
    public String toString() {
        return "DemoUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
