package com.mobile.seemesave.view.menu;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.NotificationAdapter;
import com.mobile.seemesave.application.App;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.listener.ClickListener;
import com.mobile.seemesave.model.common.NotificationModel;
import com.mobile.seemesave.model.res.NotificationRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.DialogUtils;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.cart.CartActivity;
import com.mobile.seemesave.view.cart.DeliverCartActivity;
import com.mobile.seemesave.view.cart.ShoppingCartActivity;
import com.mobile.seemesave.view.detail.BrandDetailActivity;
import com.mobile.seemesave.view.detail.ProductDetailActivity;
import com.mobile.seemesave.view.detail.PromotionDetailActivity;
import com.mobile.seemesave.view.detail.StoreDetailActivity;
import com.mobile.seemesave.view.detail.VideoPlayerActivity;
import com.mobile.seemesave.view.profile.FriendActivity;
import com.mobile.seemesave.view_model.menu.NotificationViewModel;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationActivity extends BaseActivity {

    private NotificationViewModel mViewModel;
    private NotificationActivity activity;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.imgBack)
    ImageView imgBack;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.li_empty)
    LinearLayout li_empty;
    @BindView(R.id.imgDelete)
    ImageView imgDelete;

    NotificationAdapter notificationAdapter;
    private ArrayList<NotificationModel> notificationList = new ArrayList<>();

    private int offset = 0;
    private int limit = 20;
    private boolean isLoading = false;
    private boolean isLast = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        activity = this;
