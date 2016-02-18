package com.mtest.util;

import android.app.Activity;
import android.content.Context;

import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;



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



    //nullCheck
    public static boolean nullCheck(String nullCheckString) {


        if (nullCheckString != null) {
            return nullCheckString.length() > 0 && !nullCheckString.equals("")
                    && !nullCheckString.equals("null")
                    && !nullCheckString.equals("(null)");
        } else {
            return false;
        }
    }

    //isValidEmail
    public static boolean isValidEmail(String target) {



        if (target == null || target.equals("")) {
            //Toast.makeText(mContext, "Please enter email id", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (target.contains(".web")) {
            //Toast.makeText(mContext, "Please enter vaild email id", Toast.LENGTH_SHORT).show();
            return false;
        }

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();

    }

}
