package com.autecho.model;

import java.util.Date;

/**
 * Created by Santosh on 1/14/15.
 */
public class StatusList {

    @com.google.gson.annotations.SerializedName("status")
    private String mStatus;

    @com.google.gson.annotations.SerializedName("location")
    private String mLocation;

    @com.google.gson.annotations.SerializedName("bloburl")
    private String mBloburl;

    @com.google.gson.annotations.SerializedName("userid")
    private String mUserid;

    @com.google.gson.annotations.SerializedName("mood")
    private int mMood;

    @com.google.gson.annotations.SerializedName("fullname")
    private String mFullname;

    @com.google.gson.annotations.SerializedName("address")
    private String mAddress;

    @com.google.gson.annotations.SerializedName("__createdAt")
    private Date mCreatedAt;

    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    public StatusList() {

    }

    public StatusList(String userId, String fullname, String status, int mood, String location, String bloburl, String address) {
        this.setUserid(userId);
        this.setFullname(fullname);
        this.setStatus(status);
        this.setMood(mood);
        this.setLocation(location);
        this.setBloburl(bloburl);
        this.setAddress(address);
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }

    public String getUserid() {
        return mUserid;
    }

    public void setUserid(String userid) {
        this.mUserid = userid;
    }

    public void setBloburl(String mBloburl) {
        this.mBloburl = mBloburl;
    }
    public String getBloburl() {
        return mBloburl;
    }

    public int getMood() {
        return mMood;
    }

    public void setMood(int mMood) {
        this.mMood = mMood;
    }

    public String getFullname() {
        return mFullname;
    }

    public void setFullname(String mFullname) {
        this.mFullname = mFullname;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }
}
