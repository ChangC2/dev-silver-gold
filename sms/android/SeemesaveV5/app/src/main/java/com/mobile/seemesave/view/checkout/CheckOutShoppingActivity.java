package com.mobile.seemesave.view.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.ProductOneAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.listener.ClickListener;
import com.mobile.seemesave.model.common.CurrencyRateModel;
import com.mobile.seemesave.model.common.PromotionModel;
import com.mobile.seemesave.model.common.ShoppingModel;
import com.mobile.seemesave.model.common.ProductOneModel;
import com.mobile.seemesave.model.common.ShoppingTimeModel;
import com.mobile.seemesave.model.req.CheckoutShoppingReq;
import com.mobile.seemesave.model.res.CartRes;
import com.mobile.seemesave.model.res.CheckoutRes;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.checkout.CheckOutShoppingViewModel;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckOutShoppingActivity extends BaseActivity {

    private CheckOutShoppingViewModel mViewModel;
    private CheckOutShoppingActivity activity;

    @BindView(R.id.imgBack)
    ImageView imgBack;
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
    @BindView(R.id.lytProduct)
    LinearLayout lytProduct;
    @BindView(R.id.lytExclusive)
    LinearLayout lytExclusive;
    @BindView(R.id.recyclerProductView)
    RecyclerView recyclerProductView;
    @BindView(R.id.recyclerExclusiveView)
    RecyclerView recyclerExclusiveView;
    @BindView(R.id.productIndicator)
    IndefinitePagerIndicator productIndicator;
    @BindView(R.id.exclusiveIndicator)
    IndefinitePagerIndicator exclusiveIndicator;

    @BindView(R.id.spinnerCurrency)
    Spinner spinnerCurrency;
    @BindView(R.id.spinnerDate)
    Spinner spinnerDate;
    @BindView(R.id.spinnerTime)
    Spinner spinnerTime;
    @BindView(R.id.editMsg)
    EditText editMsg;
    @BindView(R.id.txtTotalPrice)
    TextView txtTotalPrice;

    private ShoppingModel cartInfo = new ShoppingModel();
    private ArrayList<ProductOneModel> productList = new ArrayList<>();
    private ProductOneAdapter productAdapter;
    private ArrayList<ProductOneModel> exclusiveDeals = new ArrayList<>();
    private ArrayList<PromotionModel> exclusiveList = new ArrayList<>();
    private ProductOneAdapter exclusiveAdapter;
    ArrayAdapter<CurrencyRateModel> spinnerCurrencyAdapter;
    ArrayList<CurrencyRateModel> currencyList = new ArrayList<>();
    double price = 0.0;
    String currency_str = "R";

    ArrayList<ShoppingTimeModel> arrayDate = new ArrayList<>();
    ArrayList<String> arrayTime = new ArrayList<>();
    ArrayAdapter<ShoppingTimeModel> spinnerDateAdapter;
    ArrayAdapter<String> spinnerTimeAdapter;
    int dateIndex = 0;
    int timeIndex = 0;
    boolean add_flag = true;
    int cart_id = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CheckOutShoppingViewModel.class);
        setContentView(R.layout.activity_checkout_shopping);
        ButterKnife.bind(this);
        activity = this;
        G.setLightFullScreen(activity);
        cart_id = getIntent().getIntExtra("cart_id", -1);
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
        mViewModel.getDateList().observe(this, list -> {
            arrayDate.clear();
            if (list.size() == 0) {
                return;
            } else {
                arrayDate.addAll(list);
                setDateSpinner();
            }
        });
        mViewModel.getCartInfo().observe(this, info->{
            if (info != null && info.getStore() != null) {
                cartInfo = info;
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
                ratingBar.setRating((float) cartInfo.getStore().getRating());
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
        mViewModel.getExclusiveDeals().observe(this, list -> {
            exclusiveDeals.clear();
            if (list.size() == 0) {
                lytExclusive.setVisibility(View.GONE);
                return;
            } else {
                lytExclusive.setVisibility(View.VISIBLE);
                exclusiveDeals.addAll(list);
                exclusiveAdapter.setData(exclusiveDeals, -1);
            }
        });
        mViewModel.getExclusiveList().observe(this, list -> {
            exclusiveList.clear();
            if (list.size() == 0) {
                return;
            } else {
                exclusiveList.addAll(list);
            }
        });
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
    private void setTxtTPrice(){
        price = 0.0;
        for (int j=0;j<productList.size();j++){
            double cur_price = Double.parseDouble(productList.get(j).getPrice()) * productList.get(j).getCount();
            String product_currency_iso = productList.get(j).getCurrency().getIso();
            double product_rate = getCurrencyRate(product_currency_iso);
            cur_price = cur_price * product_rate;
            price += cur_price;
        }
        txtTotalPrice.setText(String.format(java.util.Locale.US, "%s %.2f",  currency_str, price));
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
    private void getTimes(){
        if (arrayDate == null || arrayDate.size() < dateIndex) return;
        arrayTime.clear();
        for (int i=0;i<arrayDate.get(dateIndex).getTime().size();i++){
            int hour = arrayDate.get(dateIndex).getTime().get(i);
            arrayTime.add(String.format("%02d:00~%02d:00", hour, hour+1));
        }
    }

    private int getHour(){
        if (arrayDate == null) return 0;
        return arrayDate.get(dateIndex).getTime().get(timeIndex);
    }
    private void setDateSpinner() {
        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dateIndex = position;
                getTimes();
                setTimeSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerDateAdapter = new ArrayAdapter<ShoppingTimeModel>(this, R.layout.custom_spinner, arrayDate);
        spinnerDateAdapter.setDropDownViewResource(R.layout.custom_spinner_combo);
        spinnerDate.setAdapter(spinnerDateAdapter);
    }

    private void setTimeSpinner() {
        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerTimeAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner, arrayTime);
        spinnerTimeAdapter.setDropDownViewResource(R.layout.custom_spinner_combo);
        spinnerTime.setAdapter(spinnerTimeAdapter);
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

    private void initView() {
        productList.clear();
        exclusiveDeals.clear();
        exclusiveList.clear();
        currencyList.clear();
        arrayDate.clear();
        arrayTime.clear();
        setProductRecycler();
        setExclusiveRecycler();

        if (cart_id != -1) {
            mViewModel.loadBaseInfo(cart_id);
            mViewModel.loadCartInfo(cart_id);
            mViewModel.loadExclusiveDeal(cart_id);
        }
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
    private void setExclusiveRecycler() {
        exclusiveAdapter = new ProductOneAdapter(activity, exclusiveDeals, false, -1,  new ProductOneAdapter.ProductOneRecyclerListener() {
            @Override
            public void onRemoveCart(int pos, ProductOneModel model) {

            }

            @Override
            public void onPlus(int pos, ProductOneModel model) {

            }

            @Override
            public void onMinus(int pos, ProductOneModel model) {

            }
        });
        recyclerExclusiveView.setAdapter(exclusiveAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recyclerExclusiveView.setLayoutManager(linearLayoutManager);
        exclusiveIndicator.attachToRecyclerView(recyclerExclusiveView);
    }
    private void onCheckOut() {
        if (G.isNetworkAvailable(activity)) {
            if (price < 200 * getCurrencyRate("ZAR")){
                Toast.makeText(activity, String.format("The minimum order value is %s for Click and Collect from this store.", currency_str + " " + 200 * getCurrencyRate("ZAR")), Toast.LENGTH_LONG).show();
                return;
            }
            CheckoutShoppingReq req = new CheckoutShoppingReq();
            req.setId(cart_id);
            req.setDescription(editMsg.getText().toString().trim());
            req.setEstimate_date(arrayDate.get(dateIndex).getDate());
            req.setEstimate_hour(String.valueOf(getHour()));
            req.setCurrency(getCurrencyIso());
            mViewModel.checkOut(req);
        } else {
            Toast.makeText(activity, R.string.msg_offline, Toast.LENGTH_LONG).show();
        }
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
                ClickListener listener = new ClickListener() {
                    @Override
                    public void onClick(boolean flag) {
                        onCheckOut();
                    }
                };
                G.showDlg(activity, "SeeMeSave does not accept any cancelation of orders, all cancelations and refunds have to be negotiated with the relevant store that you are purchasing from.", listener, true);
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessAddCart(CartRes res) {
        if (res.isStatus()) {
            Toast.makeText(activity, add_flag ? R.string.add_success_shopping_cart : R.string.remove_success_shopping_cart, Toast.LENGTH_LONG).show();
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
        finish();
    }
}