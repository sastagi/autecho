package com.autecho.dcc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class Autecho extends FragmentActivity {

    private static final int LANDING = 0;
    private static final int REGISTERATION = 1;
    private static final int FRAGMENT_COUNT = 2;

    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autecho);

        FragmentManager fm = getSupportFragmentManager();
        fragments[LANDING] = fm.findFragmentById(R.id.LandingFragment);
        fragments[REGISTERATION] = fm.findFragmentById(R.id.RegisterFragment);

        FragmentTransaction transaction = fm.beginTransaction();
        for(Fragment f:fragments) {
            transaction.hide(f);
        }
        transaction.commit();
        transaction.show(fragments[REGISTERATION]);

    }
}
