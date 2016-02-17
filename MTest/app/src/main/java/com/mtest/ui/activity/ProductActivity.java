package com.mtest.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.mtest.R;
import com.mtest.entity.Product;
import com.mtest.manager.UserManager;
import com.mtest.ui.adapter.ProductAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ProductActivity extends BaseActivity {

    GridView gridView;
    ProductAdapter mProductAdapter;
   UserManager mUserManager;


    ArrayList<Product> mProductList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);


        init();
        getProductItems();
    }


    public void init(){
        gridView = (GridView) findViewById(R.id.gvProducts);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProductActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });
    }



    public void onBackClick(View v){
        finish();
    }



    public void onSettingsClick(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
        builder.setTitle(R.string.app_name)
                .setMessage("Are you sure,want to logout ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        mUserManager.isLogin = false;
                        mUserManager.saveToSharedPreference(ProductActivity.this);

                        Intent iLogout = new Intent(ProductActivity.this,HomeActivity.class);
                        startActivity(iLogout);
                        finish();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public String getProductItems() {

        mProductList = new ArrayList<Product>();
        mUserManager = new UserManager();
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open("Product.json")));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String productsItems = sb.toString();

        try {

            JSONObject jsonObjMain = new JSONObject(productsItems);
            JSONArray jsonArray = jsonObjMain.getJSONArray("products");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);

                String title = jsonObj.getString("title");
                String author = jsonObj.getString("author");
                String cost = jsonObj.getString("cost");


                mProductList.add(new Product(title,author,cost));
                mProductAdapter = new ProductAdapter(this,mProductList);
                gridView.setAdapter(mProductAdapter);



            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return productsItems;
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