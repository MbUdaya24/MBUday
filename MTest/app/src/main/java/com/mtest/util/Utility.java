package com.mtest.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.mtest.R;

/**
 * Created by udaya on 2/15/16.
 */
public class Utility {


    //showToast
    public static void showToast(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    //HideKeyboard
    public static void hideKeyBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText()) {
            if (activity.getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    //Default AlertDialog
    public static void showAlert(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.app_name)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //nullCheck
    public static boolean nullCheck(String nullCheckString) {


        if (nullCheckString != null) {
            if (nullCheckString.length() > 0 && !nullCheckString.equals("")
                    && !nullCheckString.equals("null")
                    && !nullCheckString.equals("(null)")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //isValidEmail
    public static boolean isValidEmail(String target, Context mContext) {



        if (target == null || target.equals("")) {
            //Toast.makeText(mContext, "Please enter email id", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (target.contains(".web")) {
            //Toast.makeText(mContext, "Please enter vaild email id", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
            return true;
        } else {
            return false;
        }

    }

}
