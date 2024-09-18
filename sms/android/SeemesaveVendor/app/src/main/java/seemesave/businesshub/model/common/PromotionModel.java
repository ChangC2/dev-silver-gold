package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import seemesave.businesshub.api.user.User;

import java.util.ArrayList;

public class PromotionModel {
    @SerializedName("id")
    @Expose
    private int id;

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

    @SerializedName("SubMedia")
    @Expose
    private ArrayList<MediaModel> SubMedia = new ArrayList<>();

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

    @SerializedName("LocationList")
    @Expose
    private ArrayList<LocationModel> LocationList;


    @SerializedName("PaymentInfo")
    @Expose
    private PaymentInfoModel PaymentInfo;


    @SerializedName("Stores")
    @Expose
    private ArrayList<StoreModel> Stores;

    @SerializedName("SingleProducts")
    @Expose
    private ArrayList<SingleProductModel> SingleProducts;

    @SerializedName("ComboDeals")
    @Expose
    private ArrayList<ComboDealProductModel> ComboDeals;

    @SerializedName("Buy1Get1FreeDeals")
    @Expose
    private ArrayList<BuyGetProductModel> Buy1Get1FreeDeals;



    public PromotionModel() {}

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
}
