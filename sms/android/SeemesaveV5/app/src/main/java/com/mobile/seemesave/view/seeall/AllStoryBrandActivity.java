package com.mobile.seemesave.view.seeall;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.AllCanMissAdapter;
import com.mobile.seemesave.adapter.BrandTopAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.model.common.TrendingBrandModel;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.detail.BrandDetailActivity;
import com.mobile.seemesave.view_model.seeall.AllStoryBrandViewModel;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllStoryBrandActivity extends BaseActivity {
    private AllStoryBrandViewModel mViewModel;
    private AllStoryBrandActivity activity;

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

    private BrandTopAdapter recyclerAdapter;
    ArrayList<TrendingBrandModel> dataList = new ArrayList<>();
    private int id = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AllStoryBrandViewModel.class);
        setContentView(R.layout.activity_all_canmiss);
        ButterKnife.bind(this);
        activity = this;
        id = getIntent().getIntExtra("id", -1);
        initView();
    }

    private void initView() {
        dataList.clear();
        txtTitle.setText(getString(R.string.txt_story_brands));
        nestedScrollView.setNestedScrollingEnabled(false);
        mViewModel.setId(id);
        setRecycler();
        try {
            String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "AllStoryBrand", String.valueOf(id));
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
        mViewModel.getBrandList().observe(this, list -> {
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
        recyclerAdapter = new BrandTopAdapter(activity, dataList, new BrandTopAdapter.BrandTopRecyclerListener() {

            @Override
            public void onItemClicked(int pos, TrendingBrandModel model) {

            }

            @Override
            public void onStar(int pos, TrendingBrandModel model) {

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
