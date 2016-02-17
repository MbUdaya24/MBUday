package com.mtest.api;

import com.mtest.entity.User;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by udaya on 2/12/16.
 */
public interface IUser {


    @POST("/dbconnections/signup")
    public Call<User> register(@Body User user);


    @POST("/oauth/ro")
    public Call<User> login(@Body User user);

    @POST("stripe/php/charge.php")
    public Call<User> saveCard(@Body User user);




}
