package seemesave.businesshub.model.res;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import seemesave.businesshub.model.common.UserModel;
import seemesave.businesshub.model.common.UserOwnsModel;
import seemesave.businesshub.model.common.UserPortalModel;

public class UserInfoRes {
    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("role")
    @Expose
    private String role;

    @SerializedName("userInfo")
    @Expose
    private UserModel userInfo;

    @SerializedName("portalInfo")
    @Expose
    private UserPortalModel portalInfo;

    private boolean status;
    private String message;

    public UserInfoRes(){
        this.status = false;
        this.message = "";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UserModel getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserModel userInfo) {
        this.userInfo = userInfo;
    }

    public UserPortalModel getPortalInfo() {
        return portalInfo;
    }

    public void setPortalInfo(UserPortalModel portalInfo) {
        this.portalInfo = portalInfo;
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
