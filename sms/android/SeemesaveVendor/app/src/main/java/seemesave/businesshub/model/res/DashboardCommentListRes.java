package seemesave.businesshub.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import seemesave.businesshub.model.common.StoreReviewModel;

import java.util.ArrayList;

public class DashboardCommentListRes {
    @SerializedName("data")
    @Expose
    private ArrayList<StoreReviewModel> commentList;
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("message")
    @Expose
    private String message;

    public DashboardCommentListRes() {}

    public ArrayList<StoreReviewModel> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList<StoreReviewModel> commentList) {
        this.commentList = commentList;
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
