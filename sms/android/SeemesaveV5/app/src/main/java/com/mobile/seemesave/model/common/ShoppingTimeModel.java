package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ShoppingTimeModel {
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private ArrayList<Integer> time;
    public ShoppingTimeModel() {}
    @Override
    public String toString() {
        return date;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Integer> getTime() {
        return time;
    }

    public void setTime(ArrayList<Integer> time) {
        this.time = time;
    }
}
