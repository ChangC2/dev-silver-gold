package com.mobile.seemesave.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.model.common.PostModel;

import java.util.ArrayList;

public class PostRes {
    @SerializedName("data")
    @Expose
    private ArrayList<FeedModel> postList;
    @SerializedName("totalCount")
    @Expose
    private int totalCount;

    private boolean status;
    private String message;
    public PostRes() {
        postList = new ArrayList<>();
        this.status = false;
        this.message = "";
    }

    public ArrayList<FeedModel> getPostList() {
        return postList;
    }

    public void setPostList(ArrayList<FeedModel> postList) {
        this.postList = postList;
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
