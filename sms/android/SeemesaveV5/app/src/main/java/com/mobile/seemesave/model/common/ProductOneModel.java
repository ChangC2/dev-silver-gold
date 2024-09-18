package com.mobile.seemesave.model.common;

import java.util.ArrayList;

public class ProductOneModel {
    private int id;
    private String imageUrl;
    private String title;
    private String description;
    private String barcode;
    private CurrencyModel currency;
    private String price;
    private int product_id;
    private int stock;
    private boolean isLike;
    private String feed_type = "";
    private String pack_ize;
    private String unit;
    private String product_type;
    private int store_id = 0;
    private int count = 1;
    private boolean isCart = false;
    private String variant_string = "";
    private boolean hasVariant = false;
    private int oneProductId = -1;
    private ArrayList<ProductModel> comboDeals = new ArrayList<>();
    private ArrayList<ProductModel> buyList = new ArrayList<>();
    private ArrayList<ProductModel> getList = new ArrayList<>();
    private int parent_index = -1;
    public ProductOneModel(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CurrencyModel getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyModel currency) {
        this.currency = currency;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public String getFeed_type() {
        return feed_type;
    }

    public void setFeed_type(String feed_type) {
        this.feed_type = feed_type;
    }

    public String getPack_ize() {
        return pack_ize;
    }

    public void setPack_ize(String pack_ize) {
        this.pack_ize = pack_ize;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public boolean isCart() {
        return isCart;
    }

    public void setCart(boolean cart) {
        isCart = cart;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getVariant_string() {
        return variant_string;
    }

    public void setVariant_string(String variant_string) {
        this.variant_string = variant_string;
    }

    public ArrayList<ProductModel> getComboDeals() {
        return comboDeals;
    }

    public void setComboDeals(ArrayList<ProductModel> comboDeals) {
        this.comboDeals = comboDeals;
    }

    public ArrayList<ProductModel> getBuyList() {
        return buyList;
    }

    public void setBuyList(ArrayList<ProductModel> buyList) {
        this.buyList = buyList;
    }

    public ArrayList<ProductModel> getGetList() {
        return getList;
    }

    public void setGetList(ArrayList<ProductModel> getList) {
        this.getList = getList;
    }

    public int getParent_index() {
        return parent_index;
    }

    public void setParent_index(int parent_index) {
        this.parent_index = parent_index;
    }

    public boolean isHasVariant() {
        return hasVariant;
    }

    public void setHasVariant(boolean hasVariant) {
        this.hasVariant = hasVariant;
    }

    public int getOneProductId() {
        return oneProductId;
    }

    public void setOneProductId(int oneProductId) {
        this.oneProductId = oneProductId;
    }
}
