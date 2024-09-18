package com.mobile.seemesave.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobile.seemesave.model.common.CurrencyRateModel;
import com.mobile.seemesave.model.common.ShoppingTimeModel;

import java.util.ArrayList;

public class ShoppingCartBaseRes {
    @SerializedName("currencyData")
    @Expose
    private ArrayList<CurrencyRateModel> currencyData;
    @SerializedName("now_time")
    @Expose
    private String now_time;
    @SerializedName("string")
    @Expose
    private String time;
    @SerializedName("data")
    @Expose
    private ArrayList<ShoppingTimeModel> data;

    private boolean status;
    private String message;

    public ShoppingCartBaseRes() {
        this.status = false;
        this.message = "";
    }

    public ArrayList<CurrencyRateModel> getCurrencyData() {
        return currencyData;
    }

    public void setCurrencyData(ArrayList<CurrencyRateModel> currencyData) {
        this.currencyData = currencyData;
    }

    public String getNow_time() {
        return now_time;
    }

    public void setNow_time(String now_time) {
        this.now_time = now_time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<ShoppingTimeModel> getData() {
        return data;
    }

    public void setData(ArrayList<ShoppingTimeModel> data) {
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
