package com.mobile.seemesave.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobile.seemesave.model.common.ShoppingModel;

import java.util.ArrayList;

public class ShoppingCartOrderRes {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private ArrayList<ShoppingModel> data;
    @SerializedName("message")
    @Expose
    private String message;
    public ShoppingCartOrderRes(){}

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

    public ArrayList<ShoppingModel> getData() {
        return data;
    }

    public void setData(ArrayList<ShoppingModel> data) {
        this.data = data;
    }
}
