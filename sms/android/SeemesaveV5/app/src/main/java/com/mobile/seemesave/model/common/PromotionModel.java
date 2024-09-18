package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PromotionModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("dealType")
    @Expose
    private String dealType;

    @SerializedName("feed_type")
    @Expose
    private String feed_type;

    @SerializedName("is_best_deal")
    @Expose
    private boolean is_best_deal;

    @SerializedName("is_deliver")
    @Expose
    private boolean is_deliver;

    @SerializedName("is_click_collect")
    @Expose
    private boolean is_click_collect;

    @SerializedName("is_click_deliver")
    @Expose
    private boolean is_click_deliver;
    @SerializedName("isLike")
    @Expose
    private boolean isLike;
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("start_date")
    @Expose
    private String start_date;

    @SerializedName("end_date")
    @Expose
    private String end_date;

    @SerializedName("likeCount")
    @Expose
    private int likeCount;

    @SerializedName("shareCount")
    @Expose
    private int shareCount;

    @SerializedName("reviewCount")
    @Expose
    private int reviewCount;

    @SerializedName("rating")
    @Expose
    private float rating;

    @SerializedName("Stores")
    @Expose
    private ArrayList<StoreModel> Stores;
    @SerializedName("Categories")
    @Expose
    private ArrayList<ProductCategoryModel> Categories;
    @SerializedName("SingleProducts")
    @Expose
    private ArrayList<SingleProductModel> SingleProducts;

    @SerializedName("ComboDeals")
    @Expose
    private ArrayList<ComboDealProductModel> ComboDeals;

    @SerializedName("Buy1Get1FreeDeals")
    @Expose
    private ArrayList<BuyGetProductModel> Buy1Get1FreeDeals;

    @SerializedName("TagList")
    @Expose
    private ArrayList<String> TagList;
    @SerializedName("SubMedia")
    @Expose
    private ArrayList<MediaModel> SubMedia = new ArrayList<>();
    public PromotionModel() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public boolean isIs_best_deal() {
        return is_best_deal;
    }

    public void setIs_best_deal(boolean is_best_deal) {
        this.is_best_deal = is_best_deal;
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

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public ArrayList<StoreModel> getStores() {
        return Stores;
    }

    public void setStores(ArrayList<StoreModel> stores) {
        Stores = stores;
    }

    public ArrayList<SingleProductModel> getSingleProducts() {
        return SingleProducts;
    }

    public void setSingleProducts(ArrayList<SingleProductModel> singleProducts) {
        SingleProducts = singleProducts;
    }

    public ArrayList<ComboDealProductModel> getComboDeals() {
        return ComboDeals;
    }

    public void setComboDeals(ArrayList<ComboDealProductModel> comboDeals) {
        ComboDeals = comboDeals;
    }

    public ArrayList<BuyGetProductModel> getBuy1Get1FreeDeals() {
        return Buy1Get1FreeDeals;
    }

    public void setBuy1Get1FreeDeals(ArrayList<BuyGetProductModel> buy1Get1FreeDeals) {
        Buy1Get1FreeDeals = buy1Get1FreeDeals;
    }

    public ArrayList<String> getTagList() {
        return TagList;
    }

    public void setTagList(ArrayList<String> tagList) {
        TagList = tagList;
    }

    public String getFeed_type() {
        return feed_type;
    }

    public void setFeed_type(String feed_type) {
        this.feed_type = feed_type;
    }

    public ArrayList<MediaModel> getSubMedia() {
        return SubMedia;
    }

    public void setSubMedia(ArrayList<MediaModel> subMedia) {
        SubMedia = subMedia;
    }

    public boolean isIs_deliver() {
        return is_deliver;
    }

    public void setIs_deliver(boolean is_deliver) {
        this.is_deliver = is_deliver;
    }

    public ArrayList<ProductCategoryModel> getCategories() {
        return Categories;
    }

    public void setCategories(ArrayList<ProductCategoryModel> categories) {
        Categories = categories;
    }
}
