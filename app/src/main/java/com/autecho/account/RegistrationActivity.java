package com.autecho.account;

import android.app.Activity;
import android.os.Bundle;

import com.autecho.dcc.R;

import butterknife.ButterKnife;

/**
 * Created by sastagi on 3/26/16.
 */
public class RegistrationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_login);
        ButterKnife.bind(this);

    }
}
