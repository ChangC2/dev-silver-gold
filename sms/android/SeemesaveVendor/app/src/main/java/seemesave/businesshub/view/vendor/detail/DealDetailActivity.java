package seemesave.businesshub.view.vendor.detail;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.DealDetailAdapter;
import seemesave.businesshub.adapter.VariantsAdapter;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.listener.RecyclerClickListener;
import seemesave.businesshub.model.common.MCommon;
import seemesave.businesshub.model.common.ProductModel;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.view_model.vendor.detail.SingleProductDetailViewModel;

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
        showLoadingDialog();
        String url = String.format(java.util.Locale.US, G.GetVariantsUrl,  id);
        Ion.with(activity)
                .load(url)
                .addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
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

                                        boolean variant_flag = false;
                                        if (type.equalsIgnoreCase("combo")) {
                                            if (!TextUtils.isEmpty(comboList.get(pos).getProductDetail().getVariant_string())) {
                                                if (comboList.get(pos).getProductDetail().getVariant_string().contains(variantInfo.getString("barcode"))) {
                                                    variant_flag = true;
                                                }
                                            }
                                        } else if (type.equalsIgnoreCase("buy")) {
                                            if (!TextUtils.isEmpty(buyList.get(pos).getProductDetail().getVariant_string())) {
                                                if (buyList.get(pos).getProductDetail().getVariant_string().contains(variantInfo.getString("barcode"))) {
                                                    variant_flag = true;
                                                }
                                            }
                                        } else {
                                            if (!TextUtils.isEmpty(getList.get(pos).getProductDetail().getVariant_string())) {
                                                if (getList.get(pos).getProductDetail().getVariant_string().contains(variantInfo.getString("barcode"))) {
                                                    variant_flag = true;
                                                }
                                            }
                                        }
                                        variant.setCheck(variant_flag);
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
        dialog.findViewById(R.id.imgClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String variant_str = "";
                for (int i = 0; i < variants.size(); i ++) {
                    if (variants.get(i).isCheck()) {
                        if (TextUtils.isEmpty(variant_str)) {
                            variant_str = variants.get(i).getBarcode();
                        } else {
                            variant_str = variant_str + "," + variants.get(i).getBarcode();
                        }
                    }
                }
                if (type.equalsIgnoreCase("combo")) {
                    comboList.get(position).getProductDetail().setVariant_string(variant_str);
                    comboAdapter.setData(comboList);
                } else if (type.equalsIgnoreCase("buy")) {
                    buyList.get(position).getProductDetail().setVariant_string(variant_str);
                    buyAdapter.setData(buyList);
                } else {
                    getList.get(position).getProductDetail().setVariant_string(variant_str);
                    getAdapter.setData(getList);
                }
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
