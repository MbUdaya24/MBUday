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


    private UserManager mUserManager;
    private User mSignUpUser;

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;

    private String name;
    private String emailId;
    private String password;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initUi();
        initManager();
        onDoneClick();
    }


    private void initManager(){
        mUserManager = new UserManager();
        mSignUpUser = new User();
    }

    private void initUi(){
        etName = (EditText)findViewById(R.id.etName);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
    }


    public void onSignUpClick(View v){
        if(CheckInternetConnection.checkInternet(this)){
            checkValidation();
        }else{
            CheckInternetConnection.noInternet(this);
        }
    }

    private void onDoneClick(){
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

    public void onFBLoginClick(View v){
        Toast.makeText(this,R.string.error_notpart,Toast.LENGTH_SHORT).show();
    }


    public void onBackClick(View v){
        finish();
    }


    public void onHideKeyBoard(View v){
        Utility.hideKeyBoard(this);
    }


    private void checkValidation() {
        name = etName.getText().toString().trim();
        emailId = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();


        if (!Utility.nullCheck(name)) {
            Utility.showToast(getResources().getString(R.string.error_fullname), this);
        }else if (!Utility.nullCheck(emailId)) {
            Utility.showToast(getResources().getString(R.string.error_email), this);
        } else if (!Utility.nullCheck(password)) {
            Utility.showToast(getResources().getString(R.string.error_password), this);
        } else if (!Utility.isValidEmail(emailId)) {
            Utility.showToast(getResources().getString(R.string.error_valid_email), this);
        } else {
            signUp();
        }


    }




    private void signUp(){
        mSignUpUser.connection = Config.CONNNECTION;
        mSignUpUser.email = emailId;
        mSignUpUser.password = password;
        mSignUpUser.clientId = Config.CLIENT_ID;
        mUserManager.login(mSignUpUser,this,false);
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
