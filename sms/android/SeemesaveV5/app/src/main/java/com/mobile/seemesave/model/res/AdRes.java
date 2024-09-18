package com.mobile.seemesave.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.model.common.PostModel;
import com.mobile.seemesave.model.common.TrendingBrandModel;

import java.util.ArrayList;

public class AdRes {
    @SerializedName("feedInfo")
    @Expose
    private FeedModel feedInfo;
    @SerializedName("brandList")
    @Expose
    private ArrayList<TrendingBrandModel> brandList;

    private boolean status;
    private String message;
    public AdRes() {
        brandList = new ArrayList<>();
        this.status = false;
        this.message = "";
    }

    public FeedModel getFeedInfo() {
        return feedInfo;
    }

    public void setFeedInfo(FeedModel feedInfo) {
        this.feedInfo = feedInfo;
    }

    public ArrayList<TrendingBrandModel> getBrandList() {
        return brandList;
    }

    public void setBrandList(ArrayList<TrendingBrandModel> brandList) {
        this.brandList = brandList;
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
