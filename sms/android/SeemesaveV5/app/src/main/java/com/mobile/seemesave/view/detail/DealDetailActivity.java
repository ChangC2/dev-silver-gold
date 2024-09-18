package com.mobile.seemesave.view.detail;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.DealDetailAdapter;
import com.mobile.seemesave.adapter.VariantsAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.listener.RecyclerClickListener;
import com.mobile.seemesave.model.common.MCommon;
import com.mobile.seemesave.model.common.ProductModel;
import com.mobile.seemesave.model.common.SubImageModel;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.detail.SingleProductDetailViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DealDetailActivity extends BaseActivity {
    private SingleProductDetailViewModel mViewModel;

    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.lytCombo)
    LinearLayout lytCombo;
    @BindView(R.id.lytBuy)
    LinearLayout lytBuy;
    @BindView(R.id.recyclerComboDeal)
    RecyclerView recyclerComboDeal;
    @BindView(R.id.recyclerBuy)
    RecyclerView recyclerBuy;
    @BindView(R.id.recyclerGet)
    RecyclerView recyclerGet;

    private DealDetailActivity activity;

    private String type = "combo";
    private String title = "";
    private int pos = -1;

    private ArrayList<ProductModel> comboList = new ArrayList<>();
    private ArrayList<ProductModel> buyList = new ArrayList<>();
    private ArrayList<ProductModel> getList = new ArrayList<>();

    private DealDetailAdapter comboAdapter;
    private DealDetailAdapter buyAdapter;
    private DealDetailAdapter getAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SingleProductDetailViewModel.class);
        setContentView(R.layout.activity_deal_detail);
        ButterKnife.bind(this);
        activity = this;
        comboList.clear();
        buyList.clear();
        getList.clear();
        type = getIntent().getStringExtra("type");
        pos = getIntent().getIntExtra("pos", -1);
        Gson gson = new Gson();
        if (type.equalsIgnoreCase("combo")) {
            title = getString(R.string.txt_combo_deals);
            comboList = gson.fromJson(getIntent().getStringExtra("combodeal"), new TypeToken<ArrayList<ProductModel>>() {
            }.getType());
        } else {
            title = getString(R.string.txt_buyget);
            buyList = gson.fromJson(getIntent().getStringExtra("buys"), new TypeToken<ArrayList<ProductModel>>() {
            }.getType());
            getList = gson.fromJson(getIntent().getStringExtra("gets"), new TypeToken<ArrayList<ProductModel>>() {
            }.getType());
        }
        initView();
    }

    private void initView() {
        txtTitle.setText(title);
        if (type.equalsIgnoreCase("combo")) {
            lytCombo.setVisibility(View.VISIBLE);
            lytBuy.setVisibility(View.GONE);
            setComboRecycler();
        } else {
            lytCombo.setVisibility(View.GONE);
            lytBuy.setVisibility(View.VISIBLE);
            setBuyRecycler();
            setGetRecycler();
        }
    }
    @Override
    public void onStart() {
        super.onStart();

    }
    private void setComboRecycler() {
        comboAdapter = new DealDetailAdapter(activity, comboList, new DealDetailAdapter.DealDetailRecyclerListener() {


            @Override
            public void onItemClicked(int pos, ProductModel model) {
                Intent intent = new Intent(activity, SingleProductDetailActivity.class);
                intent.putExtra("barcode", model.getProductDetail().getBarcode());
                startActivity(intent);
            }

            @Override
            public void onItemVariantClicked(int pos, ProductModel model) {
                apiCallForGetVariants(pos, model.getId(), "combo");
            }
        });
        recyclerComboDeal.setAdapter(comboAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerComboDeal.setLayoutManager(linearLayoutManager);
    }
    private void setBuyRecycler() {
        buyAdapter = new DealDetailAdapter(activity, buyList, new DealDetailAdapter.DealDetailRecyclerListener() {


            @Override
            public void onItemClicked(int pos, ProductModel model) {
                Intent intent = new Intent(activity, SingleProductDetailActivity.class);
                intent.putExtra("barcode", model.getProductDetail().getBarcode());
                startActivity(intent);
            }

            @Override
            public void onItemVariantClicked(int pos, ProductModel model) {
                apiCallForGetVariants(pos, model.getId(), "buy");
            }
        });
        recyclerBuy.setAdapter(buyAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerBuy.setLayoutManager(linearLayoutManager);
    }
    private void setGetRecycler() {
        getAdapter = new DealDetailAdapter(activity, getList, new DealDetailAdapter.DealDetailRecyclerListener() {


            @Override
            public void onItemClicked(int pos, ProductModel model) {
                Intent intent = new Intent(activity, SingleProductDetailActivity.class);
                intent.putExtra("barcode", model.getProductDetail().getBarcode());
                startActivity(intent);
            }

            @Override
            public void onItemVariantClicked(int pos, ProductModel model) {
                apiCallForGetVariants(pos, model.getId(), "get");
            }
        });
        recyclerGet.setAdapter(getAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerGet.setLayoutManager(linearLayoutManager);
    }
    void apiCallForGetVariants(int pos, int id, String type) {
        ArrayList<MCommon> variants = new ArrayList<>();
        G.showLoading(activity);
        String token = G.pref.getString("token" , "");
        String url = String.format(java.util.Locale.US,G.GetVariantsUrl,  id);
        Ion.with(activity)
                .load(url)
                .addHeader("Authorization", "Bearer " + token)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        G.hideLoading();
                        if (e != null){
                        }else{
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("status")){
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i=0;i<jsonArray.length();i++){
                                        JSONObject variantInfo = jsonArray.getJSONObject(i);
                                        MCommon variant = new MCommon();
                                        variant.setBarcode(variantInfo.getString("barcode"));
                                        variant.setImageUrl(variantInfo.getString("thumbnail_image"));
                                        variant.setName(variantInfo.getString("Brand"));
                                        variant.setDescription(variantInfo.getString("description"));
                                        variant.setPackSize(variantInfo.getString("PackSize"));
                                        variant.setUnit(variantInfo.getString("Unit"));
                                        variants.add(variant);
                                    }
                                    if (variants.size() > 0){
                                        showVariants(variants, pos, type);
                                    }
                                }
                            } catch (JSONException jsonException) {
                            }
                        }
                    }
                });
    }
    private void showVariants(ArrayList<MCommon> variants, int position, String type){
        Dialog dialog = new Dialog(activity, R.style.DialogTheme);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.setContentView(R.layout.dlg_variant_select);
        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView txtTitle =  dialog.findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.select_product);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = dialog.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerClickListener mListner = new RecyclerClickListener() {
            @Override
            public void onClick(View v, int vPosition) {
                dialog.dismiss();
                if (type.equalsIgnoreCase("combo")) {
                    comboList.get(position).getProductDetail().setBarcode(variants.get(vPosition).getBarcode());
                    comboList.get(position).getProductDetail().setMedia(variants.get(vPosition).getImageUrl());
                    comboList.get(position).getProductDetail().setDescription(variants.get(vPosition).getDescription());
                    comboList.get(position).getProductDetail().setBrand(variants.get(vPosition).getName());
                    comboList.get(position).getProductDetail().setUnit(variants.get(vPosition).getUnit());
                    comboList.get(position).getProductDetail().setPackSize(variants.get(vPosition).getPackSize());
                    comboAdapter.setData(comboList);
                } else if (type.equalsIgnoreCase("buy")) {
                    buyList.get(position).getProductDetail().setBarcode(variants.get(vPosition).getBarcode());
                    buyList.get(position).getProductDetail().setMedia(variants.get(vPosition).getImageUrl());
                    buyList.get(position).getProductDetail().setDescription(variants.get(vPosition).getDescription());
                    buyList.get(position).getProductDetail().setBrand(variants.get(vPosition).getName());
                    buyList.get(position).getProductDetail().setUnit(variants.get(vPosition).getUnit());
                    buyList.get(position).getProductDetail().setPackSize(variants.get(vPosition).getPackSize());
                    buyAdapter.setData(buyList);
                } else {
                    getList.get(position).getProductDetail().setBarcode(variants.get(vPosition).getBarcode());
                    getList.get(position).getProductDetail().setMedia(variants.get(vPosition).getImageUrl());
                    getList.get(position).getProductDetail().setDescription(variants.get(vPosition).getDescription());
                    getList.get(position).getProductDetail().setBrand(variants.get(vPosition).getName());
                    getList.get(position).getProductDetail().setUnit(variants.get(vPosition).getUnit());
                    getList.get(position).getProductDetail().setPackSize(variants.get(vPosition).getPackSize());
                    getAdapter.setData(getList);
                }
            }

            @Override
            public void onClick(View v, int position, int type) {

            }
        };
        VariantsAdapter variantsAdapter = new VariantsAdapter(activity, variants, mListner);
        recyclerView.setAdapter(variantsAdapter);
    }
    @OnClick({R.id.btBack})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                onFinish();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onFinish();
    }

    private void onFinish()
    {
        Intent intent = new Intent("Variant");
        Gson gson = new Gson();
        String comboJson = gson.toJson(comboList);
        String buysJson = gson.toJson(buyList);
        String getsJson = gson.toJson(getList);
        intent.putExtra("combodeal", comboJson);
        intent.putExtra("buys", buysJson);
        intent.putExtra("gets", getsJson);
        intent.putExtra("type", type);
        intent.putExtra("pos", pos);
        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
        finish();
    }
}
