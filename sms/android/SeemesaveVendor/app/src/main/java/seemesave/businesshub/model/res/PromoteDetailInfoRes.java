package seemesave.businesshub.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import seemesave.businesshub.model.common.BuyGetProductModel;
import seemesave.businesshub.model.common.ComboDealProductModel;
import seemesave.businesshub.model.common.PromoteReceiverModel;
import seemesave.businesshub.model.common.SingleProductModel;
import seemesave.businesshub.model.common.StoreModel;

public class PromoteDetailInfoRes {
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

    @SerializedName("productCount")
    @Expose
    private int productCount;

    @SerializedName("storeCount")
    @Expose
    private int storeCount;

    @SerializedName("commentCount")
    @Expose
    private int commentCount;

    @SerializedName("promotion_type")
    @Expose
    private int promotion_type;

    @SerializedName("PromotingVendorCount")
    @Expose
    private int PromotingVendorCount;

    @SerializedName("Receivers")
    @Expose
    private ArrayList<PromoteReceiverModel> Receivers;

    @SerializedName("SingleProducts")
    @Expose
    private ArrayList<SingleProductModel> SingleProducts;

    @SerializedName("ComboDeals")
    @Expose
    private ArrayList<ComboDealProductModel> ComboDeals;

    @SerializedName("Buy1Get1FreeDeals")
    @Expose
    private ArrayList<BuyGetProductModel> Buy1Get1FreeDeals;

    private boolean status;
    private String message;
    public PromoteDetailInfoRes(){
        this.status = false;
        this.message = "Something went wrong!";
    }

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

    public int getPromotingVendorCount() {
        return PromotingVendorCount;
    }

    public void setPromotingVendorCount(int promotingVendorCount) {
        PromotingVendorCount = promotingVendorCount;
    }

    public ArrayList<PromoteReceiverModel> getReceivers() {
        return Receivers;
    }

    public void setReceivers(ArrayList<PromoteReceiverModel> receivers) {
        Receivers = receivers;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
