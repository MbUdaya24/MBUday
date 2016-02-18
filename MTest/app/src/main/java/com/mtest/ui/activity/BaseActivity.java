package com.mtest.ui.activity;


import android.app.Activity;
import android.os.Bundle;

import android.widget.Toast;


import com.mtest.entity.ServerError;
import com.mtest.util.ApplicationBus;
import com.squareup.otto.Subscribe;

public class BaseActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        busRegister();
    }

    @Override
    protected void onDestroy() {
        busUnRegister();
        super.onDestroy();

    }

    private void busRegister() {
        Object busEventListener = new Object() {
            //Let Activity take care of the error messages???
            @Subscribe
            public void onError(ServerError error) {
                if (!error.status) {
                    showMessage(error.reason);
                }
            }
        };
        ApplicationBus.getInstance().register(busEventListener);
        ApplicationBus.getInstance().register(this);
    }

    private void busUnRegister() {
        ApplicationBus.getInstance().unregister(this);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Subscribe
    public void onError(ServerError error) {
        if (!error.status) {
            showMessage(error.reason);
        }
    }
}
