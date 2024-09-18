package seemesave.businesshub.view.vendor.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.ProductOrderAdapter;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.model.common.CollectOrderModel;
import seemesave.businesshub.model.common.CurrencyModel;
import seemesave.businesshub.model.common.ProductDetailModel;
import seemesave.businesshub.model.common.ProductModel;
import seemesave.businesshub.model.common.ProductOneModel;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.utils.GsonUtils;

public class CollectOrderDetailActivity extends BaseActivity {

    private CollectOrderDetailActivity activity;

    @BindView(R.id.imgUser)
    CircleImageView imgUser;

    @BindView(R.id.btnUpdate)
    LinearLayout btnUpdate;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtCollectionTime)
    TextView txtCollectionTime;
    @BindView(R.id.txtPrice)
    TextView txtPrice;

    @BindView(R.id.lytSingle)
    LinearLayout lytSingle;
    @BindView(R.id.lytCombo)
    LinearLayout lytCombo;
    @BindView(R.id.lytBuy)
    LinearLayout lytBuy;

    @BindView(R.id.singleView)
    RecyclerView singleView;
    @BindView(R.id.singleIndicator)
    IndefinitePagerIndicator singleIndicator;
    private ProductOrderAdapter singleAdapter;
    private ArrayList<ProductOneModel> singleList = new ArrayList<>();

    @BindView(R.id.comboView)
    RecyclerView comboView;
    @BindView(R.id.comboIndicator)
    IndefinitePagerIndicator comboIndicator;
    private ProductOrderAdapter comboAdapter;
    private ArrayList<ProductOneModel> comboList = new ArrayList<>();


    @BindView(R.id.buyView)
    RecyclerView buyView;
    @BindView(R.id.buyIndicator)
    IndefinitePagerIndicator buyIndicator;
    private ProductOrderAdapter buyAdapter;
    private ArrayList<ProductOneModel> buyList = new ArrayList<>();


    private int order_id = -1;
    private boolean is_change_stock = false;
    private CollectOrderModel collectOrderModel = new CollectOrderModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_order_detail);
        ButterKnife.bind(this);
        activity = this;
        initView();
    }

    private void initView() {
        singleList.clear();
        comboList.clear();
        buyList.clear();
        order_id = getIntent().getIntExtra("order_id", -1);
        if (getIntent().hasExtra("is_change_stock")) {
            is_change_stock = getIntent().getBooleanExtra("is_change_stock", false);
        }
        if (is_change_stock) {
            btnUpdate.setVisibility(View.VISIBLE);
        } else {
            btnUpdate.setVisibility(View.GONE);
        }
        setSingleAdapter();
        setComboAdapter();
        setBuyAdapter();
        apiCallForGetPromotions(true);
    }

    private void setSingleAdapter() {
        singleAdapter = new ProductOrderAdapter(activity, singleList, is_change_stock, new ProductOrderAdapter.ProductOrderRecyclerListener() {
            @Override
            public void onPlus(int pos, ProductOneModel model) {
                int count = model.getStock() + 1;
                if (count <= model.getSrcStock()) {
                    singleList.get(pos).setStock(count);
                    singleAdapter.setData(singleList);
                }
            }

            @Override
            public void onMinus(int pos, ProductOneModel model) {
                int count = model.getStock() - 1;
                if (count >= 0) {
                    singleList.get(pos).setStock(count);
                    singleAdapter.setData(singleList);
                }
            }
        });
        singleView.setAdapter(singleAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        singleView.setLayoutManager(linearLayoutManager);
        singleIndicator.attachToRecyclerView(singleView);
    }

    private void setComboAdapter() {
        comboAdapter = new ProductOrderAdapter(activity, comboList, is_change_stock, new ProductOrderAdapter.ProductOrderRecyclerListener() {
            @Override
            public void onPlus(int pos, ProductOneModel model) {
                int count = model.getStock() + 1;
                if (count <= model.getSrcStock()) {
                    comboList.get(pos).setStock(count);
                    comboAdapter.setData(comboList);
                }
            }

            @Override
            public void onMinus(int pos, ProductOneModel model) {
                int count = model.getStock() - 1;
                if (count >= 0) {
                    comboList.get(pos).setStock(count);
                    comboAdapter.setData(comboList);
                }
            }
        });
        comboView.setAdapter(comboAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        comboView.setLayoutManager(linearLayoutManager);
        comboIndicator.attachToRecyclerView(comboView);
    }

    private void setBuyAdapter() {
        buyAdapter = new ProductOrderAdapter(activity, buyList, is_change_stock, new ProductOrderAdapter.ProductOrderRecyclerListener() {
            @Override
            public void onPlus(int pos, ProductOneModel model) {
                int count = model.getStock() + 1;
                if (count <= model.getSrcStock()) {
                    buyList.get(pos).setStock(count);
                    buyAdapter.setData(buyList);
                }
            }

            @Override
            public void onMinus(int pos, ProductOneModel model) {
                int count = model.getStock() - 1;
                if (count >= 0) {
                    buyList.get(pos).setStock(count);
                    buyAdapter.setData(buyList);
                }
            }
        });
        buyView.setAdapter(buyAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        buyView.setLayoutManager(linearLayoutManager);
        buyIndicator.attachToRecyclerView(buyView);
    }

    void apiCallForGetPromotions(boolean show_load) {
        if (G.isNetworkAvailable(activity)) {
            if (show_load)
                showLoadingDialog();
            String url = String.format(java.util.Locale.US, G.GetOrderDetail, String.valueOf(order_id));
            Ion.with(activity)
                    .load(url)
                    .addHeader("Authorization", App.getToken())
                    .addHeader("Content-Language", App.getPortalToken())
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            if (show_load)
                                hideLoadingDialog();
                            if (e != null) {
                                Toast.makeText(activity, R.string.msg_something_wrong, Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.getBoolean("status")) {
                                        Gson gson = new Gson();
                                        collectOrderModel = gson.fromJson(jsonObject.getJSONObject("data").getString("CartInfo"), CollectOrderModel.class);
                                        txtName.setText(String.format(Locale.US, "%1$s %2$s", collectOrderModel.getUser().getFirst_name(), collectOrderModel.getUser().getLast_name()));
                                        txtCollectionTime.setText(collectOrderModel.getOrder_time());
                                        Glide.with(activity)
                                                .load(collectOrderModel.getUser().getImage_url())
                                                .centerCrop()
                                                .placeholder(R.drawable.ic_avatar)
                                                .into(imgUser);
                                        double total_price = 0.0;
                                        JSONArray singleJsonArr = jsonObject.getJSONObject("data").getJSONArray("SingleProducts");
                                        for (int i = 0; i < singleJsonArr.length(); i++) {
                                            JSONObject singleObj = singleJsonArr.getJSONObject(i);
                                            ProductOneModel productOneModel = new ProductOneModel();
                                            productOneModel.setId(singleObj.getInt("id"));
                                            CurrencyModel currencyModel = GsonUtils.getInstance().fromJson(singleObj.optJSONObject("Currency").toString(), CurrencyModel.class);
                                            productOneModel.setCurrency(currencyModel);
                                            productOneModel.setStock(singleObj.getInt("count"));
                                            productOneModel.setSrcStock(singleObj.getInt("count"));
                                            productOneModel.setPrice(String.valueOf(singleObj.getDouble("selling_price")));
                                            total_price += singleObj.getDouble("selling_price") * singleObj.getInt("count");
                                            productOneModel.setBarcode(singleObj.getJSONObject("RealDetails").getJSONObject("Product").getString("barcode"));
                                            productOneModel.setImageUrl(singleObj.getJSONObject("RealDetails").getJSONObject("Product").getString("thumbnail_image"));
                                            productOneModel.setDescription(singleObj.getJSONObject("RealDetails").getJSONObject("Product").getString("description"));
                                            productOneModel.setTitle(singleObj.getJSONObject("RealDetails").getJSONObject("Product").getJSONObject("Brand").getString("name"));
                                            productOneModel.setUnit(singleObj.getJSONObject("RealDetails").getJSONObject("Product").getJSONObject("Unit").getString("name"));
                                            productOneModel.setPack_size(singleObj.getJSONObject("RealDetails").getJSONObject("Product").getJSONObject("PackSize").getString("name"));
                                            productOneModel.setCategory(singleObj.getJSONObject("RealDetails").getJSONObject("Product").getJSONObject("Category").getString("title"));
                                            productOneModel.setProduct_type("SingleProduct");
                                            singleList.add(productOneModel);
                                        }
                                        singleAdapter.setData(singleList);
                                        if (singleList.size() == 0) {
                                            lytSingle.setVisibility(View.GONE);
                                        } else {
                                            lytSingle.setVisibility(View.VISIBLE);
                                        }
                                        JSONArray comboJsonArr = jsonObject.getJSONObject("data").getJSONArray("ComboDeals");
                                        for (int i = 0; i < comboJsonArr.length(); i++) {
                                            JSONObject comboObj = comboJsonArr.getJSONObject(i);
                                            ProductOneModel productOneModel = new ProductOneModel();
                                            productOneModel.setId(comboObj.getInt("id"));
                                            CurrencyModel currencyModel = GsonUtils.getInstance().fromJson(comboObj.optJSONObject("Currency").toString(), CurrencyModel.class);
                                            productOneModel.setCurrency(currencyModel);
                                            productOneModel.setStock(comboObj.getInt("count"));
                                            productOneModel.setSrcStock(comboObj.getInt("count"));
                                            productOneModel.setPrice(String.valueOf(comboObj.getDouble("selling_price")));
                                            total_price += comboObj.getDouble("selling_price") * comboObj.getInt("count");
                                            if (comboObj.getJSONArray("RealDetails").length() > 0) {
                                                productOneModel.setBarcode(comboObj.getJSONArray("RealDetails").getJSONObject(0).getJSONObject("Product").getString("barcode"));
                                                productOneModel.setImageUrl(comboObj.getJSONArray("RealDetails").getJSONObject(0).getJSONObject("Product").getString("thumbnail_image"));
                                                productOneModel.setDescription(comboObj.getJSONArray("RealDetails").getJSONObject(0).getJSONObject("Product").getString("description"));
                                                productOneModel.setTitle(comboObj.getJSONArray("RealDetails").getJSONObject(0).getJSONObject("Product").getJSONObject("Brand").getString("name"));
                                                productOneModel.setUnit(comboObj.getJSONArray("RealDetails").getJSONObject(0).getJSONObject("Product").getJSONObject("Unit").getString("name"));
                                                productOneModel.setPack_size(comboObj.getJSONArray("RealDetails").getJSONObject(0).getJSONObject("Product").getJSONObject("PackSize").getString("name"));
                                                productOneModel.setCategory(comboObj.getJSONArray("RealDetails").getJSONObject(0).getJSONObject("Product").getJSONObject("Category").getString("title"));
                                                ArrayList<ProductModel> comboDeals = new ArrayList<>();
                                                comboDeals.clear();
                                                for (int j = 0; j < comboObj.getJSONArray("RealDetails").length(); j++) {
                                                    JSONObject oneCombo = comboObj.getJSONArray("RealDetails").getJSONObject(j);
                                                    ProductModel productModel = new ProductModel();
                                                    productModel.setId(oneCombo.getInt("id"));
                                                    productModel.setQuantity(oneCombo.getInt("quantity"));
                                                    ProductDetailModel productDetailModel = new ProductDetailModel();
                                                    productDetailModel.setCategory(oneCombo.getJSONObject("Product").getJSONObject("Category").getString("title"));
                                                    productDetailModel.setBrand(oneCombo.getJSONObject("Product").getJSONObject("Brand").getString("name"));
                                                    productDetailModel.setThumbnail_image(oneCombo.getJSONObject("Product").getString("thumbnail_image"));
                                                    productDetailModel.setBarcode(oneCombo.getJSONObject("Product").getString("barcode"));
                                                    productDetailModel.setPackSize(oneCombo.getJSONObject("Product").getJSONObject("PackSize").getString("name"));
                                                    productDetailModel.setUnit(oneCombo.getJSONObject("Product").getJSONObject("Unit").getString("name"));
                                                    productModel.setProductDetail(productDetailModel);
                                                    comboDeals.add(productModel);
                                                }
                                                productOneModel.setComboDeals(comboDeals);
                                            }

                                            productOneModel.setProduct_type("ComboDeal");
                                            comboList.add(productOneModel);
                                        }
                                        comboAdapter.setData(comboList);
                                        if (comboList.size() == 0) {
                                            lytCombo.setVisibility(View.GONE);
                                        } else {
                                            lytCombo.setVisibility(View.VISIBLE);
                                        }

                                        JSONArray buyfreeJsonArr = jsonObject.getJSONObject("data").getJSONArray("Buy1Get1FreeDeals");
                                        for (int i = 0; i < buyfreeJsonArr.length(); i++) {
                                            JSONObject buyfreeObj = buyfreeJsonArr.getJSONObject(i);
                                            ProductOneModel productOneModel = new ProductOneModel();
                                            productOneModel.setId(buyfreeObj.getInt("id"));
                                            CurrencyModel currencyModel = GsonUtils.getInstance().fromJson(buyfreeObj.optJSONObject("Currency").toString(), CurrencyModel.class);
                                            productOneModel.setCurrency(currencyModel);
                                            productOneModel.setStock(buyfreeObj.getInt("count"));
                                            productOneModel.setSrcStock(buyfreeObj.getInt("count"));
                                            productOneModel.setPrice(String.valueOf(buyfreeObj.getDouble("selling_price")));
                                            total_price += buyfreeObj.getDouble("selling_price") * buyfreeObj.getInt("count");
                                            if (buyfreeObj.getJSONArray("RealBuyDetails").length() > 0) {
                                                productOneModel.setBarcode(buyfreeObj.getJSONArray("RealBuyDetails").getJSONObject(0).getJSONObject("Product").getString("barcode"));
                                                productOneModel.setImageUrl(buyfreeObj.getJSONArray("RealBuyDetails").getJSONObject(0).getJSONObject("Product").getString("thumbnail_image"));
                                                productOneModel.setDescription(buyfreeObj.getJSONArray("RealBuyDetails").getJSONObject(0).getJSONObject("Product").getString("description"));
                                                productOneModel.setTitle(buyfreeObj.getJSONArray("RealBuyDetails").getJSONObject(0).getJSONObject("Product").getJSONObject("Brand").getString("name"));
                                                productOneModel.setUnit(buyfreeObj.getJSONArray("RealBuyDetails").getJSONObject(0).getJSONObject("Product").getJSONObject("Unit").getString("name"));
                                                productOneModel.setPack_size(buyfreeObj.getJSONArray("RealBuyDetails").getJSONObject(0).getJSONObject("Product").getJSONObject("PackSize").getString("name"));
                                                productOneModel.setCategory(buyfreeObj.getJSONArray("RealBuyDetails").getJSONObject(0).getJSONObject("Product").getJSONObject("Category").getString("title"));
                                                ArrayList<ProductModel> buys = new ArrayList<>();
                                                buys.clear();

                                                for (int j = 0; j < buyfreeObj.getJSONArray("RealBuyDetails").length(); j++) {
                                                    JSONObject oneCombo = buyfreeObj.getJSONArray("RealBuyDetails").getJSONObject(j);
                                                    ProductModel productModel = new ProductModel();
                                                    productModel.setId(oneCombo.getInt("id"));
                                                    productModel.setQuantity(oneCombo.getInt("quantity"));
                                                    ProductDetailModel productDetailModel = new ProductDetailModel();
                                                    productDetailModel.setCategory(oneCombo.getJSONObject("Product").getJSONObject("Category").getString("title"));
                                                    productDetailModel.setBrand(oneCombo.getJSONObject("Product").getJSONObject("Brand").getString("name"));
                                                    productDetailModel.setThumbnail_image(oneCombo.getJSONObject("Product").getString("thumbnail_image"));
                                                    productDetailModel.setBarcode(oneCombo.getJSONObject("Product").getString("barcode"));
                                                    productDetailModel.setPackSize(oneCombo.getJSONObject("Product").getJSONObject("PackSize").getString("name"));
                                                    productDetailModel.setUnit(oneCombo.getJSONObject("Product").getJSONObject("Unit").getString("name"));
                                                    productModel.setProductDetail(productDetailModel);
                                                    buys.add(productModel);
                                                }
                                                productOneModel.setBuyList(buys);
                                                ArrayList<ProductModel> gets = new ArrayList<>();
                                                gets.clear();
                                                for (int j = 0; j < buyfreeObj.getJSONArray("RealFreeDetails").length(); j++) {
                                                    JSONObject oneCombo = buyfreeObj.getJSONArray("RealFreeDetails").getJSONObject(j);
                                                    ProductModel productModel = new ProductModel();
                                                    productModel.setId(oneCombo.getInt("id"));
                                                    productModel.setQuantity(oneCombo.getInt("quantity"));
                                                    ProductDetailModel productDetailModel = new ProductDetailModel();
                                                    productDetailModel.setCategory(oneCombo.getJSONObject("Product").getJSONObject("Category").getString("title"));
                                                    productDetailModel.setBrand(oneCombo.getJSONObject("Product").getJSONObject("Brand").getString("name"));
                                                    productDetailModel.setThumbnail_image(oneCombo.getJSONObject("Product").getString("thumbnail_image"));
                                                    productDetailModel.setBarcode(oneCombo.getJSONObject("Product").getString("barcode"));
                                                    productDetailModel.setPackSize(oneCombo.getJSONObject("Product").getJSONObject("PackSize").getString("name"));
                                                    productDetailModel.setUnit(oneCombo.getJSONObject("Product").getJSONObject("Unit").getString("name"));
                                                    productModel.setProductDetail(productDetailModel);
                                                    gets.add(productModel);
                                                }
                                                productOneModel.setGetList(gets);

                                            }

                                            productOneModel.setProduct_type("Buy1Get1FreeDeal");
                                            buyList.add(productOneModel);
                                        }
                                        buyAdapter.setData(buyList);
                                        if (buyList.size() == 0) {
                                            lytBuy.setVisibility(View.GONE);
                                        } else {
                                            lytBuy.setVisibility(View.VISIBLE);
                                        }
                                        txtPrice.setText(String.format(java.util.Locale.US,"R %.2f", Float.valueOf(String.valueOf(total_price))));
                                    } else {
                                        Toast.makeText(activity, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException jsonException) {
                                    Toast.makeText(activity, R.string.msg_offline, Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        }
    }
    private void onUpdateStock() {
        String single_quantity = "";
        for (int i = 0; i < singleList.size(); i ++) {
            single_quantity += singleList.get(i).getId() + ":" + singleList.get(i).getStock() + ",";
        }
        String combo_quantity = "";
        for (int i = 0; i < comboList.size(); i ++) {
            combo_quantity += comboList.get(i).getId() + ":" + comboList.get(i).getStock() + ",";
        }
        String buyget_quantity = "";
        for (int i = 0; i < buyList.size(); i ++) {
            buyget_quantity += buyList.get(i).getId() + ":" + buyList.get(i).getStock() + ",";
        }
        showLoadingDialog();
        Ion.with(activity)
                .load("PUT", G.SetUpdateStock)
                .addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken())
                .setBodyParameter("id", String.valueOf(order_id))
                .setBodyParameter("SingleProducts", single_quantity)
                .setBodyParameter("ComboDeals", combo_quantity)
                .setBodyParameter("Buy1Get1FreeDeals", buyget_quantity)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        if (e != null) {
                            Toast.makeText(activity, R.string.msg_something_wrong, Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("status")) {
                                    Toast.makeText(activity, R.string.msg_updated_successfully, Toast.LENGTH_LONG).show();
                                    LocalBroadcastManager.getInstance(activity).sendBroadcast(new Intent("refresh_collect_data"));
                                    finish();
                                } else {
                                    Toast.makeText(activity, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException exception) {
                                exception.printStackTrace();
                            }
                        }
                    }
                });
    }
    @OnClick({R.id.btBack, R.id.btnUpdate})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.btnUpdate:
                onUpdateStock();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
