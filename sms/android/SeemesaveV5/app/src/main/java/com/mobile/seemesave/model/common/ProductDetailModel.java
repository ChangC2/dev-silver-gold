package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductDetailModel {
    @SerializedName("barcode")
    @Expose
    private String barcode;

    @SerializedName("media")
    @Expose
    private String media;

    @SerializedName("thumbnail_image")
    @Expose
    private String thumbnail_image;

    @SerializedName("Brand")
    @Expose
    private String Brand;

    @SerializedName("BrandId")
    @Expose
    private int BrandId;

    @SerializedName("isBrandFollow")
    @Expose
    private boolean isBrandFollow;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("PackSize")
    @Expose
    private String PackSize;

    @SerializedName("Unit")
    @Expose
    private String Unit;

    @SerializedName("Category")
    @Expose
    private String Category;

    @SerializedName("isLike")
    @Expose
    private boolean isLike;

    @SerializedName("likeCount")
    @Expose
    private int likeCount;

    @SerializedName("full_product_description")
    @Expose
    private String full_product_description;

    @SerializedName("is_vehicle")
    @Expose
    private boolean is_vehicle;

    @SerializedName("subImages")
    @Expose
    private ArrayList<SubImageModel> subImages;

    @SerializedName("TagList")
    @Expose
    private ArrayList<String> TagList;

    public ProductDetailModel(){}

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(String thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public int getBrandId() {
        return BrandId;
    }

    public void setBrandId(int brandId) {
        BrandId = brandId;
    }

    public boolean isBrandFollow() {
        return isBrandFollow;
    }

    public void setBrandFollow(boolean brandFollow) {
        isBrandFollow = brandFollow;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPackSize() {
        return PackSize;
    }

    public void setPackSize(String packSize) {
        PackSize = packSize;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getFull_product_description() {
        return full_product_description;
    }

    public void setFull_product_description(String full_product_description) {
        this.full_product_description = full_product_description;
    }

    public boolean isIs_vehicle() {
        return is_vehicle;
    }

    public void setIs_vehicle(boolean is_vehicle) {
        this.is_vehicle = is_vehicle;
    }

    public ArrayList<SubImageModel> getSubImages() {
        return subImages;
    }

    public void setSubImages(ArrayList<SubImageModel> subImages) {
        this.subImages = subImages;
    }

    public ArrayList<String> getTagList() {
        return TagList;
    }

    public void setTagList(ArrayList<String> tagList) {
        TagList = tagList;
    }
}
