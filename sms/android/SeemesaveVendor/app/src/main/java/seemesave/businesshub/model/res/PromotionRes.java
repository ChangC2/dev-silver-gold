package seemesave.businesshub.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import seemesave.businesshub.model.common.AdsModel;
import seemesave.businesshub.model.common.ProductDetailModel;
import seemesave.businesshub.model.common.PromotionModel;

import java.util.ArrayList;

public class PromotionRes {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private PromotionModel data;
    @SerializedName("message")
    @Expose
    private String message;

    public PromotionRes() {}

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public PromotionModel getData() {
        return data;
    }

    public void setData(PromotionModel data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
