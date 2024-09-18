package com.mobile.seemesave.model.common;

import java.util.ArrayList;

public class MPromotion2 {
    private String id = "";
    private String storeId ="";
    private String price = "0.0";
    private int qty = 1;
    private ArrayList<ProductModel> arryBuyProduct = new ArrayList<>();
    private ArrayList<ProductModel> arryGetProduct = new ArrayList<>();
    private int UnreadMessageCount = 0;
    private String variant_string = "";
    private int stock = 0;
    private CurrencyModel mCurrency = new CurrencyModel();

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getVariant_string() {
        return variant_string;
    }

    public void setVariant_string(String variant_string) {
        this.variant_string = variant_string;
    }

    public int getUnreadMessageCount() {
        return UnreadMessageCount;
    }

    public void setUnreadMessageCount(int unreadMessageCount) {
        UnreadMessageCount = unreadMessageCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public ArrayList<ProductModel> getArryBuyProduct() {
        return arryBuyProduct;
    }

    public void setArryBuyProduct(ArrayList<ProductModel> arryBuyProduct) {
        this.arryBuyProduct = arryBuyProduct;
    }

    public ArrayList<ProductModel> getArryGetProduct() {
        return arryGetProduct;
    }

    public void setArryGetProduct(ArrayList<ProductModel> arryGetProduct) {
        this.arryGetProduct = arryGetProduct;
    }

    public CurrencyModel getmCurrency() {
        return mCurrency;
    }

    public void setmCurrency(CurrencyModel mCurrency) {
        this.mCurrency = mCurrency;
    }
}