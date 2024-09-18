package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductDetailModel {
    @SerializedName("barcode")
    @Expose
    private String barcode;

    @SerializedName("thumbnail_image")
    @Expose
    private String thumbnail_image;

    @SerializedName("Brand")
    @Expose
    private String Brand;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("PackSize")
    @Expose
    private String PackSize;

    @SerializedName("brand_id")
    @Expose
    private int brand_id;

    @SerializedName("pack_size_id")
    @Expose
    private int pack_size_id;

    @SerializedName("unit_id")
    @Expose
    private int unit_id;

    @SerializedName("category_id")
    @Expose
    private int category_id;

    @SerializedName("supplier_id")
    @Expose
    private int supplier_id;

    @SerializedName("supplier_product_code")
    @Expose
    private String supplier_product_code;

    @SerializedName("Unit")
    @Expose
    private String Unit;

    @SerializedName("Category")
    @Expose
    private String Category;

    @SerializedName("active")
    @Expose
    private boolean active;

    @SerializedName("tag_list")
    @Expose
    private String tag_list;


    @SerializedName("media")
    @Expose
    private String media;

    @SerializedName("BrandId")
    @Expose
    private int BrandId;

    @SerializedName("isBrandFollow")
    @Expose
    private boolean isBrandFollow;

    @SerializedName("isLike")
    @Expose
    private boolean isLike;

    @SerializedName("likeCount")
    @Expose
    private int likeCount;

    @SerializedName("full_product_description")
    @Expose
    private String full_product_description;

    @SerializedName("is_vehicle")
    @Expose
    private boolean is_vehicle;

    @SerializedName("subImages")
    @Expose
    private ArrayList<String> subImages;

    @SerializedName("TagList")
    @Expose
    private ArrayList<String> TagList;


    private boolean isCheck;
    private int product_count;
    private String variant_string;

    public ProductDetailModel(){
        this.isCheck = false;
        this.product_count = 1;
        this.variant_string = "";
    }
    public ProductDetailModel(ProductDetailModel model){
        this.barcode = model.barcode;
        this.thumbnail_image = model.thumbnail_image;
        this.Brand = model.Brand;
        this.description = model.description;
        this.PackSize = model.PackSize;
        this.Unit = model.Unit;
        this.Category = model.Category;
        this.active = model.active;
        this.supplier_id = model.supplier_id;
        this.media = model.media;
        this.BrandId = model.BrandId;
        this.isBrandFollow = model.isBrandFollow;
        this.isLike = model.isLike;
        this.likeCount = model.likeCount;
        this.full_product_description = model.full_product_description;
        this.is_vehicle = model.is_vehicle;
        this.subImages = model.subImages;
        this.TagList = model.TagList;
        this.product_count = 1;

    }
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(String thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public int getBrandId() {
        return BrandId;
    }

    public void setBrandId(int brandId) {
        BrandId = brandId;
    }

    public boolean isBrandFollow() {
        return isBrandFollow;
    }

    public void setBrandFollow(boolean brandFollow) {
        isBrandFollow = brandFollow;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getFull_product_description() {
        return full_product_description;
    }

    public void setFull_product_description(String full_product_description) {
        this.full_product_description = full_product_description;
    }

    public boolean isIs_vehicle() {
        return is_vehicle;
    }

    public void setIs_vehicle(boolean is_vehicle) {
        this.is_vehicle = is_vehicle;
    }

    public ArrayList<String> getSubImages() {
        return subImages;
    }

    public void setSubImages(ArrayList<String> subImages) {
        this.subImages = subImages;
    }

    public ArrayList<String> getTagList() {
        return TagList;
    }

    public void setTagList(ArrayList<String> tagList) {
        TagList = tagList;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getProduct_count() {
        return product_count;
    }

    public void setProduct_count(int product_count) {
        this.product_count = product_count;
    }

    public String getVariant_string() {
        return variant_string;
    }

    public void setVariant_string(String variant_string) {
        this.variant_string = variant_string;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public int getPack_size_id() {
        return pack_size_id;
    }

    public void setPack_size_id(int pack_size_id) {
        this.pack_size_id = pack_size_id;
    }

    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getSupplier_product_code() {
        return supplier_product_code;
    }

    public void setSupplier_product_code(String supplier_product_code) {
        this.supplier_product_code = supplier_product_code;
    }

    public String getTag_list() {
        return tag_list;
    }

    public void setTag_list(String tag_list) {
        this.tag_list = tag_list;
    }
}
