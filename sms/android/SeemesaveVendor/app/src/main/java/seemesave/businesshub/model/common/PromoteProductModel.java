package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PromoteProductModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("start_date")
    @Expose
    private String start_date;
    @SerializedName("end_date")
    @Expose
    private String end_date;
    @SerializedName("Supplier")
    @Expose
    private SupplierOneModel Supplier;

    @SerializedName("SingleProducts")
    @Expose
    private ArrayList<SingleProductModel> SingleProducts;

    @SerializedName("ComboDeals")
    @Expose
    private ArrayList<ComboDealProductModel> ComboDeals;

    @SerializedName("Buy1Get1FreeDeals")
    @Expose
    private ArrayList<BuyGetProductModel> Buy1Get1FreeDeals;

    public PromoteProductModel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public SupplierOneModel getSupplier() {
        return Supplier;
    }

    public void setSupplier(SupplierOneModel supplier) {
        Supplier = supplier;
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
