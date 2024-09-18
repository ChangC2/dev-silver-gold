package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ComboDealProductCartModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("variant_string")
    @Expose
    private String variant_string;

    @SerializedName("RealDetails")
    @Expose
    private ArrayList<RealDetailsModel> RealDetails;

    @SerializedName("productDetails")
    @Expose
    private ComboDealProductModel productDetails;

    @SerializedName("promotionDetail")
    @Expose
    private PromotionSubModel promotionDetail;

    public ComboDealProductCartModel() {}

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

    public ArrayList<RealDetailsModel> getRealDetails() {
        return RealDetails;
    }

    public void setRealDetails(ArrayList<RealDetailsModel> realDetails) {
        RealDetails = realDetails;
    }

    public ComboDealProductModel getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ComboDealProductModel productDetails) {
        this.productDetails = productDetails;
    }

    public PromotionSubModel getPromotionDetail() {
        return promotionDetail;
    }

    public void setPromotionDetail(PromotionSubModel promotionDetail) {
        this.promotionDetail = promotionDetail;
    }
}
