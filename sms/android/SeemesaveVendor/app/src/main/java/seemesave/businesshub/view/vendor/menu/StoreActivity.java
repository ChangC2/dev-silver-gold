package seemesave.businesshub.view.vendor.menu;

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
import seemesave.businesshub.adapter.StoreAdapter;
import seemesave.businesshub.api.product.ProductApi;
import seemesave.businesshub.api.store.StoreApi;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.model.common.StoreModel;
import seemesave.businesshub.model.res.StoreListRes;
import seemesave.businesshub.sqlite.DatabaseQueryClass;
import seemesave.businesshub.utils.DialogUtils;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.utils.GsonUtils;
import seemesave.businesshub.view.vendor.detail.StoreCommentActivity;
import seemesave.businesshub.widget.ActionSheet;

public class StoreActivity extends BaseActivity {

    private StoreActivity activity;

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

    private ArrayList<StoreModel> storeList = new ArrayList<>();
    private StoreAdapter storeAdapter;
    private int offset = 0;
    private int limit = 20;
    private boolean isLoading = false;
    private boolean isLast = false;
    private String keyword = "";

    BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "refresh_store":
                    initParam();
                    showLoadingDialog();
                    ProductApi.getAllProduct(offset, limit, keyword);
                    break;
            }
        }
    };
    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("refresh_store");
        LocalBroadcastManager.getInstance(activity).registerReceiver(updateReceiver,
                filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        ButterKnife.bind(this);
        activity = this;
        initView();
        registerBroadcast();
    }


    private void initView() {
        storeList.clear();
        edtSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    keyword = edtSearch.getText().toString().toLowerCase();
                    storeList.clear();
                    showLoadingDialog();
                    StoreApi.getStoreList(offset, limit, keyword);
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
                StoreApi.getStoreList(offset, limit, keyword);
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
                    StoreApi.getStoreList(offset, limit, keyword);
                }

            }
        });
        setRecycler();
        try {
            String local_data = DatabaseQueryClass.getInstance().getData(App.getUserID(), "StoreList", "");
            if (TextUtils.isEmpty(local_data)) {
                showLoadingDialog();
            } else {
                loadLocalData();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        StoreApi.getStoreList(offset, limit, keyword);
    }
    private void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(App.getUserID(), "StoreList", "");
            if (!TextUtils.isEmpty(data)) {
                StoreListRes localRes = GsonUtils.getInstance().fromJson(data, StoreListRes.class);
                storeList.clear();
                storeList.addAll(localRes.getData());
                storeAdapter.setData(storeList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void initParam() {
        offset = 0;
        isLoading = false;
        isLast = false;
        storeList.clear();
    }
    private void onOption(int pos, StoreModel model) {
        ActionSheet.Item editItem = new ActionSheet.Item(R.attr.action_sheet_backgroundShowColor, R.attr.action_sheet_backgroundShowColor, 0, 0,
                R.attr.textShowColor, R.attr.textShowColor, getString(R.string.edit), 1);
        ActionSheet.Item reviewItem = new ActionSheet.Item(R.attr.action_sheet_backgroundShowColor, R.attr.action_sheet_backgroundShowColor, 0, 0,
                R.attr.textShowColor, R.attr.textShowColor, getString(R.string.txt_reviews), 1);
        ActionSheet.Item deleteItem = new ActionSheet.Item(R.attr.action_sheet_backgroundShowColor, R.attr.action_sheet_backgroundShowColor, 0, 0,
                R.attr.textShowColor, R.attr.textShowColor, getString(R.string.delete), 1);
        ActionSheet.Item cancelItem = new ActionSheet.Item(R.attr.action_sheet_backgroundShowColor, R.attr.action_sheet_backgroundShowColor, 0, 0,
                R.attr.textShowColor, R.attr.textShowColor, getString(R.string.cancel), 1);

        ActionSheet.Builder builder = ActionSheet.createBuilder(activity, getSupportFragmentManager())
                .setCancelItem(cancelItem)
                .setmTextSize(16);
        builder.setmOtherItems(editItem, reviewItem, deleteItem);
        builder.setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index, String itemStr) {
                        if (itemStr.equalsIgnoreCase(getString(R.string.edit))) {
                            Intent i = new Intent(activity, CreateStoreActivity.class);
                            i.putExtra("id", model.getId());
                            startActivity(i);
                        }
                        if (itemStr.equalsIgnoreCase(getString(R.string.txt_reviews))) {
                            Intent i = new Intent(activity, StoreCommentActivity.class);
                            i.putExtra("store_id", String.valueOf(model.getId()));
                            startActivity(i);
                        }
                        if (itemStr.equalsIgnoreCase(getString(R.string.delete))) {
                            DialogUtils.showConfirmDialogWithListener(activity, getString(R.string.txt_delete_confirmation), getString(R.string.txt_delete_user), getString(R.string.txt_yes), getString(R.string.txt_no),
                                    (dialog, which) -> onDelete(String.valueOf(model.getId()), pos),
                                    (dialog, which) -> dialog.dismiss());
                        }
                    }
                })
                .show();

    }

    private void onDelete(String id, int pos) {
        showLoadingDialog();
        Ion.with(activity)
                .load("DELETE", G.DeleteStore)
                .addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken())
                .setBodyParameter("storeId", id)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        if (e == null) {
                            storeList.remove(pos);
                            storeAdapter.setData(storeList);
                            Toast.makeText(activity, R.string.msg_deleted_successfully, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void setRecycler() {
        storeAdapter = new StoreAdapter(activity, storeList, new StoreAdapter.StoreRecyclerListener() {
            @Override
            public void onItemClicked(int pos, StoreModel model) {
                onOption(pos, model);
            }
        });
        recyclerView.setAdapter(storeAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    @OnClick({R.id.btBack, R.id.imgClear, R.id.btnCreate})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btnCreate:
                startActivity(new Intent(activity, CreateStoreActivity.class));
                break;
            case R.id.btBack:
                finish();
                break;
            case R.id.imgClear:
                edtSearch.setText("");
                keyword = "";
                storeList.clear();
                showLoadingDialog();
                StoreApi.getStoreList(offset, limit, keyword);
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessStoreList(StoreListRes res) {
        hideLoadingDialog();
        if (res.isStatus()) {
            if (offset == 0 && res.getData().size() == 0) {
                storeList.clear();
                storeAdapter.setData(storeList);
                lytEmpty.setVisibility(View.VISIBLE);
                return;
            } else {
                lytEmpty.setVisibility(View.GONE);
                if (offset == 0) {
                    storeList.clear();
                }
                if (res.getData().size() < limit) {
                    isLast = true;
                }
                storeList.addAll(res.getData());
                storeAdapter.setData(storeList);
                isLoading = false;
                if (offset == 0) {
                    String data = new Gson().toJson(res, new TypeToken<StoreListRes>() {
                    }.getType());
                    DatabaseQueryClass.getInstance().insertData(
                            App.getUserID(),
                            "StoreList",
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessCreateStore(String key) {
        if (key.equalsIgnoreCase("CreatedStore")) {
            showLoadingDialog();
            StoreApi.getStoreList(offset, limit, keyword);
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