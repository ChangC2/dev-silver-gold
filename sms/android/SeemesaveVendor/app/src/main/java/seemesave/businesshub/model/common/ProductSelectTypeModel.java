package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductSelectTypeModel {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("checked")
    @Expose
    private boolean checked;

    public ProductSelectTypeModel(){}
    public ProductSelectTypeModel(String name, boolean checked){
        this.name = name;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
