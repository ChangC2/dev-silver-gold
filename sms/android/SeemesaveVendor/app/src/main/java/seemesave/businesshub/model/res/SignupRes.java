package seemesave.businesshub.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import seemesave.businesshub.model.common.UserModel;
import seemesave.businesshub.model.common.UserOwnsModel;
import seemesave.businesshub.model.common.UserPortalModel;

public class SignupRes {
    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("user")
    @Expose
    private UserModel user;

    @SerializedName("portal")
    @Expose
    private UserPortalModel portal;

    private boolean status;
    private String message;

    public SignupRes(){
        this.status = false;
        this.message = "";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public UserPortalModel getPortal() {
        return portal;
    }

    public void setPortal(UserPortalModel portal) {
        this.portal = portal;
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
