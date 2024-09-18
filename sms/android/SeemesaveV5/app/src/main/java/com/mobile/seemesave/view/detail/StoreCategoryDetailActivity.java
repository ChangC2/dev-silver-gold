package com.mobile.seemesave.view.detail;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.StoreAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.StoreModel;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.detail.AViewModel;
import com.mobile.seemesave.view_model.detail.StoreCategoryDetailViewModel;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoreCategoryDetailActivity extends BaseActivity {
    private StoreCategoryDetailViewModel mViewModel;


    private StoreCategoryDetailActivity activity;

    @BindView(R.id.txtTitle)
    TextView txtTitle;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private StoreAdapter recyclerAdapter;

    @BindView(R.id.li_empty)
    LinearLayout li_empty;

    private ArrayList<StoreModel> storeList = new ArrayList<>();

    private int category_id = -1;
    private String category_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StoreCategoryDetailViewModel.class);
        setContentView(R.layout.activity_store_category_detail);
        ButterKnife.bind(this);
        activity = this;
        category_id = getIntent().getIntExtra("category_id", -1);
        mViewModel.setCategory_id(category_id);
        category_name = getIntent().getStringExtra("category_name");
        initView();
    }

    private void initView() {
        txtTitle.setText(category_name);
        storeList.clear();
        setRecycler();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                mViewModel.setIsBusy(true);
                mViewModel.loadData();
            }
        });
        mViewModel.getIsBusy().observe(this, isBusy -> {
            if (isBusy) {
                showLoadingDialog();
            } else {
                hideLoadingDialog();
            }
        });
        mViewModel.getStoreList().observe(this, list -> {
            storeList.clear();
            if (list.size() == 0) {
                li_empty.setVisibility(View.VISIBLE);
                return;
            } else {
                li_empty.setVisibility(View.GONE);
                storeList.addAll(list);
                recyclerAdapter.setData(storeList);
            }
        });

        loadData();
    }

    public void loadData() {
        try {
            String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "StoreCategoryDetail", String.valueOf(category_id));
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

    private void setRecycler() {

        recyclerAdapter = new StoreAdapter(activity, storeList, new StoreAdapter.StoreRecyclerListener() {

            @Override
            public void onItemClicked(int pos, StoreModel model) {

            }

            @Override
            public void onRate(int pos, StoreModel model) {

            }

            @Override
            public void onFollow(int pos, StoreModel model) {

            }

            @Override
            public void onUnFollow(int pos, StoreModel model) {

            }
        });
        recyclerView.setAdapter(recyclerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    @OnClick({R.id.btBack})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
