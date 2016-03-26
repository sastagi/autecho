package com.autecho.account;

import android.app.Activity;
import android.os.Bundle;

import com.autecho.dcc.R;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * Created by sastagi on 3/22/16.
 */
public class LoginActivity extends Activity{
    //String token = UUID.randomUUID()+"2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }
}
