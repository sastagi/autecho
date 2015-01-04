package com.autecho.dcc;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.autecho.helpers.Helpers;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

import java.net.MalformedURLException;


public class RegisterFragment extends Fragment{

    private Helpers helpers;
    private String emailId, fullname, password, response, deviceid;

    private MobileServiceClient mClient;

    private MobileServiceTable<TempUsers> mTempTable;

    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        helpers = new Helpers();
        return inflater.inflate(R.layout.register_login, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.loadingProgressBar);

        // Initialize the progress bar
        mProgressBar.setVisibility(ProgressBar.GONE);
        //User clicks on the registeration button
        getActivity().findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().findViewById(R.id.loginmain).setVisibility(View.GONE);
                getActivity().findViewById(R.id.registerationmain).setVisibility(View.VISIBLE);
            }
        });
        //User clicks on the "Done" button on the registration screen
        getActivity().findViewById(R.id.send_email).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               validate_registration_information();
            }
        });
        getActivity().findViewById(R.id.register_again).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                register_Again();
            }
        });
        getActivity().findViewById(R.id.send_another_confirmation_link).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                another_confirmation();
            }
        });
        getActivity().findViewById(R.id.another_confirmation_link).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                another_confirmation();
            }
        });
        //Create the database connection
        try {
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    "https://autecho.azure-mobile.net/",
                    "EJGwalGeNhbONhLArapTkycXhFQXww10",
                    getActivity()).withFilter(new ProgressFilter());

            // Get the Mobile Service Table instance to use
            mTempTable = mClient.getTable(TempUsers.class);
        } catch (MalformedURLException e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }
    }

    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }

    private void createAndShowDialog(String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

    private class ProgressFilter implements ServiceFilter {

        @Override
        public void handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback,
                                  final ServiceFilterResponseCallback responseCallback) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }
            });

            nextServiceFilterCallback.onNext(request, new ServiceFilterResponseCallback() {

                @Override
                public void onResponse(ServiceFilterResponse response, Exception exception) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
                        }
                    });

                    if (responseCallback != null)  responseCallback.onResponse(response, exception);
                }
            });
        }
    }

    public void  validate_registration_information(){

        //Validate username, email, password
        getActivity().findViewById(R.id.emailexists_validation).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.emailformat_validation).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.another_confirmation_link).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.password_validation).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.fullname_validation).setVisibility(View.INVISIBLE);
        ((EditText)getActivity().findViewById(R.id.email_address)).setBackgroundResource(R.drawable.correctfield);
        ((EditText)getActivity().findViewById(R.id.fullname)).setBackgroundResource(R.drawable.correctfield);
        ((EditText)getActivity().findViewById(R.id.password)).setBackgroundResource(R.drawable.correctfield);

        Log.d("Test",getActivity().toString());
        if (helpers.isOnline(getActivity())){  //check is user is online
            emailId = ((EditText)getActivity().findViewById(R.id.email_address)).getText().toString();
            fullname = ((EditText)getActivity().findViewById(R.id.fullname)).getText().toString();
            password = ((EditText)getActivity().findViewById(R.id.password)).getText().toString();
            if((helpers.isEmailValid(emailId))&&(password.trim().length() > 5)&&(fullname.trim().length() > 0)){
                //Start Async task to send information to Azure
                //Generate email token
                //Generate MD5 password
                //
                //new Register().execute();
                //new Register().execute("addtempuser.php?email_value="+emailId+"&device_id="+deviceid+"&fullname="+fullname+"&password="+password);
                Toast.makeText(getActivity(), "Validation successful", Toast.LENGTH_SHORT).show();
                registerUser();
            }
            else{
                if(!(helpers.isEmailValid(emailId))){
                    ((EditText)getActivity().findViewById(R.id.email_address)).setBackgroundResource(R.drawable.errorfield);
                    getActivity().findViewById(R.id.emailformat_validation).setVisibility(View.VISIBLE);
                }
                if(password.trim().length()<6){
                    ((EditText)getActivity().findViewById(R.id.password)).setBackgroundResource(R.drawable.errorfield);
                    getActivity().findViewById(R.id.password_validation).setVisibility(View.VISIBLE);
                }
                if(fullname.trim().length()==0){
                    ((EditText)getActivity().findViewById(R.id.fullname)).setBackgroundResource(R.drawable.errorfield);
                    getActivity().findViewById(R.id.fullname_validation).setVisibility(View.VISIBLE);
                }
            }
        }
        else
            Toast.makeText(getActivity(), "No internet Connection", Toast.LENGTH_SHORT).show();
    }

    public void registerUser(){
        // Create a new item
        TempUsers item = new TempUsers(fullname, emailId, password);

        // Insert the new item
        mTempTable.insert(item, new TableOperationCallback<TempUsers>() {

            public void onCompleted(TempUsers entity, Exception exception, ServiceFilterResponse response) {

                if (exception == null) {
                    getActivity().findViewById(R.id.request_sent).setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.request_body).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.send_email).setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Registration successful", Toast.LENGTH_SHORT).show();
                } else {
                    createAndShowDialog(exception, "Error");
                }

            }
        });
    }

    public void register_Again(){
           String t="";
    }

    public void another_confirmation(){

    }

}

