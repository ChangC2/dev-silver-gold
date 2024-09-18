package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserAgentsModel {
    @SerializedName("vendors")
    @Expose
    private ArrayList<UserAgentModel> vendors;
    @SerializedName("suppliers")
    @Expose
    private ArrayList<UserAgentModel> suppliers;
    @SerializedName("stores")
    @Expose
    private ArrayList<UserAgentModel> stores;

    public UserAgentsModel(){
        suppliers = new ArrayList<>();
        suppliers.clear();
        vendors = new ArrayList<>();
        vendors.clear();
    }

    public ArrayList<UserAgentModel> getVendors() {
        return vendors;
    }

    public void setVendors(ArrayList<UserAgentModel> vendors) {
        this.vendors = vendors;
    }

    public ArrayList<UserAgentModel> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(ArrayList<UserAgentModel> suppliers) {
        this.suppliers = suppliers;
    }

    public ArrayList<UserAgentModel> getStores() {
        return stores;
    }

    public void setStores(ArrayList<UserAgentModel> stores) {
        this.stores = stores;
    }
}
