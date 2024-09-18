package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BrandDetailModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("media")
    @Expose
    private String media;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("Promotions")
    @Expose
    private ArrayList<PromotionModel> Promotions;

    public BrandDetailModel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<PromotionModel> getPromotions() {
        return Promotions;
    }

    public void setPromotions(ArrayList<PromotionModel> promotions) {
        Promotions = promotions;
    }
}
