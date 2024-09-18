package com.mobile.seemesave.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobile.seemesave.model.common.FriendModel;

import java.util.ArrayList;

public class FriendRes {
    @SerializedName("friendInfoList")
    @Expose
    private ArrayList<FriendModel> friendList;
    @SerializedName("totalCount")
    @Expose
    private int totalCount;
    private boolean status;
    private String message;

    public FriendRes() {}

    public ArrayList<FriendModel> getFriendList() {
        return friendList;
    }

    public void setFriendList(ArrayList<FriendModel> friendList) {
        this.friendList = friendList;
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
