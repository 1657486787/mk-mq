/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.vo <br>
 *
 * @author mk <br>
 * Date:2018-11-26 9:51 <br>
 */

package com.suns.vo;

import java.io.Serializable;

/**
 * ClassName: User <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-26 9:51 <br>
 * @version
 */
public class User implements Serializable {

    private String name;
    private char sex;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                '}';
    }
}
