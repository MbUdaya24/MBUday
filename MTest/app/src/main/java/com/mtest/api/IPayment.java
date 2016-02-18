package com.mtest.api;


import com.mtest.entity.Order;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by udaya on 2/17/16.
 */
public interface IPayment {

    @POST("stripe/php/charge.php")
    Call<Order> saveCard(@Body Order payment);
}
