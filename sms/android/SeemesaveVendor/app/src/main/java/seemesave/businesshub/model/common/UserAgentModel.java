package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserAgentModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("portal_token")
    @Expose
    private String portal_token;
    @SerializedName("Vendor")
    @Expose
    private UserPortalModel Vendor;
    @SerializedName("Supplier")
    @Expose
    private UserPortalModel Supplier;
    @SerializedName("role")
    @Expose
    private String role;

    @SerializedName("isPending")
    @Expose
    private boolean isPending;

    @SerializedName("created")
    @Expose
    private String created;

    public UserAgentModel(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPortal_token() {
        return portal_token;
    }

    public void setPortal_token(String portal_token) {
        this.portal_token = portal_token;
    }

    public UserPortalModel getVendor() {
        return Vendor;
    }

    public void setVendor(UserPortalModel vendor) {
        Vendor = vendor;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public UserPortalModel getSupplier() {
        return Supplier;
    }

    public void setSupplier(UserPortalModel supplier) {
        Supplier = supplier;
    }
}
