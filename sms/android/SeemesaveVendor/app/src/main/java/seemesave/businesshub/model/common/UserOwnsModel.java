package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserOwnsModel {
    @SerializedName("vendors")
    @Expose
    private ArrayList<UserPortalModel> vendors;
    @SerializedName("suppliers")
    @Expose
    private ArrayList<UserPortalModel> suppliers;
    @SerializedName("stores")
    @Expose
    private ArrayList<UserPortalModel> stores;

    public UserOwnsModel(){
        suppliers = new ArrayList<>();
        suppliers.clear();
        vendors = new ArrayList<>();
        vendors.clear();
    }

    public ArrayList<UserPortalModel> getVendors() {
        return vendors;
    }

    public void setVendors(ArrayList<UserPortalModel> vendors) {
        this.vendors = vendors;
    }

    public ArrayList<UserPortalModel> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(ArrayList<UserPortalModel> suppliers) {
        this.suppliers = suppliers;
    }

    public ArrayList<UserPortalModel> getStores() {
        return stores;
    }

    public void setStores(ArrayList<UserPortalModel> stores) {
        this.stores = stores;
    }
}
