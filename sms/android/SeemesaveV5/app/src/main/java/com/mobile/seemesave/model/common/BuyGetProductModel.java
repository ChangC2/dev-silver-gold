package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BuyGetProductModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("selling_price")
    @Expose
    private String selling_price;

    @SerializedName("Currency")
    @Expose
    private CurrencyModel Currency;

    @SerializedName("stock")
    @Expose
    private int stock;

    @SerializedName("UnreadMessageCount")
    @Expose
    private int UnreadMessageCount;

    @SerializedName("BookedStoreId")
    @Expose
    private int BookedStoreId;

    @SerializedName("BuyProducts")
    @Expose
    private ArrayList<ProductModel> BuyProducts;

    @SerializedName("FreeProducts")
    @Expose
    private ArrayList<ProductModel> FreeProducts;

    public BuyGetProductModel() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
    }

    public CurrencyModel getCurrency() {
        return Currency;
    }

    public void setCurrency(CurrencyModel currency) {
        Currency = currency;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getUnreadMessageCount() {
        return UnreadMessageCount;
    }

    public void setUnreadMessageCount(int unreadMessageCount) {
        UnreadMessageCount = unreadMessageCount;
    }

    public int getBookedStoreId() {
        return BookedStoreId;
    }

    public void setBookedStoreId(int bookedStoreId) {
        BookedStoreId = bookedStoreId;
    }

    public ArrayList<ProductModel> getBuyProducts() {
        return BuyProducts;
    }

    public void setBuyProducts(ArrayList<ProductModel> buyProducts) {
        BuyProducts = buyProducts;
    }

    public ArrayList<ProductModel> getFreeProducts() {
        return FreeProducts;
    }

    public void setFreeProducts(ArrayList<ProductModel> freeProducts) {
        FreeProducts = freeProducts;
    }
}
