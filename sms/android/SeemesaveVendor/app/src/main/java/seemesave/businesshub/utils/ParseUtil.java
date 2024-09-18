package seemesave.businesshub.utils;

import com.google.gson.reflect.TypeToken;

import seemesave.businesshub.application.App;
import seemesave.businesshub.model.common.BuyGetProductModel;
import seemesave.businesshub.model.common.ComboDealProductModel;
import seemesave.businesshub.model.common.CurrencyModel;
import seemesave.businesshub.model.common.ProductModel;
import seemesave.businesshub.model.common.ProductOneModel;
import seemesave.businesshub.model.common.PromoteProductModel;
import seemesave.businesshub.model.common.SingleProductModel;
import seemesave.businesshub.model.common.SupplierModel;
import seemesave.businesshub.model.common.SupplierOneModel;
import seemesave.businesshub.model.res.ProductPromoteListRes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ParseUtil {
    public static int parseOptInt(JSONObject object, String key) {
        int value = 0;
        try {
            value = object.getInt(key);
        } catch (Exception e) {
            value = 0;
        }
        return value;
    }

    public static String parseOptString(JSONObject object, String key) {
        String value = "";
        try {
            value = object.getString(key);
        } catch (Exception e) {
            value = "";
        }
        return value;
    }

    public static boolean parseOptBoolean(JSONObject object, String key) {
        boolean value = false;
        try {
            value = object.getBoolean(key);
        } catch (Exception e) {
            value = false;
        }
        return value;
    }

    public static ArrayList<ProductOneModel> getProductsFromPromote(ProductPromoteListRes res) {
        ArrayList<ProductOneModel> productList = new ArrayList<>();
        productList.clear();
        for (int i = 0; i < res.getProductList().size(); i++) {
            PromoteProductModel promoteProductModel = res.getProductList().get(i);
            int singleCount = promoteProductModel.getSingleProducts().size();
            int comboCount = promoteProductModel.getComboDeals().size();
            int buyCount = promoteProductModel.getBuy1Get1FreeDeals().size();
            String product_type = "";
            int paytopromote = -1;
            String logo = "";
            int product_id = -1;
            float dailyPayment = 0.0f;
            String description = "";
            String packSize = "";
            String barcode = "";
            String unit = "";
            String title = "";
            boolean active = false;
            int supplier_id = -1;
            CurrencyModel currencyModel;
            if (singleCount > 0) {
                product_type = "SingleProduct";
                for (int j = 0; j < singleCount; j++) {
                    SingleProductModel singleProductModel = promoteProductModel.getSingleProducts().get(j);
                    paytopromote = singleProductModel.getPay_to_promote();
                    product_id = singleProductModel.getId();
                    dailyPayment = singleProductModel.getDailyPayment();
                    currencyModel = singleProductModel.getCurrency();
                    logo = singleProductModel.getProduct().getProductDetail().getThumbnail_image();
                    packSize = singleProductModel.getProduct().getProductDetail().getPackSize();
                    barcode = singleProductModel.getProduct().getProductDetail().getBarcode();
                    unit = singleProductModel.getProduct().getProductDetail().getUnit();
                    description = singleProductModel.getProduct().getProductDetail().getDescription();
                    title = singleProductModel.getProduct().getProductDetail().getBrand();
                    active = singleProductModel.getProduct().getProductDetail().isActive();
                    supplier_id = singleProductModel.getProduct().getProductDetail().getSupplier_id();

                    ProductOneModel tmpProduct = new ProductOneModel();
                    tmpProduct.setImageUrl(logo);
                    tmpProduct.setCurrency(currencyModel);
                    tmpProduct.setTitle(title);
                    tmpProduct.setDescription(description);
                    tmpProduct.setPack_size(packSize);
                    tmpProduct.setUnit(unit);
                    tmpProduct.setProduct_id(product_id);
                    tmpProduct.setProduct_type(product_type);
                    tmpProduct.setParent_index(i);
                    tmpProduct.setBarcode(barcode);
                    tmpProduct.setCheck(false);
                    tmpProduct.setPay_to_promote(paytopromote);
                    tmpProduct.setDailyPayment(dailyPayment);
                    tmpProduct.setActive(active);
                    tmpProduct.setSupplier_id(supplier_id);
                    productList.add(tmpProduct);
                }
            }
            if (comboCount > 0) {
                product_type = "ComboDeal";
                for (int j = 0; j < comboCount; j++) {
                    ComboDealProductModel comboDealProductModel = promoteProductModel.getComboDeals().get(j);
                    paytopromote = comboDealProductModel.getPay_to_promote();
                    product_id = comboDealProductModel.getId();
                    dailyPayment = comboDealProductModel.getDailyPayment();
                    currencyModel = comboDealProductModel.getCurrency();

                    ProductModel productModel = comboDealProductModel.getProducts().get(0);
                    logo = productModel.getProductDetail().getThumbnail_image();
                    packSize = productModel.getProductDetail().getPackSize();
                    barcode = productModel.getProductDetail().getBarcode();
                    unit = productModel.getProductDetail().getUnit();
                    description = productModel.getProductDetail().getDescription();
                    title = productModel.getProductDetail().getBrand();
                    active = productModel.getProductDetail().isActive();
                    supplier_id = productModel.getProductDetail().getSupplier_id();

                    ProductOneModel tmpProduct = new ProductOneModel();
                    tmpProduct.setImageUrl(logo);
                    tmpProduct.setCurrency(currencyModel);
                    tmpProduct.setTitle(title);
                    tmpProduct.setDescription(description);
                    tmpProduct.setPack_size(packSize);
                    tmpProduct.setUnit(unit);
                    tmpProduct.setProduct_id(product_id);
                    tmpProduct.setProduct_type(product_type);
                    tmpProduct.setParent_index(i);
                    tmpProduct.setBarcode(barcode);
                    tmpProduct.setCheck(false);
                    tmpProduct.setPay_to_promote(paytopromote);
                    tmpProduct.setDailyPayment(dailyPayment);
                    tmpProduct.setActive(active);
                    tmpProduct.setSupplier_id(supplier_id);
                    tmpProduct.setComboDeals(comboDealProductModel.getProducts());
                    productList.add(tmpProduct);

                }
            }
            if (buyCount > 0) {
                product_type = "Buy1Get1FreeDeal";
                for (int j = 0; j < buyCount; j++) {
                    BuyGetProductModel buyGetProductModel = promoteProductModel.getBuy1Get1FreeDeals().get(j);
                    paytopromote = buyGetProductModel.getPay_to_promote();
                    product_id = buyGetProductModel.getId();
                    dailyPayment = buyGetProductModel.getDailyPayment();
                    currencyModel = buyGetProductModel.getCurrency();
                    ArrayList<ProductModel> bTmpList = new ArrayList<>();
                    bTmpList.clear();
                    ArrayList<ProductModel> gTmpList = new ArrayList<>();
                    gTmpList.clear();
                    ProductModel productModel = buyGetProductModel.getBuyProducts().get(0);
                    logo = productModel.getProductDetail().getThumbnail_image();
                    packSize = productModel.getProductDetail().getPackSize();
                    barcode = productModel.getProductDetail().getBarcode();
                    unit = productModel.getProductDetail().getUnit();
                    description = productModel.getProductDetail().getDescription();
                    title = productModel.getProductDetail().getBrand();
                    active = productModel.getProductDetail().isActive();
                    supplier_id = productModel.getProductDetail().getSupplier_id();

                    ProductOneModel tmpProduct = new ProductOneModel();
                    tmpProduct.setImageUrl(logo);
                    tmpProduct.setCurrency(currencyModel);
                    tmpProduct.setTitle(title);
                    tmpProduct.setDescription(description);
                    tmpProduct.setPack_size(packSize);
                    tmpProduct.setUnit(unit);
                    tmpProduct.setProduct_id(product_id);
                    tmpProduct.setProduct_type(product_type);
                    tmpProduct.setParent_index(i);
                    tmpProduct.setBarcode(barcode);
                    tmpProduct.setCheck(false);
                    tmpProduct.setPay_to_promote(paytopromote);
                    tmpProduct.setDailyPayment(dailyPayment);
                    tmpProduct.setActive(active);
                    tmpProduct.setSupplier_id(supplier_id);
                    tmpProduct.setBuyList(buyGetProductModel.getBuyProducts());
                    tmpProduct.setGetList(buyGetProductModel.getFreeProducts());
                    productList.add(tmpProduct);
                }
            }

        }
        return productList;
    }

    public static ArrayList<SupplierOneModel> getSuppliersFromPromote(ProductPromoteListRes res) {
        ArrayList<SupplierOneModel> supperList = new ArrayList<>();
        supperList.clear();
        for (int i = 0; i < res.getProductList().size(); i++) {
            PromoteProductModel promoteProductModel = res.getProductList().get(i);
            supperList.add(promoteProductModel.getSupplier());
        }
        return supperList;
    }
    public static ArrayList<ProductOneModel> getSingleProductsFromPromotion(ArrayList<SingleProductModel> singleList) {
        ArrayList<ProductOneModel> productList = new ArrayList<>();
        productList.clear();
        for (int i = 0; i < singleList.size(); i++) {
            String product_type = "";
            int paytopromote = -1;
            String logo = "";
            int product_id = -1;
            float dailyPayment = 0.0f;
            String description = "";
            String packSize = "";
            String barcode = "";
            String unit = "";
            String title = "";
            boolean active = false;
            int supplier_id = -1;
            CurrencyModel currencyModel;
            product_type = "SingleProduct";
            SingleProductModel singleProductModel = singleList.get(i);
            paytopromote = singleProductModel.getPay_to_promote();
            product_id = singleProductModel.getId();
            if (App.getPortalType()) {
                dailyPayment = singleProductModel.getDailyPayment();
            } else {
                dailyPayment = singleProductModel.getDaily_payment();
            }

            currencyModel = singleProductModel.getCurrency();
            logo = singleProductModel.getProduct().getProductDetail().getThumbnail_image();
            packSize = singleProductModel.getProduct().getProductDetail().getPackSize();
            barcode = singleProductModel.getProduct().getProductDetail().getBarcode();
            unit = singleProductModel.getProduct().getProductDetail().getUnit();
            description = singleProductModel.getProduct().getProductDetail().getDescription();
            title = singleProductModel.getProduct().getProductDetail().getBrand();
            active = singleProductModel.getProduct().getProductDetail().isActive();
            supplier_id = singleProductModel.getProduct().getProductDetail().getSupplier_id();

            ProductOneModel tmpProduct = new ProductOneModel();
            tmpProduct.setImageUrl(logo);
            if (App.getPortalType()) {
                tmpProduct.setPrice(singleProductModel.getSelling_price());
            } else {
                tmpProduct.setPrice(String.valueOf(dailyPayment));
            }
            tmpProduct.setStock(singleProductModel.getProduct().getQuantity());
            tmpProduct.setSrcStock(singleProductModel.getProduct().getQuantity());
            tmpProduct.setCurrency(currencyModel);
            tmpProduct.setTitle(title);
            tmpProduct.setDescription(description);
            tmpProduct.setPack_size(packSize);
            tmpProduct.setUnit(unit);
            tmpProduct.setProduct_id(product_id);
            tmpProduct.setProduct_type(product_type);
            tmpProduct.setParent_index(i);
            tmpProduct.setBarcode(barcode);
            tmpProduct.setCheck(false);
            tmpProduct.setPay_to_promote(paytopromote);
            tmpProduct.setDailyPayment(dailyPayment);
            tmpProduct.setActive(active);
            tmpProduct.setSupplier_id(supplier_id);

            productList.add(tmpProduct);
        }
        return productList;
    }
    public static ArrayList<ProductOneModel> getComboProductsFromPromotion(ArrayList<ComboDealProductModel> comboList) {
        ArrayList<ProductOneModel> productList = new ArrayList<>();
        productList.clear();
        for (int i = 0; i < comboList.size(); i++) {
            String product_type = "";
            int paytopromote = -1;
            String logo = "";
            int product_id = -1;
            float dailyPayment = 0.0f;
            String description = "";
            String packSize = "";
            String barcode = "";
            String unit = "";
            String title = "";
            boolean active = false;
            int supplier_id = -1;
            CurrencyModel currencyModel;
            product_type = "ComboDeal";
            ComboDealProductModel comboDealProductModel = comboList.get(i);
            paytopromote = comboDealProductModel.getPay_to_promote();
            product_id = comboDealProductModel.getId();
            if (App.getPortalType()) {
                dailyPayment = comboDealProductModel.getDailyPayment();
            } else {
                dailyPayment = comboDealProductModel.getDaily_payment();
            }
            currencyModel = comboDealProductModel.getCurrency();

            ProductModel productModel = comboDealProductModel.getProducts().get(0);
            logo = productModel.getProductDetail().getThumbnail_image();
            packSize = productModel.getProductDetail().getPackSize();
            barcode = productModel.getProductDetail().getBarcode();
            unit = productModel.getProductDetail().getUnit();
            description = productModel.getProductDetail().getDescription();
            title = productModel.getProductDetail().getBrand();
            active = productModel.getProductDetail().isActive();
            supplier_id = productModel.getProductDetail().getSupplier_id();

            ProductOneModel tmpProduct = new ProductOneModel();
            tmpProduct.setImageUrl(logo);
            tmpProduct.setCurrency(currencyModel);
            if (App.getPortalType()) {
                tmpProduct.setPrice(comboDealProductModel.getSelling_price());
            } else {
                tmpProduct.setPrice(String.valueOf(dailyPayment));
            }
            tmpProduct.setStock(comboDealProductModel.getStock());
            tmpProduct.setSrcStock(comboDealProductModel.getStock());
            tmpProduct.setTitle(title);
            tmpProduct.setDescription(description);
            tmpProduct.setPack_size(packSize);
            tmpProduct.setUnit(unit);
            tmpProduct.setProduct_id(product_id);
            tmpProduct.setProduct_type(product_type);
            tmpProduct.setParent_index(i);
            tmpProduct.setBarcode(barcode);
            tmpProduct.setCheck(false);
            tmpProduct.setPay_to_promote(paytopromote);
            tmpProduct.setDailyPayment(dailyPayment);
            tmpProduct.setActive(active);
            tmpProduct.setSupplier_id(supplier_id);
            tmpProduct.setComboDeals(comboDealProductModel.getProducts());
            productList.add(tmpProduct);

        }
        return productList;
    }
    public static ArrayList<ProductOneModel> getBuyGetProductsFromPromotion(ArrayList<BuyGetProductModel> buygetList) {
        ArrayList<ProductOneModel> productList = new ArrayList<>();
        productList.clear();
        for (int i = 0; i < buygetList.size(); i++) {
            String product_type = "";
            int paytopromote = -1;
            String logo = "";
            int product_id = -1;
            float dailyPayment = 0.0f;
            String description = "";
            String packSize = "";
            String barcode = "";
            String unit = "";
            String title = "";
            boolean active = false;
            int supplier_id = -1;
            CurrencyModel currencyModel;
            product_type = "Buy1Get1FreeDeal";
            BuyGetProductModel buyGetProductModel = buygetList.get(i);
            paytopromote = buyGetProductModel.getPay_to_promote();
            product_id = buyGetProductModel.getId();
            if (App.getPortalType()) {
                dailyPayment = buyGetProductModel.getDailyPayment();
            } else {
                dailyPayment = buyGetProductModel.getDaily_payment();
            }
            currencyModel = buyGetProductModel.getCurrency();
            ArrayList<ProductModel> bTmpList = new ArrayList<>();
            bTmpList.clear();
            ArrayList<ProductModel> gTmpList = new ArrayList<>();
            gTmpList.clear();
            ProductModel productModel = buyGetProductModel.getBuyProducts().get(0);
            logo = productModel.getProductDetail().getThumbnail_image();
            packSize = productModel.getProductDetail().getPackSize();
            barcode = productModel.getProductDetail().getBarcode();
            unit = productModel.getProductDetail().getUnit();
            description = productModel.getProductDetail().getDescription();
            title = productModel.getProductDetail().getBrand();
            active = productModel.getProductDetail().isActive();
            supplier_id = productModel.getProductDetail().getSupplier_id();

            ProductOneModel tmpProduct = new ProductOneModel();
            tmpProduct.setImageUrl(logo);
            if (App.getPortalType()) {
                tmpProduct.setPrice(buyGetProductModel.getSelling_price());
            } else {
                tmpProduct.setPrice(String.valueOf(dailyPayment));
            }
            tmpProduct.setStock(buyGetProductModel.getStock());
            tmpProduct.setSrcStock(buyGetProductModel.getStock());
            tmpProduct.setCurrency(currencyModel);
            tmpProduct.setTitle(title);
            tmpProduct.setDescription(description);
            tmpProduct.setPack_size(packSize);
            tmpProduct.setUnit(unit);
            tmpProduct.setProduct_id(product_id);
            tmpProduct.setProduct_type(product_type);
            tmpProduct.setParent_index(i);
            tmpProduct.setBarcode(barcode);
            tmpProduct.setCheck(false);
            tmpProduct.setPay_to_promote(paytopromote);
            tmpProduct.setDailyPayment(dailyPayment);
            tmpProduct.setActive(active);
            tmpProduct.setSupplier_id(supplier_id);
            tmpProduct.setBuyList(buyGetProductModel.getBuyProducts());
            tmpProduct.setGetList(buyGetProductModel.getFreeProducts());
            productList.add(tmpProduct);
        }
        return productList;
    }
}
