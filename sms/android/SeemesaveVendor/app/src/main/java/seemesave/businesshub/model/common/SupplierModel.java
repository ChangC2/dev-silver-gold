package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SupplierModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("isPending")
    @Expose
    private boolean isPending;

    @SerializedName("inviter")
    @Expose
    private int inviter;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("Supplier")
    @Expose
    private SupplierOneModel Supplier;

    @SerializedName("Vendor")
    @Expose
    private SupplierOneModel Vendor;

    public SupplierModel() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public int getInviter() {
        return inviter;
    }

    public void setInviter(int inviter) {
        this.inviter = inviter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SupplierOneModel getSupplier() {
        return Supplier;
    }

    public void setSupplier(SupplierOneModel supplier) {
        Supplier = supplier;
    }

    public SupplierOneModel getVendor() {
        return Vendor;
    }

    public void setVendor(SupplierOneModel vendor) {
        Vendor = vendor;
    }
}
