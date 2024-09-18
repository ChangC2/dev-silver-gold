package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AdsModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("feed_type")
    @Expose
    private String feed_type;

    @SerializedName("dealType")
    @Expose
    private String dealType;

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

    @SerializedName("Creator")
    @Expose
    private UserModel Creator;

    @SerializedName("likeCount")
    @Expose
    private int likeCount;

    @SerializedName("shareCount")
    @Expose
    private int shareCount;

    @SerializedName("reviewCount")
    @Expose
    private int reviewCount;

    @SerializedName("productCount")
    @Expose
    private int productCount;

    @SerializedName("is_deliver")
    @Expose
    private boolean is_deliver;

    @SerializedName("active")
    @Expose
    private boolean active;

    @SerializedName("Stores")
    @Expose
    private ArrayList<StoreModel> Stores;

    @SerializedName("LocationList")
    @Expose
    private ArrayList<LocationModel> LocationList;

    @SerializedName("Brand")
    @Expose
    private BrandModel Brand;

    @SerializedName("PaymentInfo")
    @Expose
    private PaymentInfoModel PaymentInfo;
    private boolean isCheck;
    public AdsModel() {
        this.isCheck = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFeed_type() {
        return feed_type;
    }

    public void setFeed_type(String feed_type) {
        this.feed_type = feed_type;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
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

    public UserModel getCreator() {
        return Creator;
    }

    public void setCreator(UserModel creator) {
        Creator = creator;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
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

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public boolean isIs_deliver() {
        return is_deliver;
    }

    public void setIs_deliver(boolean is_deliver) {
        this.is_deliver = is_deliver;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ArrayList<StoreModel> getStores() {
        return Stores;
    }

    public void setStores(ArrayList<StoreModel> stores) {
        Stores = stores;
    }

    public PaymentInfoModel getPaymentInfo() {
        return PaymentInfo;
    }

    public void setPaymentInfo(PaymentInfoModel paymentInfo) {
        PaymentInfo = paymentInfo;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public ArrayList<LocationModel> getLocationList() {
        return LocationList;
    }

    public void setLocationList(ArrayList<LocationModel> locationList) {
        LocationList = locationList;
    }

    public BrandModel getBrand() {
        return Brand;
    }

    public void setBrand(BrandModel brand) {
        Brand = brand;
    }
}
