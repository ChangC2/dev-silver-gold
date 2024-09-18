package com.mobile.seemesave.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobile.seemesave.model.common.PromotionModel;
import com.mobile.seemesave.model.common.PromotionOneModel;
import com.mobile.seemesave.model.common.StoreModel;

import java.util.ArrayList;

public class StorePromotionRes {

    @SerializedName("storeInfo")
    @Expose
    private StoreModel storeInfo;

    @SerializedName("promotionList")
    @Expose
    private ArrayList<PromotionModel> promotionList;

    private ArrayList<PromotionOneModel> collectList = new ArrayList<>();
    private ArrayList<PromotionOneModel> deliverList = new ArrayList<>();

    @SerializedName("totalCount")
    @Expose
    private int totalCount;

    private boolean status;
    private String message;
    public StorePromotionRes(){
        promotionList = new ArrayList<>();
        collectList.clear();
        deliverList.clear();
        this.status = false;
        this.message = "";
    }

    public StoreModel getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(StoreModel storeInfo) {
        this.storeInfo = storeInfo;
    }

    public ArrayList<PromotionModel> getPromotionList() {
        return promotionList;
    }

    public void setPromotionList(ArrayList<PromotionModel> promotionList) {
        this.promotionList = promotionList;
    }

    public ArrayList<PromotionOneModel> getCollectList() {
        return collectList;
    }

    public void setCollectList(ArrayList<PromotionOneModel> promotionProductList) {
        this.collectList = promotionProductList;
    }

    public ArrayList<PromotionOneModel> getDeliverList() {
        return deliverList;
    }

    public void setDeliverList(ArrayList<PromotionOneModel> deliverList) {
        this.deliverList = deliverList;
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
