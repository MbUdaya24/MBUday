package com.mtest.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mtest.R;
import com.mtest.entity.Product;
import com.mtest.manager.PaymentManager;
import com.mtest.manager.UserManager;
import com.mtest.ui.adapter.ProductAdapter;




import java.io.IOException;
import java.io.InputStream;


public class ProductActivity extends BaseActivity {

    private GridView gridView;
    private ProductAdapter mProductAdapter;
    private UserManager mUserManager;
    private PaymentManager mPaymentManager;

    private Product product;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);


        init();
        getProductItems();

    }


    private void init() {
        gridView = (GridView) findViewById(R.id.gvProducts);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProductActivity.this, OrderActivity.class);
                intent.putExtra("Price",product.products.get(position).getCost().toString());
                startActivity(intent);
            }
        });

    }


    public void onBackClick(View v) {
        finish();
    }


    public void onSettingsClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
        builder.setTitle(R.string.app_name)
                .setMessage(R.string.dialog_logout)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        mUserManager.isLogin = false;
                        mUserManager.saveToSharedPreference(ProductActivity.this);
                        mPaymentManager.clearSharedPreference(ProductActivity.this);
                        Intent iLogout = new Intent(ProductActivity.this, HomeActivity.class);
                        startActivity(iLogout);
                        finish();
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void getProductItems() {


        mUserManager = new UserManager();
        mPaymentManager = new PaymentManager();
        String json;
        try {
            InputStream inputStream = getAssets().open("Product.json");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
            Gson gson = new Gson();
            product = gson.fromJson(json,Product.class);
            Log.d("lenght",""+product.products.size());
            mProductAdapter = new ProductAdapter(this,product.products);
            gridView.setAdapter(mProductAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();


        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}