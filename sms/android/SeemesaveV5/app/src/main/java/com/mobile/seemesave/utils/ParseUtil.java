package com.mobile.seemesave.utils;

import com.google.gson.reflect.TypeToken;
import com.mobile.seemesave.model.common.CurrencyModel;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.model.common.PostModel;
import com.mobile.seemesave.model.common.ProductCategoryModel;
import com.mobile.seemesave.model.common.ProductModel;
import com.mobile.seemesave.model.common.ProductOneModel;
import com.mobile.seemesave.model.common.PromotionModel;
import com.mobile.seemesave.model.common.PromotionOneModel;
import com.mobile.seemesave.model.common.RealDetailsModel;
import com.mobile.seemesave.model.common.SingleProductModel;
import com.mobile.seemesave.model.common.StoreModel;
import com.mobile.seemesave.model.res.DeliverCartInfoRes;
import com.mobile.seemesave.model.res.ShoppingCartInfoRes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ParseUtil {
    public static int parseOptInt(JSONObject object, String key) {
        int value = 0;
        try{
            value = object.getInt(key);
        }catch (Exception e){
            value = 0;
        }
        return value;
    }
    public static String parseOptString(JSONObject object, String key) {
        String value = "";
        try{
            value = object.getString(key);
        }catch (Exception e){
            value = "";
        }
        return value;
    }
    public static boolean parseOptBoolean(JSONObject object, String key) {
        boolean value = false;
        try{
            value = object.getBoolean(key);
        }catch (Exception e){
            value = false;
        }
        return value;
    }

    public static HashMap<String, ArrayList<PromotionOneModel>> parsePromotion(JSONArray jsonArray) {
        ArrayList<PromotionOneModel> collect_list = new ArrayList<>();
        collect_list.clear();
        ArrayList<PromotionOneModel> deliver_list = new ArrayList<>();
        deliver_list.clear();
        try {
            for (int i = 0; i < jsonArray.length(); i ++) {
                JSONObject oneObj =jsonArray.getJSONObject(i);
                PromotionOneModel promotionOneModel = new PromotionOneModel();
                promotionOneModel.setId(oneObj.optInt("id"));
                promotionOneModel.setFeed_type(oneObj.optString("feed_type"));
                promotionOneModel.setIs_click_collect(oneObj.optBoolean("is_click_collect"));
                promotionOneModel.setIs_click_deliver(oneObj.optBoolean("is_click_deliver"));
                promotionOneModel.setTitle(oneObj.optString("title"));
                promotionOneModel.setDescription(oneObj.optString("description"));
                promotionOneModel.setStores(GsonUtils.getInstance().fromJson(oneObj.optJSONArray("Stores").toString(), new TypeToken<ArrayList< StoreModel >>() {
                }.getType()));

                int singleCount = oneObj.optJSONArray("SingleProducts").length();
                promotionOneModel.setSingleProductCount(singleCount);
                int comboCount = oneObj.optJSONArray("ComboDeals").length();
                int buyCount = oneObj.optJSONArray("Buy1Get1FreeDeals").length();
                int stock = 0;
                CurrencyModel currencyModel = new CurrencyModel();
                String price = "";
                String logo = "";
                int product_id = -1;
                int one_product_id = -1;
                String description = "";
                String packSize = "";
                String barcode = "";
                String unit = "";
                String title = "";
                boolean isLike = false;
                boolean has_variant = false;
                String product_type = "";
                String variant_string = "";
                String feed_type = oneObj.optString("feed_type");
                int store_id = promotionOneModel.getStores().get(0).getId();
                promotionOneModel.setSingleProductCount(singleCount);
                ArrayList<ProductOneModel> productList = new ArrayList<>();
                productList.clear();
                if (singleCount > 0) {
                    product_type = "SingleProduct";
                    for (int j = 0; j <oneObj.optJSONArray("SingleProducts").length(); j ++) {
                        JSONObject singleJson = oneObj.optJSONArray("SingleProducts").getJSONObject(j);
                        price = parseOptString(singleJson, "selling_price");
                        stock = parseOptInt(singleJson, "stock");
                        product_id = singleJson.getInt("id");
                        currencyModel = GsonUtils.getInstance().fromJson(singleJson.optJSONObject("Currency").toString(), CurrencyModel.class);
                        logo = singleJson.optJSONObject("Product").optJSONObject("ProductDetail").optString("media");
                        packSize = singleJson.optJSONObject("Product").optJSONObject("ProductDetail").optString("PackSize");
                        barcode = singleJson.optJSONObject("Product").optJSONObject("ProductDetail").optString("barcode");
                        unit = singleJson.optJSONObject("Product").optJSONObject("ProductDetail").optString("Unit");
                        description = singleJson.optJSONObject("Product").optJSONObject("ProductDetail").optString("description");
                        title = singleJson.optJSONObject("Product").optJSONObject("ProductDetail").optString("Brand");
                        isLike = singleJson.optJSONObject("Product").optJSONObject("ProductDetail").optBoolean("isLike");
                        has_variant = singleJson.optJSONObject("Product").getBoolean("hasVariants");
                        one_product_id = singleJson.optJSONObject("Product").getInt("id");
                        ProductOneModel tmpProduct = new ProductOneModel();
                        tmpProduct.setImageUrl(logo);
                        tmpProduct.setCurrency(currencyModel);
                        tmpProduct.setPrice(price);
                        tmpProduct.setStock(stock);
                        tmpProduct.setTitle(title);
                        tmpProduct.setDescription(description);
                        tmpProduct.setPack_ize(packSize);
                        tmpProduct.setUnit(unit);
                        tmpProduct.setProduct_id(product_id);
                        tmpProduct.setLike(isLike);
                        tmpProduct.setProduct_type(product_type);
                        tmpProduct.setStore_id(store_id);
                        tmpProduct.setParent_index(i);
                        tmpProduct.setBarcode(barcode);
                        tmpProduct.setFeed_type(feed_type);
                        tmpProduct.setOneProductId(one_product_id);
                        tmpProduct.setHasVariant(has_variant);
                        productList.add(tmpProduct);
                    }
                }
                if (comboCount > 0) {
                    product_type = "ComboDeal";
                    for (int j = 0; j < oneObj.optJSONArray("ComboDeals").length(); j ++) {
                        JSONObject comboJson = oneObj.optJSONArray("ComboDeals").getJSONObject(j);
                        price = parseOptString(comboJson, "selling_price");
                        stock = parseOptInt(comboJson, "stock");
                        product_id = comboJson.getInt("id");
                        currencyModel = GsonUtils.getInstance().fromJson(comboJson.optJSONObject("Currency").toString(), CurrencyModel.class);
                        logo = comboJson.optJSONArray("Products").getJSONObject(0).optJSONObject("ProductDetail").optString("media");
                        packSize = comboJson.optJSONArray("Products").getJSONObject(0).optJSONObject("ProductDetail").optString("PackSize");
                        barcode = comboJson.optJSONArray("Products").getJSONObject(0).optJSONObject("ProductDetail").optString("barcode");
                        unit = comboJson.optJSONArray("Products").getJSONObject(0).optJSONObject("ProductDetail").optString("Unit");
                        description = comboJson.optJSONArray("Products").getJSONObject(0).optJSONObject("ProductDetail").optString("description");
                        title = comboJson.optJSONArray("Products").getJSONObject(0).optJSONObject("ProductDetail").optString("Brand");
                        isLike = comboJson.optJSONArray("Products").getJSONObject(0).optJSONObject("ProductDetail").optBoolean("isLike");
                        one_product_id = comboJson.optJSONArray("Products").getJSONObject(0).getInt("id");
                        for (int jj = 0; jj < comboJson.optJSONArray("Products").length(); jj ++) {
                            if (comboJson.optJSONArray("Products").getJSONObject(jj).getBoolean("hasVariants")) {
                                has_variant = true;
                                break;
                            }
                        }


                        ProductOneModel tmpProduct = new ProductOneModel();
                        tmpProduct.setImageUrl(logo);
                        tmpProduct.setCurrency(currencyModel);
                        tmpProduct.setPrice(price);
                        tmpProduct.setStock(stock);
                        tmpProduct.setTitle(title);
                        tmpProduct.setDescription(description);
                        tmpProduct.setPack_ize(packSize);
                        tmpProduct.setUnit(unit);
                        tmpProduct.setProduct_id(product_id);
                        tmpProduct.setLike(isLike);
                        tmpProduct.setProduct_type(product_type);
                        tmpProduct.setStore_id(store_id);
                        tmpProduct.setParent_index(i);
                        tmpProduct.setFeed_type(feed_type);
                        tmpProduct.setBarcode(barcode);
                        tmpProduct.setOneProductId(one_product_id);
                        tmpProduct.setHasVariant(has_variant);
                        ArrayList<ProductModel> cTmpList = new ArrayList<>();
                        cTmpList.clear();
                        for (int k = 0; k < comboJson.optJSONArray("Products").length(); k++) {
                            ProductModel one = GsonUtils.getInstance().fromJson(comboJson.optJSONArray("Products").getJSONObject(k).toString(), ProductModel.class);
                            cTmpList.add(one);
                        }
                        tmpProduct.setComboDeals(cTmpList);
                        productList.add(tmpProduct);
                    }
                }
                if (buyCount > 0) {
                    product_type = "Buy1Get1FreeDeal";
                    for (int j = 0; j < oneObj.optJSONArray("Buy1Get1FreeDeals").length(); j ++) {
                        JSONObject buyJson = oneObj.optJSONArray("Buy1Get1FreeDeals").getJSONObject(j);
                        price = parseOptString(buyJson, "selling_price");
                        stock = parseOptInt(buyJson, "stock");
                        product_id = buyJson.getInt("id");
                        ArrayList<ProductModel> bTmpList = new ArrayList<>();
                        bTmpList.clear();
                        ArrayList<ProductModel> gTmpList = new ArrayList<>();
                        gTmpList.clear();

                        currencyModel = GsonUtils.getInstance().fromJson(buyJson.optJSONObject("Currency").toString(), CurrencyModel.class);
                        if (buyJson.optJSONArray("BuyProducts").length() > 0) {
                            logo = buyJson.optJSONArray("BuyProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("media");
                            packSize = buyJson.optJSONArray("BuyProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("PackSize");
                            unit = buyJson.optJSONArray("BuyProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("Unit");
                            barcode = buyJson.optJSONArray("BuyProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("barcode");
                            description = buyJson.optJSONArray("BuyProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("description");
                            title = buyJson.optJSONArray("BuyProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("Brand");
                            isLike = buyJson.optJSONArray("BuyProducts").getJSONObject(0).optJSONObject("ProductDetail").optBoolean("isLike");

                            one_product_id = buyJson.optJSONArray("BuyProducts").getJSONObject(0).getInt("id");

                            for (int jj = 0; jj < buyJson.optJSONArray("BuyProducts").length(); jj ++) {
                                if (buyJson.optJSONArray("BuyProducts").getJSONObject(jj).getBoolean("hasVariants")) {
                                    has_variant = true;
                                    break;
                                }
                            }

                            for (int k = 0; k < buyJson.optJSONArray("BuyProducts").length(); k++) {
                                ProductModel one = GsonUtils.getInstance().fromJson(buyJson.optJSONArray("BuyProducts").getJSONObject(k).toString(), ProductModel.class);
                                bTmpList.add(one);
                            }
                            variant_string = String.format("%s:%s", buyJson.optJSONArray("BuyProducts").getJSONObject(0).optInt("id"), barcode);
                            for (int k = 1; k < buyJson.optJSONArray("BuyProducts").length(); k++) {
                                variant_string += "," + String.format("%s:%s", buyJson.optJSONArray("BuyProducts").getJSONObject(k).optInt("id"), buyJson.optJSONArray("BuyProducts").getJSONObject(k).optJSONObject("ProductDetail").optString("barcode"));
                            }
                        } else {
                            if (buyJson.optJSONArray("FreeProducts").length() > 0) {
                                logo = buyJson.optJSONArray("FreeProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("media");
                                packSize = buyJson.optJSONArray("FreeProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("PackSize");
                                unit = buyJson.optJSONArray("FreeProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("Unit");
                                barcode = buyJson.optJSONArray("FreeProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("barcode");
                                description = buyJson.optJSONArray("FreeProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("description");
                                title = buyJson.optJSONArray("FreeProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("Brand");
                                isLike = buyJson.optJSONArray("FreeProducts").getJSONObject(0).optJSONObject("ProductDetail").optBoolean("isLike");
                                one_product_id = buyJson.optJSONArray("FreeProducts").getJSONObject(0).getInt("id");
                                for (int jj = 0; jj < buyJson.optJSONArray("FreeProducts").length(); jj ++) {
                                    if (buyJson.optJSONArray("FreeProducts").getJSONObject(jj).getBoolean("hasVariants")) {
                                        has_variant = true;
                                        break;
                                    }
                                }
                                for (int k = 0; k < buyJson.optJSONArray("FreeProducts").length(); k++) {
                                    ProductModel one = GsonUtils.getInstance().fromJson(buyJson.optJSONArray("FreeProducts").getJSONObject(k).toString(), ProductModel.class);
                                    bTmpList.add(one);
                                }
                            } else {
                                continue;
                            }
                        }
                        ProductOneModel tmpProduct = new ProductOneModel();
                        tmpProduct.setImageUrl(logo);
                        tmpProduct.setCurrency(currencyModel);
                        tmpProduct.setPrice(price);
                        tmpProduct.setStock(stock);
                        tmpProduct.setTitle(title);
                        tmpProduct.setDescription(description);
                        tmpProduct.setPack_ize(packSize);
                        tmpProduct.setUnit(unit);
                        tmpProduct.setProduct_id(product_id);
                        tmpProduct.setLike(isLike);
                        tmpProduct.setProduct_type(product_type);
                        tmpProduct.setStore_id(store_id);
                        tmpProduct.setParent_index(i);
                        tmpProduct.setFeed_type(feed_type);
                        tmpProduct.setBarcode(barcode);
                        tmpProduct.setVariant_string(variant_string);
                        tmpProduct.setBuyList(bTmpList);
                        tmpProduct.setGetList(gTmpList);
                        tmpProduct.setOneProductId(one_product_id);
                        tmpProduct.setHasVariant(has_variant);
                        productList.add(tmpProduct);
                    }
                }
                promotionOneModel.setProductList(productList);
//                if (promotionOneModel.getFeed_type().equalsIgnoreCase("ClickCollectDeal")) {
//                    collect_list.add(promotionOneModel);
//                } else if (promotionOneModel.getFeed_type().equalsIgnoreCase("ClickDeliverDeal")){
//                    deliver_list.add(promotionOneModel);
//                }
                if (promotionOneModel.isIs_click_collect()) {
                    collect_list.add(promotionOneModel);
                } else if (promotionOneModel.isIs_click_deliver()){
                    deliver_list.add(promotionOneModel);
                }

            }
        } catch (JSONException e) {

        }
        HashMap<String, ArrayList<PromotionOneModel>> result = new HashMap<String, ArrayList<PromotionOneModel>>();
        result.put("collect", collect_list);
        result.put("deliver", deliver_list);
        return result;
    }
    public static PromotionOneModel parseOnePromotion(PromotionModel model) {
        PromotionOneModel promotionOneModel = new PromotionOneModel();
        promotionOneModel.setId(model.getId());
        promotionOneModel.setFeed_type(model.getFeed_type());
        promotionOneModel.setIs_click_collect(model.isIs_click_collect());
        promotionOneModel.setIs_click_deliver(model.isIs_click_deliver());
        promotionOneModel.setTitle(model.getTitle());
        promotionOneModel.setDescription(model.getDescription());
        promotionOneModel.setStores(model.getStores());


        int singleCount = model.getSingleProducts().size();
        int comboCount = model.getComboDeals().size();
        int buyCount = model.getBuy1Get1FreeDeals().size();
        int stock = 0;
        CurrencyModel currencyModel = new CurrencyModel();
        String price = "";
        String logo = "";
        int product_id = -1;
        int one_product_id = -1;
        String description = "";
        String packSize = "";
        String unit = "";
        String title = "";
        boolean isLike = false;
        boolean has_variant = false;
        String product_type = "";
        String variant_string = "";
        String barcode = "";
        String feed_type = model.getFeed_type();
        int store_id = promotionOneModel.getStores().get(0).getId();

        ArrayList<ProductOneModel> productList = new ArrayList<>();
        ArrayList<ProductOneModel> comboProductList = new ArrayList<>();
        ArrayList<ProductOneModel> buyGetProductList = new ArrayList<>();
        productList.clear();
        comboProductList.clear();
        buyGetProductList.clear();
//        if (singleCount > 0) {
//            product_type = "SingleProduct";
//            for (int i = 0; i <singleCount; i ++) {
//                price = model.getSingleProducts().get(i).getSelling_price();
//                stock = model.getSingleProducts().get(i).getStock();
//                product_id = model.getSingleProducts().get(i).getId();
//                currencyModel = model.getSingleProducts().get(i).getCurrency();
//                logo = model.getSingleProducts().get(i).getProduct().getProductDetail().getMedia();
//                packSize = model.getSingleProducts().get(i).getProduct().getProductDetail().getPackSize();
//                unit = model.getSingleProducts().get(i).getProduct().getProductDetail().getUnit();
//                barcode = model.getSingleProducts().get(i).getProduct().getProductDetail().getBarcode();
//                description = model.getSingleProducts().get(i).getProduct().getProductDetail().getDescription();
//                title = model.getSingleProducts().get(i).getProduct().getProductDetail().getBrand();
//                isLike = model.getSingleProducts().get(i).getProduct().getProductDetail().isLike();
//                has_variant = model.getSingleProducts().get(i).getProduct().isHasVariants();
//                one_product_id = model.getSingleProducts().get(i).getProduct().getId();
//
//                ProductOneModel tmpProduct = new ProductOneModel();
//                tmpProduct.setImageUrl(logo);
//                tmpProduct.setCurrency(currencyModel);
//                tmpProduct.setPrice(price);
//                tmpProduct.setStock(stock);
//                tmpProduct.setTitle(title);
//                tmpProduct.setDescription(description);
//                tmpProduct.setPack_ize(packSize);
//                tmpProduct.setUnit(unit);
//                tmpProduct.setProduct_id(product_id);
//                tmpProduct.setLike(isLike);
//                tmpProduct.setBarcode(barcode);
//                tmpProduct.setFeed_type(feed_type);
//                tmpProduct.setProduct_type(product_type);
//                tmpProduct.setStore_id(store_id);
//                tmpProduct.setOneProductId(one_product_id);
//                tmpProduct.setHasVariant(has_variant);
//                productList.add(tmpProduct);
//            }
//        }

        ArrayList<ProductCategoryModel> arrList = new ArrayList<>();
        arrList.clear();
        for (int ii = 0; ii < model.getCategories().size(); ii++) {
            ProductCategoryModel productCategoryModel = new ProductCategoryModel();
            if (ii == 0) {
                productCategoryModel.setCheck(true);
            }
            productCategoryModel.setProducts(model.getCategories().get(ii).getProducts());
            productCategoryModel.setId(model.getCategories().get(ii).getId());
            productCategoryModel.setTitle(model.getCategories().get(ii).getTitle());
            ArrayList<ProductOneModel> singleProductList = new ArrayList<>();
            singleProductList.clear();
            for (int i = 0; i <model.getCategories().get(ii).getProducts().size(); i ++) {
                price = model.getCategories().get(ii).getProducts().get(i).getSelling_price();
                stock = model.getCategories().get(ii).getProducts().get(i).getStock();
                product_id = model.getCategories().get(ii).getProducts().get(i).getId();
                currencyModel = model.getCategories().get(ii).getProducts().get(i).getCurrency();
                logo = model.getCategories().get(ii).getProducts().get(i).getProduct().getProductDetail().getMedia();
                packSize = model.getCategories().get(ii).getProducts().get(i).getProduct().getProductDetail().getPackSize();
                unit = model.getCategories().get(ii).getProducts().get(i).getProduct().getProductDetail().getUnit();
                barcode = model.getCategories().get(ii).getProducts().get(i).getProduct().getProductDetail().getBarcode();
                description = model.getCategories().get(ii).getProducts().get(i).getProduct().getProductDetail().getDescription();
                title = model.getCategories().get(ii).getProducts().get(i).getProduct().getProductDetail().getBrand();
                isLike = model.getCategories().get(ii).getProducts().get(i).getProduct().getProductDetail().isLike();
                has_variant = model.getCategories().get(ii).getProducts().get(i).getProduct().isHasVariants();
                one_product_id = model.getCategories().get(ii).getProducts().get(i).getProduct().getId();

                ProductOneModel tmpProduct = new ProductOneModel();
                tmpProduct.setImageUrl(logo);
                tmpProduct.setCurrency(currencyModel);
                tmpProduct.setPrice(price);
                tmpProduct.setStock(stock);
                tmpProduct.setTitle(title);
                tmpProduct.setDescription(description);
                tmpProduct.setPack_ize(packSize);
                tmpProduct.setUnit(unit);
                tmpProduct.setProduct_id(product_id);
                tmpProduct.setLike(isLike);
                tmpProduct.setBarcode(barcode);
                tmpProduct.setFeed_type(feed_type);
                tmpProduct.setProduct_type("SingleProduct");
                tmpProduct.setStore_id(store_id);
                tmpProduct.setOneProductId(one_product_id);
                tmpProduct.setHasVariant(has_variant);
                singleProductList.add(tmpProduct);
            }
            productCategoryModel.setProductList(singleProductList);
            arrList.add(productCategoryModel);
        }
        promotionOneModel.setCategories(arrList);
        if (comboCount > 0) {
            product_type = "ComboDeal";
            for (int i = 0; i < comboCount; i ++) {
                if (model.getComboDeals().get(i).getProducts().size() > 0) {
                    price = model.getComboDeals().get(i).getSelling_price();
                    stock = model.getComboDeals().get(i).getStock();
                    product_id = model.getComboDeals().get(i).getId();
                    currencyModel = model.getComboDeals().get(i).getCurrency();
                    logo = model.getComboDeals().get(i).getProducts().get(0).getProductDetail().getMedia();
                    packSize = model.getComboDeals().get(i).getProducts().get(0).getProductDetail().getPackSize();
                    unit = model.getComboDeals().get(i).getProducts().get(0).getProductDetail().getUnit();
                    barcode = model.getComboDeals().get(i).getProducts().get(0).getProductDetail().getBarcode();
                    description = model.getComboDeals().get(i).getProducts().get(0).getProductDetail().getDescription();
                    title = model.getComboDeals().get(i).getProducts().get(0).getProductDetail().getBrand();
                    isLike = model.getComboDeals().get(i).getProducts().get(0).getProductDetail().isLike();
                    one_product_id = model.getComboDeals().get(i).getProducts().get(0).getId();

                    for (int jj = 0; jj < model.getComboDeals().get(i).getProducts().size(); jj ++) {
                        if (model.getComboDeals().get(i).getProducts().get(jj).isHasVariants()) {
                            has_variant = true;
                            break;
                        }
                    }

                    ProductOneModel tmpProduct = new ProductOneModel();
                    tmpProduct.setImageUrl(logo);
                    tmpProduct.setCurrency(currencyModel);
                    tmpProduct.setPrice(price);
                    tmpProduct.setStock(stock);
                    tmpProduct.setTitle(title);
                    tmpProduct.setDescription(description);
                    tmpProduct.setPack_ize(packSize);
                    tmpProduct.setUnit(unit);
                    tmpProduct.setProduct_id(product_id);
                    tmpProduct.setLike(isLike);
                    tmpProduct.setProduct_type(product_type);
                    tmpProduct.setBarcode(barcode);
                    tmpProduct.setFeed_type(feed_type);
                    tmpProduct.setComboDeals(model.getComboDeals().get(i).getProducts());
                    tmpProduct.setStore_id(store_id);
                    tmpProduct.setOneProductId(one_product_id);
                    tmpProduct.setHasVariant(has_variant);
                    comboProductList.add(tmpProduct);
                }
            }
        }
        if (buyCount > 0) {
            product_type = "Buy1Get1FreeDeal";
            for (int i = 0; i < buyCount; i ++) {
                price = model.getBuy1Get1FreeDeals().get(i).getSelling_price();
                stock = model.getBuy1Get1FreeDeals().get(i).getStock();
                product_id = model.getBuy1Get1FreeDeals().get(i).getId();
                currencyModel = model.getBuy1Get1FreeDeals().get(i).getCurrency();
                if (model.getBuy1Get1FreeDeals().get(i).getBuyProducts().size() > 0) {
                    logo = model.getBuy1Get1FreeDeals().get(i).getBuyProducts().get(0).getProductDetail().getMedia();
                    packSize = model.getBuy1Get1FreeDeals().get(i).getBuyProducts().get(0).getProductDetail().getPackSize();
                    unit = model.getBuy1Get1FreeDeals().get(i).getBuyProducts().get(0).getProductDetail().getUnit();
                    barcode = model.getBuy1Get1FreeDeals().get(i).getBuyProducts().get(0).getProductDetail().getBarcode();
                    description = model.getBuy1Get1FreeDeals().get(i).getBuyProducts().get(0).getProductDetail().getDescription();
                    title = model.getBuy1Get1FreeDeals().get(i).getBuyProducts().get(0).getProductDetail().getBrand();
                    isLike = model.getBuy1Get1FreeDeals().get(i).getBuyProducts().get(0).getProductDetail().isLike();
                    one_product_id = model.getBuy1Get1FreeDeals().get(i).getBuyProducts().get(0).getId();

                    for (int jj = 0; jj < model.getBuy1Get1FreeDeals().get(i).getBuyProducts().size(); jj ++) {
                        if (model.getBuy1Get1FreeDeals().get(i).getBuyProducts().get(jj).isHasVariants()) {
                            has_variant = true;
                            break;
                        }
                    }


                    variant_string = String.format("%s:%s", model.getBuy1Get1FreeDeals().get(i).getBuyProducts().get(0).getId(), barcode);
                    for (int k = 1; k < model.getBuy1Get1FreeDeals().get(i).getBuyProducts().size(); k++) {
                        variant_string += "," + String.format("%s:%s", model.getBuy1Get1FreeDeals().get(i).getBuyProducts().get(k).getId(), model.getBuy1Get1FreeDeals().get(i).getBuyProducts().get(k).getProductDetail().getBarcode());
                    }
                } else {
                    if (model.getBuy1Get1FreeDeals().get(i).getFreeProducts().size() > 0) {
                        logo = model.getBuy1Get1FreeDeals().get(i).getFreeProducts().get(0).getProductDetail().getMedia();
                        packSize = model.getBuy1Get1FreeDeals().get(i).getFreeProducts().get(0).getProductDetail().getPackSize();
                        unit = model.getBuy1Get1FreeDeals().get(i).getFreeProducts().get(0).getProductDetail().getUnit();
                        description = model.getBuy1Get1FreeDeals().get(i).getFreeProducts().get(0).getProductDetail().getDescription();
                        title = model.getBuy1Get1FreeDeals().get(i).getFreeProducts().get(0).getProductDetail().getBrand();
                        isLike = model.getBuy1Get1FreeDeals().get(i).getFreeProducts().get(0).getProductDetail().isLike();
                        for (int jj = 0; jj < model.getBuy1Get1FreeDeals().get(i).getFreeProducts().size(); jj ++) {
                            if (model.getBuy1Get1FreeDeals().get(i).getFreeProducts().get(jj).isHasVariants()) {
                                has_variant = true;
                                break;
                            }
                        }
                        one_product_id = model.getBuy1Get1FreeDeals().get(i).getFreeProducts().get(0).getId();
                        variant_string = String.format("%s:%s", model.getBuy1Get1FreeDeals().get(i).getFreeProducts().get(0).getId(), barcode);
                        for (int k = 1; k < model.getBuy1Get1FreeDeals().get(i).getFreeProducts().size(); k++) {
                            variant_string += "," + String.format("%s:%s", model.getBuy1Get1FreeDeals().get(i).getFreeProducts().get(k).getId(), model.getBuy1Get1FreeDeals().get(i).getFreeProducts().get(k).getProductDetail().getBarcode());
                        }
                    } else {
                        continue;
                    }
                }

                ProductOneModel tmpProduct = new ProductOneModel();
                tmpProduct.setImageUrl(logo);
                tmpProduct.setCurrency(currencyModel);
                tmpProduct.setPrice(price);
                tmpProduct.setStock(stock);
                tmpProduct.setTitle(title);
                tmpProduct.setDescription(description);
                tmpProduct.setPack_ize(packSize);
                tmpProduct.setUnit(unit);
                tmpProduct.setFeed_type(feed_type);
                tmpProduct.setProduct_id(product_id);
                tmpProduct.setLike(isLike);
                tmpProduct.setBarcode(barcode);
                tmpProduct.setFeed_type(feed_type);
                tmpProduct.setVariant_string(variant_string);
                tmpProduct.setProduct_type(product_type);
                tmpProduct.setStore_id(store_id);
                tmpProduct.setOneProductId(one_product_id);
                tmpProduct.setHasVariant(has_variant);
                tmpProduct.setBuyList(model.getBuy1Get1FreeDeals().get(i).getBuyProducts());
                tmpProduct.setGetList(model.getBuy1Get1FreeDeals().get(i).getFreeProducts());
                buyGetProductList.add(tmpProduct);
            }
        }
        promotionOneModel.setSingleProductCount(singleCount);
        promotionOneModel.setProductList(productList);
        promotionOneModel.setComboProductList(comboProductList);
        promotionOneModel.setBuyGetProductList(buyGetProductList);
        return promotionOneModel;
    }
    public static ArrayList<ProductOneModel> parseSingleProduct(ArrayList<SingleProductModel> pList, String feedType, int storeId) {
        ArrayList<ProductOneModel> productList = new ArrayList<>();
        productList.clear();
        int stock = 0;
        CurrencyModel currencyModel = new CurrencyModel();
        String price = "";
        String logo = "";
        int product_id = -1;
        String description = "";
        String packSize = "";
        String unit = "";
        String title = "";
        boolean isLike = false;
        boolean has_variant = false;
        int one_product_id = -1;
        String product_type = "";
        String variant_string = "";
        String barcode = "";


        if (pList.size() > 0) {
            product_type = "SingleProduct";
            for (int i = 0; i <pList.size(); i ++) {
                price = pList.get(i).getSelling_price();
                stock = pList.get(i).getStock();
                product_id = pList.get(i).getId();
                currencyModel = pList.get(i).getCurrency();
                logo = pList.get(i).getProduct().getProductDetail().getMedia();
                packSize = pList.get(i).getProduct().getProductDetail().getPackSize();
                unit = pList.get(i).getProduct().getProductDetail().getUnit();
                barcode = pList.get(i).getProduct().getProductDetail().getBarcode();
                description = pList.get(i).getProduct().getProductDetail().getDescription();
                title = pList.get(i).getProduct().getProductDetail().getBrand();
                isLike = pList.get(i).getProduct().getProductDetail().isLike();
                has_variant = pList.get(i).getProduct().isHasVariants();
                one_product_id = pList.get(i).getProduct().getId();


                ProductOneModel tmpProduct = new ProductOneModel();
                tmpProduct.setImageUrl(logo);
                tmpProduct.setCurrency(currencyModel);
                tmpProduct.setPrice(price);
                tmpProduct.setStock(stock);
                tmpProduct.setTitle(title);
                tmpProduct.setDescription(description);
                tmpProduct.setPack_ize(packSize);
                tmpProduct.setUnit(unit);
                tmpProduct.setProduct_id(product_id);
                tmpProduct.setLike(isLike);
                tmpProduct.setBarcode(barcode);
                tmpProduct.setFeed_type(feedType);
                tmpProduct.setProduct_type(product_type);
                tmpProduct.setStore_id(storeId);
                tmpProduct.setOneProductId(one_product_id);
                tmpProduct.setHasVariant(has_variant);
                productList.add(tmpProduct);

            }
        }
        return productList;
    }
    public static ArrayList<ProductOneModel> parseDeliverCartProduct(DeliverCartInfoRes model) {

        int singleCount = model.getSingleProducts().size();
        int comboCount = model.getComboDeals().size();
        int buyCount = model.getBuy1Get1FreeDeals().size();
        int stock = 0;
        CurrencyModel currencyModel = new CurrencyModel();
        String price = "";
        String logo = "";
        int product_id = -1;
        int one_product_id = -1;
        String description = "";
        String packSize = "";
        String unit = "";
        String title = "";
        boolean isLike = false;
        boolean has_variant = false;
        String product_type = "";
        String variant_string = "";
        String barcode = "";
        int product_count = 1;
        int id = 0;
        int store_id = model.getCartInfo().getStore().getId();
        String feed_type = "ClickDeliverDeal";
        ArrayList<ProductOneModel> productList = new ArrayList<>();
        productList.clear();
        if (singleCount > 0) {
            product_type = "SingleProduct";
            for (int i = 0; i <singleCount; i ++) {
                product_count = model.getSingleProducts().get(i).getCount();
                variant_string = model.getSingleProducts().get(i).getVariant_string();
                price = model.getSingleProducts().get(i).getProductDetails().getSelling_price();
                stock = model.getSingleProducts().get(i).getProductDetails().getStock();
                id = model.getSingleProducts().get(i).getId();
                currencyModel = model.getSingleProducts().get(i).getProductDetails().getCurrency();
                product_id = model.getSingleProducts().get(i).getProductDetails().getId();
                logo = model.getSingleProducts().get(i).getRealDetails().getRealProduct().getThumbnail_image();
                packSize = model.getSingleProducts().get(i).getRealDetails().getRealProduct().getPackSize();
                unit = model.getSingleProducts().get(i).getRealDetails().getRealProduct().getUnit();
                description = model.getSingleProducts().get(i).getRealDetails().getRealProduct().getDescription();
                barcode = model.getSingleProducts().get(i).getRealDetails().getRealProduct().getBarcode();
                title = model.getSingleProducts().get(i).getRealDetails().getRealProduct().getBrand();
                isLike = model.getSingleProducts().get(i).getRealDetails().getRealProduct().isLike();
                has_variant = model.getSingleProducts().get(i).getRealDetails().getOrgProduct().isHasVariants();
                one_product_id = model.getSingleProducts().get(i).getRealDetails().getOrgProduct().getId();

                ProductOneModel tmpProduct = new ProductOneModel();
                tmpProduct.setId(id);
                tmpProduct.setImageUrl(logo);
                tmpProduct.setCurrency(currencyModel);
                tmpProduct.setPrice(price);
                tmpProduct.setStock(stock);
                tmpProduct.setTitle(title);
                tmpProduct.setDescription(description);
                tmpProduct.setPack_ize(packSize);
                tmpProduct.setUnit(unit);
                tmpProduct.setProduct_id(product_id);
                tmpProduct.setLike(isLike);
                tmpProduct.setProduct_type(product_type);
                tmpProduct.setStore_id(store_id);
                tmpProduct.setCart(true);
                tmpProduct.setVariant_string(variant_string);
                tmpProduct.setCount(product_count);
                tmpProduct.setBarcode(barcode);
                tmpProduct.setFeed_type(feed_type);
                tmpProduct.setHasVariant(has_variant);
                tmpProduct.setOneProductId(one_product_id);
                productList.add(tmpProduct);
            }
        }
        if (comboCount > 0) {
            product_type = "ComboDeal";
            for (int i = 0; i < comboCount; i ++) {
                product_count = model.getComboDeals().get(i).getCount();
                variant_string = model.getComboDeals().get(i).getVariant_string();
                price = model.getComboDeals().get(i).getProductDetails().getSelling_price();
                stock = model.getComboDeals().get(i).getProductDetails().getStock();
                id = model.getComboDeals().get(i).getId();
                currencyModel = model.getComboDeals().get(i).getProductDetails().getCurrency();
                product_id = model.getComboDeals().get(i).getProductDetails().getId();
                RealDetailsModel realDetailsModel = model.getComboDeals().get(i).getRealDetails().get(0);
                logo = realDetailsModel.getRealProduct().getThumbnail_image();
                packSize = realDetailsModel.getRealProduct().getPackSize();
                unit = realDetailsModel.getRealProduct().getUnit();
                description = realDetailsModel.getRealProduct().getDescription();
                barcode = realDetailsModel.getRealProduct().getBarcode();
                title = realDetailsModel.getRealProduct().getBrand();
                isLike = realDetailsModel.getRealProduct().isLike();
                has_variant = realDetailsModel.getOrgProduct().isHasVariants();
                one_product_id = realDetailsModel.getOrgProduct().getId();

                ProductOneModel tmpProduct = new ProductOneModel();
                tmpProduct.setImageUrl(logo);
                tmpProduct.setCurrency(currencyModel);
                tmpProduct.setPrice(price);
                tmpProduct.setStock(stock);
                tmpProduct.setTitle(title);
                tmpProduct.setDescription(description);
                tmpProduct.setPack_ize(packSize);
                tmpProduct.setUnit(unit);
                tmpProduct.setProduct_id(product_id);
                tmpProduct.setLike(isLike);
                tmpProduct.setProduct_type(product_type);
                tmpProduct.setStore_id(store_id);
                tmpProduct.setCart(true);
                tmpProduct.setId(id);
                tmpProduct.setVariant_string(variant_string);
                tmpProduct.setCount(product_count);
                tmpProduct.setBarcode(barcode);
                tmpProduct.setFeed_type(feed_type);
                tmpProduct.setOneProductId(one_product_id);
                tmpProduct.setHasVariant(has_variant);
                tmpProduct.setComboDeals(model.getComboDeals().get(i).getProductDetails().getProducts());
                productList.add(tmpProduct);

            }
        }
        if (buyCount > 0) {
            product_type = "Buy1Get1FreeDeal";
            for (int i = 0; i < buyCount; i ++) {
                product_count = model.getBuy1Get1FreeDeals().get(i).getCount();
                variant_string = model.getBuy1Get1FreeDeals().get(i).getVariant_string();
                price = model.getBuy1Get1FreeDeals().get(i).getProductDetails().getSelling_price();
                currencyModel = model.getBuy1Get1FreeDeals().get(i).getProductDetails().getCurrency();
                stock = model.getBuy1Get1FreeDeals().get(i).getProductDetails().getStock();
                product_id = model.getBuy1Get1FreeDeals().get(i).getProductDetails().getId();
                id = model.getBuy1Get1FreeDeals().get(i).getId();
                if (model.getBuy1Get1FreeDeals().get(i).getRealBuyDetails().size() > 0) {
                    RealDetailsModel realDetailsModel = model.getBuy1Get1FreeDeals().get(i).getRealBuyDetails().get(0);
                    logo = realDetailsModel.getRealProduct().getThumbnail_image();
                    packSize = realDetailsModel.getRealProduct().getPackSize();
                    unit = realDetailsModel.getRealProduct().getUnit();
                    description = realDetailsModel.getRealProduct().getDescription();
                    barcode = realDetailsModel.getRealProduct().getBarcode();
                    title = realDetailsModel.getRealProduct().getBrand();
                    isLike = realDetailsModel.getRealProduct().isLike();
                    has_variant = realDetailsModel.getOrgProduct().isHasVariants();
                    one_product_id = realDetailsModel.getOrgProduct().getId();

                    ProductOneModel tmpProduct = new ProductOneModel();
                    tmpProduct.setImageUrl(logo);
                    tmpProduct.setCurrency(currencyModel);
                    tmpProduct.setPrice(price);
                    tmpProduct.setStock(stock);
                    tmpProduct.setTitle(title);
                    tmpProduct.setDescription(description);
                    tmpProduct.setPack_ize(packSize);
                    tmpProduct.setUnit(unit);
                    tmpProduct.setProduct_id(product_id);
                    tmpProduct.setLike(isLike);
                    tmpProduct.setProduct_type(product_type);
                    tmpProduct.setStore_id(store_id);
                    tmpProduct.setCart(true);
                    tmpProduct.setId(id);
                    tmpProduct.setOneProductId(one_product_id);
                    tmpProduct.setHasVariant(has_variant);
                    tmpProduct.setVariant_string(variant_string);
                    tmpProduct.setCount(product_count);
                    tmpProduct.setBarcode(barcode);
                    tmpProduct.setFeed_type(feed_type);
                    tmpProduct.setBuyList(model.getBuy1Get1FreeDeals().get(i).getProductDetails().getBuyProducts());
                    tmpProduct.setGetList(model.getBuy1Get1FreeDeals().get(i).getProductDetails().getFreeProducts());
                    productList.add(tmpProduct);
                }
            }
        }

        return productList;
    }
    public static ArrayList<ProductOneModel> parseShoppingCartProduct(ShoppingCartInfoRes model) {

        int singleCount = model.getSingleProducts().size();
        int comboCount = model.getComboDeals().size();
        int buyCount = model.getBuy1Get1FreeDeals().size();
        int stock = 0;
        CurrencyModel currencyModel = new CurrencyModel();
        String price = "";
        String logo = "";
        int product_id = -1;
        int one_product_id = -1;
        String description = "";
        String packSize = "";
        String unit = "";
        String title = "";
        boolean isLike = false;
        boolean has_variant = false;
        String product_type = "";
        String variant_string = "";
        String barcode = "";
        int product_count = 1;
        int id = 0;
        int store_id = model.getCartInfo().getStore().getId();
        String feed_type = "ClickCollectDeal";

        ArrayList<ProductOneModel> productList = new ArrayList<>();
        productList.clear();
        if (singleCount > 0) {
            product_type = "SingleProduct";
            for (int i = 0; i <singleCount; i ++) {
                product_count = model.getSingleProducts().get(i).getCount();
                variant_string = model.getSingleProducts().get(i).getVariant_string();
                price = model.getSingleProducts().get(i).getProductDetails().getSelling_price();
                stock = model.getSingleProducts().get(i).getProductDetails().getStock();
                id = model.getSingleProducts().get(i).getId();
                currencyModel = model.getSingleProducts().get(i).getProductDetails().getCurrency();
                product_id = model.getSingleProducts().get(i).getProductDetails().getId();
                logo = model.getSingleProducts().get(i).getRealDetails().getRealProduct().getThumbnail_image();
                packSize = model.getSingleProducts().get(i).getRealDetails().getRealProduct().getPackSize();
                unit = model.getSingleProducts().get(i).getRealDetails().getRealProduct().getUnit();
                description = model.getSingleProducts().get(i).getRealDetails().getRealProduct().getDescription();
                barcode = model.getSingleProducts().get(i).getRealDetails().getRealProduct().getBarcode();
                title = model.getSingleProducts().get(i).getRealDetails().getRealProduct().getBrand();
                isLike = model.getSingleProducts().get(i).getRealDetails().getRealProduct().isLike();
                has_variant = model.getSingleProducts().get(i).getRealDetails().getOrgProduct().isHasVariants();
                one_product_id = model.getSingleProducts().get(i).getRealDetails().getOrgProduct().getId();

                ProductOneModel tmpProduct = new ProductOneModel();
                tmpProduct.setId(id);
                tmpProduct.setImageUrl(logo);
                tmpProduct.setCurrency(currencyModel);
                tmpProduct.setPrice(price);
                tmpProduct.setStock(stock);
                tmpProduct.setTitle(title);
                tmpProduct.setDescription(description);
                tmpProduct.setPack_ize(packSize);
                tmpProduct.setUnit(unit);
                tmpProduct.setProduct_id(product_id);
                tmpProduct.setLike(isLike);
                tmpProduct.setProduct_type(product_type);
                tmpProduct.setStore_id(store_id);
                tmpProduct.setCart(true);
                tmpProduct.setVariant_string(variant_string);
                tmpProduct.setCount(product_count);
                tmpProduct.setBarcode(barcode);
                tmpProduct.setFeed_type(feed_type);
                tmpProduct.setHasVariant(has_variant);
                tmpProduct.setOneProductId(one_product_id);
                productList.add(tmpProduct);
            }
        }
        if (comboCount > 0) {
            product_type = "ComboDeal";
            for (int i = 0; i < comboCount; i ++) {
                product_count = model.getComboDeals().get(i).getCount();
                price = model.getComboDeals().get(i).getProductDetails().getSelling_price();
                stock = model.getComboDeals().get(i).getProductDetails().getStock();
                variant_string = model.getComboDeals().get(i).getVariant_string();
                id = model.getComboDeals().get(i).getId();
                currencyModel = model.getComboDeals().get(i).getProductDetails().getCurrency();
                product_id = model.getComboDeals().get(i).getProductDetails().getId();
                RealDetailsModel realDetailsModel = model.getComboDeals().get(i).getRealDetails().get(0);
                logo = realDetailsModel.getRealProduct().getThumbnail_image();
                packSize = realDetailsModel.getRealProduct().getPackSize();
                unit = realDetailsModel.getRealProduct().getUnit();
                description = realDetailsModel.getRealProduct().getDescription();
                barcode = realDetailsModel.getRealProduct().getBarcode();
                title = realDetailsModel.getRealProduct().getBrand();
                isLike = realDetailsModel.getRealProduct().isLike();
                has_variant = realDetailsModel.getOrgProduct().isHasVariants();
                one_product_id = realDetailsModel.getOrgProduct().getId();

                ProductOneModel tmpProduct = new ProductOneModel();
                tmpProduct.setImageUrl(logo);
                tmpProduct.setCurrency(currencyModel);
                tmpProduct.setPrice(price);
                tmpProduct.setStock(stock);
                tmpProduct.setTitle(title);
                tmpProduct.setDescription(description);
                tmpProduct.setPack_ize(packSize);
                tmpProduct.setUnit(unit);
                tmpProduct.setProduct_id(product_id);
                tmpProduct.setLike(isLike);
                tmpProduct.setProduct_type(product_type);
                tmpProduct.setStore_id(store_id);
                tmpProduct.setCart(true);
                tmpProduct.setId(id);
                tmpProduct.setVariant_string(variant_string);
                tmpProduct.setCount(product_count);
                tmpProduct.setBarcode(barcode);
                tmpProduct.setFeed_type(feed_type);
                tmpProduct.setOneProductId(one_product_id);
                tmpProduct.setHasVariant(has_variant);
                tmpProduct.setComboDeals(model.getComboDeals().get(i).getProductDetails().getProducts());
                productList.add(tmpProduct);

            }
        }
        if (buyCount > 0) {
            product_type = "Buy1Get1FreeDeal";
            for (int i = 0; i < buyCount; i ++) {
                product_count = model.getBuy1Get1FreeDeals().get(i).getCount();
                price = model.getBuy1Get1FreeDeals().get(i).getProductDetails().getSelling_price();
                stock = model.getBuy1Get1FreeDeals().get(i).getProductDetails().getStock();
                product_id = model.getBuy1Get1FreeDeals().get(i).getProductDetails().getId();
                currencyModel = model.getBuy1Get1FreeDeals().get(i).getProductDetails().getCurrency();
                id = model.getBuy1Get1FreeDeals().get(i).getId();
                variant_string = model.getBuy1Get1FreeDeals().get(i).getVariant_string();
                RealDetailsModel realDetailsModel = model.getBuy1Get1FreeDeals().get(i).getRealBuyDetails().get(0);
                logo = realDetailsModel.getRealProduct().getThumbnail_image();
                packSize = realDetailsModel.getRealProduct().getPackSize();
                unit = realDetailsModel.getRealProduct().getUnit();
                description = realDetailsModel.getRealProduct().getDescription();
                barcode = realDetailsModel.getRealProduct().getBarcode();
                title = realDetailsModel.getRealProduct().getBrand();
                isLike = realDetailsModel.getRealProduct().isLike();
                has_variant = realDetailsModel.getOrgProduct().isHasVariants();
                one_product_id = realDetailsModel.getOrgProduct().getId();

                ProductOneModel tmpProduct = new ProductOneModel();
                tmpProduct.setImageUrl(logo);
                tmpProduct.setCurrency(currencyModel);
                tmpProduct.setPrice(price);
                tmpProduct.setStock(stock);
                tmpProduct.setTitle(title);
                tmpProduct.setDescription(description);
                tmpProduct.setPack_ize(packSize);
                tmpProduct.setUnit(unit);
                tmpProduct.setProduct_id(product_id);
                tmpProduct.setLike(isLike);
                tmpProduct.setProduct_type(product_type);
                tmpProduct.setStore_id(store_id);
                tmpProduct.setCart(true);
                tmpProduct.setId(id);
                tmpProduct.setVariant_string(variant_string);
                tmpProduct.setCount(product_count);
                tmpProduct.setBarcode(barcode);
                tmpProduct.setOneProductId(one_product_id);
                tmpProduct.setHasVariant(has_variant);
                tmpProduct.setFeed_type(feed_type);
                tmpProduct.setBuyList(model.getBuy1Get1FreeDeals().get(i).getProductDetails().getBuyProducts());
                tmpProduct.setGetList(model.getBuy1Get1FreeDeals().get(i).getProductDetails().getFreeProducts());
                productList.add(tmpProduct);
            }
        }

        return productList;
    }
    public static ArrayList<ProductOneModel> parseProduct(JSONArray jsonArray) {
        ArrayList<ProductOneModel> list = new ArrayList<>();
        list.clear();
        try {
            for (int i = 0; i < jsonArray.length(); i ++) {
                JSONObject oneObj =jsonArray.getJSONObject(i);
                ProductOneModel productOneModel = new ProductOneModel();
                productOneModel.setId(oneObj.optInt("id"));
                productOneModel.setLike(oneObj.optBoolean("isLike"));
                productOneModel.setFeed_type(oneObj.optString("feed_type"));
                int singleCount = oneObj.optJSONArray("SingleProducts").length();
                int comboCount = oneObj.optJSONArray("ComboDeals").length();
                int buyCount = oneObj.optJSONArray("Buy1Get1FreeDeals").length();
                int stock = 0;
                CurrencyModel currencyModel = new CurrencyModel();
                String price = "";
                String logo = "";
                int product_id = -1;
                int one_product_id = -1;
                String description = "";
                String packSize = "";
                String unit = "";
                String title = "";
                String product_type = "";
                String barcode = "";
                String variant_string = "";
                boolean has_variant = false;
                ArrayList<ProductModel> cTmpList = new ArrayList<>();
                cTmpList.clear();
                ArrayList<ProductModel> bTmpList = new ArrayList<>();
                bTmpList.clear();
                ArrayList<ProductModel> gTmpList = new ArrayList<>();
                gTmpList.clear();

                int store_id = oneObj.getJSONArray("Stores").getJSONObject(0).getInt("id");

                if (singleCount > 0) {
                    product_type = "SingleProduct";
                    JSONObject singleJson = oneObj.optJSONArray("SingleProducts").getJSONObject(0);
                    price = parseOptString(singleJson, "selling_price");
                    stock = parseOptInt(singleJson, "stock");
                    product_id = singleJson.getInt("id");
                    currencyModel = GsonUtils.getInstance().fromJson(singleJson.optJSONObject("Currency").toString(), CurrencyModel.class);
                    logo = singleJson.optJSONObject("Product").optJSONObject("ProductDetail").optString("media");
                    packSize = singleJson.optJSONObject("Product").optJSONObject("ProductDetail").optString("PackSize");
                    unit = singleJson.optJSONObject("Product").optJSONObject("ProductDetail").optString("Unit");
                    description = singleJson.optJSONObject("Product").optJSONObject("ProductDetail").optString("description");
                    title = singleJson.optJSONObject("Product").optJSONObject("ProductDetail").optString("Brand");
                    barcode = singleJson.optJSONObject("Product").optJSONObject("ProductDetail").optString("barcode");
                    has_variant = singleJson.optJSONObject("Product").getBoolean("hasVariants");
                    one_product_id = singleJson.optJSONObject("Product").getInt("id");
                } else {
                    if (comboCount > 0 ) {
                        product_type = "ComboDeal";
                        JSONObject comboJson = oneObj.optJSONArray("ComboDeals").getJSONObject(0);
                        if (comboJson.optJSONArray("Products").length() > 0) {
                            price = parseOptString(comboJson, "selling_price");
                            stock = parseOptInt(comboJson, "stock");
                            product_id = comboJson.getInt("id");
                            currencyModel = GsonUtils.getInstance().fromJson(comboJson.optJSONObject("Currency").toString(), CurrencyModel.class);
                            logo = comboJson.optJSONArray("Products").getJSONObject(0).optJSONObject("ProductDetail").optString("media");
                            packSize = comboJson.optJSONArray("Products").getJSONObject(0).optJSONObject("ProductDetail").optString("PackSize");
                            unit = comboJson.optJSONArray("Products").getJSONObject(0).optJSONObject("ProductDetail").optString("Unit");
                            description = comboJson.optJSONArray("Products").getJSONObject(0).optJSONObject("ProductDetail").optString("description");
                            title = comboJson.optJSONArray("Products").getJSONObject(0).optJSONObject("ProductDetail").optString("Brand");
                            barcode = comboJson.optJSONArray("Products").getJSONObject(0).optJSONObject("ProductDetail").optString("barcode");
                            for (int jj = 0; jj < comboJson.optJSONArray("Products").length(); jj ++) {
                                if (comboJson.optJSONArray("Products").getJSONObject(jj).getBoolean("hasVariants")) {
                                    has_variant = true;
                                    break;
                                }
                            }

                            one_product_id = comboJson.optJSONArray("Products").getJSONObject(0).getInt("id");
                            for (int k = 0; k < comboJson.optJSONArray("Products").length(); k++) {
                                ProductModel one = GsonUtils.getInstance().fromJson(comboJson.optJSONArray("Products").getJSONObject(k).toString(), ProductModel.class);
                                cTmpList.add(one);
                            }
                        }
                    } else {
                        if (buyCount > 0) {
                            product_type = "Buy1Get1FreeDeal";
                            JSONObject buyJson = oneObj.optJSONArray("Buy1Get1FreeDeals").getJSONObject(0);
                            price = parseOptString(buyJson, "selling_price");
                            stock = parseOptInt(buyJson, "stock");
                            product_id = buyJson.getInt("id");
                            currencyModel = GsonUtils.getInstance().fromJson(buyJson.optJSONObject("Currency").toString(), CurrencyModel.class);
                            if (buyJson.optJSONArray("BuyProducts").length() > 0) {
                                logo = buyJson.optJSONArray("BuyProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("media");
                                packSize = buyJson.optJSONArray("BuyProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("PackSize");
                                unit = buyJson.optJSONArray("BuyProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("Unit");
                                description = buyJson.optJSONArray("BuyProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("description");
                                title = buyJson.optJSONArray("BuyProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("Brand");
                                barcode = buyJson.optJSONArray("BuyProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("barcode");
                                one_product_id = buyJson.optJSONArray("BuyProducts").getJSONObject(0).getInt("id");
                                for (int jj = 0; jj < buyJson.optJSONArray("BuyProducts").length(); jj ++) {
                                    if (buyJson.optJSONArray("BuyProducts").getJSONObject(jj).getBoolean("hasVariants")) {
                                        has_variant = true;
                                        break;
                                    }
                                }
                                for (int k = 0; k < buyJson.optJSONArray("BuyProducts").length(); k++) {
                                    ProductModel one = GsonUtils.getInstance().fromJson(buyJson.optJSONArray("BuyProducts").getJSONObject(k).toString(), ProductModel.class);
                                    bTmpList.add(one);
                                }
                                variant_string = String.format("%s:%s", buyJson.optJSONArray("BuyProducts").getJSONObject(0).optInt("id"), barcode);
                                for (int k = 1; k < buyJson.optJSONArray("BuyProducts").length(); k++) {
                                    variant_string += "," + String.format("%s:%s", buyJson.optJSONArray("BuyProducts").getJSONObject(k).optInt("id"), buyJson.optJSONArray("BuyProducts").getJSONObject(k).optJSONObject("ProductDetail").optString("barcode"));
                                }
                            } else {
                                if (buyJson.optJSONArray("FreeProducts").length() > 0) {
                                    logo = buyJson.optJSONArray("FreeProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("media");
                                    packSize = buyJson.optJSONArray("FreeProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("PackSize");
                                    unit = buyJson.optJSONArray("FreeProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("Unit");
                                    description = buyJson.optJSONArray("FreeProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("description");
                                    title = buyJson.optJSONArray("FreeProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("Brand");
                                    barcode = buyJson.optJSONArray("FreeProducts").getJSONObject(0).optJSONObject("ProductDetail").optString("barcode");
                                    one_product_id = buyJson.optJSONArray("FreeProducts").getJSONObject(0).getInt("id");
                                    for (int jj = 0; jj < buyJson.optJSONArray("FreeProducts").length(); jj ++) {
                                        if (buyJson.optJSONArray("FreeProducts").getJSONObject(jj).getBoolean("hasVariants")) {
                                            has_variant = true;
                                            break;
                                        }
                                    }
                                    for (int k = 0; k < buyJson.optJSONArray("FreeProducts").length(); k++) {
                                        ProductModel one = GsonUtils.getInstance().fromJson(buyJson.optJSONArray("FreeProducts").getJSONObject(k).toString(), ProductModel.class);
                                        bTmpList.add(one);
                                    }
                                } else {
                                    continue;
                                }
                            }

                        } else {
                            continue;
                        }
                    }
                }
                productOneModel.setImageUrl(logo);
                productOneModel.setCurrency(currencyModel);
                productOneModel.setPrice(price);
                productOneModel.setStock(stock);
                productOneModel.setTitle(title);
                productOneModel.setDescription(description);
                productOneModel.setPack_ize(packSize);
                productOneModel.setUnit(unit);
                productOneModel.setProduct_id(product_id);
                productOneModel.setStore_id(store_id);
                productOneModel.setProduct_type(product_type);
                productOneModel.setBarcode(barcode);
                productOneModel.setVariant_string(variant_string);
                productOneModel.setComboDeals(cTmpList);
                productOneModel.setBuyList(bTmpList);
                productOneModel.setGetList(gTmpList);
                productOneModel.setHasVariant(has_variant);
                productOneModel.setOneProductId(one_product_id);
                list.add(productOneModel);
            }
        } catch (JSONException e) {

        }
        return list;
    }
    public static ArrayList<ProductOneModel> parseProductFromArry(ArrayList<PromotionModel> jsonArray) {
        ArrayList<ProductOneModel> list = new ArrayList<>();
        list.clear();
        for (int i = 0; i < jsonArray.size(); i ++) {
            PromotionModel oneObj = jsonArray.get(i);
            ProductOneModel productOneModel = new ProductOneModel();
            productOneModel.setId(oneObj.getId());
            productOneModel.setLike(oneObj.isLike());
            productOneModel.setFeed_type(oneObj.getFeed_type());
            int singleCount = oneObj.getSingleProducts().size();
            int comboCount = oneObj.getComboDeals().size();
            int buyCount = oneObj.getBuy1Get1FreeDeals().size();
            int stock = 0;
            CurrencyModel currencyModel = new CurrencyModel();
            String price = "";
            String logo = "";
            int product_id = -1;
            int one_product_id = -1;
            String description = "";
            String packSize = "";
            String unit = "";
            String title = "";
            String product_type = "";
            String barcode = "";
            String variant_string = "";
            boolean has_variant = false;
            ArrayList<ProductModel> cTmpList = new ArrayList<>();
            cTmpList.clear();
            ArrayList<ProductModel> bTmpList = new ArrayList<>();
            bTmpList.clear();
            ArrayList<ProductModel> gTmpList = new ArrayList<>();
            gTmpList.clear();

            int store_id = oneObj.getStores().get(0).getId();

            if (singleCount > 0) {
                product_type = "SingleProduct";
                price = oneObj.getSingleProducts().get(0).getSelling_price();
                stock = oneObj.getSingleProducts().get(0).getStock();
                product_id = oneObj.getSingleProducts().get(0).getId();
                currencyModel = oneObj.getSingleProducts().get(0).getCurrency();
                logo = oneObj.getSingleProducts().get(0).getProduct().getProductDetail().getMedia();
                packSize = oneObj.getSingleProducts().get(0).getProduct().getProductDetail().getPackSize();
                unit = oneObj.getSingleProducts().get(0).getProduct().getProductDetail().getUnit();
                description = oneObj.getSingleProducts().get(0).getProduct().getProductDetail().getDescription();
                title = oneObj.getSingleProducts().get(0).getProduct().getProductDetail().getBrand();
                barcode = oneObj.getSingleProducts().get(0).getProduct().getProductDetail().getBarcode();
                has_variant = oneObj.getSingleProducts().get(0).getProduct().isHasVariants();
                one_product_id = oneObj.getSingleProducts().get(0).getProduct().getId();
            } else {
                if (comboCount > 0 ) {
                    product_type = "ComboDeal";
                    if (oneObj.getComboDeals().get(0).getProducts().size() > 0) {
                        price = oneObj.getComboDeals().get(0).getSelling_price();
                        stock = oneObj.getComboDeals().get(0).getStock();
                        product_id = oneObj.getComboDeals().get(0).getId();
                        currencyModel = oneObj.getComboDeals().get(0).getCurrency();
                        logo = oneObj.getComboDeals().get(0).getProducts().get(0).getProductDetail().getMedia();
                        packSize = oneObj.getComboDeals().get(0).getProducts().get(0).getProductDetail().getPackSize();
                        unit = oneObj.getComboDeals().get(0).getProducts().get(0).getProductDetail().getUnit();
                        description = oneObj.getComboDeals().get(0).getProducts().get(0).getProductDetail().getDescription();
                        title = oneObj.getComboDeals().get(0).getProducts().get(0).getProductDetail().getBrand();
                        barcode = oneObj.getComboDeals().get(0).getProducts().get(0).getProductDetail().getBarcode();
                        has_variant = oneObj.getComboDeals().get(0).getProducts().get(0).isHasVariants();
                        one_product_id = oneObj.getComboDeals().get(0).getProducts().get(0).getId();
                        for (int k = 0; k < oneObj.getComboDeals().get(0).getProducts().size(); k++) {
                            ProductModel one = oneObj.getComboDeals().get(0).getProducts().get(k);
                            cTmpList.add(one);
                        }
                    }
                } else {
                    if (buyCount > 0) {
                        product_type = "Buy1Get1FreeDeal";
                        price = oneObj.getBuy1Get1FreeDeals().get(0).getSelling_price();
                        stock = oneObj.getBuy1Get1FreeDeals().get(0).getStock();
                        product_id = oneObj.getBuy1Get1FreeDeals().get(0).getId();
                        currencyModel = oneObj.getBuy1Get1FreeDeals().get(0).getCurrency();
                        if (oneObj.getBuy1Get1FreeDeals().get(0).getBuyProducts().size() > 0) {
                            logo = oneObj.getBuy1Get1FreeDeals().get(0).getBuyProducts().get(0).getProductDetail().getMedia();
                            packSize = oneObj.getBuy1Get1FreeDeals().get(0).getBuyProducts().get(0).getProductDetail().getPackSize();
                            unit = oneObj.getBuy1Get1FreeDeals().get(0).getBuyProducts().get(0).getProductDetail().getUnit();
                            description = oneObj.getBuy1Get1FreeDeals().get(0).getBuyProducts().get(0).getProductDetail().getDescription();
                            title = oneObj.getBuy1Get1FreeDeals().get(0).getBuyProducts().get(0).getProductDetail().getBrand();
                            barcode = oneObj.getBuy1Get1FreeDeals().get(0).getBuyProducts().get(0).getProductDetail().getBarcode();
                            for (int jj = 0; jj < oneObj.getBuy1Get1FreeDeals().get(0).getBuyProducts().size(); jj ++) {
                                if (oneObj.getBuy1Get1FreeDeals().get(0).getBuyProducts().get(jj).isHasVariants()) {
                                    has_variant = true;
                                    break;
                                }
                            }
                            one_product_id = oneObj.getBuy1Get1FreeDeals().get(0).getBuyProducts().get(0).getId();
                            for (int k = 0; k < oneObj.getBuy1Get1FreeDeals().get(0).getBuyProducts().size(); k++) {
                                ProductModel one = oneObj.getBuy1Get1FreeDeals().get(0).getBuyProducts().get(k);
                                bTmpList.add(one);
                            }
                            variant_string = String.format("%s:%s", oneObj.getBuy1Get1FreeDeals().get(0).getBuyProducts().get(0).getId(), barcode);
                            for (int k = 1; k < oneObj.getBuy1Get1FreeDeals().get(0).getBuyProducts().size(); k++) {
                                variant_string += "," + String.format("%s:%s", oneObj.getBuy1Get1FreeDeals().get(0).getBuyProducts().get(k).getId(), oneObj.getBuy1Get1FreeDeals().get(0).getBuyProducts().get(k).getProductDetail().getBarcode());
                            }
                        } else {
                            if (oneObj.getBuy1Get1FreeDeals().get(0).getFreeProducts().size() > 0) {
                                logo = oneObj.getBuy1Get1FreeDeals().get(0).getFreeProducts().get(0).getProductDetail().getMedia();
                                packSize = oneObj.getBuy1Get1FreeDeals().get(0).getFreeProducts().get(0).getProductDetail().getPackSize();
                                unit = oneObj.getBuy1Get1FreeDeals().get(0).getFreeProducts().get(0).getProductDetail().getUnit();
                                description = oneObj.getBuy1Get1FreeDeals().get(0).getFreeProducts().get(0).getProductDetail().getDescription();
                                title = oneObj.getBuy1Get1FreeDeals().get(0).getFreeProducts().get(0).getProductDetail().getBrand();
                                barcode = oneObj.getBuy1Get1FreeDeals().get(0).getFreeProducts().get(0).getProductDetail().getBarcode();
                                for (int jj = 0; jj < oneObj.getBuy1Get1FreeDeals().get(0).getFreeProducts().size(); jj ++) {
                                    if (oneObj.getBuy1Get1FreeDeals().get(0).getFreeProducts().get(jj).isHasVariants()) {
                                        has_variant = true;
                                        break;
                                    }
                                }
                                one_product_id = oneObj.getBuy1Get1FreeDeals().get(0).getFreeProducts().get(0).getId();
                                for (int k = 0; k < oneObj.getBuy1Get1FreeDeals().get(0).getFreeProducts().size(); k++) {
                                    ProductModel one = oneObj.getBuy1Get1FreeDeals().get(0).getFreeProducts().get(k);
                                    bTmpList.add(one);
                                }
                            } else {
                                continue;
                            }
                        }

                    } else {
                        continue;
                    }
                }
            }
            productOneModel.setImageUrl(logo);
            productOneModel.setCurrency(currencyModel);
            productOneModel.setPrice(price);
            productOneModel.setStock(stock);
            productOneModel.setTitle(title);
            productOneModel.setDescription(description);
            productOneModel.setPack_ize(packSize);
            productOneModel.setUnit(unit);
            productOneModel.setProduct_id(product_id);
            productOneModel.setStore_id(store_id);
            productOneModel.setProduct_type(product_type);
            productOneModel.setBarcode(barcode);
            productOneModel.setVariant_string(variant_string);
            productOneModel.setComboDeals(cTmpList);
            productOneModel.setBuyList(bTmpList);
            productOneModel.setGetList(gTmpList);
            productOneModel.setOneProductId(one_product_id);
            productOneModel.setHasVariant(has_variant);
            list.add(productOneModel);
        }
        return list;
    }
    public static ArrayList<FeedModel> parseFeed(JSONArray jsonArray) {
        ArrayList<FeedModel> list = new ArrayList<>();
        list.clear();
        try {
            for (int i = 0; i < jsonArray.length(); i ++) {
                FeedModel feedModel = new FeedModel();
                feedModel = GsonUtils.getInstance().fromJson(jsonArray.getJSONObject(i).toString(), FeedModel.class);
                if (feedModel.getFeed_type() == null) {
                    feedModel.setFeed_type("UserPost");
                }
                if (feedModel.getMedia() == null || feedModel.getMedia().isEmpty() || feedModel.getMedia_type().equalsIgnoreCase("None")) {
                    if (feedModel.getSubMedia().size() > 0) {
                        feedModel.setMedia_type(feedModel.getSubMedia().get(0).getMedia_Type());
                        feedModel.setMedia_gif(feedModel.getSubMedia().get(0).getVideoThumb());

                        for (int j = 0; j < feedModel.getSubMedia().size(); j++) {
                            feedModel.getMediaList().add(feedModel.getSubMedia().get(j).getMedia());
                        }
                    } else {
                        feedModel.getMediaList().add("");
                        feedModel.setMedia_type("None");
                        feedModel.setMedia_gif("");
                    }
                } else {
                    feedModel.getMediaList().add(feedModel.getMedia());
                    feedModel.setMedia_type(feedModel.getMedia_type());
                    feedModel.setMedia_gif("");
                }
                list.add(feedModel);
            }
        } catch (JSONException e) {

        }
        return list;
    }
    public static ArrayList<PostModel> parsePost(JSONArray jsonArray) {
        ArrayList<PostModel> list = new ArrayList<>();
        list.clear();
        try {
            for (int i = 0; i < jsonArray.length(); i ++) {
                PostModel postModel = new PostModel();
                postModel = GsonUtils.getInstance().fromJson(jsonArray.getJSONObject(i).toString(), PostModel.class);
                if (postModel.getMedia() == null || postModel.getMedia().isEmpty() || postModel.getMedia_type().equalsIgnoreCase("None")) {
                    if (postModel.getSubMedia().size() > 0) {
                        postModel.setMedia_type(postModel.getSubMedia().get(0).getMedia_Type());
                        postModel.setMedia_gif(postModel.getSubMedia().get(0).getVideoThumb());
                        postModel.setMedia(postModel.getSubMedia().get(0).getMedia());
                    } else {
                        postModel.setMedia("");
                        postModel.setMedia_type("None");
                        postModel.setMedia_gif("");
                    }
                } else {
                    postModel.setMedia_gif("");
                }
                list.add(postModel);
            }
        } catch (JSONException e) {

        }
        return list;
    }
}
