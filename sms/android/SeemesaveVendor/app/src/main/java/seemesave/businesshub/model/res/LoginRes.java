package seemesave.businesshub.model.res;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import seemesave.businesshub.model.common.TokenModel;
import seemesave.businesshub.model.common.UserAgentsModel;
import seemesave.businesshub.model.common.UserModel;
import seemesave.businesshub.model.common.UserOwnsModel;

public class LoginRes {
    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("user")
    @Expose
    private UserModel user;

    @SerializedName("owns")
    @Expose
    private UserOwnsModel owns;

    @SerializedName("agents")
    @Expose
    private UserAgentsModel agents;

    private boolean status;
    private String message;

    public LoginRes(){
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

    public UserOwnsModel getOwns() {
        return owns;
    }

    public void setOwns(UserOwnsModel owns) {
        this.owns = owns;
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

    public UserAgentsModel getAgents() {
        return agents;
    }

    public void setAgents(UserAgentsModel agents) {
        this.agents = agents;
    }
}
