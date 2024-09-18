package com.mobile.seemesave.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobile.seemesave.model.common.ProductDetailModel;

public class ProductRes {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private ProductDetailModel data;
    @SerializedName("message")
    @Expose
    private String message;



    public ProductRes(){
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ProductDetailModel getData() {
        return data;
    }

    public void setData(ProductDetailModel data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
