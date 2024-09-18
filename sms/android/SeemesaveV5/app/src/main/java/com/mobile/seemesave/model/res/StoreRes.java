package com.mobile.seemesave.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import com.mobile.seemesave.model.common.StoreModel;

public class StoreRes {
    @SerializedName("data")
    @Expose
    private ArrayList<StoreModel> data;
    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("message")
    @Expose
    private String message;

    public StoreRes(){
        data = new ArrayList<>();
        this.status = false;
        this.message = "";
    }

    public ArrayList<StoreModel> getData() {
        return data;
    }

    public void setData(ArrayList<StoreModel> data) {
        this.data.clear();
        this.data.addAll(data);
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
