package seemesave.businesshub.view.vendor.main;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import seemesave.businesshub.R;
import seemesave.businesshub.adapter.CollectOrderAdapter;
import seemesave.businesshub.api.order.OrderApi;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseFragment;
import seemesave.businesshub.model.common.CollectOrderModel;
import seemesave.businesshub.model.res.CollectOrderListRes;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.view_model.vendor.main.StoreFragViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderCollectFragment extends BaseFragment {
    private StoreFragViewModel mViewModel;
    private View mFragView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.lytEmpty)
    LinearLayout lytEmpty;
    private CollectOrderAdapter collectOrderAdapter;
    private ArrayList<CollectOrderModel> orderList = new ArrayList<>();
    private ArrayList<CollectOrderModel> pendingList = new ArrayList<>();
    private ArrayList<CollectOrderModel> collectionList = new ArrayList<>();
    private ArrayList<CollectOrderModel> finishList = new ArrayList<>();

    private int limit = 20;

    private int orderOffset = 0;
    private boolean orderIsLoading = false;
    private boolean orderIsLast = false;
    private boolean firstOrder = true;

    private int pendingOffset = 0;
    private boolean pendingIsLoading = false;
    private boolean pendingIsLast = false;
    private boolean firstPending = false;

    private int collectOffset = 0;
    private boolean collectIsLoading = false;
    private boolean collectIsLast = false;
    private boolean firstCollect = false;

    private int finishOffset = 0;
    private boolean finishIsLoading = false;
    private boolean finishIsLast = false;
    private boolean firstFinish = false;


    @BindView(R.id.edtSearch)
    EditText edtSearch;
    @BindView(R.id.imgSearch)
    ImageView imgSearch;
    @BindView(R.id.imgClear)
    ImageView imgClear;

    @BindView(R.id.txtOrder)
    TextView txtOrder;
    @BindView(R.id.txtPending)
    TextView txtPending;
    @BindView(R.id.txtReadyCollect)
    TextView txtReadyCollect;
    @BindView(R.id.txtCompleted)
    TextView txtCompleted;

    private String keyword = "";
    private int curTab = 0;

    BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "refresh_collect_data":
                    curTab = 1;
                    reload(false);
                    break;
            }
        }
    };
    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("refresh_collect_data");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateReceiver,
                filter);
    }

    public OrderCollectFragment() {
    }

    public static OrderCollectFragment newInstance() {
        OrderCollectFragment fragment = new OrderCollectFragment();
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
        mViewModel = new ViewModelProvider(this).get(StoreFragViewModel.class);
        mFragView = inflater.inflate(R.layout.fragment_order_collect, container, false);
        ButterKnife.bind(this, mFragView);
        initView();
        registerBroadcast();
        return mFragView;
    }


    private void initView() {
        nestedScrollView.setNestedScrollingEnabled(false);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() == 0) {
                    imgClear.setVisibility(View.GONE);
                } else {
                    imgClear.setVisibility(View.VISIBLE);
                }

            }
        });
        edtSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    keyword = edtSearch.getText().toString().toLowerCase();
                    reload(true);
                    return true;
                }
                return false;
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                reload(true);
            }
        });
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
                int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));
                scroll(diff);

            }
        });
        setRecycler();
        showLoadingDialog();
        OrderApi.getCollectList(false, false, keyword, orderOffset, limit);
    }

    private void initParam() {
        switch (curTab) {
            case 0:
                orderOffset = 0;
                orderIsLoading = false;
                orderIsLast = false;
                orderList.clear();
                break;
            case 1:
                pendingOffset = 0;
                pendingIsLoading = false;
                pendingIsLast = false;
                pendingList.clear();
                break;
            case 2:
                collectOffset = 0;
                collectIsLoading = false;
                collectIsLast = false;
                collectionList.clear();
                break;
            case 3:
                finishOffset = 0;
                finishIsLoading = false;
                finishIsLast = false;
                finishList.clear();
                break;

        }
    }

    private void reload(boolean loadDlg) {
        initParam();
        switch (curTab) {
            case 0:
                if (loadDlg)
                    showLoadingDialog();
                OrderApi.getCollectList(false, false, keyword, orderOffset, limit);
                break;
            case 1:
                if (loadDlg)
                    showLoadingDialog();
                OrderApi.getCollectPendingList(keyword, pendingOffset, limit);
                break;
            case 2:
                if (loadDlg)
                    showLoadingDialog();
                OrderApi.getCollectList(true, false, keyword, collectOffset, limit);
                break;
            case 3:
                if (loadDlg)
                    showLoadingDialog();
                OrderApi.getCollectList(true, true, keyword, finishOffset, limit);
                break;
        }
    }

    private void scroll(int diff) {
        switch (curTab) {
            case 0:
                if (diff == 0 && !orderIsLoading && !orderIsLast) {
                    orderIsLoading = true;
                    orderOffset = orderOffset + limit;
                    showLoadingDialog();
                    OrderApi.getCollectList(false, false, keyword, orderOffset, limit);
                }
                break;
            case 1:
                if (diff == 0 && !pendingIsLoading && !pendingIsLast) {
                    pendingIsLoading = true;
                    pendingOffset = pendingOffset + limit;
                    showLoadingDialog();
                    OrderApi.getCollectPendingList(keyword, collectOffset, limit);
                }
                break;
            case 2:
                if (diff == 0 && !collectIsLoading && !collectIsLast) {
                    collectIsLoading = true;
                    collectOffset = collectOffset + limit;
                    showLoadingDialog();
                    OrderApi.getCollectList(true, false, keyword, collectOffset, limit);
                }
                break;
            case 3:
                if (diff == 0 && !finishIsLoading && !finishIsLast) {
                    finishIsLoading = true;
                    finishOffset = finishOffset + limit;
                    showLoadingDialog();
                    OrderApi.getCollectList(true, true, keyword, finishOffset, limit);
                }
                break;
        }

    }

    private void setRecycler() {
        collectOrderAdapter = new CollectOrderAdapter(getActivity(), orderList, new CollectOrderAdapter.OrderRecyclerListener() {

            @Override
            public void onItemClicked(int pos, CollectOrderModel model) {
                if (!model.isIs_ready() && !model.isIs_finished()) {
                    showLoadingDialog();
                    Ion.with(getActivity())
                            .load("POST", G.SetOrderReady)
                            .addHeader("Authorization", App.getToken())
                            .addHeader("Content-Language", App.getPortalToken())
                            .setBodyParameter("id", String.valueOf(model.getId()))
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {
                                    hideLoadingDialog();
                                    orderList.remove(pos);
                                    collectOrderAdapter.setData(orderList);
                                    setPage(1);
                                }
                            });

                } else if (model.isIs_ready() && !model.isIs_finished()){
                    View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dlg_order_confirm, null);

                    final android.app.AlertDialog dlg = new android.app.AlertDialog.Builder(getActivity())
                            .setView(dialogView)
                            .setCancelable(true)
                            .create();

                    dlg.setCanceledOnTouchOutside(true);
                    TextInputEditText edtNumber = dialogView.findViewById(R.id.edtNumber);
                    TextView btnYes = dialogView.findViewById(R.id.btnYes);

                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (TextUtils.isEmpty(edtNumber.getText().toString())) {
                                Toast.makeText(getActivity(), R.string.msg_enter_order_number, Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (!edtNumber.getText().toString().equalsIgnoreCase(model.getSecurity_code())) {
                                Toast.makeText(getActivity(), R.string.msg_confirm_order_number, Toast.LENGTH_LONG).show();
                                return;
                            }
                            edtNumber.setText("");
                            dlg.dismiss();
                            showLoadingDialog();
                            Ion.with(getActivity())
                                    .load("PUT", G.SetOrderFinished)
                                    .addHeader("Authorization", App.getToken())
                                    .addHeader("Content-Language", App.getPortalToken())
                                    .setBodyParameter("id", String.valueOf(model.getId()))
                                    .asString()
                                    .setCallback(new FutureCallback<String>() {
                                        @Override
                                        public void onCompleted(Exception e, String result) {
                                            hideLoadingDialog();
                                            collectionList.remove(pos);
                                            collectOrderAdapter.setData(collectionList);
                                            setPage(2);
                                        }
                                    });
                        }
                    });

                    dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dlg.show();
                }
            }
        });
        recyclerView.setAdapter(collectOrderAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @SuppressLint("ResourceAsColor")
    private void initPage() {
        txtOrder.setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_600));
        txtPending.setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_600));
        txtReadyCollect.setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_600));
        txtCompleted.setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_600));
        txtOrder.setTypeface(null, Typeface.NORMAL);
        txtPending.setTypeface(null, Typeface.NORMAL);
        txtReadyCollect.setTypeface(null, Typeface.NORMAL);
        txtCompleted.setTypeface(null, Typeface.NORMAL);
    }

    private void setPage(int pos) {
        if (orderIsLoading || collectIsLoading || finishIsLoading) {
            return;
        }
        curTab = pos;
        lytEmpty.setVisibility(View.GONE);
        initPage();
        switch (pos) {
            case 0:
                txtOrder.setTextColor(ContextCompat.getColor(getContext(), R.color.main_blue_color));
                txtOrder.setTypeface(null, Typeface.BOLD);
                if (!firstOrder) {
                    reload(true);
                } else {
                    collectOrderAdapter.setData(orderList);
                }
                break;
            case 1:
                txtPending.setTextColor(ContextCompat.getColor(getContext(), R.color.main_blue_color));
                txtPending.setTypeface(null, Typeface.BOLD);
                if (!firstPending) {
                    reload(true);
                } else {
                    collectOrderAdapter.setData(pendingList);
                }
                break;
            case 2:
                txtReadyCollect.setTextColor(ContextCompat.getColor(getContext(), R.color.main_blue_color));
                txtReadyCollect.setTypeface(null, Typeface.BOLD);
                if (!firstCollect) {
                    reload(true);
                } else {
                    collectOrderAdapter.setData(collectionList);
                }
                break;
            case 3:
                txtCompleted.setTextColor(ContextCompat.getColor(getContext(), R.color.main_blue_color));
                txtCompleted.setTypeface(null, Typeface.BOLD);
                if (!firstFinish) {
                    reload(true);
                } else {
                    collectOrderAdapter.setData(finishList);
                }
                break;
        }
    }

    @OnClick({R.id.txtOrder, R.id.txtPending, R.id.txtReadyCollect, R.id.txtCompleted, R.id.imgClear})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.imgClear:
                edtSearch.setText("");
                keyword = "";
                reload(true);
                break;
            case R.id.txtOrder:
                setPage(0);
                break;
            case R.id.txtPending:
                setPage(1);
                break;
            case R.id.txtReadyCollect:
                setPage(2);
                break;
            case R.id.txtCompleted:
                setPage(3);
                break;
        }
    }

    private void initData(ArrayList<CollectOrderModel> list) {
        switch (curTab) {
            case 0:
                if (orderOffset == 0 && list.size() == 0) {
                    orderList.clear();
                    collectOrderAdapter.setData(orderList);
                    lytEmpty.setVisibility(View.VISIBLE);
                    return;
                } else {
                    lytEmpty.setVisibility(View.GONE);
                    if (orderOffset == 0) {
                        orderList.clear();
                    }
                    if (list.size() < limit) {
                        orderIsLast = true;
                    }
                    orderList.addAll(list);
                    collectOrderAdapter.setData(orderList);
                    orderIsLoading = false;
                }
                firstOrder = true;
                break;
            case 1:
                if (pendingOffset == 0 && list.size() == 0) {
                    pendingList.clear();
                    collectOrderAdapter.setData(pendingList);
                    lytEmpty.setVisibility(View.VISIBLE);
                    return;
                } else {
                    lytEmpty.setVisibility(View.GONE);
                    if (pendingOffset == 0) {
                        pendingList.clear();
                    }
                    if (list.size() < limit) {
                        pendingIsLast = true;
                    }
                    pendingList.addAll(list);
                    collectOrderAdapter.setData(pendingList);
                    pendingIsLoading = false;
                }
                firstPending = true;
                break;
            case 2:
                if (collectOffset == 0 && list.size() == 0) {
                    collectionList.clear();
                    collectOrderAdapter.setData(collectionList);
                    lytEmpty.setVisibility(View.VISIBLE);
                    return;
                } else {
                    lytEmpty.setVisibility(View.GONE);
                    if (collectOffset == 0) {
                        collectionList.clear();
                    }
                    if (list.size() < limit) {
                        collectIsLast = true;
                    }
                    collectionList.addAll(list);
                    collectOrderAdapter.setData(collectionList);
                    collectIsLoading = false;
                }
                firstCollect = true;
                break;
            case 3:
                if (finishOffset == 0 && list.size() == 0) {
                    finishList.clear();
                    collectOrderAdapter.setData(finishList);
                    lytEmpty.setVisibility(View.VISIBLE);
                    return;
                } else {
                    lytEmpty.setVisibility(View.GONE);
                    if (finishOffset == 0) {
                        finishList.clear();
                    }
                    if (list.size() < limit) {
                        finishIsLast = true;
                    }
                    finishList.addAll(list);
                    collectOrderAdapter.setData(finishList);
                    finishIsLoading = false;
                }
                firstFinish = true;
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessList(CollectOrderListRes res) {
        hideLoadingDialog();
        if (res.isStatus()) {
            initData(res.getDataList());
        } else {
            if (!TextUtils.isEmpty(res.getMessage())) {
                Toast.makeText(getActivity(), res.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}