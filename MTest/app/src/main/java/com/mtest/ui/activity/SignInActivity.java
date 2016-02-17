package com.mtest.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mtest.R;
import com.mtest.entity.User;
import com.mtest.manager.UserManager;
import com.mtest.util.CheckInternetConnection;
import com.mtest.util.Utility;
import com.squareup.otto.Subscribe;

public class SignInActivity extends BaseActivity {


    UserManager mUserManager;
    User mUser;

    EditText etEmail,etPassword;

    String emailId,password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        initUi();
        initManager();
    }

    public void onBackClick(View v){
        finish();
    }

    public void onFBLoginClick(View v){
        Toast.makeText(this,R.string.error_notpart,Toast.LENGTH_SHORT).show();;
    }

    public void onSignInClick(View v){
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
        mUser = new User();
    }

    public void initUi(){
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
    }


    public void loginApiCall(){
        mUser.username = emailId;
        mUser.password = password;
        mUserManager.login(mUser,this,true);
    }

    public void checkValidation() {
        emailId = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();


        if (!Utility.nullCheck(emailId)) {
            Utility.showToast(getResources().getString(R.string.error_email), this);
        } else if (!Utility.nullCheck(password)) {
            Utility.showToast(getResources().getString(R.string.error_password), this);
        } else if (!Utility.isValidEmail(emailId, this)) {
            Utility.showToast(getResources().getString(R.string.error_valid_email), this);
        } else {
            loginApiCall();

        }


    }



    @Subscribe
    public void signInSucess(User user){
        if(user.status){
            Intent i = new Intent(this,ProductActivity.class);
            startActivity(i);
            finish();
            mUserManager.isLogin = true;
            mUserManager.saveToSharedPreference(this);
        }else{
            Toast.makeText(this,user.error_description,Toast.LENGTH_SHORT).show();
        }



    }




}
