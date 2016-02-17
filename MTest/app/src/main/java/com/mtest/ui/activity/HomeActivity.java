package com.mtest.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mtest.R;

public class HomeActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    public void onSignUpClick(View v){
        Intent i = new Intent(this,SignUpActivity.class);
        startActivity(i);
    }

    public void onSignInClick(View v){
        Intent i = new Intent(this,SignInActivity.class);
        startActivity(i);
    }
}
