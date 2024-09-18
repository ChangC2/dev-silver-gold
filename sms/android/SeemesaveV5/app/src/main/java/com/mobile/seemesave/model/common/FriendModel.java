package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FriendModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("is_blocked")
    @Expose
    private boolean is_blocked;
    @SerializedName("is_pending")
    @Expose
    private boolean is_pending;
    @SerializedName("isMaster")
    @Expose
    private boolean isMaster;
    @SerializedName("Friend")
    @Expose
    private UserSubModel Friend;
    public FriendModel() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIs_blocked() {
        return is_blocked;
    }

    public void setIs_blocked(boolean is_blocked) {
        this.is_blocked = is_blocked;
    }

    public boolean isIs_pending() {
        return is_pending;
    }

    public void setIs_pending(boolean is_pending) {
        this.is_pending = is_pending;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public UserSubModel getFriend() {
        return Friend;
    }

    public void setFriend(UserSubModel friend) {
        Friend = friend;
    }
}
