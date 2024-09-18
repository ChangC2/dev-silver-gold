package seemesave.businesshub.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import seemesave.businesshub.model.common.PayPromoteModel;
import seemesave.businesshub.model.common.StoreReviewModel;

import java.util.ArrayList;

public class DashboardInfoRes {
    @SerializedName("comments")
    @Expose
    private ArrayList<StoreReviewModel> commentList;
    @SerializedName("paytopromotes")
    @Expose
    private ArrayList<PayPromoteModel> payPromoteList;

    @SerializedName("payToPromtes")
    @Expose
    private ArrayList<PayPromoteModel> supplierPayToPromtes;

    @SerializedName("stores")
    @Expose
    private int stores;
    @SerializedName("promotions")
    @Expose
    private int promotions;
    @SerializedName("posts")
    @Expose
    private int posts;
    @SerializedName("follows")
    @Expose
    private int follows;
    @SerializedName("searchCount")
    @Expose
    private int searchCount;
    @SerializedName("postCount")
    @Expose
    private int postCount;
    @SerializedName("productCount")
    @Expose
    private int productCount;
    private boolean status;
    private String message;

    public DashboardInfoRes() {
        this.message = "Something went wrong!";
    }

    public ArrayList<StoreReviewModel> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList<StoreReviewModel> commentList) {
        this.commentList = commentList;
    }

    public ArrayList<PayPromoteModel> getPayPromoteList() {
        return payPromoteList;
    }

    public void setPayPromoteList(ArrayList<PayPromoteModel> payPromoteList) {
        this.payPromoteList = payPromoteList;
    }

    public int getStores() {
        return stores;
    }

    public void setStores(int stores) {
        this.stores = stores;
    }

    public int getPromotions() {
        return promotions;
    }

    public void setPromotions(int promotions) {
        this.promotions = promotions;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
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

    public ArrayList<PayPromoteModel> getSupplierPayToPromtes() {
        return supplierPayToPromtes;
    }

    public void setSupplierPayToPromtes(ArrayList<PayPromoteModel> supplierPayToPromtes) {
        this.supplierPayToPromtes = supplierPayToPromtes;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }
}
