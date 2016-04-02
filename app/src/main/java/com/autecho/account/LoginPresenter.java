package com.autecho.account;

/**
 * Created by sastagi on 3/26/16.
 */
public class LoginPresenter implements LoginInteractor.OnLoginFinishedListener{

    private LoginActivity mLoginActivity;
    private LoginInteractor mLoginInteractor;

    public LoginPresenter(LoginActivity loginView) {
        this.mLoginActivity = loginView;
        this.mLoginInteractor = new LoginInteractor();
    }

    public void validateCredentials(String username, String password) {
        mLoginInteractor.login(username, password, this);
    }

    public void goToRegistration() {
        mLoginActivity.navigateToRegistration();
    }

    @Override public void onCredentialsError() {
        mLoginActivity.hideProgress();
        mLoginActivity.showCredentialsError();
    }

    @Override public void onSuccess() {
        if (mLoginActivity != null) {
            mLoginActivity.navigateToHome();
        }
    }


}
