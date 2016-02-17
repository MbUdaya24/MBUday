package com.mtest.ui.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mtest.R;
import com.mtest.android.Stripe;
import com.mtest.android.TokenCallback;
import com.mtest.android.model.Card;
import com.mtest.android.model.Token;
import com.mtest.entity.User;
import com.mtest.manager.UserManager;
import com.mtest.util.Utility;
import com.squareup.otto.Subscribe;


import java.util.Calendar;

public class OrderActivity extends BaseActivity {


    UserManager mUserManager;
    User mUser;

    EditText etFirstName,etLastName,etCardNumber,etCvv,etAddress1,etAddress2;



    static TextView tvDate;
    static int year;
    static int month;
    static int day;


    //Payment
    public static final String PUBLISHABLE_KEY = "pk_test_6pRNASCoBOKtIshFeQd4XMUh";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        initUi();
        initManager();
        onDoneClick();


    }





    public void initUi(){
        tvDate = (TextView)findViewById(R.id.tvDate);
        etFirstName = (EditText)findViewById(R.id.etFirstName);
        etLastName = (EditText)findViewById(R.id.etLastName);
        etCardNumber = (EditText)findViewById(R.id.etCardNumber);
        etCvv = (EditText)findViewById(R.id.etCvv);
        etAddress1 = (EditText)findViewById(R.id.etAddress1);
        etAddress2 = (EditText)findViewById(R.id.etAddress2);
    }


    public void initManager(){
        mUserManager  = new UserManager();
        mUser = new User();
    }



    public void onBackClick(View v){
        finish();
    }

    public void onHideKeyBoard(View v){
        Utility.hideKeyBoard(this);
    }
    public void onDateClick(View v){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");

    }


    public void onDoneClick(){
        etAddress2.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    saveCreditCard(etCardNumber.getText().toString(),month,year,etCvv.getText().toString());

                    return true;
                }
                return false;
            }
        });
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
             year = c.get(Calendar.YEAR);
             month = c.get(Calendar.MONTH);
             day = c.get(Calendar.DAY_OF_MONTH);


            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int  selectedyear, int  selectedmonth, int  selectedday) {

            year = selectedyear;
            month = selectedmonth;
            day = selectedday;


            tvDate.setText(new StringBuilder().append(month + 1)
                    .append("/").append(day).append("/").append(year)
                    .append(" "));
        }
    }


    public void saveCreditCard(String CardNumber,Integer ExpMonth,Integer ExpYear,String Cvc) {

        Card card = new Card(
                CardNumber,
                2,
                2020,
                "123");
        card.setCurrency("usd");

        boolean validation = card.validateCard();
        if (validation) {





           new Stripe().createToken(card, PUBLISHABLE_KEY, new TokenCallback() {
               @Override
               public void onError(Exception error) {

               }

               @Override
               public void onSuccess(Token token) {
                   Log.e("onSuccess",token.getId()+"");
                   mUser.stripeToken = token.getId();
                   mUser.amount = "90";
                   mUserManager.saveCard(mUser,OrderActivity.this);
               }
           });
        } else if (!card.validateNumber()) {
            Toast.makeText(OrderActivity.this,"The card number that you entered is invalid",Toast.LENGTH_SHORT).show();

        } else if (!card.validateExpiryDate()) {
            Toast.makeText(OrderActivity.this,"The expiration date that you entered is invalid",Toast.LENGTH_SHORT).show();

        } else if (!card.validateCVC()) {
            Toast.makeText(OrderActivity.this,"The CVC code that you entered is invalid",Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(OrderActivity.this,"The card details that you entered are invalid",Toast.LENGTH_SHORT).show();

        }
    }


    @Subscribe
    public void onCreditSucess(User user){
        Log.e("SUCESSS","Sucesss");
    }

}
