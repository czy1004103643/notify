package xyz.melodyl.pojo;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class User implements Serializable {
    private Integer userID;
    private String userName;
    private String password;

    public User() {
    }

    public User(Integer userID, String userName, String password) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
