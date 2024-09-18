package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("place_id")
    @Expose
    private String place_id;
    @SerializedName("full_address")
    @Expose
    private String full_address;
    @SerializedName("loc_latitude")
    @Expose
    private double loc_latitude;
    @SerializedName("loc_longitude")
    @Expose
    private double loc_longitude;

    public LocationModel() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public double getLoc_latitude() {
        return loc_latitude;
    }

    public void setLoc_latitude(double loc_latitude) {
        this.loc_latitude = loc_latitude;
    }

    public double getLoc_longitude() {
        return loc_longitude;
    }

    public void setLoc_longitude(double loc_longitude) {
        this.loc_longitude = loc_longitude;
    }
}
