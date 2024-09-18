package seemesave.businesshub.view.supplier.menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.SupplierAdapter;
import seemesave.businesshub.api.agent.AgentApi;
import seemesave.businesshub.api.supplier.SupplierApi;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.model.common.SupplierModel;
import seemesave.businesshub.model.res.SupplierListRes;
import seemesave.businesshub.sqlite.DatabaseQueryClass;
import seemesave.businesshub.utils.DialogUtils;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.utils.GsonUtils;
import seemesave.businesshub.view.vendor.menu.InviteAgentFragment;
import seemesave.businesshub.widget.ActionSheet;

public class VendorActivity extends BaseActivity {

    private VendorActivity activity;
    private InviteVendorFragment inviteVendorFragment;

    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.btnInvite)
    LinearLayout btnInvite;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.lytEmpty)
    LinearLayout lytEmpty;
    @BindView(R.id.edtSearch)
    EditText edtSearch;
    @BindView(R.id.imgClear)
    ImageView imgClear;

    private ArrayList<SupplierModel> supplierList = new ArrayList<>();
    private SupplierAdapter supplierAdapter;
    private int offset = 0;
    private int limit = 20;
    private boolean isLoading = false;
    private boolean isLast = false;
    private String keyword = "";

    BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "refresh_data":
                    initParam();
                    showLoadingDialog();
                    SupplierApi.getVendorList(offset, limit, keyword);
                    break;
            }
        }
    };
    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("refresh_data");
        LocalBroadcastManager.getInstance(activity).registerReceiver(updateReceiver,
                filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);
        ButterKnife.bind(this);
        activity = this;
        initView();
    }


    private void initView() {
        txtTitle.setText(R.string.txt_vendors);
        supplierList.clear();
        edtSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    keyword = edtSearch.getText().toString().toLowerCase();
                    supplierList.clear();
                    showLoadingDialog();
                    SupplierApi.getVendorList(offset, limit, keyword);
                    return true;
                }
                return false;
            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() == 0) {
                    imgClear.setVisibility(View.GONE);
                } else {
                    imgClear.setVisibility(View.VISIBLE);
                }

            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                initParam();
                showLoadingDialog();
                SupplierApi.getVendorList(offset, limit, keyword);
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
                    showLoadingDialog();
                    SupplierApi.getVendorList(offset, limit, keyword);
                }

            }
        });
        setRecycler();
        try {
            String local_data = DatabaseQueryClass.getInstance().getData(App.getUserID(), "VendorList", "");
            if (TextUtils.isEmpty(local_data)) {
                showLoadingDialog();
            } else {
                loadLocalData();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        SupplierApi.getVendorList(offset, limit, keyword);
    }
    private void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(App.getUserID(), "VendorList", "");
            if (!TextUtils.isEmpty(data)) {
                SupplierListRes localRes = GsonUtils.getInstance().fromJson(data, SupplierListRes.class);
                supplierList.addAll(localRes.getData());
                supplierAdapter.setData(supplierList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void initParam() {
        offset = 0;
        isLoading = false;
        isLast = false;
        supplierList.clear();
    }
    private void onOption(int pos, SupplierModel model) {
        ActionSheet.Item resendItem = new ActionSheet.Item(R.attr.action_sheet_backgroundShowColor, R.attr.action_sheet_backgroundShowColor, 0, 0,
                R.attr.textShowColor, R.attr.textShowColor, getString(R.string.txt_resend), 1);
        ActionSheet.Item deleteItem = new ActionSheet.Item(R.attr.action_sheet_backgroundShowColor, R.attr.action_sheet_backgroundShowColor, 0, 0,
                R.attr.textShowColor, R.attr.textShowColor, getString(R.string.txt_delete), 1);
        ActionSheet.Item cancelItem = new ActionSheet.Item(R.attr.action_sheet_backgroundShowColor, R.attr.action_sheet_backgroundShowColor, 0, 0,
                R.attr.textShowColor, R.attr.textShowColor, getString(R.string.cancel), 1);

        ActionSheet.Builder builder = ActionSheet.createBuilder(activity, getSupportFragmentManager())
                .setCancelItem(cancelItem)
                .setmTextSize(16);
        if (model.isPending()) {
            builder.setmOtherItems(resendItem, deleteItem);
        } else {
            builder.setmOtherItems(deleteItem);
        }
        builder.setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index, String itemStr) {
                        if (itemStr.equalsIgnoreCase(getString(R.string.txt_resend))) {
                            DialogUtils.showConfirmDialogWithListener(activity, getString(R.string.txt_resend_confirmation), getString(R.string.txt_accept_invitation), getString(R.string.txt_yes), getString(R.string.txt_no),
                                    (dialog, which) -> onResend(String.valueOf(model.getId())),
                                    (dialog, which) -> dialog.dismiss());
                        }
                        if (itemStr.equalsIgnoreCase(getString(R.string.txt_delete))) {
                            DialogUtils.showConfirmDialogWithListener(activity, getString(R.string.txt_delete_confirmation), getString(R.string.txt_delete_user), getString(R.string.txt_yes), getString(R.string.txt_no),
                                    (dialog, which) -> onDelete(String.valueOf(model.getId()), pos),
                                    (dialog, which) -> dialog.dismiss());
                        }
                    }
                })
                .show();

    }
    private void onResend(String id) {
        showLoadingDialog();
        Ion.with(activity)
                .load("PUT", G.ResendVendorInvitation)
                .addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken())
                .setBodyParameter("id", id)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        if (e == null) {
                            Toast.makeText(activity, getString(R.string.msg_resend_invitation), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void onDelete(String id, int pos) {
        showLoadingDialog();
        Ion.with(activity)
                .load("DELETE", G.DeleteVendorInvitation)
                .addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken())
                .setBodyParameter("id", id)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        if (e == null) {
                            supplierList.remove(pos);
                            supplierAdapter.setData(supplierList);
                        }
                    }
                });
    }
    private void setRecycler() {
        supplierAdapter = new SupplierAdapter(activity, supplierList, new SupplierAdapter.SupplierRecyclerListener() {

            @Override
            public void onItemClicked(int pos, SupplierModel model) {
                onOption(pos, model);
            }
        });
        recyclerView.setAdapter(supplierAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    private void onInvite() {
        inviteVendorFragment = new InviteVendorFragment(activity, new InviteVendorFragment.BottomFragListener() {
            @Override
            public void onDismiss() {

            }

        });
        inviteVendorFragment.setCancelable(true);
        inviteVendorFragment.show(getSupportFragmentManager(), inviteVendorFragment.getTag());
    }
    @OnClick({R.id.btBack, R.id.imgClear, R.id.btnInvite})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.btnInvite:
                onInvite();
                break;
            case R.id.imgClear:
                edtSearch.setText("");
                keyword = "";
                supplierList.clear();
                showLoadingDialog();
                SupplierApi.getVendorList(offset, limit, keyword);
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessFollowerList(SupplierListRes res) {
        hideLoadingDialog();
        if (res.isStatus()) {
            if (offset == 0 && res.getData().size() == 0) {
                supplierList.clear();
                supplierAdapter.setData(supplierList);
                lytEmpty.setVisibility(View.VISIBLE);
                return;
            } else {
                lytEmpty.setVisibility(View.GONE);
                if (offset == 0) {
                    supplierList.clear();
                }
                if (res.getData().size() < limit) {
                    isLast = true;
                }
                supplierList.addAll(res.getData());
                supplierAdapter.setData(supplierList);
                isLoading = false;
                if (offset == 0) {
                    String data = new Gson().toJson(res, new TypeToken<SupplierListRes>() {
                    }.getType());
                    DatabaseQueryClass.getInstance().insertData(
                            App.getUserID(),
                            "VendorList",
                            data,
                            "",
                            ""
                    );
                }
            }
        } else {
            if (!TextUtils.isEmpty(res.getMessage())) {
                Toast.makeText(activity, res.getMessage(), Toast.LENGTH_LONG).show();    
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
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