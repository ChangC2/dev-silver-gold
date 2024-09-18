package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrencyRateOneModel {
    @SerializedName("ZAR")
    @Expose
    private float ZAR;
    @SerializedName("USD")
    @Expose
    private float USD;
    public CurrencyRateOneModel() {}

    public float getZAR() {
        return ZAR;
    }

    public void setZAR(float ZAR) {
        this.ZAR = ZAR;
    }

    public float getUSD() {
        return USD;
    }

    public void setUSD(float USD) {
        this.USD = USD;
    }
}
