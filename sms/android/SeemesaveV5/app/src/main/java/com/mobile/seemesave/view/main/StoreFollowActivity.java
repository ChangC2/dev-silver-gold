package com.mobile.seemesave.view.main;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.StoreAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.StoreModel;
import com.mobile.seemesave.view_model.main.FavStoreFragViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoreFollowActivity extends BaseActivity {
    private FavStoreFragViewModel mViewModel;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private StoreAdapter recyclerAdapter;

    private StoreFollowActivity activity;
    @BindView(R.id.li_empty)
    LinearLayout li_empty;
    @BindView(R.id.txtTitle)
    TextView txtTitle;

    private int offset = 0;
    private int limit = 20;
    private boolean isLoading = false;
    private boolean isLast = false;
    private boolean firstLoading = true;
    private ArrayList<StoreModel> storeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FavStoreFragViewModel.class);
        setContentView(R.layout.activity_product_like);
        ButterKnife.bind(this);
        activity = this;
        initView();
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
        mViewModel.getStoreList().observe(this, list -> {
            if (offset == 0 && list.size() == 0) {
                li_empty.setVisibility(View.VISIBLE);
                return;
            } else {
                li_empty.setVisibility(View.GONE);
                if (offset == 0) {
                    storeList.clear();
                }
                if (list.size() < limit) {
                    isLast = true;
                }
                storeList.addAll(list);
                recyclerAdapter.setData(storeList);
                isLoading = false;
            }
        });
        mViewModel.loadData();
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    private void initView() {
        txtTitle.setText(getString(R.string.txt_stores_follow));
        storeList.clear();
        setRecycler();
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
