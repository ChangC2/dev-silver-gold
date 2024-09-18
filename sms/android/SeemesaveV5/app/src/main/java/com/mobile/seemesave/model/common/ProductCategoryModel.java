package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductCategoryModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("products")
    @Expose
    private ArrayList<SingleProductModel> products;

    private ArrayList<ProductOneModel> productList;

    private boolean check;

    public ProductCategoryModel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<SingleProductModel> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<SingleProductModel> products) {
        this.products = products;
    }

    public ArrayList<ProductOneModel> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<ProductOneModel> productList) {
        this.productList = productList;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
