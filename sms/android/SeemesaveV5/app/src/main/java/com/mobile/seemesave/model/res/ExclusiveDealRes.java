package com.mobile.seemesave.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobile.seemesave.model.common.ProductOneModel;
import com.mobile.seemesave.model.common.PromotionModel;

import java.util.ArrayList;

public class ExclusiveDealRes {
    @SerializedName("data")
    @Expose
    private ArrayList<PromotionModel> data;

    private ArrayList<ProductOneModel> productList = new ArrayList<>();

    @SerializedName("totalCount")
    @Expose
    private int totalCount;

    private boolean status;
    private String message;

    public ExclusiveDealRes() {}

    public ArrayList<PromotionModel> getData() {
        return data;
    }

    public void setData(ArrayList<PromotionModel> data) {
        this.data = data;
    }

    public ArrayList<ProductOneModel> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<ProductOneModel> productList) {
        this.productList = productList;
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
