package com.autecho.dcc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.autecho.model.UserList;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.net.MalformedURLException;
import java.util.List;

public class Autecho extends FragmentActivity {

    private static final int LANDING = 0;
    private static final int REGISTERATION = 1;
    private static final int FRAGMENT_COUNT = 2;
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

    public static MobileServiceClient mClient;

    private MobileServiceTable<UserList> mUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autecho);

        fragments[LANDING] = getSupportFragmentManager().findFragmentById(R.id.LandingFragment);
        fragments[REGISTERATION] = getSupportFragmentManager().findFragmentById(R.id.RegisterFragment);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for(Fragment f:fragments) {
            transaction.hide(f);
        }
        transaction.commit();
        transaction.show(fragments[LANDING]);
        //Create the database connection
        try {
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    "https://autecho.azure-mobile.net/",
                    "EJGwalGeNhbONhLArapTkycXhFQXww10",
                    this);

            // Get the Mobile Service Table instance to use
            mUserList = mClient.getTable(UserList.class);
        } catch (MalformedURLException e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }

        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        if(sharedPref.getString(getString(R.string.username),null)!=null){
            Intent mainIntent = new Intent(Autecho.this,MainActivity.class);
            mainIntent.putExtra(getString(R.string.username), ((EditText)findViewById(R.id.login_email_address)).getText().toString());
            Autecho.this.startActivity(mainIntent);
            Autecho.this.finish();
        }
        ((EditText) findViewById(R.id.login_password)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    final String username = ((EditText)findViewById(R.id.login_email_address)).getText().toString();
                    final String password = ((EditText)findViewById(R.id.login_password)).getText().toString();
                    //Show progress bar
                    (findViewById(R.id.invalidcredentials)).setVisibility(View.GONE);
                    (findViewById(R.id.progress_indicator)).setVisibility(View.VISIBLE);
                    mUserList.where().field("username").eq(username).execute(new TableQueryCallback<UserList>() {

                        public void onCompleted(List<UserList> result, int count, Exception exception, ServiceFilterResponse response) {
                            if (exception == null) {
                                for (UserList item : result) {
                                    //Check if passwords are equal
                                    //if equal show feed
                                    (findViewById(R.id.progress_indicator)).setVisibility(View.GONE);
                                    if(password.equals(item.getPassword())){
                                        //put identifier in shared preferences
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString(getString(R.string.username), username);
                                        editor.commit();
                                        Intent mainIntent = new Intent(Autecho.this,MainActivity.class);
                                        //mainIntent.putExtra("accountId", accountId);
                                        //mainIntent.putExtra("apikey", apikey);
                                        Autecho.this.startActivity(mainIntent);
                                        Autecho.this.finish();
                                    }
                                    else{
                                        (findViewById(R.id.invalidcredentials)).setVisibility(View.VISIBLE);
                                    }
                                    Log.d("THE PASSWORD IS:", item.getPassword());
                                }

                            } else {
                                //createAndShowDialog(exception, "Error");
                                Log.e("ERROR", "Error getting mobile service");
                            }
                        }
                    });
                }
                return false;
            }
        });
        //Check if user is logged in
        //Hide the splash screen and show login page if not logged else listview
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //If user is logged in, show activity feed
                /*Intent mainIntent = new Intent(Autecho.this,MainActivity.class);
                //mainIntent.putExtra("accountId", accountId);
                //mainIntent.putExtra("apikey", apikey);
                Autecho.this.startActivity(mainIntent);
                Autecho.this.finish();*/
                //else show login screen
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                for (Fragment f : fragments) {
                    transaction.show(f);
                }
                transaction.commit();
                transaction.show(fragments[REGISTERATION]);
                Log.d("show reg", "showreg");
                //Show activity feed

            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}
