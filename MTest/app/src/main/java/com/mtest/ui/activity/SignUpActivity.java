package com.mtest.ui.activity;

import android.content.Intent;
import android.os.Bundle;
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

public class SignUpActivity extends BaseActivity {


    UserManager mUserManager;
    User mSignUpUser;

    EditText etName,etEmail,etPassword;

    String name,emailId,password;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initUi();
        initManager();
        onDoneClick();
    }


    public void onBackClick(View v){
        finish();
    }


    public void onFBLoginClick(View v){
        Toast.makeText(this,R.string.error_notpart,Toast.LENGTH_SHORT).show();
    }

    public void onSignUpClick(View v){
        if(CheckInternetConnection.checkInternet(this)){
            checkValidation();
        }else{
            CheckInternetConnection.noInternet(this);
        }
    }

    public void onHideKeyBoard(View v){
        Utility.hideKeyBoard(this);
    }


    public void initManager(){
        mUserManager = new UserManager();
        mSignUpUser = new User();
    }

    public void initUi(){
        etName = (EditText)findViewById(R.id.etName);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
    }

    public void registerApiCall(){
        mSignUpUser.connection = Config.CONNNECTION;
        mSignUpUser.email = emailId;
        mSignUpUser.password = password;
        mSignUpUser.clientId = Config.CLIENT_ID;
        mUserManager.login(mSignUpUser,this,false);
    }

    public void checkValidation() {
        name = etName.getText().toString().trim();
        emailId = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();


        if (!Utility.nullCheck(name)) {
            Utility.showToast(getResources().getString(R.string.error_fullname), this);
        }else if (!Utility.nullCheck(emailId)) {
            Utility.showToast(getResources().getString(R.string.error_email), this);
        } else if (!Utility.nullCheck(password)) {
            Utility.showToast(getResources().getString(R.string.error_password), this);
        } else if (!Utility.isValidEmail(emailId, this)) {
            Utility.showToast(getResources().getString(R.string.error_valid_email), this);
        } else {
            registerApiCall();
        }


    }


    public void onDoneClick(){
        etPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    if(CheckInternetConnection.checkInternet(SignUpActivity.this)){
                        checkValidation();
                    }else{
                        CheckInternetConnection.noInternet(SignUpActivity.this);
                    }

                    return true;
                }
                return false;
            }
        });
    }


    @Subscribe
    public void signUpSucess(User user){
        if(user.status){
            Intent i = new Intent(this,ProductActivity.class);
            startActivity(i);
            finish();
            mUserManager.isLogin = true;
            mUserManager.saveToSharedPreference(this);
        }else{
            Toast.makeText(this,user.errorDescription,Toast.LENGTH_SHORT).show();
        }
    }


}
