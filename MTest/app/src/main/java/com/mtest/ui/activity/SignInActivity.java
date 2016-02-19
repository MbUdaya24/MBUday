package com.mtest.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.mtest.R;
import com.mtest.entity.User;
import com.mtest.manager.UserManager;
import com.mtest.util.CheckInternetConnection;
import com.mtest.util.Config;
import com.mtest.util.Utility;
import com.squareup.otto.Subscribe;

public class SignInActivity extends BaseActivity {


    private UserManager mUserManager;
    private User mUser;

    private EditText etEmail;
    private EditText etPassword;

    private TextInputLayout ilEmail;
    private TextInputLayout ilPassword;

    private String emailId;
    private String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        initUi();
        initManager();
        onDoneClick();
    }



    private void initManager() {
        mUserManager = new UserManager();
        mUser = new User();
    }

    private void initUi() {
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        ilEmail = (TextInputLayout)findViewById(R.id.ilEmail);
        ilPassword = (TextInputLayout)findViewById(R.id.ilPassword);
    }

    public void onSignInClick(View v) {
        if (CheckInternetConnection.checkInternet(this)) {
            checkValidation();
        } else {
            CheckInternetConnection.noInternet(this);
        }
    }

    private void onDoneClick() {
        Utility.hideKeyBoard(this);
        etPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    if (CheckInternetConnection.checkInternet(SignInActivity.this)) {
                        checkValidation();
                    } else {
                        CheckInternetConnection.noInternet(SignInActivity.this);
                    }

                    return true;
                }
                return false;
            }
        });
    }

    public void onFBLoginClick(View v) {
        Toast.makeText(this, R.string.error_notpart, Toast.LENGTH_SHORT).show();

    }


    public void onBackClick(View v) {
        Utility.hideKeyBoard(this);
        finish();
    }






    public void onHideKeyBoard(View v) {
        Utility.hideKeyBoard(this);
    }

    private void checkValidation() {
        emailId = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();


        ilEmail.setErrorEnabled(false);
        ilPassword.setErrorEnabled(false);

        if (!Utility.nullCheck(emailId)) {
            ilEmail.setError(getResources().getString(R.string.error_email));
        } else if (!Utility.nullCheck(password)) {
            ilPassword.setError(getResources().getString(R.string.error_password));
        } else if (!Utility.isValidEmail(emailId)) {
            ilEmail.setError(getResources().getString(R.string.error_valid_email));

        } else {

            signIn();

        }


    }


    private void signIn() {
        mUser.username = emailId;
        mUser.password = password;
        mUser.connection = Config.CONNNECTION;
        mUser.clientId = Config.CLIENT_ID;
        mUserManager.login(mUser, this, true);
    }




    @Subscribe
    public void signInSucess(User user) {
        if (user.status) {
            Intent i = new Intent(this, ProductActivity.class);
            startActivity(i);
            finish();
            mUserManager.isLogin = true;
            mUserManager.saveToSharedPreference(this);
        } else {
            Toast.makeText(this, user.errorDescription, Toast.LENGTH_SHORT).show();
        }


    }


}
