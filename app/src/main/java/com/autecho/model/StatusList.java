package com.autecho.model;

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

    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    public StatusList() {

    }

    public StatusList(String userId, String status, int mood, String location, String bloburl) {
        this.setUserid(userId);
        this.setStatus(status);
        this.setMood(mood);
        this.setLocation(location);
        this.setBloburl(bloburl);
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
}
