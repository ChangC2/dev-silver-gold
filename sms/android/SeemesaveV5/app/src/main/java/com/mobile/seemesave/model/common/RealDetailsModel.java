package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RealDetailsModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("OrgProduct")
    @Expose
    private ProductModel OrgProduct;
    @SerializedName("RealProduct")
    @Expose
    private ProductDetailModel RealProduct;

    public RealDetailsModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductModel getOrgProduct() {
        return OrgProduct;
    }

    public void setOrgProduct(ProductModel orgProduct) {
        OrgProduct = orgProduct;
    }

    public ProductDetailModel getRealProduct() {
        return RealProduct;
    }

    public void setRealProduct(ProductDetailModel realProduct) {
        RealProduct = realProduct;
    }
}
