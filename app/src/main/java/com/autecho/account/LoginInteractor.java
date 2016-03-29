package com.autecho.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.autecho.dcc.Autecho;
import com.autecho.dcc.R;
import com.autecho.model.UserList;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

import static com.autecho.dcc.Autecho.mClient;
import static com.autecho.dcc.Autecho.mContext;

/**
 * Created by sastagi on 3/26/16.
 */
public class LoginInteractor {
    public void login(final String username, final String password, final OnLoginFinishedListener listener) {
        MobileServiceTable<UserList>  mUserList = mClient.getTable(UserList.class);
        final SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        mUserList.where().field("username").eq(username).execute(new TableQueryCallback<UserList>() {

            public void onCompleted(List<UserList> result, int count, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    for (UserList userDetail : result) {
                        //Check if passwords are equal
                        //if equal show feed
                        if (password.equals(userDetail.getPassword())) {
                            //put identifier in shared preferences
                            Log.d("THE USERID IS:", userDetail.getmId());
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(mContext.getResources().getString(R.string.userid), userDetail.getmId());
                            editor.putString(mContext.getResources().getString(R.string.fullname), userDetail.getFullname());
                            editor.commit();
                            //Create new account
                            final Account account = new Account(username, AccountInfo.ACCOUNT_TYPE);
                            Autecho.mAccountManager.addAccountExplicitly(account, password, null);
                            Autecho.mAccountManager.setAuthToken(account,userDetail.getmId(), AccountInfo.AUTHTOKENTYPE);
                            listener.onSuccess();
                        }
                        else{
                            listener.onCredentialsError();
                        }
                    }
                } else {
                    //TODO:Check other cases
                    //createAndShowDialog(exception, "Error");
                }
            }
        });
    }



    interface OnLoginFinishedListener {
        void onCredentialsError();
        void onSuccess();
    }
}
