package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankDetailModel {
    @SerializedName("account_name")
    @Expose
    private String account_name;
    @SerializedName("account_number")
    @Expose
    private String account_number;
    @SerializedName("branch_code")
    @Expose
    private String branch_code;
    @SerializedName("branch_name")
    @Expose
    private String branch_name;

    public BankDetailModel(){}

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getBranch_code() {
        return branch_code;
    }

    public void setBranch_code(String branch_code) {
        this.branch_code = branch_code;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }
}
