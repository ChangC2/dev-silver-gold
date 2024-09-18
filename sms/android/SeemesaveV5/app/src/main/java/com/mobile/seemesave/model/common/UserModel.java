package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class UserModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("first_name")
    @Expose
    private String first_name;
    @SerializedName("last_name")
    @Expose
    private String last_name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("country_code")
    @Expose
    private String country_code;
    @SerializedName("register_with")
    @Expose
    private String register_with;
    @SerializedName("date_joined")
    @Expose
    private String date_joined;
    @SerializedName("image_url")
    @Expose
    private String image_url;
    @SerializedName("bg_image_url")
    @Expose
    private String bg_image_url;
    @SerializedName("latitude")
    @Expose
    @Nullable
    private double latitude;
    @SerializedName("longitude")
    @Expose
    @Nullable
    private double longitude;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("is_trader")
    @Expose
    private boolean is_trader = false;
    @SerializedName("age")
    @Expose
    private int age;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("time_zone")
    @Expose
    private String time_zone;
    @SerializedName("time_offset")
    @Expose
    private int time_offset;
    @SerializedName("friendCount")
    @Expose
    private int friendCount;
    @SerializedName("likedProductCount")
    @Expose
    private int likedProductCount;
    @SerializedName("followedStoreCount")
    @Expose
    private int followedStoreCount;
    @SerializedName("postCount")
    @Expose
    private int postCount;
    @SerializedName("Currency")
    @Expose
    private CurrencyModel currency;
    @SerializedName("TagList")
    @Expose
    private ArrayList tagList = new ArrayList();
    public UserModel(){}

    public UserModel(double lat, double lnt){
        setLatitude(lat);
        setLongitude(lnt);
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getRegister_with() {
        return register_with;
    }

    public void setRegister_with(String register_with) {
        this.register_with = register_with;
    }

    public String getDate_joined() {
        return date_joined;
    }

    public void setDate_joined(String date_joined) {
        this.date_joined = date_joined;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isIs_trader() {
        return is_trader;
    }

    public void setIs_trader(boolean is_trader) {
        this.is_trader = is_trader;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public int getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(int friendCount) {
        this.friendCount = friendCount;
    }

    public int getLikedProductCount() {
        return likedProductCount;
    }

    public void setLikedProductCount(int likedProductCount) {
        this.likedProductCount = likedProductCount;
    }

    public int getFollowedStoreCount() {
        return followedStoreCount;
    }

    public void setFollowedStoreCount(int followedStoreCount) {
        this.followedStoreCount = followedStoreCount;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public CurrencyModel getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyModel currency) {
        this.currency = currency;
    }

    public ArrayList getTagList() {
        return tagList;
    }

    public void setTagList(ArrayList tagList) {
        this.tagList = tagList;
    }

    public int getTime_offset() {
        return time_offset;
    }

    public void setTime_offset(int time_offset) {
        this.time_offset = time_offset;
    }
}
