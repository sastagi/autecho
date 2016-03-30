package com.autecho.dcc;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.autecho.account.AccountInfo;
import com.autecho.account.LoginActivity;
import com.autecho.model.UserList;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.net.MalformedURLException;
import java.util.List;

;

public class Autecho extends FragmentActivity {

    private static final int LANDING = 0;
    private static final int REGISTERATION = 1;
    private static final int FRAGMENT_COUNT = 2;
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

    public static MobileServiceClient mClient;

    public static Context mContext;

    private MobileServiceTable<UserList> mUserList;

    public static AccountManager mAccountManager;

    private boolean mInvalidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autecho);
        mContext = this;

        //check if account exists

        //If account exists, go to MainActivity else go to login

        /*fragments[LANDING] = getSupportFragmentManager().findFragmentById(R.id.LandingFragment);
        fragments[REGISTERATION] = getSupportFragmentManager().findFragmentById(R.id.RegisterFragment);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for(Fragment f:fragments) {
            transaction.hide(f);
        }
        transaction.commit();
        transaction.show(fragments[LANDING]);*/
        //Create the database connection
        try {
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    "https://autecho.azure-mobile.net/",
                    "EJGwalGeNhbONhLArapTkycXhFQXww10",
                    this);

            // Get the Mobile Service Table instance to use
        } catch (MalformedURLException e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }

        mAccountManager = AccountManager.get(this);
        checkAccount(AccountInfo.AUTHTOKENTYPE, false);
        /*final SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        if(sharedPref.getString(getString(R.string.userid),null)!=null){
            Intent mainIntent = new Intent(Autecho.this,MainActivity.class);
            mainIntent.putExtra(getString(R.string.userid), ((EditText)findViewById(R.id.login_email_address)).getText().toString());
            Autecho.this.startActivity(mainIntent);
            Autecho.this.finish();
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
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
                                for (UserList userDetail : result) {
                                    //Check if passwords are equal
                                    //if equal show feed
                                    (findViewById(R.id.progress_indicator)).setVisibility(View.GONE);
                                    if(password.equals(userDetail.getPassword())){
                                        //put identifier in shared preferences
                                        Log.d("THE USERID IS:", userDetail.getmId());
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString(getString(R.string.userid), userDetail.getmId());
                                        editor.putString(getString(R.string.fullname), userDetail.getFullname());
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
                                    Log.d("THE PASSWORD IS:", userDetail.getPassword());
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
        });*/
    }

    private void checkAccount(final String authTokenType, final boolean invalidate) {
        mInvalidate = invalidate;
        final Account availableAccounts[] = mAccountManager.getAccountsByType(AccountInfo.ACCOUNT_TYPE);

        if (availableAccounts.length == 0) {
            addNewAccount(AccountInfo.ACCOUNT_TYPE,authTokenType);
        } else {
            String name[] = new String[availableAccounts.length];
            for (int i = 0; i < availableAccounts.length; i++) {
                name[i] = availableAccounts[i].name;
                showMessage(name[i]);
            }
            getExistingAccountAuthToken(availableAccounts[0], authTokenType);
            //Show feed screen
        }
    }

    private void addNewAccount(String accountType, String authTokenType) {
        final AccountManagerFuture<Bundle> future = mAccountManager.addAccount(accountType, authTokenType, null, null, this, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                try {
                    Bundle bnd = future.getResult();
                    showMessage("Account was created");

                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage(e.getMessage());
                }
            }
        }, null);
    }

    private void getExistingAccountAuthToken(Account account, String authTokenType) {
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(account, authTokenType, null, this, null, null);

        new Thread(new Runnable() {
            @Override
            public void run() {
                showMessage("Succcess");
                try {
                    Bundle bnd = future.getResult();
                    final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                    showMessage("Succcess"+authtoken);
                    Intent mainIntent = new Intent(Autecho.this,MainActivity.class);
                    //mainIntent.putExtra(getString(R.string.userid), ((EditText)findViewById(R.id.login_email_address)).getText().toString());
                    Autecho.this.startActivity(mainIntent);
                    Autecho.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage("Failure"+e.getMessage());
                }
            }
        }).start();
    }

    private void showMessage(final String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
