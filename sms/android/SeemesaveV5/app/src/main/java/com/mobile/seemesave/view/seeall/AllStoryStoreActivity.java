package com.mobile.seemesave.view.seeall;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.BrandTopAdapter;
import com.mobile.seemesave.adapter.StoreAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.StoreModel;
import com.mobile.seemesave.model.common.TrendingBrandModel;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.seeall.AllStoryBrandViewModel;
import com.mobile.seemesave.view_model.seeall.AllStoryStoreViewModel;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllStoryStoreActivity extends BaseActivity {
    private AllStoryStoreViewModel mViewModel;
    private AllStoryStoreActivity activity;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.li_empty)
    LinearLayout li_empty;
    @BindView(R.id.txtTitle)
    TextView txtTitle;

    private StoreAdapter recyclerAdapter;
    ArrayList<StoreModel> dataList = new ArrayList<>();
    private int id = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AllStoryStoreViewModel.class);
        setContentView(R.layout.activity_all_canmiss);
        ButterKnife.bind(this);
        activity = this;
        id = getIntent().getIntExtra("id", -1);
        initView();
    }

    private void initView() {
        dataList.clear();
        txtTitle.setText(getString(R.string.txt_story_stores));
        nestedScrollView.setNestedScrollingEnabled(false);
        mViewModel.setId(id);
        setRecycler();
        try {
            String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "AllStoryStore", String.valueOf(id));
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
                mViewModel.setIsBusy(true);
                mViewModel.loadData();
            }
        });

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
        mViewModel.getStores().observe(this, list -> {
            dataList.clear();
            if (list.size() == 0) {
                recyclerAdapter.setData(dataList);
                li_empty.setVisibility(View.VISIBLE);
            } else {
                li_empty.setVisibility(View.GONE);
                dataList.addAll(list);
                recyclerAdapter.setData(dataList);
            }
        });

    }

    private void setRecycler() {
        recyclerAdapter = new StoreAdapter(activity, dataList, new StoreAdapter.StoreRecyclerListener() {

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
