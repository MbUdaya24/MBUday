package com.mtest.android;


import com.mtest.android.model.Token;

public abstract class TokenCallback {
    public abstract void onError(Exception error);
    public abstract void onSuccess(Token token);
}
