package com.mobile.seemesave.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobile.seemesave.model.common.StoreModel;
import com.mobile.seemesave.model.common.StoryModel;
import com.mobile.seemesave.model.common.TrendingBrandModel;

import java.util.ArrayList;

public class StoryStoreRes {
    @SerializedName("storyInfo")
    @Expose
    private StoryModel storyInfo;
    @SerializedName("stores")
    @Expose
    private ArrayList<StoreModel> stores;
    private boolean status;
    private String message;



    public StoryStoreRes(){
    }

    public StoryModel getStoryInfo() {
        return storyInfo;
    }

    public void setStoryInfo(StoryModel storyInfo) {
        this.storyInfo = storyInfo;
    }

    public ArrayList<StoreModel> getStores() {
        return stores;
    }

    public void setStores(ArrayList<StoreModel> stores) {
        this.stores = stores;
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
