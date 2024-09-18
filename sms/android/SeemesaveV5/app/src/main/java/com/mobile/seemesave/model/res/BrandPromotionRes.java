package com.mobile.seemesave.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobile.seemesave.model.common.BrandModel;
import com.mobile.seemesave.model.common.PromotionModel;
import com.mobile.seemesave.model.common.PromotionOneModel;

import java.util.ArrayList;

public class BrandPromotionRes {
    @SerializedName("brandInfo")
    @Expose
    private BrandModel brandInfo;

    @SerializedName("promotionList")
    @Expose
    private ArrayList<PromotionModel> promotionList;
    private ArrayList<PromotionModel> collectPromotionList = new ArrayList<>();
    private ArrayList<PromotionModel> deliverPromotionList = new ArrayList<>();
    private ArrayList<PromotionOneModel> collectList = new ArrayList<>();
    private ArrayList<PromotionOneModel> deliverList = new ArrayList<>();


    @SerializedName("totalCount")
    @Expose
    private int totalCount;

    private boolean status;
    private String message;
    public BrandPromotionRes(){
        promotionList = new ArrayList<>();
        collectPromotionList.clear();
        deliverPromotionList.clear();
        collectList.clear();
        deliverList.clear();
        this.status = false;
        this.message = "";
    }

    public BrandModel getBrandInfo() {
        return brandInfo;
    }

    public void setBrandInfo(BrandModel brandInfo) {
        this.brandInfo = brandInfo;
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

    public ArrayList<PromotionModel> getCollectPromotionList() {
        return collectPromotionList;
    }

    public void setCollectPromotionList(ArrayList<PromotionModel> collectPromotionList) {
        this.collectPromotionList = collectPromotionList;
    }

    public ArrayList<PromotionModel> getDeliverPromotionList() {
        return deliverPromotionList;
    }

    public void setDeliverPromotionList(ArrayList<PromotionModel> deliverPromotionList) {
        this.deliverPromotionList = deliverPromotionList;
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
