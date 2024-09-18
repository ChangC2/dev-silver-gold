package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BuyGetProductCartModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("variant_string")
    @Expose
    private String variant_string;

    @SerializedName("RealBuyDetails")
    @Expose
    private ArrayList<RealDetailsModel> RealBuyDetails;

    @SerializedName("RealFreeDetails")
    @Expose
    private ArrayList<RealDetailsModel> RealFreeDetails;

    @SerializedName("productDetails")
    @Expose
    private BuyGetProductModel productDetails;

    @SerializedName("promotionDetail")
    @Expose
    private PromotionSubModel promotionDetail;

    public BuyGetProductCartModel() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getVariant_string() {
        return variant_string;
    }

    public void setVariant_string(String variant_string) {
        this.variant_string = variant_string;
    }

    public ArrayList<RealDetailsModel> getRealBuyDetails() {
        return RealBuyDetails;
    }

    public void setRealBuyDetails(ArrayList<RealDetailsModel> realBuyDetails) {
        RealBuyDetails = realBuyDetails;
    }

    public ArrayList<RealDetailsModel> getRealFreeDetails() {
        return RealFreeDetails;
    }

    public void setRealFreeDetails(ArrayList<RealDetailsModel> realFreeDetails) {
        RealFreeDetails = realFreeDetails;
    }

    public BuyGetProductModel getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(BuyGetProductModel productDetails) {
        this.productDetails = productDetails;
    }

    public PromotionSubModel getPromotionDetail() {
        return promotionDetail;
    }

    public void setPromotionDetail(PromotionSubModel promotionDetail) {
        this.promotionDetail = promotionDetail;
    }
}
