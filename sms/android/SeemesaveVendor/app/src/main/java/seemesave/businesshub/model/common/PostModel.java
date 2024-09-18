package seemesave.businesshub.model.common;

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

    @SerializedName("feed_type")
    @Expose
    private String feed_type;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("start_date")
    @Expose
    private String start_date;

    @SerializedName("end_date")
    @Expose
    private String end_date;

    @SerializedName("Media_Type")
    @Expose
    private String Media_Type;

    @SerializedName("Media")
    @Expose
    private String Media;

    @SerializedName("SubMedia")
    @Expose
    private ArrayList<MediaModel> SubMedia = new ArrayList<>();

    @SerializedName("viewCount")
    @Expose
    private int viewCount;

    @SerializedName("private")
    @Expose
    private boolean isPrivate;

    @SerializedName("active")
    @Expose
    private boolean active;

    @SerializedName("AllowedUsers")
    @Expose
    private ArrayList<UserModel> allowedUsers;

    @SerializedName("is_deliver")
    @Expose
    private boolean is_deliver;

    @SerializedName("Creator")
    @Expose
    private UserModel Creator;

    @SerializedName("tag_list")
    @Expose
    private String TagList;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("LocationList")
    @Expose
    private ArrayList<LocationModel> LocationList;


    @SerializedName("PaymentInfo")
    @Expose
    private PaymentInfoModel PaymentInfo;

    @SerializedName("Store")
    @Expose
    private StoreModel Store;

    @SerializedName("Brand")
    @Expose
    private BrandModel Brand;

    @SerializedName("supplier")
    @Expose
    private int supplier;

    private boolean isCheck;
    public PostModel(){
        this.isCheck = false;
    }

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

    public ArrayList<MediaModel> getSubMedia() {
        return SubMedia;
    }

    public void setSubMedia(ArrayList<MediaModel> subMedia) {
        SubMedia = subMedia;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getFeed_type() {
        return feed_type;
    }

    public void setFeed_type(String feed_type) {
        this.feed_type = feed_type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ArrayList<UserModel> getAllowedUsers() {
        return allowedUsers;
    }

    public void setAllowedUsers(ArrayList<UserModel> allowedUsers) {
        this.allowedUsers = allowedUsers;
    }

    public boolean isIs_deliver() {
        return is_deliver;
    }

    public void setIs_deliver(boolean is_deliver) {
        this.is_deliver = is_deliver;
    }

    public UserModel getCreator() {
        return Creator;
    }

    public void setCreator(UserModel creator) {
        Creator = creator;
    }

    public String getTagList() {
        return TagList;
    }

    public void setTagList(String tagList) {
        TagList = tagList;
    }

    public ArrayList<LocationModel> getLocationList() {
        return LocationList;
    }

    public void setLocationList(ArrayList<LocationModel> locationList) {
        LocationList = locationList;
    }

    public PaymentInfoModel getPaymentInfo() {
        return PaymentInfo;
    }

    public void setPaymentInfo(PaymentInfoModel paymentInfo) {
        PaymentInfo = paymentInfo;
    }

    public StoreModel getStore() {
        return Store;
    }

    public void setStore(StoreModel store) {
        Store = store;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public BrandModel getBrand() {
        return Brand;
    }

    public void setBrand(BrandModel brand) {
        Brand = brand;
    }

    public int getSupplier() {
        return supplier;
    }

    public void setSupplier(int supplier) {
        this.supplier = supplier;
    }

    public String getMedia_Type() {
        return Media_Type;
    }

    public void setMedia_Type(String media_Type) {
        Media_Type = media_Type;
    }

    public String getMedia() {
        return Media;
    }

    public void setMedia(String media) {
        Media = media;
    }
}
