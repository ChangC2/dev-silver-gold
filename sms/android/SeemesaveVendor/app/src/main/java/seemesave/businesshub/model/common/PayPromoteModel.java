package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayPromoteModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("media")
    @Expose
    private String media;
    @SerializedName("media_type")
    @Expose
    private String media_type;
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
    @SerializedName("is_normal_promotion")
    @Expose
    private boolean is_normal_promotion;
    @SerializedName("is_click_collect")
    @Expose
    private boolean is_click_collect;
    @SerializedName("is_best_deal")
    @Expose
    private boolean is_best_deal;
    @SerializedName("is_exclusive_deal")
    @Expose
    private boolean is_exclusive_deal;
    @SerializedName("productCount")
    @Expose
    private int productCount;
    @SerializedName("storeCount")
    @Expose
    private int storeCount;
    @SerializedName("singleProductsCount")
    @Expose
    private int singleProductsCount;
    @SerializedName("comboDealsCount")
    @Expose
    private int comboDealsCount;
    @SerializedName("buy1get1freeDealsCount")
    @Expose
    private int buy1get1freeDealsCount;
    @SerializedName("commentCount")
    @Expose
    private int commentCount;
    @SerializedName("promotion_type")
    @Expose
    private int promotion_type;
    public PayPromoteModel(){}

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

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
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

    public boolean isIs_normal_promotion() {
        return is_normal_promotion;
    }

    public void setIs_normal_promotion(boolean is_normal_promotion) {
        this.is_normal_promotion = is_normal_promotion;
    }

    public boolean isIs_click_collect() {
        return is_click_collect;
    }

    public void setIs_click_collect(boolean is_click_collect) {
        this.is_click_collect = is_click_collect;
    }

    public boolean isIs_best_deal() {
        return is_best_deal;
    }

    public void setIs_best_deal(boolean is_best_deal) {
        this.is_best_deal = is_best_deal;
    }

    public boolean isIs_exclusive_deal() {
        return is_exclusive_deal;
    }

    public void setIs_exclusive_deal(boolean is_exclusive_deal) {
        this.is_exclusive_deal = is_exclusive_deal;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public int getStoreCount() {
        return storeCount;
    }

    public void setStoreCount(int storeCount) {
        this.storeCount = storeCount;
    }

    public int getSingleProductsCount() {
        return singleProductsCount;
    }

    public void setSingleProductsCount(int singleProductsCount) {
        this.singleProductsCount = singleProductsCount;
    }

    public int getComboDealsCount() {
        return comboDealsCount;
    }

    public void setComboDealsCount(int comboDealsCount) {
        this.comboDealsCount = comboDealsCount;
    }

    public int getBuy1get1freeDealsCount() {
        return buy1get1freeDealsCount;
    }

    public void setBuy1get1freeDealsCount(int buy1get1freeDealsCount) {
        this.buy1get1freeDealsCount = buy1get1freeDealsCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getPromotion_type() {
        return promotion_type;
    }

    public void setPromotion_type(int promotion_type) {
        this.promotion_type = promotion_type;
    }
}
