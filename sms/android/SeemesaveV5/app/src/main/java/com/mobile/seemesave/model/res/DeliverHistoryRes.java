package com.mobile.seemesave.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobile.seemesave.model.common.DeliverModel;

import java.util.ArrayList;

public class DeliverHistoryRes {
    @SerializedName("data")
    @Expose
    private ArrayList<DeliverModel> data;


    @SerializedName("totalCount")
    @Expose
    private int totalCount;

    private boolean status;
    private String message;

    public DeliverHistoryRes() {}

    public ArrayList<DeliverModel> getData() {
        return data;
    }

    public void setData(ArrayList<DeliverModel> data) {
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
