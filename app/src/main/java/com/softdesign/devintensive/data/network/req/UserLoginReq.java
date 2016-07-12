package com.softdesign.devintensive.data.network.req;

/**
 * Created by Andrey on 12.07.2016.
 */
public class UserLoginReq {
    private String email;
    private String password;

    public UserLoginReq(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
