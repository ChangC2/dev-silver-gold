package com.mobile.seemesave.view.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.ProductOneAdapter;
import com.mobile.seemesave.application.App;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.BuyGetProductCartModel;
import com.mobile.seemesave.model.common.ComboDealProductCartModel;
import com.mobile.seemesave.model.common.CurrencyRateModel;
import com.mobile.seemesave.model.common.DeliverModel;
import com.mobile.seemesave.model.common.DeliverShipModel;
import com.mobile.seemesave.model.common.DeliverStatusModel;
import com.mobile.seemesave.model.common.ProductOneModel;
import com.mobile.seemesave.model.res.CartRes;
import com.mobile.seemesave.model.res.CheckoutRes;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.menu.NotificationActivity;
import com.mobile.seemesave.view_model.checkout.CheckOutDeliverViewModel;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckOutDeliverActivity extends BaseActivity {

    private CheckOutDeliverViewModel mViewModel;
    private CheckOutDeliverActivity activity;

    @BindView(R.id.imgLogo)
    RoundedImageView imgLogo;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtDescription)
    TextView txtDescription;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.btnFollow)
    LinearLayout btnFollow;
    @BindView(R.id.txt_follow)
    TextView txt_follow;

    @BindView(R.id.editMsg)
    EditText editMsg;

    @BindView(R.id.txtTotalPrice)
    TextView txtTotalPrice;

    @BindView(R.id.txtProductPrice)
    TextView txtProductPrice;

    @BindView(R.id.txtFee)
    TextView txtFee;


    @BindView(R.id.spinnerCurrency)
    Spinner spinnerCurrency;

    @BindView(R.id.lytProduct)
    LinearLayout lytProduct;
    @BindView(R.id.recyclerProductView)
    RecyclerView recyclerProductView;
    @BindView(R.id.productIndicator)
    IndefinitePagerIndicator productIndicator;

    private ArrayList<CurrencyRateModel> currencyList = new ArrayList<>();
    private DeliverShipModel shipModel = new DeliverShipModel();
    private DeliverStatusModel statusData = new DeliverStatusModel();

    private DeliverModel cartInfo = new DeliverModel();
    private ArrayList<ProductOneModel> productList = new ArrayList<>();
    private ArrayList<ComboDealProductCartModel> comboDeals = new ArrayList<>();
    private ArrayList<BuyGetProductCartModel> buygetDeals = new ArrayList<>();
    private ProductOneAdapter productAdapter;
    int cart_id = -1;

    ArrayAdapter<CurrencyRateModel> spinnerCurrencyAdapter;
    double price = 0.0;
    private float fee_val = 0;
    private float src_fee_val = 0;
    String currency_str = "R";
    double store_lat = 0.00, store_lon = 0.00;
    String delivery_link = "";
    String m_currency_iso = "ZAR";
    private boolean add_flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CheckOutDeliverViewModel.class);
        setContentView(R.layout.activity_checkout_deliver);
        ButterKnife.bind(this);
        activity = this;
        G.setLightFullScreen(activity);
        if (!G.pref.getString("relId", "").equalsIgnoreCase("")) {
            cart_id = Integer.valueOf(G.pref.getString("relId", "-1"));
        } else {
            cart_id = getIntent().getIntExtra("cart_id", -1);
        }
        if (cart_id == -1) {
            Intent intent = new Intent(activity, NotificationActivity.class);
            startActivity(intent);
            finish();
        }
        App.getInstance().setDelverRel("", "");
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mViewModel.getIsBusy().observe(this, isBusy -> {
            if (isBusy) {
                showLoadingDialog();
            } else {
                hideLoadingDialog();
            }
        });
        mViewModel.getCurrencyData().observe(this, list -> {
            currencyList.clear();
            if (list.size() == 0) {
                return;
            } else {
                currencyList.addAll(list);
                setCurrencySpinner();
            }
        });
        mViewModel.getShipData().observe(this, info->{
            if (info != null) {
                shipModel = info;
            }
        });
        mViewModel.getStatusData().observe(this, info->{
            if (info != null) {
                statusData = info;
            }
        });
        mViewModel.getCartInfo().observe(this, info->{
            if (info != null && info.getStore() != null) {
                cartInfo = info;
                showCartInfo();
                setTxtTPrice();
            }
        });
        mViewModel.getProductList().observe(this, list -> {
            productList.clear();
            if (list.size() == 0) {
                return;
            } else {
                productList.addAll(list);
                productAdapter.setData(productList, -1);
                setTxtTPrice();
            }
        });
        mViewModel.getComboDeals().observe(this, list -> {
            comboDeals.clear();
            if (list.size() == 0) {
                return;
            } else {
                comboDeals.addAll(list);
            }
        });
        mViewModel.getBuy1Get1FreeDeals().observe(this, list -> {
            buygetDeals.clear();
            if (list.size() == 0) {
                return;
            } else {
                buygetDeals.addAll(list);
            }
        });
        mViewModel.getDeliveryFee().observe(this, value -> {
            if (value == null) return;
            src_fee_val = value;
            setTxtTPrice();
        });

    }

    private void initView() {
        currencyList.clear();
        productList.clear();
        comboDeals.clear();
        buygetDeals.clear();
        setProductRecycler();
        if (cart_id != -1) {
            mViewModel.loadBaseInfo(cart_id);
            mViewModel.loadCartInfo(cart_id);
        }
    }
    private void showCartInfo() {
        Glide.with(activity)
                .load(cartInfo.getStore().getLogo())
                .centerCrop()
                .into(imgLogo);
        txtTitle.setText(cartInfo.getStore().getName());
        if (cartInfo.getStore().isFollowing()) {
            btnFollow.setBackground(getDrawable(R.drawable.bk_blue_rect_20));
            txt_follow.setText(getString(R.string.txt_following));
            txt_follow.setTextColor(getColor(R.color.white_color));
        } else {
            btnFollow.setBackground(getDrawable(R.drawable.bk_white_rect_20));
            txt_follow.setText(getString(R.string.txt_follow));
            txt_follow.setTextColor(getColor(R.color.grey_dark));
        }
        if (G.location != null) {
            int dist = (int) G.meterDistanceBetweenPoints(G.location.getLatitude(), G.location.getLongitude(), cartInfo.getStore().getCoordinates().get(0), cartInfo.getStore().getCoordinates().get(1));
            txtDescription.setText(String.format("%.2f Km from you", ((float)dist )/ 1000.0f));
        } else {
            int dist = (int) G.meterDistanceBetweenPoints(G.user.getLatitude(), G.user.getLongitude(), cartInfo.getStore().getCoordinates().get(0), cartInfo.getStore().getCoordinates().get(1));
            txtDescription.setText(String.format("%.2f Km from you", ((float)dist )/ 1000.0f));
        }
        ratingBar.setRating(cartInfo.getStore().getRating());
        store_lat = cartInfo.getStore().getCoordinates().get(0);
        store_lon = cartInfo.getStore().getCoordinates().get(1);
        try {
            m_currency_iso = cartInfo.getCurrency().getIso();
        } catch (Exception ex) {
            m_currency_iso = "ZAR";
        }
    }
    private int getCurrencyIndex(){
        for(int i=0; i<currencyList.size(); i++){
            if(currencyList.get(i).getSimple().equalsIgnoreCase(currency_str)){
                return i;
            }
        }
        return -1;
    }
    private String getCurrencyIso(){
        for(int i=0; i<currencyList.size(); i++){
            if(currencyList.get(i).getSimple().equalsIgnoreCase(currency_str)){
                return currencyList.get(i).getIso();
            }
        }
        return "ZAR";
    }
    private void setCurrencySpinner() {
        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currency_str = currencyList.get(position).getSimple();

                setTxtTPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerCurrencyAdapter = new ArrayAdapter<CurrencyRateModel>(activity, R.layout.custom_spinner, currencyList);
        spinnerCurrencyAdapter.setDropDownViewResource(R.layout.custom_spinner_combo);
        spinnerCurrency.setAdapter(spinnerCurrencyAdapter);
        spinnerCurrency.setSelection(getCurrencyIndex());
    }
    private void setTxtTPrice(){
        price = 0.0;
        for (int i=0;i<productList.size();i++){
            double cur_price = Double.parseDouble(productList.get(i).getPrice()) * productList.get(i).getCount();
            String product_currency_iso = productList.get(i).getCurrency().getIso();
            double product_rate = getCurrencyRate(product_currency_iso);
            cur_price = cur_price * product_rate;
            price += cur_price;
        }
        float fee_rate = getCurrencyRate(m_currency_iso);
        fee_val = src_fee_val * fee_rate;
        txtProductPrice.setText(String.format(java.util.Locale.US, "%s %.2f",  currency_str, price));
        txtFee.setText(String.format(java.util.Locale.US, "%s %.2f",  currency_str, fee_val));
        txtTotalPrice.setText(String.format(java.util.Locale.US, "%s %.2f",  currency_str, price + fee_val));
    }

    private float getCurrencyRate(String product_iso) {
        float sel_cur_rate = 1;
        for (int i = 0; i < currencyList.size(); i++) {
            if (currencyList.get(i).getIso().equalsIgnoreCase(product_iso)) {
                if (getCurrencyIso().equalsIgnoreCase("ZAR")) {
                    sel_cur_rate = sel_cur_rate * currencyList.get(i).getRate().getZAR();
                } else {
                    sel_cur_rate = sel_cur_rate * currencyList.get(i).getRate().getUSD();
                }

            }
        }

        return sel_cur_rate;
    }
    private void setProductRecycler() {
        productAdapter = new ProductOneAdapter(activity, productList, false, -1, new ProductOneAdapter.ProductOneRecyclerListener() {
            @Override
            public void onRemoveCart(int pos, ProductOneModel model) {
                setTxtTPrice();
            }

            @Override
            public void onPlus(int pos, ProductOneModel model) {
                add_flag = true;
                setTxtTPrice();
            }

            @Override
            public void onMinus(int pos, ProductOneModel model) {
                add_flag = false;
                setTxtTPrice();
            }
        });
        recyclerProductView.setAdapter(productAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recyclerProductView.setLayoutManager(linearLayoutManager);
        productIndicator.attachToRecyclerView(recyclerProductView);
    }
    private void onNext() {
        Intent intent = G.isAvailableGoogleApi(activity)
                ? new Intent(activity, ShipInformationGoogleActivity.class)
                : new Intent(activity, ShipInformationGoogleActivity.class);

        intent.putExtra("cart_id", cart_id);
        intent.putExtra("description", editMsg.getText().toString().trim());
        intent.putExtra("currency", getCurrencyIso());
        intent.putExtra("store_lat", store_lat);
        intent.putExtra("store_lon", store_lon);
        Gson gson = new Gson();
        String json = gson.toJson(shipModel);
        intent.putExtra("shipData", json);
        startActivity(intent);
    }
    private void onFollow() {
        if (!cartInfo.getStore().isFollowing()) {
            btnFollow.setBackground(getDrawable(R.drawable.bk_blue_rect_20));
            txt_follow.setText(getString(R.string.txt_following));
            txt_follow.setTextColor(getColor(R.color.white_color));
        } else {
            btnFollow.setBackground(getDrawable(R.drawable.bk_white_rect_20));
            txt_follow.setText(getString(R.string.txt_follow));
            txt_follow.setTextColor(getColor(R.color.grey_dark));
        }
        cartInfo.getStore().setFollowing(!cartInfo.getStore().isFollowing());
        if (G.isNetworkAvailable(activity)) {
            Ion.with(activity)
                    .load(G.StoreFollowUrl)
                    .addHeader("Authorization", "Bearer " + G.pref.getString("token" , ""))
                    .setBodyParameter("id", String.valueOf(cartInfo.getStore().getId()))
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                        }
                    });
        } else {
            Toast.makeText(activity, R.string.msg_offline, Toast.LENGTH_LONG).show();
        }
    }
    @OnClick({R.id.imgBack, R.id.imgDelete, R.id.imgView, R.id.btnFollow, R.id.btnConfirm})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.imgDelete:
                break;
            case R.id.btnFollow:
                onFollow();
                break;
            case R.id.imgView:
                break;
            case R.id.btnConfirm:
                onNext();
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessAddCart(CartRes res) {
        if (res.isStatus()) {
            Toast.makeText(activity, add_flag ? R.string.add_success_deliver_cart : R.string.remove_success_deliver_cart, Toast.LENGTH_LONG).show();
        } else {
            if (!res.getMessage().equalsIgnoreCase("")) {
                Toast.makeText(activity, res.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessCheckout(CheckoutRes res) {
        if (res.isStatus()) {
            Intent intent = new Intent(activity, PaymentActivity.class);
            intent.putExtra("id", cart_id);
            startActivity(intent);
        } else {
            if (!res.getMessage().equalsIgnoreCase("")) {
                Toast.makeText(activity, res.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("disable_notification"));
        finish();
    }
}