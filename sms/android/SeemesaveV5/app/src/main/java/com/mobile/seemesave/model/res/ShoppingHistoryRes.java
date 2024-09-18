package com.mobile.seemesave.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobile.seemesave.model.common.ShoppingModel;

import java.util.ArrayList;

public class ShoppingHistoryRes {
    @SerializedName("data")
    @Expose
    private ArrayList<ShoppingModel> data;

    @SerializedName("totalCount")
    @Expose
    private int totalCount;

    private boolean status;
    private String message;

    public ShoppingHistoryRes() {}

    public ArrayList<ShoppingModel> getData() {
        return data;
    }

    public void setData(ArrayList<ShoppingModel> data) {
        this.data = data;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
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
