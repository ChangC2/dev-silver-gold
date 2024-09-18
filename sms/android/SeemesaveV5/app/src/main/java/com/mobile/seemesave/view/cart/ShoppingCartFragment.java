package com.mobile.seemesave.view.cart;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mobile.seemesave.base.BaseFragment;
import com.mobile.seemesave.model.common.ShoppingModel;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.checkout.CheckOutShoppingActivity;
import com.mobile.seemesave.view.checkout.OrderShoppingDetailActivity;
import com.mobile.seemesave.view_model.cart.ShoppingCartViewModel;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingCartFragment  extends BaseFragment {
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


    CartActivity activity;


    ArrayList<ShoppingModel> paidCartList = new ArrayList<>();
    ArrayList<ShoppingModel> unPaidCartList = new ArrayList<>();
    ArrayList<ShoppingModel> pendingCartList = new ArrayList<>();
    CartShoppingPaidAdapter paidAdapter;
    CartShoppingUnPaidAdapter unpaidAdapter;
    CartShoppingPendingAdapter pendingAdapter;

    private ShoppingCartViewModel mViewModel;
    private View mFragView;
    private boolean firstLoading = false;
    boolean showPaidOrder = true;
    public ShoppingCartFragment() {
    }

    public static ShoppingCartFragment newInstance() {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ShoppingCartViewModel.class);
        mFragView = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        ButterKnife.bind(this, mFragView);
        initView();
        return mFragView;
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
            } else {
                lytPaid.setVisibility(View.VISIBLE);
                paidCartList.addAll(list);
                paidAdapter.setData(paidCartList);
            }
        });
        mViewModel.getPendingCartList().observe(this, list -> {
            pendingCartList.clear();
            if (list.size() == 0) {
                lytPending.setVisibility(View.GONE);
            } else {
                lytPending.setVisibility(View.VISIBLE);
                pendingCartList.addAll(list);
                pendingAdapter.setData(pendingCartList);
            }
        });

        mViewModel.getCartList().observe(this, list -> {
            if (list.size() == 0) {
                txtNoShopping.setVisibility(View.VISIBLE);
            } else {
                txtNoShopping.setVisibility(View.GONE);
            }
        });
    }

    public void loadData() {
        if (!firstLoading) {
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
            firstLoading = true;
        }
    }
    private void initView() {
        paidCartList.clear();
        unPaidCartList.clear();
        pendingCartList.clear();
        setUnPaidRecycler();
        setPendingRecycler();
        setPaidRecycler();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                if (G.isNetworkAvailable(getContext())) {
                    LocalBroadcastManager.getInstance(activity).sendBroadcast(new Intent("refresh_count"));
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
    }
    private void setUnPaidRecycler() {
        unpaidAdapter = new CartShoppingUnPaidAdapter(getActivity(), unPaidCartList, new CartShoppingUnPaidAdapter.CartShoppingUnPaidRecyclerListener() {
            @Override
            public void onItemClicked(int pos, ShoppingModel model) {
                Intent intent = new Intent(getActivity(), CheckOutShoppingActivity.class);
                intent.putExtra("cart_id", model.getId());
                startActivity(intent);
            }

            @Override
            public void onRemove(int pos, ShoppingModel model) {
                Ion.with(getContext())
                        .load("DELETE", G.DeleteShoppingCart)
                        .addHeader("Authorization", "Bearer " + G.pref.getString("token" , ""))
                        .setBodyParameter("id", String.valueOf(model.getId()))
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                unPaidCartList.remove(pos);
                                unpaidAdapter.setData(unPaidCartList);
                                LocalBroadcastManager.getInstance(activity).sendBroadcast(new Intent("refresh_count"));
                                if (unPaidCartList.size() == 0 && paidCartList.size() == 0 && pendingCartList.size() == 0){
                                    lytUnpaid.setVisibility(View.GONE);
                                    txtNoShopping.setVisibility(View.VISIBLE);
                                }else{
                                    txtNoShopping.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
        recyclerUnpaid.setAdapter(unpaidAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerUnpaid.setLayoutManager(linearLayoutManager);
    }
    private void setPendingRecycler() {
        pendingAdapter = new CartShoppingPendingAdapter(getActivity(), pendingCartList, new CartShoppingPendingAdapter.CartShoppingPendingRecyclerListener() {
            @Override
            public void onItemClicked(int pos, ShoppingModel model) {
                Intent intent = new Intent(getActivity(), OrderShoppingDetailActivity.class);
                intent.putExtra("cart_id", model.getId());
                startActivity(intent);
            }

            @Override
            public void onAccept(int pos, ShoppingModel model) {
                Ion.with(getContext())
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
                Ion.with(getContext())
                        .load("DELETE", G.RejectShoppingCartChange)
                        .addHeader("Authorization", "Bearer " + G.pref.getString("token" , ""))
                        .setBodyParameter("id", String.valueOf(model.getId()))
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                if (G.isNetworkAvailable(getContext())) {
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerPending.setLayoutManager(linearLayoutManager);
    }
    private void setPaidRecycler() {
        paidAdapter = new CartShoppingPaidAdapter(getActivity(), paidCartList, new CartShoppingPaidAdapter.CartShoppingPaidRecyclerListener() {
            @Override
            public void onItemClicked(int pos, ShoppingModel model) {
                Intent intent = new Intent(getActivity(), OrderShoppingDetailActivity.class);
                intent.putExtra("cart_id", model.getId());
                startActivity(intent);
            }

            @Override
            public void onRate(int pos, ShoppingModel model) {
                showRatingDlg(model.getId());
            }

        });
        recyclerPaid.setAdapter(paidAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerPaid.setLayoutManager(linearLayoutManager);
    }
    public void showRatingDlg(int id) {
        Dialog dialog = new Dialog(getActivity(), R.style.DialogTheme);
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
}
