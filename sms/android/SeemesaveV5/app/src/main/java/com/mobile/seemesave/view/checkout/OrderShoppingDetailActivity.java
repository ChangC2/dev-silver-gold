package com.mobile.seemesave.view.checkout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.OrderBuy1Get1Adapter;
import com.mobile.seemesave.adapter.OrderComboAdapter;
import com.mobile.seemesave.adapter.OrderSingleAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.BuyGetProductCartModel;
import com.mobile.seemesave.model.common.ComboDealProductCartModel;
import com.mobile.seemesave.model.common.ShoppingModel;
import com.mobile.seemesave.model.common.SingleProductCartModel;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.checkout.CheckOutShoppingViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderShoppingDetailActivity extends BaseActivity {
    private CheckOutShoppingViewModel mViewModel;
    private OrderShoppingDetailActivity activity;

    @BindView(R.id.imgPhoto)
    ImageView imgPhoto;

    @BindView(R.id.txtName)
    TextView txtName;

    @BindView(R.id.txtTPrice)
    TextView txtTPrice;

    @BindView(R.id.txtTime)
    TextView txtTime;

    @BindView(R.id.txtTotalPrice)
    TextView txtTotalPrice;

    @BindView(R.id.txtDescription)
    TextView txtDescription;
    @BindView(R.id.lytDesc)
    LinearLayout lytDesc;

    @BindView(R.id.lytSingle)
    LinearLayout lytSingle;

    @BindView(R.id.lytCombo)
    LinearLayout lytCombo;

    @BindView(R.id.lytBuy)
    LinearLayout lytBuy;

    @BindView(R.id.txtDist)
    TextView txtDist;

    @BindView(R.id.recyclerSingle)
    RecyclerView recyclerSingle;

    @BindView(R.id.recyclerCombo)
    RecyclerView recyclerCombo;

    @BindView(R.id.recyclerBuy1Get1)
    RecyclerView recyclerBuy1Get1;

    @BindView(R.id.txt_follow)
    TextView txt_follow;

    @BindView(R.id.btnFollow)
    LinearLayout btnFollow;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.txtPick)
    TextView txtPick;

    private ShoppingModel cartInfo = new ShoppingModel();
    private ArrayList<SingleProductCartModel> singleList = new ArrayList<>();
    private ArrayList<ComboDealProductCartModel> comboList = new ArrayList<>();
    private ArrayList<BuyGetProductCartModel> buygetList = new ArrayList<>();
    private OrderSingleAdapter singleAdapter;
    private OrderComboAdapter comboAdapter;
    private OrderBuy1Get1Adapter buy1Get1Adapter;
    private double total_price_val = 0;
    private String total_price_val_simple = "";
    private String fee_val_simple = "";
    private double fee_val = 0;

    private int cart_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CheckOutShoppingViewModel.class);
        setContentView(R.layout.activity_order_shopping_detail);
        ButterKnife.bind(this);
        activity = this;
        cart_id = getIntent().getIntExtra("cart_id", -1);
        initView();
    }

    private void initView() {
        singleList.clear();
        comboList.clear();
        buygetList.clear();
        setRecyclerSingle();
        setRecyclerCombo();
        setRecyclerBuyGet();
        if (cart_id != -1) {
            mViewModel.loadCartInfo(cart_id);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.getIsBusy().observe(this, isBusy -> {
            if (isBusy) {
                showLoadingDialog();
            } else {
                hideLoadingDialog();
            }
        });
        mViewModel.getCartInfo().observe(this, info -> {
            if (info != null && info.getStore() != null) {
                cartInfo = info;
                showCartInfo();
            }
        });
        mViewModel.getSingleProducts().observe(this, list -> {
            singleList.clear();
            if (list.size() == 0) {
                lytSingle.setVisibility(View.GONE);
                return;
            } else {
                lytSingle.setVisibility(View.VISIBLE);
                singleList.addAll(list);
                setTxtTPrice();
                singleAdapter.setData(singleList);
            }
        });
        mViewModel.getComboDeals().observe(this, list -> {
            comboList.clear();
            if (list.size() == 0) {
                lytCombo.setVisibility(View.GONE);
                return;
            } else {
                lytCombo.setVisibility(View.VISIBLE);
                comboList.addAll(list);
                setTxtTPrice();
                comboAdapter.setData(comboList);
            }
        });
        mViewModel.getBuy1Get1FreeDeals().observe(this, list -> {
            buygetList.clear();
            if (list.size() == 0) {
                lytBuy.setVisibility(View.GONE);
                return;
            } else {
                lytBuy.setVisibility(View.VISIBLE);
                buygetList.addAll(list);
                setTxtTPrice();
                buy1Get1Adapter.setData(buygetList);
            }
        });

    }

    private void setRecyclerSingle() {
        singleAdapter = new OrderSingleAdapter(activity, singleList, new OrderSingleAdapter.OrderSingleRecyclerListener() {


            @Override
            public void onItemClicked(int pos, SingleProductCartModel model) {

            }

            @Override
            public void onStar(int pos, SingleProductCartModel model) {

            }
        });
        recyclerSingle.setAdapter(singleAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerSingle.setLayoutManager(linearLayoutManager);
    }

    private void setRecyclerCombo() {
        comboAdapter = new OrderComboAdapter(activity, comboList, new OrderComboAdapter.ComboRecyclerListener() {

            @Override
            public void onItemClicked(int pos, ComboDealProductCartModel model) {

            }
        });
        recyclerCombo.setAdapter(comboAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerCombo.setLayoutManager(linearLayoutManager);
    }

    private void setRecyclerBuyGet() {
        buy1Get1Adapter = new OrderBuy1Get1Adapter(activity, buygetList, new OrderBuy1Get1Adapter.OrderBuy1Get1RecyclerListener() {

            @Override
            public void onItemClicked(int pos, BuyGetProductCartModel model) {

            }
        });
        recyclerBuy1Get1.setAdapter(buy1Get1Adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerBuy1Get1.setLayoutManager(linearLayoutManager);
    }

    @SuppressLint("DefaultLocale")
    private void showCartInfo() {

        if (TextUtils.isEmpty(cartInfo.getEstimate_date()) || cartInfo.getEstimate_date() == "null") {
            String strTime = cartInfo.getEstimate_time();
            txtTime.setText(strTime);
        } else {
            String strTime = cartInfo.getEstimate_date();
            strTime = String.format("%s %02d:00~%02d:00", strTime, cartInfo.getEstimate_hour(), cartInfo.getEstimate_hour() + 1);
            txtTime.setText(strTime);
        }
        Log.e("note:1", cartInfo.getDescription().trim());
        if (TextUtils.isEmpty(cartInfo.getDescription().trim()) || cartInfo.getDescription().equalsIgnoreCase("null")) {
            lytDesc.setVisibility(View.GONE);
        } else {
            txtDescription.setText(cartInfo.getDescription());
            lytDesc.setVisibility(View.VISIBLE);
        }


        Glide.with(activity)
                .load(cartInfo.getStore().getLogo())
                .centerCrop()
                .into(imgPhoto);
        txtName.setText(cartInfo.getStore().getName());
        if (cartInfo.getStore().isFollowing()) {
            btnFollow.setBackground(getDrawable(R.drawable.bk_blue_rect_20));
            txt_follow.setText(getString(R.string.txt_following));
            txt_follow.setTextColor(getColor(R.color.white_color));
        } else {
            btnFollow.setBackground(getDrawable(R.drawable.bk_white_rect_20));
            txt_follow.setText(getString(R.string.txt_follow));
            txt_follow.setTextColor(getColor(R.color.grey_dark));
        }
        int dist;
        if (G.location != null) {
            dist = (int) G.meterDistanceBetweenPoints(G.location.getLatitude(), G.location.getLongitude(), cartInfo.getStore().getCoordinates().get(0), cartInfo.getStore().getCoordinates().get(1));
        } else {
            dist = (int) G.meterDistanceBetweenPoints(G.user.getLatitude(), G.user.getLongitude(), cartInfo.getStore().getCoordinates().get(0), cartInfo.getStore().getCoordinates().get(1));
        }
        txtDist.setText(String.format("%.2f Km from you", ((float) dist) / 1000.0f));
        ratingBar.setRating(cartInfo.getStore().getRating());

    }

    private void setTxtTPrice() {

        double price = 0.0;
        String currency_str = "R";
        for (int j = 0; j < singleList.size(); j++) {
            price += Double.parseDouble(singleList.get(j).getProductDetails().getSelling_price()) * singleList.get(j).getCount();
            currency_str = singleList.get(0).getProductDetails().getCurrency().getSimple();
        }

        for (int j = 0; j < comboList.size(); j++) {
            price += Double.parseDouble(comboList.get(j).getProductDetails().getSelling_price()) * comboList.get(j).getCount();
            currency_str = comboList.get(0).getProductDetails().getCurrency().getSimple();
        }
        for (int j = 0; j < buygetList.size(); j++) {
            price += Double.parseDouble(buygetList.get(j).getProductDetails().getSelling_price()) * buygetList.get(j).getCount();
            currency_str = buygetList.get(0).getProductDetails().getCurrency().getSimple();
        }
        txtTPrice.setText(String.format(java.util.Locale.US, "%s%.2f", currency_str, price));
        txtTotalPrice.setText(String.format(java.util.Locale.US, "%s%.2f", currency_str, price));
    }

    public void showRatingDlg(int id) {
        Dialog dialog = new Dialog(activity, R.style.DialogTheme);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.setContentView(R.layout.dlg_rating);
        EditText editMsg = dialog.findViewById(R.id.editMsg);
        RatingBar ratingBar = dialog.findViewById(R.id.ratingbar);
        dialog.findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                JsonObject json = new JsonObject();
                json.addProperty("id", id);
                json.addProperty("comment", editMsg.getText().toString().trim());
                json.addProperty("rating", (int) ratingBar.getRating());
                String token = G.pref.getString("token", "");
                Ion.with(activity)
                        .load(G.OrderRating)
                        .addHeader("Authorization", "Bearer " + token)
                        .addHeader("Content-Type", "application/json")
                        .setJsonObjectBody(json)
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                Toast.makeText(activity, "Review submitted", Toast.LENGTH_LONG).show();
                            }
                        });
                G.hideSoftKeyboard(activity);
            }
        });
        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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
                    .addHeader("Authorization", "Bearer " + G.pref.getString("token", ""))
                    .setBodyParameter("id", String.valueOf(cartInfo.getStore().getId()))
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                        }
                    });
        }
    }

    @OnClick({R.id.btBack, R.id.btnRate, R.id.btnFollow})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.btnRate:
                showRatingDlg(cart_id);
                break;
            case R.id.btnFollow:
                onFollow();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
