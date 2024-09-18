package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StoreModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("Categories")
    @Expose
    private ArrayList<StoreCategoryModel> Categories;


    @SerializedName("street")
    @Expose
    private String street;

    @SerializedName("building_number")
    @Expose
    private String building_number;

    @SerializedName("vat_number")
    @Expose
    private String vat_number;

    @SerializedName("contact_number")
    @Expose
    private String contact_number;

    @SerializedName("logo")
    @Expose
    private String logo;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("rating")
    @Expose
    private float rating;

    @SerializedName("ReviewCount")
    @Expose
    private int ReviewCount;

    @SerializedName("FollowerCount")
    @Expose
    private int FollowerCount;

    @SerializedName("is_click_collect")
    @Expose
    private boolean is_click_collect;

    @SerializedName("is_click_deliver")
    @Expose
    private boolean is_click_deliver;

    @SerializedName("tag_list")
    @Expose
    private String tag_list;

    @SerializedName("trading_time")
    @Expose
    private String trading_time;

    @SerializedName("time_zone")
    @Expose
    private String time_zone;

    @SerializedName("time_offset")
    @Expose
    private int time_offset;

    @SerializedName("Currency")
    @Expose
    private CurrencyModel currency;

    @SerializedName("currency")
    @Expose
    private int currencyId;

    @SerializedName("badgeCount")
    @Expose
    private float badgeCount;


    @SerializedName("address_street1")
    @Expose
    private String address_street1;

    @SerializedName("address_street2")
    @Expose
    private String address_street2;

    @SerializedName("address_suburb")
    @Expose
    private String address_suburb;

    @SerializedName("address_city")
    @Expose
    private String address_city;

    @SerializedName("address_state")
    @Expose
    private String address_state;

    @SerializedName("address_country")
    @Expose
    private String address_country;

    @SerializedName("address_postal_code")
    @Expose
    private String address_postal_code;

    @SerializedName("coordinates")
    @Expose
    private ArrayList<Double> coordinates;

    private boolean isCheck;
    @Override
    public String toString() {
        return name;
    }

    public StoreModel(){
        this.isCheck = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getVat_number() {
        return vat_number;
    }

    public void setVat_number(String vat_number) {
        this.vat_number = vat_number;
    }

    public String getTrading_time() {
        return trading_time;
    }

    public void setTrading_time(String trading_time) {
        this.trading_time = trading_time;
    }

    public String getBuilding_number() {
        return building_number;
    }

    public void setBuilding_number(String building_number) {
        this.building_number = building_number;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public ArrayList<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return ReviewCount;
    }

    public void setReviewCount(int reviewCount) {
        ReviewCount = reviewCount;
    }

    public int getFollowerCount() {
        return FollowerCount;
    }

    public void setFollowerCount(int followerCount) {
        FollowerCount = followerCount;
    }

    public boolean isIs_click_collect() {
        return is_click_collect;
    }

    public void setIs_click_collect(boolean is_click_collect) {
        this.is_click_collect = is_click_collect;
    }

    public boolean isIs_click_deliver() {
        return is_click_deliver;
    }

    public void setIs_click_deliver(boolean is_click_deliver) {
        this.is_click_deliver = is_click_deliver;
    }

    public String getTag_list() {
        return tag_list;
    }

    public void setTag_list(String tag_list) {
        this.tag_list = tag_list;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public int getTime_offset() {
        return time_offset;
    }

    public void setTime_offset(int time_offset) {
        this.time_offset = time_offset;
    }

    public CurrencyModel getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyModel currency) {
        this.currency = currency;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public float getBadgeCount() {
        return badgeCount;
    }

    public void setBadgeCount(float badgeCount) {
        this.badgeCount = badgeCount;
    }

    public String getAddress_street1() {
        return address_street1;
    }

    public void setAddress_street1(String address_street1) {
        this.address_street1 = address_street1;
    }

    public String getAddress_street2() {
        return address_street2;
    }

    public void setAddress_street2(String address_street2) {
        this.address_street2 = address_street2;
    }

    public String getAddress_suburb() {
        return address_suburb;
    }

    public void setAddress_suburb(String address_suburb) {
        this.address_suburb = address_suburb;
    }

    public String getAddress_city() {
        return address_city;
    }

    public void setAddress_city(String address_city) {
        this.address_city = address_city;
    }

    public String getAddress_state() {
        return address_state;
    }

    public void setAddress_state(String address_state) {
        this.address_state = address_state;
    }

    public String getAddress_country() {
        return address_country;
    }

    public void setAddress_country(String address_country) {
        this.address_country = address_country;
    }

    public String getAddress_postal_code() {
        return address_postal_code;
    }

    public void setAddress_postal_code(String address_postal_code) {
        this.address_postal_code = address_postal_code;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public ArrayList<StoreCategoryModel> getCategories() {
        return Categories;
    }

    public void setCategories(ArrayList<StoreCategoryModel> categories) {
        Categories = categories;
    }
}
