package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MissDealModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("feedTitle")
    @Expose
    private String feedTitle;
    @SerializedName("feed_type")
    @Expose
    private String feed_type;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("is_click_collect")
    @Expose
    private boolean is_click_collect = false;
    @SerializedName("is_click_deliver")
    @Expose
    private boolean is_click_deliver = false;
    @SerializedName("media")
    @Expose
    private String media;

    @SerializedName("SubMedia")
    @Expose
    private ArrayList<MediaModel> SubMedia = new ArrayList<>();

    @SerializedName("media_type")
    @Expose
    private String media_type;

    @SerializedName("Media")
    @Expose
    private String Media;

    @SerializedName("Media_Type")
    @Expose
    private String Media_Type;

    @SerializedName("alt_media")
    @Expose
    private String alt_media;

    @SerializedName("start_date")
    @Expose
    private String start_date;

    @SerializedName("end_date")
    @Expose
    private String end_date;

    @SerializedName("Youtube_id")
    @Expose
    private String Youtube_id;

    @SerializedName("likeCount")
    @Expose
    private int likeCount = 0;

    @SerializedName("likedFriendsCount")
    @Expose
    private int likedFriendsCount = 0;

    @SerializedName("shareCount")
    @Expose
    private int shareCount = 0;

    @SerializedName("reviewCount")
    @Expose
    private int reviewCount = 0;

    @SerializedName("isLike")
    @Expose
    private boolean isLike = false;

    @SerializedName("isMarked")
    @Expose
    private boolean isMarked = false;

    @SerializedName("isFollow")
    @Expose
    private boolean isFollow = false;

    @SerializedName("TagList")
    @Expose
    private ArrayList<String> tags = new ArrayList<>();

    @SerializedName("related_id")
    @Expose
    private int related_id;

    private ArrayList<String> mediaList = new ArrayList<>();

    public MissDealModel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFeedTitle() {
        return feedTitle;
    }

    public void setFeedTitle(String feedTitle) {
        this.feedTitle = feedTitle;
    }

    public String getFeed_type() {
        return feed_type;
    }

    public void setFeed_type(String feed_type) {
        this.feed_type = feed_type;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isIs_click_collect() {
        return is_click_collect;
    }

    public void setIs_click_collect(boolean is_click_collect) {
        this.is_click_collect = is_click_collect;
    }

    public boolean isIs_click_deliver() {
        return is_click_deliver;
    }

    public void setIs_click_deliver(boolean is_click_deliver) {
        this.is_click_deliver = is_click_deliver;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getMedia_Type() {
        return Media_Type;
    }

    public void setMedia_Type(String media_Type) {
        Media_Type = media_Type;
    }

    public String getAlt_media() {
        return alt_media;
    }

    public void setAlt_media(String alt_media) {
        this.alt_media = alt_media;
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

    public String getYoutube_id() {
        return Youtube_id;
    }

    public void setYoutube_id(String youtube_id) {
        Youtube_id = youtube_id;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getLikedFriendsCount() {
        return likedFriendsCount;
    }

    public void setLikedFriendsCount(int likedFriendsCount) {
        this.likedFriendsCount = likedFriendsCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public int getRelated_id() {
        return related_id;
    }

    public void setRelated_id(int related_id) {
        this.related_id = related_id;
    }

    public ArrayList<String> getMediaList() {
        return mediaList;
    }

    public void setMediaList(ArrayList<String> mediaList) {
        this.mediaList = mediaList;
    }

    public ArrayList<MediaModel> getSubMedia() {
        return SubMedia;
    }

    public void setSubMedia(ArrayList<MediaModel> subMedia) {
        SubMedia = subMedia;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }
}
