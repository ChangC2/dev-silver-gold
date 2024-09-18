package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PostModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("media")
    @Expose
    private String media;
    @SerializedName("alt_media")
    @Expose
    private String alt_media;
    @SerializedName("SubMedia")
    @Expose
    private ArrayList<MediaModel> SubMedia;
    @SerializedName("User")
    @Expose
    private UserSubModel User;
    @SerializedName("media_type")
    @Expose
    private String media_type;
    @SerializedName("start_date")
    @Expose
    private String start_date;
    @SerializedName("end_date")
    @Expose
    private String end_date;
    @SerializedName("rating")
    @Expose
    private float rating;
    @SerializedName("likeCount")
    @Expose
    private int likeCount;
    @SerializedName("reviewCount")
    @Expose
    private int reviewCount;
    @SerializedName("shareCount")
    @Expose
    private int shareCount;
    @SerializedName("Media_Type")
    @Expose
    private String Media_Type;
    @SerializedName("Media")
    @Expose
    private String Media;
    @SerializedName("TagList")
    @Expose
    private ArrayList<String> TagList;
    @SerializedName("LocationList")
    @Expose
    private ArrayList<LocationModel> LocationList;
    @SerializedName("isLike")
    @Expose
    private boolean isLike = false;
    @SerializedName("isFollow")
    @Expose
    private boolean isFollow = false;
    @SerializedName("likedFriendsCount")
    @Expose
    private int likedFriendsCount = 0;
    @SerializedName("isMarked")
    @Expose
    private boolean isMarked = false;
    private String media_gif;

    public PostModel() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public ArrayList<String> getTagList() {
        return TagList;
    }

    public void setTagList(ArrayList<String> tagList) {
        TagList = tagList;
    }

    public ArrayList<LocationModel> getLocationList() {
        return LocationList;
    }

    public void setLocationList(ArrayList<LocationModel> locationList) {
        LocationList = locationList;
    }

    public String getAlt_media() {
        return alt_media;
    }

    public void setAlt_media(String alt_media) {
        this.alt_media = alt_media;
    }

    public ArrayList<MediaModel> getSubMedia() {
        return SubMedia;
    }

    public void setSubMedia(ArrayList<MediaModel> subMedia) {
        SubMedia = subMedia;
    }

    public UserSubModel getUser() {
        return User;
    }

    public void setUser(UserSubModel user) {
        User = user;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public String getMedia_Type() {
        return Media_Type;
    }

    public void setMedia_Type(String media_Type) {
        Media_Type = media_Type;
    }

    public String getMedia_gif() {
        return media_gif;
    }

    public void setMedia_gif(String media_gif) {
        this.media_gif = media_gif;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public int getLikedFriendsCount() {
        return likedFriendsCount;
    }

    public void setLikedFriendsCount(int likedFriendsCount) {
        this.likedFriendsCount = likedFriendsCount;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }
}
