package com.mobile.seemesave.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobile.seemesave.model.common.CurrencyRateModel;
import com.mobile.seemesave.model.common.DeliverShipModel;
import com.mobile.seemesave.model.common.DeliverStatusModel;

import java.util.ArrayList;

public class DeliverCartBaseRes {
    @SerializedName("currencyData")
    @Expose
    private ArrayList<CurrencyRateModel> currencyData;
    @SerializedName("shipData")
    @Expose
    private DeliverShipModel shipData;
    @SerializedName("statusConstant")
    @Expose
    private DeliverStatusModel statusConstant;
    private boolean status;
    private String message;

    public DeliverCartBaseRes() {
        this.status = false;
        this.message = "";
    }

    public ArrayList<CurrencyRateModel> getCurrencyData() {
        return currencyData;
    }

    public void setCurrencyData(ArrayList<CurrencyRateModel> currencyData) {
        this.currencyData = currencyData;
    }

    public DeliverShipModel getShipData() {
        return shipData;
    }

    public void setShipData(DeliverShipModel shipData) {
        this.shipData = shipData;
    }

    public DeliverStatusModel getStatusConstant() {
        return statusConstant;
    }

    public void setStatusConstant(DeliverStatusModel statusConstant) {
        this.statusConstant = statusConstant;
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
