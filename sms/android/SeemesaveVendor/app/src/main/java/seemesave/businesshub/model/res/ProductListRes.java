package seemesave.businesshub.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import seemesave.businesshub.model.common.ProductDetailModel;

import java.util.ArrayList;

public class ProductListRes {
    @SerializedName("data")
    @Expose
    private ArrayList<ProductDetailModel> productList;
    @SerializedName("totalCount")
    @Expose
    private int totalCount;
    private boolean status;
    private String message;

    public ProductListRes() {}

    public ArrayList<ProductDetailModel> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<ProductDetailModel> productList) {
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
