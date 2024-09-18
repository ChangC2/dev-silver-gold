package seemesave.businesshub.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import seemesave.businesshub.model.common.ProductDetailModel;
import seemesave.businesshub.model.common.PromoteProductModel;

import java.util.ArrayList;

public class ProductPromoteListRes {
    @SerializedName("data")
    @Expose
    private ArrayList<PromoteProductModel> productList;
    @SerializedName("totalCount")
    @Expose
    private int totalCount;
    private boolean status;
    private String message;

    public ProductPromoteListRes() {}

    public ArrayList<PromoteProductModel> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<PromoteProductModel> productList) {
        this.productList = productList;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
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
