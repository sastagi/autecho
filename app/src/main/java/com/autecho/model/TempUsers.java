package com.autecho.model;

import java.util.UUID;

/**
 * Created by Santosh on 11/20/14.
 */
public class TempUsers {

    @com.google.gson.annotations.SerializedName("fullname")
    private String mFullname;

    @com.google.gson.annotations.SerializedName("email")
    private String mEmail;

    @com.google.gson.annotations.SerializedName("password")
    private String mPassword;

    public void setConfirmationCode(String mConfirmationCode) {
        this.mConfirmationCode = mConfirmationCode;
    }

    @com.google.gson.annotations.SerializedName("confirmationcode")
    private String mConfirmationCode;

    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    public TempUsers() {

    }

    public TempUsers(String fullname, String email, String password, String confirmationCode) {
        this.setFullname(fullname);
        this.setEmail(email);
        this.setPassword(password);
        this.setConfirmationCode(confirmationCode);
        mConfirmationCode = UUID.randomUUID().toString();
        this.setConfirmationCode(confirmationCode);
    }

    /*public String getId() {
        return mId;
    }

    public final void setId(String id) {
        mId = id;
    }*/

    public String getFullname() {
        return mFullname;
    }

    public final void setFullname(String fullname) {
        mFullname = fullname;
    }

    public String getEmail() {
        return mEmail;
    }

    public final void setEmail(String email) {
        mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public final void setPassword(String password) {
        //do MD5 hashing
        mPassword = password;
    }

    /*public String getConfirmationCode() {
        return mConfirmationCode;
    }

    public final void setConfirmationCode(String confirmationCode) {
        mConfirmationCode = confirmationCode;
    }*/
    //test

    @Override
    public boolean equals(Object o) {return o instanceof TempUsers && ((TempUsers) o).mConfirmationCode == mConfirmationCode;}
}
