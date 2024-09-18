package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleProductCartModel {
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
    private RealDetailsModel RealDetails;


    @SerializedName("productDetails")
    @Expose
    private SingleProductModel productDetails;

    @SerializedName("promotionDetail")
    @Expose
    private PromotionSubModel promotionDetail;

    public SingleProductCartModel() {}

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

    public RealDetailsModel getRealDetails() {
        return RealDetails;
    }

    public void setRealDetails(RealDetailsModel realDetails) {
        RealDetails = realDetails;
    }

    public SingleProductModel getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(SingleProductModel productDetails) {
        this.productDetails = productDetails;
    }

    public PromotionSubModel getPromotionDetail() {
        return promotionDetail;
    }

    public void setPromotionDetail(PromotionSubModel promotionDetail) {
        this.promotionDetail = promotionDetail;
    }
}
