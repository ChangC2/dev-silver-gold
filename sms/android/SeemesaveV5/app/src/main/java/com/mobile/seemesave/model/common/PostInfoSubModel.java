package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostInfoSubModel {
    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("site")
    @Expose
    private String site;


    public PostInfoSubModel() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
