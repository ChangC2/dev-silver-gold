package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleProductModel {
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

    @SerializedName("Product")
    @Expose
    private ProductModel Product;

    private int count = 1;
    private boolean isCart = false;

    public SingleProductModel() {}

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

    public ProductModel getProduct() {
        return Product;
    }

    public void setProduct(ProductModel product) {
        Product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isCart() {
        return isCart;
    }

    public void setCart(boolean cart) {
        isCart = cart;
    }
}
