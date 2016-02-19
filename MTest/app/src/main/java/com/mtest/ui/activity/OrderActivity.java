package com.mtest.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mtest.R;

import com.mtest.entity.Order;
import com.mtest.manager.PaymentManager;
import com.mtest.util.Config;
import com.mtest.util.Utility;
import com.squareup.otto.Subscribe;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

public class OrderActivity extends BaseActivity {


    private PaymentManager mPaymentManager;
    private Order order;

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etCardNumber;
    private EditText etCvv;
    private EditText etAddress1;
    private EditText etAddress2;
    private EditText etCity;
    private EditText etState;
    private EditText etCountry;
    private EditText etZipCode;
    private EditText etComments;
    private EditText etMonth;
    private EditText etYear;
    private Button btnPayment;
    private RelativeLayout rlSucess;
    private TextInputLayout ilFirstName;
    private TextInputLayout ilLastName;
    private TextInputLayout ilCardNumber;
    private TextInputLayout ilCvv;
    private TextInputLayout ilMonth;
    private TextInputLayout ilYear;
    private TextInputLayout ilAddress1;
    private TextInputLayout ilAddress2;
    private TextInputLayout ilCity;
    private TextInputLayout ilState;
    private TextInputLayout ilZipCode;
    private TextInputLayout ilCountry;
    private TextInputLayout ilComments;


    private String price;

