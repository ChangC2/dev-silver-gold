package seemesave.businesshub.model.req;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SignupReq {
    @SerializedName("user_id")
    @Expose
    private RequestBody user_id;
    @SerializedName("dest_portal")
    @Expose
    private RequestBody dest_portal;

    @SerializedName("new_logo")
    @Expose
    private MultipartBody.Part new_logo;

    @SerializedName("name")
    @Expose
    private RequestBody name;

    @SerializedName("email")
    @Expose
    private RequestBody email;


    @SerializedName("phone")
    @Expose
    private RequestBody phone;

    @SerializedName("address")
    @Expose
    private RequestBody address;

    @SerializedName("website")
    @Expose
    private RequestBody website;

    public SignupReq(){}

    public RequestBody getUser_id() {
        return user_id;
    }

    public void setUser_id(RequestBody user_id) {
        this.user_id = user_id;
    }

    public RequestBody getDest_portal() {
        return dest_portal;
    }

    public void setDest_portal(RequestBody dest_portal) {
        this.dest_portal = dest_portal;
    }

    public MultipartBody.Part getNew_logo() {
        return new_logo;
    }

    public void setNew_logo(MultipartBody.Part new_logo) {
        this.new_logo = new_logo;
    }

    public RequestBody getName() {
        return name;
    }

    public void setName(RequestBody name) {
        this.name = name;
    }

    public RequestBody getEmail() {
        return email;
    }

    public void setEmail(RequestBody email) {
        this.email = email;
    }

    public RequestBody getPhone() {
        return phone;
    }

    public void setPhone(RequestBody phone) {
        this.phone = phone;
    }

    public RequestBody getAddress() {
        return address;
    }

    public void setAddress(RequestBody address) {
        this.address = address;
    }

    public RequestBody getWebsite() {
        return website;
    }

    public void setWebsite(RequestBody website) {
        this.website = website;
    }
}
