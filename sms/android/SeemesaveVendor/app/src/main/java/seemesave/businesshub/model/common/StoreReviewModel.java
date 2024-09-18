package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StoreReviewModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("User")
    @Expose
    private UserModel User;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("rating")
    @Expose
    private float rating;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("ReplyList")
    @Expose
    private ArrayList<ReplyModel> ReplyList;
    public StoreReviewModel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserModel getUser() {
        return User;
    }

    public void setUser(UserModel user) {
        User = user;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public ArrayList<ReplyModel> getReplyList() {
        return ReplyList;
    }

    public void setReplyList(ArrayList<ReplyModel> replyList) {
        ReplyList = replyList;
    }
}
