package com.autecho.model;

/**
 * Created by Santosh on 1/9/15.
 */
public class UserList {

    @com.google.gson.annotations.SerializedName("username")
    private String mUsername;

    @com.google.gson.annotations.SerializedName("password")
    private String mPassword;

    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    public UserList() {

    }

    public UserList(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getmId() {
        return mId;
    }

}
