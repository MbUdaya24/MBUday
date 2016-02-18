package com.mtest.entity;

import java.util.ArrayList;

/**
 * Created by udaya on 2/15/16.
 */
public class Product {


    public String title;
    public String author;
    public String cost;
    public String url;

    public Product(String mTitle, String mAuthor,String mCost) {
         this.title = mTitle;
         this.author = mAuthor;
         this.cost = mCost;
    }

    public String getAuthor() {
        return author;
    }


    public String getCost() {
        return cost;
    }


    public String getTitle() {
        return title;
    }

    public ArrayList<Product> products;
}


