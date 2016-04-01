package com.autecho.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.autecho.dcc.MainActivity;
import com.autecho.dcc.R;

import java.security.SecureRandom;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * Created by sastagi on 3/22/16.
 */
public class LoginActivity extends Activity{
    //String token = UUID.randomUUID()+"2";
    private LoginPresenter mLoginPresenter;

    @Bind(R.id.register)
    Button register;

    @Bind(R.id.login)
    Button login;

    @Bind(R.id.progress_indicator)
    ProgressBar mProgressIndicator;

    @Bind(R.id.invalidcredentials)
    TextView mInvlaidCredentials;

    @Bind(R.id.login_email_address)
    EditText mEmailField;

    @Bind(R.id.login_password)
    EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);

        mLoginPresenter = new LoginPresenter(this);
    }

    public void showProgress() {
        mProgressIndicator.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mProgressIndicator.setVisibility(View.GONE);
    }

    public void showCredentialsError() {
        mInvlaidCredentials.setVisibility(View.VISIBLE);
    }

    public void hideCredentialsError() {
        mInvlaidCredentials.setVisibility(View.GONE);
    }

    public void navigateToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void navigateToRegistration() {
        startActivity(new Intent(this, RegistrationActivity.class));
    }

    @OnClick(R.id.register)
    protected void registerUser(){
        mLoginPresenter.goToRegistration();
    }

    @OnClick(R.id.login)
    protected void validateUser(){
        mLoginPresenter.validateCredentials(mEmailField.getText().toString(), mPasswordField.getText().toString());
    }

}
