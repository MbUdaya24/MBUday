package com.mtest.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;

import com.mtest.R;
import com.mtest.manager.UserManager;

public class SplashActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        final UserManager mUserManager = new UserManager();
        mUserManager.loadFromSharedPreference(this);


        new Handler().postDelayed(new Runnable() {



            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                boolean isLogin = pref.getBoolean("isLogin",false);
                if(isLogin){
                    Intent i = new Intent(SplashActivity.this, ProductActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, 2*1000);
    }
}
