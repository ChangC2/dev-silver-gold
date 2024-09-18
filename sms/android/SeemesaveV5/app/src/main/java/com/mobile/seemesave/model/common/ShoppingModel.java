package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShoppingModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("Store")
    @Expose
    private StoreModel Store;
    @SerializedName("productCount")
    @Expose
    private int productCount;
    @SerializedName("security_code")
    @Expose
    private String security_code;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("is_paid")
    @Expose
    private boolean is_paid;
    @SerializedName("estimate_time")
    @Expose
    private String estimate_time;
    @SerializedName("estimate_date")
    @Expose
    private String estimate_date;
    @SerializedName("estimate_hour")
    @Expose
    private int estimate_hour;
    @SerializedName("is_ready")
    @Expose
    private boolean is_ready;
    @SerializedName("is_pending")
    @Expose
    private boolean is_pending;
    @SerializedName("order_time")
    @Expose
    private String order_time;
    @SerializedName("is_finished")
    @Expose
    private boolean is_finished;
    @SerializedName("finish_time")
    @Expose
    private String finish_time;
    @SerializedName("total_price")
    @Expose
    private float total_price;
    @SerializedName("Currency")
    @Expose
    private CurrencyModel Currency;
    private int status;
    private String time= "";
    private String name = "";
    private String address = "";
    private String image_url = "";
    private float rating = 0;
    private int count = 0;
    private double lat = 0.0;
    private double lon = 0.0;

    public ShoppingModel() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StoreModel getStore() {
        return Store;
    }

    public void setStore(StoreModel store) {
        Store = store;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public String getSecurity_code() {
        return security_code;
    }

    public void setSecurity_code(String security_code) {
        this.security_code = security_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIs_paid() {
        return is_paid;
    }

    public void setIs_paid(boolean is_paid) {
        this.is_paid = is_paid;
    }

    public String getEstimate_time() {
        return estimate_time;
    }

    public void setEstimate_time(String estimate_time) {
        this.estimate_time = estimate_time;
    }

    public String getEstimate_date() {
        return estimate_date;
    }

    public void setEstimate_date(String estimate_date) {
        this.estimate_date = estimate_date;
    }

    public int getEstimate_hour() {
        return estimate_hour;
    }

    public void setEstimate_hour(int estimate_hour) {
        this.estimate_hour = estimate_hour;
    }

    public boolean isIs_ready() {
        return is_ready;
    }

    public void setIs_ready(boolean is_ready) {
        this.is_ready = is_ready;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public boolean isIs_finished() {
        return is_finished;
    }

    public void setIs_finished(boolean is_finished) {
        this.is_finished = is_finished;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }

    public CurrencyModel getCurrency() {
        return Currency;
    }

    public void setCurrency(CurrencyModel currency) {
        Currency = currency;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public boolean isIs_pending() {
        return is_pending;
    }

    public void setIs_pending(boolean is_pending) {
        this.is_pending = is_pending;
    }
}
