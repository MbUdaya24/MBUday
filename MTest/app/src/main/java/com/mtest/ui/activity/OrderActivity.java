package com.mtest.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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


    PaymentManager mPaymentManager;
    Order order;

    EditText etFirstName, etLastName, etCardNumber, etCvv, etAddress1, etAddress2, etCity, etState, etCountry, etZipCode, etComments, etMonth, etYear;
    Button btnPayment;
    RelativeLayout rlSucess;


    String price;

    ProgressDialog dialog;


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


    public void initUi() {

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



    }


    public void initManager() {
        mPaymentManager = new PaymentManager();

    }


    public void onBackClick(View v) {
        finish();
    }

    public void onHideKeyBoard(View v) {
        Utility.hideKeyBoard(this);
    }


    public void onDoneClick() {
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


    public void getOrderDetails() {
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


    public void setOrderDetails() {

        order = mPaymentManager.loadFromSharedPreference(this);
        Intent getData = getIntent();
        if (getData != null) {
            price = getData.getStringExtra("Price");
            btnPayment.setText("PAY $" + price);
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


    public void onPayClick(View v) {
        orderPayment();


    }


    public void orderPayment() {


        if (Utility.nullCheck(etFirstName.getText().toString().trim())&&
                Utility.nullCheck(etLastName.getText().toString().trim())&&
                Utility.nullCheck(etCardNumber.getText().toString().trim()) &&
                Utility.nullCheck(etCvv.getText().toString().trim()) &&
                Utility.nullCheck(etMonth.getText().toString().trim()) &&
                Utility.nullCheck(etYear.getText().toString().trim()) &&
                Utility.nullCheck(etAddress1.getText().toString().trim()) &&
                Utility.nullCheck(etAddress2.getText().toString().trim()) &&
                Utility.nullCheck(etCity.getText().toString().trim()) &&
                Utility.nullCheck(etState.getText().toString().trim()) &&
                Utility.nullCheck(etZipCode.getText().toString().trim()) &&
                Utility.nullCheck(etComments.getText().toString().trim())) {


            getOrderDetails();
            saveCreditCard(order.cardNumber, order.expMonth, order.expYear, order.cvv);

        } else {
            Toast.makeText(this, "Please enter all values", Toast.LENGTH_SHORT).show();
        }


    }


    public void creditCardFormat() {
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


    public void saveCreditCard(String CardNumber, Integer ExpMonth, Integer ExpYear, String Cvc) {
        dialog = ProgressDialog.show(this, "", "loading...");

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

                }

                @Override
                public void onSuccess(Token token) {

                    order.stripeToken = token.getId();
                    order.amount = price;
                    mPaymentManager.saveCard(order, OrderActivity.this);
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
