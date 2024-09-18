package seemesave.businesshub.view.supplier.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.PayPromoteAdapter;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseFragment;
import seemesave.businesshub.model.common.PayPromoteModel;
import seemesave.businesshub.sqlite.DatabaseQueryClass;
import seemesave.businesshub.utils.DialogUtils;
import seemesave.businesshub.view.supplier.promote.CreatePaytoPromoteActivity;
import seemesave.businesshub.view.supplier.promote.PromoteDetailActivity;
import seemesave.businesshub.view.supplier.promote.PromoteStoreActivity;
import seemesave.businesshub.view_model.supplier.main.PromoteFragViewModel;
import seemesave.businesshub.widget.ActionSheet;

public class PaytoPromoteFragment extends BaseFragment {
    private View mFragView;
    private PromoteFragViewModel mViewModel;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.lytEmpty)
    LinearLayout lytEmpty;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.edtSearch)
    EditText edtSearch;
    @BindView(R.id.imgClear)
    ImageView imgClear;

    private PayPromoteAdapter recyclerAdapter;
    private ArrayList<PayPromoteModel> dataList = new ArrayList<>();


    private int offset = 0;
    private int limit = 20;
    private boolean isLoading = false;
    private boolean isLast = false;

    private String keyword = "";


    BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "refresh_paytopromote":
                    initParam();
                    mViewModel.setIsBusy(true);
                    mViewModel.loadData(keyword);
                    break;
            }
        }
    };
    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("refresh_paytopromote");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateReceiver,
                filter);
    }

    public PaytoPromoteFragment() {
    }

    public static PaytoPromoteFragment newInstance() {
        PaytoPromoteFragment fragment = new PaytoPromoteFragment();
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
        mViewModel = new ViewModelProvider(this).get(PromoteFragViewModel.class);
        mFragView = inflater.inflate(R.layout.fragment_paytopromote, container, false);
        ButterKnife.bind(this, mFragView);
        initView();
        registerBroadcast();
        return mFragView;
    }


    private void initView() {
        nestedScrollView.setNestedScrollingEnabled(false);
        dataList.clear();
        setRecycler();

        mViewModel.getIsBusy().observe(this, isBusy -> {
            if (isBusy) {
                showLoadingDialog();
            } else {
                hideLoadingDialog();
            }
        });
        mViewModel.getDataList().observe(this, list -> {
            if (offset == 0 && list.size() == 0) {
                lytEmpty.setVisibility(View.VISIBLE);
                dataList.clear();
                recyclerAdapter.setData(dataList);
                return;
            } else {
                lytEmpty.setVisibility(View.GONE);
                if (offset == 0) {
                    dataList.clear();
                }
                if (list.size() < limit) {
                    isLast = true;
                }
                dataList.addAll(list);
                recyclerAdapter.setData(dataList);
                isLoading = false;
            }
        });


        try {
            String local_data = DatabaseQueryClass.getInstance().getData(App.getUserID(), "PaytoPromote", "");
            if (TextUtils.isEmpty(local_data)) {
                mViewModel.setIsBusy(true);
            } else {
                mViewModel.loadLocalData();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mViewModel.loadData(keyword);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                initParam();
                mViewModel.setIsBusy(true);
                mViewModel.loadData(keyword);
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
                    mViewModel.loadData(keyword);
                }

            }
        });
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
                    dataList.clear();
                    showLoadingDialog();
                    mViewModel.loadData(keyword);
                    return true;
                }
                return false;
            }
        });
    }

    private void initParam() {
        offset = 0;
        isLast = false;
        isLoading = false;
        mViewModel.setOffset(offset);
    }

    private void setRecycler() {
        recyclerAdapter = new PayPromoteAdapter(getActivity(), dataList, new PayPromoteAdapter.PayProductRecyclerListener() {
            @Override
            public void onItemClicked(int pos, PayPromoteModel model) {
                onOption(pos, model);
            }
        });
        recyclerView.setAdapter(recyclerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),linearLayoutManager.getOrientation()));
    }
    private void onOption(int pos, PayPromoteModel model) {
        ActionSheet.Item viewItem = new ActionSheet.Item(R.attr.action_sheet_backgroundShowColor, R.attr.action_sheet_backgroundShowColor, 0, 0,
                R.attr.textShowColor, R.attr.textShowColor, getString(R.string.view), 1);
        ActionSheet.Item deleteItem = new ActionSheet.Item(R.attr.action_sheet_backgroundShowColor, R.attr.action_sheet_backgroundShowColor, 0, 0,
                R.attr.textShowColor, R.attr.textShowColor, getString(R.string.txt_update_store), 1);
        ActionSheet.Item cancelItem = new ActionSheet.Item(R.attr.action_sheet_backgroundShowColor, R.attr.action_sheet_backgroundShowColor, 0, 0,
                R.attr.textShowColor, R.attr.textShowColor, getString(R.string.cancel), 1);

        ActionSheet.createBuilder(getActivity(), getChildFragmentManager())
                .setCancelItem(cancelItem)
                .setmTextSize(16)
                .setmOtherItemSpacing(1)
                .setmOtherItems(viewItem, deleteItem)
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index, String itemStr) {
                        if (itemStr.equalsIgnoreCase(getString(R.string.view))) {
                            Intent i = new Intent(getActivity(), PromoteDetailActivity.class);
                            i.putExtra("id", model.getId());
                            startActivity(i);
                        }

                        if (itemStr.equalsIgnoreCase(getString(R.string.txt_update_store))) {
                            Intent i = new Intent(getActivity(), PromoteStoreActivity.class);
                            i.putExtra("id", model.getId());
                            startActivity(i);
                        }
                    }
                })
                .show();

    }
    private void onDelete(String id, int pos) {

    }
    @OnClick({R.id.imgClear, R.id.btnCreate})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.imgClear:
                edtSearch.setText("");
                keyword = "";
                break;
            case R.id.btnCreate:
                startActivity(new Intent(getActivity(), CreatePaytoPromoteActivity.class));
                break;
        }
    }

}