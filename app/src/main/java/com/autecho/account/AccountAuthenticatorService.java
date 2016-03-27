package com.autecho.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by sastagi on 3/26/16.
 */
public class AccountAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {

        AccountAuthenticator authenticator = new AccountAuthenticator(this);
        return authenticator.getIBinder();
    }
}