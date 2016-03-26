package com.autecho.account;

import android.os.Handler;
import android.text.TextUtils;

/**
 * Created by sastagi on 3/26/16.
 */
public class LoginInteractor {
    public void login(final String username, final String password, final OnLoginFinishedListener listener) {
        // Mock login. I'm creating a handler to delay the answer a couple of seconds
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                boolean error = false;
                if (TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
                    listener.onCredentialsError();
                    error = true;
                }
                if (!error){
                    //Add account
                    listener.onSuccess();
                }
            }
        }, 2000);
    }



    interface OnLoginFinishedListener {
        void onCredentialsError();

        void onSuccess();
    }
}
