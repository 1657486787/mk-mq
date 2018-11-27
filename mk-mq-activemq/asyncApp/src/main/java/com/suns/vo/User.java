/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.vo <br>
 *
 * @author mk <br>
 * Date:2018-11-27 10:25 <br>
 */

package com.suns.vo;

/**
 * ClassName: User <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-27 10:25 <br>
 * @version
 */
public class User {

    private final String name;
    private final String email;
    private final String phoneNumber;
    private final String address;

    public User(String name, String email, String phoneNumber, String address) {
        super();
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "User [name=" + name
                + ", email=" + email
                + ", phoneNumber=" + phoneNumber
                + ", address=" + address
                + "]";
    }
}
