package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLoginModel {
    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("user")
    @Expose
    private UserModel user;

    @SerializedName("owns")
    @Expose
    private UserOwnsModel owns;

    public UserLoginModel() {}

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

    public UserOwnsModel getOwns() {
        return owns;
    }

    public void setOwns(UserOwnsModel owns) {
        this.owns = owns;
    }
}
