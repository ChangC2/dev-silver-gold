package com.mobile.seemesave.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import com.mobile.seemesave.model.common.ProductDetailModel;
import com.mobile.seemesave.model.common.StoreModel;
import com.mobile.seemesave.model.common.TrendingBrandModel;
import com.mobile.seemesave.model.common.UserModel;

public class UserRes {
    @SerializedName("userInfo")
    @Expose
    private UserModel userInfo;
    @SerializedName("topStoreList")
    @Expose
    private ArrayList<StoreModel> topStoreList;
    @SerializedName("topBrandList")
    @Expose
    private ArrayList<TrendingBrandModel> topBrandList;
    @SerializedName("topProductList")
    @Expose
    private ArrayList<ProductDetailModel> topProductList;

    private boolean status;
    private String message;

    public UserRes(){
        this.status = false;
        this.message = "";
    }

    public UserModel getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserModel userInfo) {
        this.userInfo = userInfo;
    }

    public ArrayList<StoreModel> getTopStoreList() {
        return topStoreList;
    }

    public void setTopStoreList(ArrayList<StoreModel> topStoreList) {
        this.topStoreList = topStoreList;
    }

    public ArrayList<TrendingBrandModel> getTopBrandList() {
        return topBrandList;
    }

    public void setTopBrandList(ArrayList<TrendingBrandModel> topBrandList) {
        this.topBrandList = topBrandList;
    }

    public ArrayList<ProductDetailModel> getTopProductList() {
        return topProductList;
    }

    public void setTopProductList(ArrayList<ProductDetailModel> topProductList) {
        this.topProductList = topProductList;
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
