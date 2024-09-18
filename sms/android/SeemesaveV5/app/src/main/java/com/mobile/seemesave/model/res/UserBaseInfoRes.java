package com.mobile.seemesave.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobile.seemesave.model.common.CurrencyModel;
import com.mobile.seemesave.model.common.TimeZoneModel;

import java.util.ArrayList;

public class UserBaseInfoRes {
    @SerializedName("currencyList")
    @Expose
    private ArrayList<CurrencyModel> currencyList;
    @SerializedName("timezoneList")
    @Expose
    private ArrayList<TimeZoneModel> timezoneList;
    private boolean status;
    private String message;
    public UserBaseInfoRes() {
        this.status = false;
        this.message = "";
    }

    public ArrayList<CurrencyModel> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(ArrayList<CurrencyModel> currencyList) {
        this.currencyList = currencyList;
    }

    public ArrayList<TimeZoneModel> getTimezoneList() {
        return timezoneList;
    }

    public void setTimezoneList(ArrayList<TimeZoneModel> timezoneList) {
        this.timezoneList = timezoneList;
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
