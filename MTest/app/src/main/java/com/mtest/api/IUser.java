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
    Call<User> register(@Body User user);


    @POST("/oauth/ro")
    Call<User> login(@Body User user);






}
