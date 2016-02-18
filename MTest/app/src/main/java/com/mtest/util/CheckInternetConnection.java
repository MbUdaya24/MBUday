package com.mtest.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.mtest.R;
/**
 * Created by udaya on 2/15/16.
 */

public class CheckInternetConnection {
    private final Context mContext;
    public CheckInternetConnection(Context context) {
        this.mContext = context;
    }

    public  static boolean checkInternet(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            noInternet(mContext);
            return false;
        } else
            return true;
    }

    //NoInternet Alert
    public static void noInternet(Context mContext) {

        final Dialog dNoInternet = new Dialog(mContext);
        dNoInternet.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dNoInternet.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dNoInternet.setContentView(R.layout.dialog_no_internet);
        dNoInternet.setCancelable(true);
        dNoInternet.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ImageView ivNoInternet = (ImageView) dNoInternet.findViewById(R.id.ivNoInternet);
        final AnimationDrawable animation_checkinternet = (AnimationDrawable) ivNoInternet.getDrawable();
        ivNoInternet.post(new Runnable() {
            @Override
            public void run() {
                animation_checkinternet.start();
            }

        });
        dNoInternet.show();

    }
}