    private ProgressDialog dialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        initUi();
        initManager();
        creditCardFormat();
        onDoneClick();
        setOrderDetails();


    }


    private void initUi() {

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etCardNumber = (EditText) findViewById(R.id.etCardNumber);
        etCvv = (EditText) findViewById(R.id.etCvv);
        etAddress1 = (EditText) findViewById(R.id.etAddress1);
        etAddress2 = (EditText) findViewById(R.id.etAddress2);
        etCity = (EditText) findViewById(R.id.etCity);
        etState = (EditText) findViewById(R.id.etState);
        etCountry = (EditText) findViewById(R.id.etCountry);
        etZipCode = (EditText) findViewById(R.id.etZipCode);
        etComments = (EditText) findViewById(R.id.etComments);
        btnPayment = (Button) findViewById(R.id.btnPayment);
        etMonth = (EditText) findViewById(R.id.etMonth);
        etYear = (EditText) findViewById(R.id.etYear);
        rlSucess = (RelativeLayout)findViewById(R.id.rlSucess);


        ilFirstName = (TextInputLayout)findViewById(R.id.ilFirstName);
        ilLastName = (TextInputLayout)findViewById(R.id.ilLastName);
        ilCardNumber = (TextInputLayout)findViewById(R.id.ilCardNumber);

        ilCvv = (TextInputLayout)findViewById(R.id.ilCvv);
        ilMonth = (TextInputLayout)findViewById(R.id.ilMonth);
        ilYear = (TextInputLayout)findViewById(R.id.ilYear);

        ilAddress1 = (TextInputLayout)findViewById(R.id.ilAddress1);
        ilAddress2 = (TextInputLayout)findViewById(R.id.ilAddress2);
        ilCity = (TextInputLayout)findViewById(R.id.ilCity);


        ilState = (TextInputLayout)findViewById(R.id.ilState);
        ilZipCode = (TextInputLayout)findViewById(R.id.ilZipCode);
        ilCountry = (TextInputLayout)findViewById(R.id.ilCountry);

        ilComments = (TextInputLayout)findViewById(R.id.ilComments);
    }


    private void initManager() {
        mPaymentManager = new PaymentManager();

    }


    private void creditCardFormat() {
        etCardNumber.addTextChangedListener(new TextWatcher() {
            private static final char space = ' ';

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                int pos = 0;
                while (true) {
                    if (pos >= s.length()) break;
                    if (space == s.charAt(pos) && (((pos + 1) % 5) != 0 || pos + 1 == s.length())) {
                        s.delete(pos, pos + 1);
                    } else {
                        pos++;
                    }
                }


                pos = 4;
                while (true) {
                    if (pos >= s.length()) break;
                    final char c = s.charAt(pos);
                    if ("0123456789".indexOf(c) >= 0) {
                        s.insert(pos, "" + space);
                    }
                    pos += 5;
                }
            }
        });
    }


    public void onPayClick(View v) {

        orderPayment();


    }


    public void onBackClick(View v) {

        finish();
    }

    public void onHideKeyBoard(View v) {
        Utility.hideKeyBoard(this);
    }


    private void onDoneClick() {

        etComments.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    orderPayment();
                    return true;
                }
                return false;
            }
        });
    }



    private void getOrderDetails() {
        order = new Order();
        order.firstName = etFirstName.getText().toString().trim();
        order.lastName = etLastName.getText().toString().trim();
        order.cardNumber = etCardNumber.getText().toString().trim();
        order.cvv = etCvv.getText().toString().trim();
        order.expMonth = Integer.parseInt(etMonth.getText().toString().trim());
        order.expYear = Integer.parseInt(etYear.getText().toString().trim());
        order.address1 = etAddress1.getText().toString().trim();
        order.address2 = etAddress2.getText().toString().trim();
        order.city = etCity.getText().toString().trim();
        order.state = etState.getText().toString().trim();
        order.zipCode = etZipCode.getText().toString().trim();
        order.country = etCountry.getText().toString().trim();
        order.comments = etComments.getText().toString().trim();
        mPaymentManager.saveToSharedPreference(this, order);
    }


    private void setOrderDetails() {

        order = mPaymentManager.loadFromSharedPreference(this);
        Intent getData = getIntent();
        if (getData != null) {
            price = "PAY $" +getData.getStringExtra("Price");
            btnPayment.setText(price);
        }
        if (order != null) {
            etFirstName.setText(order.firstName);
            etLastName.setText(order.lastName);
            etCardNumber.setText(order.cardNumber);
            etCvv.setText(order.cvv);
            etMonth.setText(String.valueOf(order.expMonth));
            etYear.setText(String.valueOf(order.expYear));
            etAddress1.setText(order.address1);
            etAddress2.setText(order.address2);
            etCity.setText(order.city);
            etState.setText(order.state);
            etCountry.setText(order.country);
            etComments.setText(order.comments);
            etZipCode.setText(order.zipCode);
        }
    }


   private void errorDisabled(){
       ilFirstName.setErrorEnabled(false);
       ilLastName.setErrorEnabled(false);
       ilCardNumber.setErrorEnabled(false);
       ilCvv.setErrorEnabled(false);
       ilMonth.setErrorEnabled(false);
       ilYear.setErrorEnabled(false);
       ilAddress1.setErrorEnabled(false);
       ilAddress2.setErrorEnabled(false);
       ilState.setErrorEnabled(false);
       ilCity.setErrorEnabled(false);
       ilZipCode.setErrorEnabled(false);
       ilCountry.setErrorEnabled(false);
       ilComments.setErrorEnabled(false);
   }


   private void validation(){
       if (!Utility.nullCheck(etFirstName.getText().toString().trim())){
           ilFirstName.setError(getResources().getString(R.string.error_empty));
       }else if(!Utility.nullCheck(etLastName.getText().toString().trim())){
           ilLastName.setError(getResources().getString(R.string.error_empty));
       }else if(!Utility.nullCheck(etCardNumber.getText().toString().trim())){
           ilCardNumber.setError(getResources().getString(R.string.error_empty));
       }else if(!Utility.nullCheck(etCvv.getText().toString().trim())){
           ilCvv.setError(getResources().getString(R.string.error_empty));
       }else if(!Utility.nullCheck(etMonth.getText().toString().trim())){
           ilMonth.setError(getResources().getString(R.string.error_empty));
       }else if(!Utility.nullCheck(etYear.getText().toString().trim())){
           ilYear.setError(getResources().getString(R.string.error_empty));
       }else if(!Utility.nullCheck(etAddress1.getText().toString().trim())){
           ilAddress1.setError(getResources().getString(R.string.error_empty));
       }else if(!Utility.nullCheck(etState.getText().toString().trim())){
           ilState.setError(getResources().getString(R.string.error_empty));
       }else if(!Utility.nullCheck(etCity.getText().toString().trim())){
           ilCity.setError(getResources().getString(R.string.error_empty));
       }else if(!Utility.nullCheck(etZipCode.getText().toString().trim())){
           ilZipCode.setError(getResources().getString(R.string.error_empty));
       }else if(!Utility.nullCheck(etCountry.getText().toString().trim())){
           ilCountry.setError(getResources().getString(R.string.error_empty));
       }else{
           getOrderDetails();
           saveCreditCard(order.cardNumber, order.expMonth, order.expYear, order.cvv);
       }

   }


    private void orderPayment() {


        errorDisabled();
        validation();


    }





    private void saveCreditCard(String CardNumber, Integer ExpMonth, Integer ExpYear, String Cvc) {


        Card card = new Card(
                CardNumber,
                ExpMonth,
                ExpYear,
                Cvc);
        card.setCurrency("usd");

        boolean validation = card.validateCard();
        if (validation) {


            new Stripe().createToken(card, Config.PUBLISHABLE_KEY, new TokenCallback() {
                @Override
                public void onError(Exception error) {
                    Utility.hideKeyBoard(OrderActivity.this);
                }

                @Override
                public void onSuccess(Token token) {
                    Utility.hideKeyBoard(OrderActivity.this);
                    dialog = ProgressDialog.show(OrderActivity.this, "", "loading...");
                    order.stripeToken = token.getId();
                    order.amount = price;
                    mPaymentManager.saveCard(order);
                }
            });
        } else if (!card.validateNumber()) {
            Toast.makeText(OrderActivity.this, R.string.error_cardnumber, Toast.LENGTH_SHORT).show();

        } else if (!card.validateExpiryDate()) {
            Toast.makeText(OrderActivity.this, R.string.error_exp, Toast.LENGTH_SHORT).show();

        } else if (!card.validateCVC()) {
            Toast.makeText(OrderActivity.this, R.string.error_cvc, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(OrderActivity.this, R.string.error_carddetails, Toast.LENGTH_SHORT).show();

        }
    }


    @Subscribe
    public void onCreditSucess(Order payment) {
        Utility.hideKeyBoard(OrderActivity.this);
        dialog.dismiss();
        rlSucess.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },3000);
        rlSucess.setVisibility(View.VISIBLE);

    }

}
