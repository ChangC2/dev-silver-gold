package com.mobile.seemesave.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobile.seemesave.model.common.PromotionModel;
import com.mobile.seemesave.model.common.PromotionOneModel;

public class PromotionRes {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private PromotionModel data;
    @SerializedName("message")
    @Expose
    private String message;

    private PromotionOneModel promotionData;

    public PromotionRes(){
        promotionData = new PromotionOneModel();
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public PromotionModel getData() {
        return data;
    }

    public void setData(PromotionModel data) {
        this.data = data;
    }

    public PromotionOneModel getPromotionData() {
        return promotionData;
    }

    public void setPromotionData(PromotionOneModel promotionData) {
        this.promotionData = promotionData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
