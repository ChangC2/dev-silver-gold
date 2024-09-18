package com.mobile.seemesave.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobile.seemesave.model.common.NotificationModel;

import java.util.ArrayList;

public class NotificationRes {
    @SerializedName("data")
    @Expose
    private ArrayList<NotificationModel> notificationList;
    @SerializedName("totalCount")
    @Expose
    private int totalCount;
    private boolean status;
    private String message;

    public NotificationRes() {}

    public ArrayList<NotificationModel> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(ArrayList<NotificationModel> notificationList) {
        this.notificationList = notificationList;
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
