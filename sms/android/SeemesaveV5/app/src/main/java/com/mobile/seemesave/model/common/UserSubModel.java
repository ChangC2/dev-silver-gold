package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserSubModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("first_name")
    @Expose
    private String first_name;

    @SerializedName("last_name")
    @Expose
    private String last_name;

    @SerializedName("image_url")
    @Expose
    private String image_url;

    @SerializedName("bg_image_url")
    @Expose
    private String bg_image_url;

    @SerializedName("TagList")
    @Expose
    private ArrayList<String> TagList;

    public UserSubModel() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getBg_image_url() {
        return bg_image_url;
    }

    public void setBg_image_url(String bg_image_url) {
        this.bg_image_url = bg_image_url;
    }

    public ArrayList<String> getTagList() {
        return TagList;
    }

    public void setTagList(ArrayList<String> tagList) {
        TagList = tagList;
    }
}
