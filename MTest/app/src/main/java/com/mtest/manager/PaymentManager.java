package com.mtest.manager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mtest.api.IPayment;
import com.mtest.entity.Order;
import com.mtest.util.ApplicationBus;
import com.mtest.util.Config;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by udaya on 2/17/16.
 */
public class PaymentManager {

    Order mOrder;
    private final Gson gson = new Gson();

    public void saveCard(Order mPayment, Context mContext) {


        Retrofit client = new Retrofit.Builder()
                .baseUrl(Config.SAVE_CARD)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IPayment methods = client.create(IPayment.class);
        Callback callback = new Callback() {

            @Override
            public void onResponse(Response response, Retrofit retrofit) {

                if (response.isSuccess()) {

                    Order payment = (Order) response.body();
                    payment.status = true;
                    ApplicationBus.getInstance().post(payment);

                } else {
                    try {
                        Order myError = (Order) retrofit.responseConverter(
                                Order.class, Order.class.getAnnotations())
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


        Call<Order> call = methods.saveCard(mPayment);
        call.enqueue(callback);


    }


    public Order loadFromSharedPreference(Context context) {
        SharedPreferences pref = context.getSharedPreferences("com.mtest", Context.MODE_PRIVATE);
        String userJson = pref.getString(Config.USER_SHARED_PREFERENCE_KEY, null);
        if (userJson != null && !userJson.isEmpty()) {
            try {

                mOrder = gson.fromJson(userJson, Order.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mOrder;
    }


    public void saveToSharedPreference(Context context, Order user) {
        if (user != null && user.firstName != null) {
            SharedPreferences pref = context.getSharedPreferences("com.mtest", Context.MODE_PRIVATE);
            ;
            SharedPreferences.Editor editor = pref.edit();
            String json = gson.toJson(user);
            editor.putString(Config.USER_SHARED_PREFERENCE_KEY, json);
            editor.commit();
        }
    }

    public void clearSharedPreference(Context context) {
        SharedPreferences pref = context.getSharedPreferences("com.mtest", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(Config.USER_SHARED_PREFERENCE_KEY);
        editor.commit();
        editor.clear();
    }
}
