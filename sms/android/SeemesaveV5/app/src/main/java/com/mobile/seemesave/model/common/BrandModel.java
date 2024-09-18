package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BrandModel {
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

    @SerializedName("active")
    @Expose
    private boolean active;

    @SerializedName("isFollowing")
    @Expose
    private boolean isFollowing;

    @SerializedName("followerCount")
    @Expose
    private int followerCount;

    @SerializedName("Supplier")
    @Expose
    private SupplierModel Supplier;

    @SerializedName("TagList")
    @Expose
    private ArrayList<String> TagList;


    public BrandModel(){}
    public BrandModel(int id, String imageUrl, String name, String description){
        this.id = id;
        this.media = imageUrl;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public SupplierModel getSupplier() {
        return Supplier;
    }

    public void setSupplier(SupplierModel supplier) {
        Supplier = supplier;
    }

    public ArrayList<String> getTagList() {
        return TagList;
    }

    public void setTagList(ArrayList<String> tagList) {
        TagList = tagList;
    }
}
