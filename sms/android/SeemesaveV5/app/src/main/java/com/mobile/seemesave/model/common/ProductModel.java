package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("hasVariants")
    @Expose
    private boolean hasVariants;

    @SerializedName("ProductDetail")
    @Expose
    private ProductDetailModel ProductDetail;

    public ProductModel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isHasVariants() {
        return hasVariants;
    }

    public void setHasVariants(boolean hasVariants) {
        this.hasVariants = hasVariants;
    }

    public ProductDetailModel getProductDetail() {
        return ProductDetail;
    }

    public void setProductDetail(ProductDetailModel productDetail) {
        ProductDetail = productDetail;
    }
}
