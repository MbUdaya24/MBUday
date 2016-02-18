package com.mtest.entity;

import java.util.ArrayList;

/**
 * Created by udaya on 2/15/16.
 */
public class Product {


    private final String title;
    private final String author;
    private final String cost;


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


