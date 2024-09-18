package seemesave.businesshub.model.req;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvitePostReq {
    private boolean isMail;
    private String email;
    private String countryCode;
    private String phoneNumber;
    private String firstName;

    public InvitePostReq(){
    }

    public boolean isMail() {
        return isMail;
    }

    public void setMail(boolean mail) {
        isMail = mail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
