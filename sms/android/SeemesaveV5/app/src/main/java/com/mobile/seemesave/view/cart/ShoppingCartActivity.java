package com.mobile.seemesave.view.cart;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.CartShoppingPaidAdapter;
import com.mobile.seemesave.adapter.CartShoppingPendingAdapter;
import com.mobile.seemesave.adapter.CartShoppingUnPaidAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.ShoppingModel;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.checkout.CheckOutShoppingActivity;
import com.mobile.seemesave.view.checkout.OrderShoppingDetailActivity;
import com.mobile.seemesave.view.main.MainActivity;
import com.mobile.seemesave.view_model.cart.ShoppingCartViewModel;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShoppingCartActivity extends BaseActivity {
    private ShoppingCartViewModel mViewModel;
    private ShoppingCartActivity activity;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.txtNoShopping)
    TextView txtNoShopping;

    @BindView(R.id.recyclerUnpaid)
    RecyclerView recyclerUnpaid;

    @BindView(R.id.recyclerPaid)
    RecyclerView recyclerPaid;

    @BindView(R.id.recyclerPending)
    RecyclerView recyclerPending;

    @BindView(R.id.lytRoot)
    LinearLayout lytRoot;

    @BindView(R.id.lytUnpaid)
    LinearLayout lytUnpaid;
    @BindView(R.id.lytPaid)
    LinearLayout lytPaid;
    @BindView(R.id.lytPending)
    LinearLayout lytPending;

    @BindView(R.id.lytPaidOrderHeader)
    RelativeLayout lytPaidOrderHeader;

    @BindView(R.id.imgDrop)
    ImageView imgDrop;

    ArrayList<ShoppingModel> paidCartList = new ArrayList<>();
    ArrayList<ShoppingModel> unPaidCartList = new ArrayList<>();
    ArrayList<ShoppingModel> pendingCartList = new ArrayList<>();
    CartShoppingPaidAdapter paidAdapter;
    CartShoppingUnPaidAdapter unpaidAdapter;
    CartShoppingPendingAdapter pendingAdapter;
    boolean showPaidOrder = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ShoppingCartViewModel.class);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);
        activity = this;
        G.setLightFullScreen(activity);
        initView();
    }

    private void initView() {
        paidCartList.clear();
        unPaidCartList.clear();
        pendingCartList.clear();
        setUnPaidRecycler();
        setPaidRecycler();
        setPendingRecycler();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                if (G.isNetworkAvailable(activity)) {
                    mViewModel.loadData();
                }

            }
        });
        rotateImageDetailshow();
        lytPaidOrderHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showPaidOrder){
                    rotateImageDetailhide();
                    recyclerPaid.setVisibility(View.GONE);
                }else{
                    rotateImageDetailshow();
                    recyclerPaid.setVisibility(View.VISIBLE);
                }
                showPaidOrder = !showPaidOrder;
            }
        });
        try {
            String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "ShoppingCart", "");
            if (TextUtils.isEmpty(local_data)) {
                mViewModel.setIsBusy(true);
            } else {
                mViewModel.loadLocalData();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mViewModel.loadData();
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
        mViewModel.getUnPaidCartList().observe(this, list -> {
            unPaidCartList.clear();
            if (list.size() == 0) {
                lytUnpaid.setVisibility(View.GONE);
                return;
            } else {
                lytUnpaid.setVisibility(View.VISIBLE);
                unPaidCartList.addAll(list);
                unpaidAdapter.setData(unPaidCartList);
            }
        });
        mViewModel.getPaidCartList().observe(this, list -> {
            paidCartList.clear();
            if (list.size() == 0) {
                lytPaid.setVisibility(View.GONE);
                return;
            } else {
                lytPaid.setVisibility(View.VISIBLE);
                paidCartList.addAll(list);
                paidAdapter.setData(paidCartList);
            }
        });
        mViewModel.getCartList().observe(this, list -> {
            if (list.size() == 0) {
                txtNoShopping.setVisibility(View.VISIBLE);
                return;
            } else {
                txtNoShopping.setVisibility(View.GONE);
            }
        });
        mViewModel.getPendingCartList().observe(this, list -> {
            pendingCartList.clear();
            if (list.size() == 0) {
                lytPending.setVisibility(View.GONE);
                return;
            } else {
                lytPending.setVisibility(View.VISIBLE);
                pendingCartList.addAll(list);
                pendingAdapter.setData(pendingCartList);
            }
        });
    }

    private void setUnPaidRecycler() {
        unpaidAdapter = new CartShoppingUnPaidAdapter(activity, unPaidCartList, new CartShoppingUnPaidAdapter.CartShoppingUnPaidRecyclerListener() {


            @Override
            public void onItemClicked(int pos, ShoppingModel model) {
                Intent intent = new Intent(activity, CheckOutShoppingActivity.class);
                intent.putExtra("cart_id", model.getId());
                startActivity(intent);
            }

            @Override
            public void onRemove(int pos, ShoppingModel model) {
                Ion.with(activity)
                        .load("DELETE", G.DeleteShoppingCart)
                        .addHeader("Authorization", "Bearer " + G.pref.getString("token" , ""))
                        .setBodyParameter("id", String.valueOf(model.getId()))
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                            }
                        });
                unPaidCartList.remove(pos);
                unpaidAdapter.setData(unPaidCartList);
            }
        });
        recyclerUnpaid.setAdapter(unpaidAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerUnpaid.setLayoutManager(linearLayoutManager);
    }
    private void setPendingRecycler() {
        pendingAdapter = new CartShoppingPendingAdapter(activity, pendingCartList, new CartShoppingPendingAdapter.CartShoppingPendingRecyclerListener() {
            @Override
            public void onItemClicked(int pos, ShoppingModel model) {
                Intent intent = new Intent(activity, OrderShoppingDetailActivity.class);
                intent.putExtra("cart_id", model.getId());
                startActivity(intent);
            }

            @Override
            public void onAccept(int pos, ShoppingModel model) {
                Ion.with(activity)
                        .load("PUT", G.AcceptShoppingCartChange)
                        .addHeader("Authorization", "Bearer " + G.pref.getString("token" , ""))
                        .setBodyParameter("id", String.valueOf(model.getId()))
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                            }
                        });
                pendingCartList.remove(pos);
                pendingAdapter.setData(pendingCartList);
                LocalBroadcastManager.getInstance(activity).sendBroadcast(new Intent("refresh_count"));
            }

            @Override
            public void onReject(int pos, ShoppingModel model) {
                Ion.with(activity)
                        .load("DELETE", G.RejectShoppingCartChange)
                        .addHeader("Authorization", "Bearer " + G.pref.getString("token" , ""))
                        .setBodyParameter("id", String.valueOf(model.getId()))
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                if (G.isNetworkAvailable(activity)) {
                                    LocalBroadcastManager.getInstance(activity).sendBroadcast(new Intent("refresh_count"));
                                    mViewModel.loadData();
                                }
                            }
                        });
                pendingCartList.remove(pos);
                pendingAdapter.setData(pendingCartList);
                LocalBroadcastManager.getInstance(activity).sendBroadcast(new Intent("refresh_count"));
            }
        });
        recyclerPending.setAdapter(pendingAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerPending.setLayoutManager(linearLayoutManager);
    }
    private void setPaidRecycler() {
        paidAdapter = new CartShoppingPaidAdapter(activity, paidCartList, new CartShoppingPaidAdapter.CartShoppingPaidRecyclerListener() {


            @Override
            public void onItemClicked(int pos, ShoppingModel model) {
                Intent intent = new Intent(activity, OrderShoppingDetailActivity.class);
                intent.putExtra("cart_id", model.getId());
                startActivity(intent);
            }

            @Override
            public void onRate(int pos, ShoppingModel model) {
                showRatingDlg(model.getId());
            }

        });
        recyclerPaid.setAdapter(paidAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerPaid.setLayoutManager(linearLayoutManager);
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
    public void rotateImageDetailshow(){
        ObjectAnimator rotate = ObjectAnimator.ofFloat(imgDrop, "rotation", 180f, 0f);
        rotate.setDuration(500);
        rotate.start();
    }

    public void rotateImageDetailhide(){
        ObjectAnimator rotate = ObjectAnimator.ofFloat(imgDrop, "rotation", 0f, 180f);
        rotate.setDuration(500);
        rotate.start();
    }
    @OnClick({R.id.btBack})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                onBack();
                break;
        }
    }
    private void onBack() {
        if (getIntent().hasExtra("after_pay")) {
            Intent i = new Intent(activity, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else {
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        onBack();
    }

}
