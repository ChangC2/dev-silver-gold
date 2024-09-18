package com.mobile.seemesave.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobile.seemesave.model.common.StoryModel;
import com.mobile.seemesave.model.common.TrendingBrandModel;

import java.util.ArrayList;

public class StoryBrandRes {
    @SerializedName("storyInfo")
    @Expose
    private StoryModel storyInfo;
    @SerializedName("brands")
    @Expose
    private ArrayList<TrendingBrandModel> brands;
    private boolean status;
    private String message;



    public StoryBrandRes(){
    }

    public StoryModel getStoryInfo() {
        return storyInfo;
    }

    public void setStoryInfo(StoryModel storyInfo) {
        this.storyInfo = storyInfo;
    }

    public ArrayList<TrendingBrandModel> getBrands() {
        return brands;
    }

    public void setBrands(ArrayList<TrendingBrandModel> brands) {
        this.brands = brands;
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