//        if (!App.getInstance().CartNotiRelID.equalsIgnoreCase("")) {
//            finish();
//        }
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.getIsBusy().observe(this, isBusy -> {
            if (isBusy){
                showLoadingDialog();
            }else {
                hideLoadingDialog();
            }
        });
        mViewModel.getNotificationList().observe(this, list -> {
            if (offset == 0 && list.size() == 0) {
                li_empty.setVisibility(View.VISIBLE);
                imgDelete.setVisibility(View.GONE);
                return;
            } else {
                li_empty.setVisibility(View.GONE);
                imgDelete.setVisibility(View.VISIBLE);
                if (offset == 0) {
                    notificationList.clear();
                }
                if (list.size() < limit) {
                    isLast = true;
                }
                notificationList.addAll(list);
                notificationAdapter.setData(notificationList);
                isLoading = false;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initView() {
        nestedScrollView.setNestedScrollingEnabled(false);
        notificationList.clear();
        setRecycler();
        try {
            String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "Notification", "");
            if (TextUtils.isEmpty(local_data)) {
                mViewModel.setIsBusy(true);
            } else {
                mViewModel.loadLocalData();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mViewModel.loadData();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                initParam();
                mViewModel.setIsBusy(true);
                mViewModel.loadData();
            }
        });
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
                int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));
                if (diff == 0 && !isLoading && !isLast) {
                    isLoading = true;
                    offset = offset + limit;
                    mViewModel.setOffset(offset);
                    mViewModel.setIsBusy(true);
                    mViewModel.loadData();
                }

            }
        });
    }
    private void initParam() {
        offset = 0;
        mViewModel.setOffset(offset);
    }
    private void setRecycler() {
        notificationAdapter = new NotificationAdapter(activity, notificationList, new NotificationAdapter.NotificationRecyclerListener() {
            @Override
            public void onItemClicked(int pos, NotificationModel model) {
                apiCallForReadMessage(model.getId());
                notificationList.get(pos).setIs_read(true);
                notificationAdapter.setData(notificationList);
                if (model.isCanAccept()) {
                    showAcceptDlg(model);
                } else {
                    viewDetail(model);
                }
            }

            @Override
            public void onOption(int pos, NotificationModel model) {
                showOptionDlg(pos);
            }
        });
        recyclerView.setAdapter(notificationAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    private void showAcceptDlg(NotificationModel model) {
        View dialogView = activity.getLayoutInflater().inflate(R.layout.dlg_accept_notification, null);

        final android.app.AlertDialog dlg = new android.app.AlertDialog.Builder(activity)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dlg.setCanceledOnTouchOutside(true);
        TextView txtTitle = dialogView.findViewById(R.id.title);
        TextView content = dialogView.findViewById(R.id.confirm_txt);
        TextView btnAccept = dialogView.findViewById(R.id.btnYes);
        TextView btnCancel = dialogView.findViewById(R.id.btnCancel);
        content.setText(model.getSenderName() + model.getNotificationMessage());
        if (model.getNotification_type().equalsIgnoreCase("Friend")) {
            txtTitle.setText(getResources().getString(R.string.friend_request));
        }
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiCallForAcceptOrRejectMessage(String.valueOf(model.getId()), 1, model);
                dlg.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiCallForAcceptOrRejectMessage(String.valueOf(model.getId()), 2, model);
                dlg.dismiss();
            }
        });

        dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dlg.show();
    }
    private void viewDetail(NotificationModel model) {
        if (model.getDetailPageType().equalsIgnoreCase("Promotion")) {
            Intent intent = new Intent(activity, PromotionDetailActivity.class);
            intent.putExtra("id", Integer.valueOf(model.getDetailID()));
            if (model.getNotification_type().equalsIgnoreCase("BestDealCreate")) {
                intent.putExtra("disable", true);
            }
            startActivity(intent);
            return;
        }
        if (model.getDetailPageType().equalsIgnoreCase("Store")) {
            Intent intent = new Intent(activity, StoreDetailActivity.class);
            intent.putExtra("id", Integer.valueOf(model.getDetailID()));
            startActivity(intent);
            return;
        }
        if (model.getDetailPageType().equalsIgnoreCase("Post")) {
            Intent intent = new Intent(activity, VideoPlayerActivity.class);
            intent.putExtra("post_id", Integer.valueOf(model.getDetailID()));
            startActivity(intent);
            return;
        }
        if (model.getDetailPageType().equalsIgnoreCase("Brand")) {
            Intent intent = new Intent(activity, BrandDetailActivity.class);
            intent.putExtra("id", Integer.valueOf(model.getDetailID()));
            startActivity(intent);
            return;
        }
        if (model.getDetailPageType().equalsIgnoreCase("CollectCart")) {
            Intent intent = new Intent(activity, ShoppingCartActivity.class);
            startActivity(intent);
            return;
        }
        if (model.getDetailPageType().equalsIgnoreCase("DeliverCart")) {
            Intent intent = new Intent(activity, DeliverCartActivity.class);
            startActivity(intent);
            return;
        }
        if (model.getDetailPageType().equalsIgnoreCase("Product")) {
            Intent intent = new Intent(activity, ProductDetailActivity.class);
            intent.putExtra("barcode", model.getDetailID());
            startActivity(intent);
            return;
        }
        if (model.getDetailPageType().equalsIgnoreCase("Friend")) {
            Intent intent = new Intent(activity, FriendActivity.class);
            startActivity(intent);
            return;
        }
    }
    private void showOptionDlg(int pos){
        View dialogView = activity.getLayoutInflater().inflate(R.layout.dlg_notification_option, null);

        final android.app.AlertDialog dlg = new android.app.AlertDialog.Builder(activity)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dlg.setCanceledOnTouchOutside(true);
        LinearLayout btnDelete = dialogView.findViewById(R.id.btnDelete);
        LinearLayout btnCancel = dialogView.findViewById(R.id.btnCancel);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiDeleteNotification(pos);
                dlg.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        dlg.setCanceledOnTouchOutside(false);
        dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dlg.show();
    }
    private void apiDeleteNotification(int position) {
        if (notificationList == null || notificationList.size() == 0) return;
        if (G.isNetworkAvailable(activity)) {
            Ion.with(activity)
                    .load("DELETE", G.DeleteNotification)
                    .addHeader("Authorization", "Bearer " + G.pref.getString("token" , ""))
                    .setBodyParameter("notification_id", String.valueOf(notificationList.get(position).getId()))
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            notificationList.remove(position);
                            notificationAdapter.setData(notificationList);
                            DatabaseQueryClass.getInstance().insertData(
                                    G.getUserID(),
                                    "Notification",
                                    "",
                                    "",
                                    ""
                            );
                        }
                    });
        } else {
            Toast.makeText(activity, R.string.msg_offline, Toast.LENGTH_LONG).show();
        }

    }
    private void apiCallForReadMessage(int id) {
        Ion.with(activity)
                .load("PUT", G.SetNotificationReadUrl)
                .addHeader("Authorization", "Bearer " + G.pref.getString("token", ""))
                .setBodyParameter("notification_id", String.valueOf(id))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                    }
                });
    }

    private void apiCallForAcceptOrRejectMessage(String id, int type, NotificationModel model) {
        Ion.with(activity)
                .load("POST", type == 1 ? G.AcceptNotification : G.RejectNotification)
                .addHeader("Authorization", "Bearer " + G.pref.getString("token", ""))
                .setBodyParameter("notification_id", id)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                    }
                });
        if (type == 1)
            viewDetail(model);
    }
    private void onClear() {
        if (notificationList.size() == 0) return;
        ClickListener listener = new ClickListener() {
            @Override
            public void onClick(boolean flag) {
                notificationList.clear();
                notificationAdapter.setData(notificationList);
                li_empty.setVisibility(View.VISIBLE);
                imgDelete.setVisibility(View.GONE);
                Ion.with(NotificationActivity.this)
                        .load("DELETE", G.ClearNotification)
                        .addHeader("Authorization", "Bearer " + G.pref.getString("token", ""))
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                DatabaseQueryClass.getInstance().insertData(
                                        G.getUserID(),
                                        "Notification",
                                        "",
                                        "",
                                        ""
                                );
                            }
                        });

            }
        };
        G.showDlg(activity, getString(R.string.confirm_clear_notification), listener, true);
    }
    @OnClick({R.id.imgBack, R.id.imgDelete})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.imgDelete:
                onClear();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}