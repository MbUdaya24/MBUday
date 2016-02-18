package com.mtest.manager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

import com.mtest.api.IUser;
import com.mtest.entity.User;
import com.mtest.util.ApplicationBus;
import com.mtest.util.Config;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by udaya on 2/12/16.
 */
public class UserManager {


    public boolean isLogin;


    public void login(User mUser, Context mContext, boolean isLogin) {
        final ProgressDialog dialog = ProgressDialog.show(mContext, "", "loading...");

        Retrofit client = new Retrofit.Builder()
                .baseUrl(Config.HOST_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IUser methods = client.create(IUser.class);
        Callback callback = new Callback() {

            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                dialog.dismiss();
                if (response.isSuccess()) {
                    User user = (User) response.body();
                    user.status = true;
                    ApplicationBus.getInstance().post(user);
                } else {
                    try {
                        User myError = (User) retrofit.responseConverter(
                                User.class, User.class.getAnnotations())
                                .convert(response.errorBody());
                        myError.status = false;
                        ApplicationBus.getInstance().post(myError);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }

        };

        if (isLogin) {
            Call<User> call = methods.login(mUser);
            call.enqueue(callback);
        } else {
            Call<User> call = methods.register(mUser);
            call.enqueue(callback);
        }

    }


    public void loadFromSharedPreference(Context context) {
        SharedPreferences pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        isLogin = pref.getBoolean("isLogin", false);


    }


    public void saveToSharedPreference(Context context) {
        SharedPreferences pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isLogin", isLogin);
        editor.apply();

    }


}
