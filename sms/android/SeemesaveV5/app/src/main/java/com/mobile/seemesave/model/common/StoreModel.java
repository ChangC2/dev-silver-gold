package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StoreModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("logo")
    @Expose
    private String logo;

    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("barcode")
    @Expose
    private String barcode;
    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("contact_number")
    @Expose
    private String contact_number;

    private double price;

    @SerializedName("Currency")
    @Expose
    private CurrencyModel currency;

    @SerializedName("is_click_collect")
    @Expose
    private boolean is_click_collect;

    @SerializedName("vendor_name")
    @Expose
    private String vendor_name;

    @SerializedName("vendor")
    @Expose
    private int vendor;



    @SerializedName("building_number")
    @Expose
    private String building_number;

    @SerializedName("featured_start_date")
    @Expose
    private String featured_start_date;

    @SerializedName("featured_end_date")
    @Expose
    private String featured_end_date;

    @SerializedName("rating")
    @Expose
    private float rating;

    @SerializedName("open_time")
    @Expose
    private int open_time;

    @SerializedName("close_time")
    @Expose
    private int close_time;

    @SerializedName("working_days")
    @Expose
    private String working_days;

    @SerializedName("isFollowing")
    @Expose
    private boolean isFollowing;

    @SerializedName("followerCount")
    @Expose
    private int followerCount;

    @SerializedName("badgeCount")
    @Expose
    private float badgeCount;

    @SerializedName("TagList")
    @Expose
    private ArrayList<String> TagList;

    @SerializedName("coordinates")
    @Expose
    private ArrayList<Double> coordinates;


    public StoreModel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public CurrencyModel getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyModel currency) {
        this.currency = currency;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public int getVendor() {
        return vendor;
    }

    public void setVendor(int vendor) {
        this.vendor = vendor;
    }

    public boolean isIs_click_collect() {
        return is_click_collect;
    }

    public void setIs_click_collect(boolean is_click_collect) {
        this.is_click_collect = is_click_collect;
    }

    public String getBuilding_number() {
        return building_number;
    }

    public void setBuilding_number(String building_number) {
        this.building_number = building_number;
    }

    public String getFeatured_start_date() {
        return featured_start_date;
    }

    public void setFeatured_start_date(String featured_start_date) {
        this.featured_start_date = featured_start_date;
    }

    public String getFeatured_end_date() {
        return featured_end_date;
    }

    public void setFeatured_end_date(String featured_end_date) {
        this.featured_end_date = featured_end_date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getOpen_time() {
        return open_time;
    }

    public void setOpen_time(int open_time) {
        this.open_time = open_time;
    }

    public int getClose_time() {
        return close_time;
    }

    public void setClose_time(int close_time) {
        this.close_time = close_time;
    }

    public String getWorking_days() {
        return working_days;
    }

    public void setWorking_days(String working_days) {
        this.working_days = working_days;
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

    public float getBadgeCount() {
        return badgeCount;
    }

    public void setBadgeCount(float badgeCount) {
        this.badgeCount = badgeCount;
    }

    public ArrayList<String> getTagList() {
        return TagList;
    }

    public void setTagList(ArrayList<String> tagList) {
        TagList = tagList;
    }

    public ArrayList<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
