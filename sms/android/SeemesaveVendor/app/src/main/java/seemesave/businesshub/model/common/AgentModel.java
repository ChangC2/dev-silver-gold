package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AgentModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("role")
    @Expose
    private String role;

    @SerializedName("isPending")
    @Expose
    private boolean isPending;

    @SerializedName("User")
    @Expose
    private UserModel User;


    public AgentModel() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
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

    public UserModel getUser() {
        return User;
    }

    public void setUser(UserModel user) {
        User = user;
    }
}
