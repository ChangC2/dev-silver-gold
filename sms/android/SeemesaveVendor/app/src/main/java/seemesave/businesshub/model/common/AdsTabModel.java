package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdsTabModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("label")
    @Expose
    private String label;

    public AdsTabModel(){}
    public AdsTabModel(int id, String label){
        this.id = id;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
