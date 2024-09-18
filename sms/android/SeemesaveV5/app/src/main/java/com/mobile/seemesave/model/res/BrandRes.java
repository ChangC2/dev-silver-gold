package com.mobile.seemesave.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobile.seemesave.model.common.TrendingBrandModel;

import java.util.ArrayList;

public class BrandRes {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private ArrayList<TrendingBrandModel> data;
    @SerializedName("message")
    @Expose
    private String message;



    public BrandRes(){
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<TrendingBrandModel> getData() {
        return data;
    }

    public void setData(ArrayList<TrendingBrandModel> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
