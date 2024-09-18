package seemesave.businesshub.model.common;


import seemesave.businesshub.utils.G;

import java.util.ArrayList;

public class MCommon implements Comparable<MCommon>{
    private String id = "";
    private String barcode = "";
    private String name = "";
    private String description = "";
    private String address = "";
    private String phone = "";
    private double price = 0.0;
    private CurrencyModel currency = new CurrencyModel();
    private String imageUrl = "";
    private int count = 1;
    private boolean isNew = false;
    private double rating = 0.0;
    private String PackSize = "";
    private String Unit = "";
    private boolean hasLiked = false;
    private double lat = 0.0;
    private double lon = 0.0;
    private String productId = "";
    private String freindInfoId = "";
    private boolean hasVariants = false;
    private int status = 0;
    private boolean isCheck = false;

    private double badge = 0;

    private String Created = "";

    public double getBadge() {
        return badge;
    }

    public void setBadge(double badge) {
        this.badge = badge;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private boolean is_click_collect = false;
    private boolean is_click_deliver = false;

    public boolean isIs_click_collect() {
        return is_click_collect;
    }

    public boolean isIs_click_deliver() {
        return is_click_deliver;
    }

    public void setIs_click_deliver(boolean is_click_deliver) {
        this.is_click_deliver = is_click_deliver;
    }

    public void setIs_click_collect(boolean is_click_collect) {
        this.is_click_collect = is_click_collect;
    }

    public boolean isHasVariants() {
        return hasVariants;
    }

    public void setHasVariants(boolean hasVariants) {
        this.hasVariants = hasVariants;
    }

    private String full_desc = "";

    public String getFull_desc() {
        return full_desc;
    }

    public void setFull_desc(String full_desc) {
        this.full_desc = full_desc;
    }

    private String time = "";
    private double delivery_price = 0;
    private String deliveryLink = "";

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private int type = 0;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private ArrayList<MCommon> subItems = new ArrayList<>();

    public String getFreindInfoId() {
        return freindInfoId;
    }

    public void setFreindInfoId(String freindInfoId) {
        this.freindInfoId = freindInfoId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isHasLiked() {
        return hasLiked;
    }

    public void setHasLiked(boolean hasLiked) {
        this.hasLiked = hasLiked;
    }

    public String getPackSize() {
        return PackSize;
    }

    public void setPackSize(String packSize) {
        PackSize = packSize;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }


    public ArrayList<MCommon> getSubItems() {
        return subItems;
    }

    public CurrencyModel getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyModel currency) {
        this.currency = currency;
    }

    public void setSubItems(ArrayList<MCommon> subItems) {
        this.subItems.clear();
        this.subItems.addAll(subItems);
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCreated() {
        return Created;
    }

    public void setCreated(String created) {
        Created = created;
    }

    public double getDelivery_price() {
        return delivery_price;
    }

    public void setDelivery_price(double delivery_price) {
        this.delivery_price = delivery_price;
    }

    public String getDeliveryLink() {
        return deliveryLink;
    }

    public void setDeliveryLink(String deliveryLink) {
        this.deliveryLink = deliveryLink;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public int compareTo(MCommon other) {
//        if (G.location != null) {
//            int dist1 = (int) G.meterDistanceBetweenPoints(G.location.getLatitude(), G.location.getLongitude(), getLat(), getLon());
//            int dist2 = (int) G.meterDistanceBetweenPoints(G.location.getLatitude(), G.location.getLongitude(), other.getLat(), other.getLon());
//            if  (dist1 > dist2){
//                return 1;
//            }else if (dist1 < dist2){
//                return -1;
//            }else{
//                return 0;
//            }
//        } else {
//            return 0;
//        }
        return 0;
    }

    @Override
    public String toString(){
        return this.id;
    }
}