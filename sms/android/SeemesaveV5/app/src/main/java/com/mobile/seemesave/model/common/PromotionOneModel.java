package com.mobile.seemesave.model.common;

import java.util.ArrayList;

public class PromotionOneModel {
    private int id;
    private String feed_type;
    private boolean is_click_collect;
    private boolean is_click_deliver;
    private String title;
    private String description;
    private ArrayList<StoreModel> Stores;

    private ArrayList<ProductCategoryModel> categories;
    private ArrayList<ProductOneModel> productList;
    private ArrayList<ProductOneModel> comboProductList;
    private ArrayList<ProductOneModel> buyGetProductList;
    private int singleProductCount = 0;

    public PromotionOneModel() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFeed_type() {
        return feed_type;
    }

    public void setFeed_type(String feed_type) {
        this.feed_type = feed_type;
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

    public ArrayList<StoreModel> getStores() {
        return Stores;
    }

    public void setStores(ArrayList<StoreModel> stores) {
        Stores = stores;
    }

    public ArrayList<ProductOneModel> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<ProductOneModel> productList) {
        this.productList = productList;
    }

    public int getSingleProductCount() {
        return singleProductCount;
    }

    public void setSingleProductCount(int singleProductCount) {
        this.singleProductCount = singleProductCount;
    }

    public ArrayList<ProductOneModel> getComboProductList() {
        return comboProductList;
    }

    public void setComboProductList(ArrayList<ProductOneModel> comboProductList) {
        this.comboProductList = comboProductList;
    }

    public ArrayList<ProductOneModel> getBuyGetProductList() {
        return buyGetProductList;
    }

    public void setBuyGetProductList(ArrayList<ProductOneModel> buyGetProductList) {
        this.buyGetProductList = buyGetProductList;
    }

    public ArrayList<ProductCategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<ProductCategoryModel> categories) {
        this.categories = categories;
    }
}
