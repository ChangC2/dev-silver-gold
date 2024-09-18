package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ComboDealProductModel {
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

    @SerializedName("Products")
    @Expose
    private ArrayList<ProductModel> Products;

    public ComboDealProductModel() {}

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

    public ArrayList<ProductModel> getProducts() {
        return Products;
    }

    public void setProducts(ArrayList<ProductModel> products) {
        Products = products;
    }
}
