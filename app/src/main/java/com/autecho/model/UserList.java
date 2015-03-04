package com.autecho.model;

/**
 * Created by Santosh on 1/9/15.
 */
public class UserList {

    @com.google.gson.annotations.SerializedName("username")
    private String mUsername;

    @com.google.gson.annotations.SerializedName("fullname")
    private String mFullname;

    @com.google.gson.annotations.SerializedName("password")
    private String mPassword;

    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    public UserList() {

    }

    public UserList(String username, String password, String fullname) {
        this.setUsername(username);
        this.setPassword(password);
        this.setFullname(fullname);
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public void setFullname(String mFullname) {
        this.mFullname = mFullname;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getFullname() {
        return mFullname;
    }

    public String getmId() {
        return mId;
    }

}
