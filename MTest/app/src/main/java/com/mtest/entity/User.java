package com.mtest.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by udaya on 2/12/16.
 */
public class User {

    @SerializedName("client_id")
    public String clientId;
    public String email;
    public String password;
    public String connection;
    public String name;
    public String username;
    public boolean status;
    @SerializedName("error_description")
    public String errorDescription;




}
