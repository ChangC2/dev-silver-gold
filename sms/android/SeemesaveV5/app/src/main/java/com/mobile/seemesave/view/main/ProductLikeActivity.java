package com.mobile.seemesave.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.ProductTopAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.listener.pagination.PaginationScrollListener;
import com.mobile.seemesave.model.common.ProductDetailModel;
import com.mobile.seemesave.view.detail.ProductDetailActivity;
import com.mobile.seemesave.view_model.main.FavProductFragViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductLikeActivity extends BaseActivity {
    private FavProductFragViewModel mViewModel;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private ProductTopAdapter recyclerAdapter;

    private ProductLikeActivity activity;
    @BindView(R.id.li_empty)
    LinearLayout li_empty;

    private int offset = 0;
    private int limit = 20;
    private boolean isLoading = false;
    private boolean isLast = false;
    private boolean firstLoading = true;
    private ArrayList<ProductDetailModel> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FavProductFragViewModel.class);
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
        mViewModel.getProductList().observe(this, list -> {
            if (offset == 0 && list.size() == 0) {
                li_empty.setVisibility(View.VISIBLE);
                return;
            } else {
                li_empty.setVisibility(View.GONE);
                if (offset == 0) {
                    productList.clear();
                }
                if (list.size() < limit) {
                    isLast = true;
                }
                productList.addAll(list);
                recyclerAdapter.setData(productList);
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
        productList.clear();
        setRecycler();
    }
    private void setRecycler() {
        recyclerAdapter = new ProductTopAdapter(activity, productList, new ProductTopAdapter.ProductTopRecyclerListener() {
            @Override
            public void onItemClicked(int pos, ProductDetailModel model) {
            }

            @Override
            public void onViewPromotion(int pos, ProductDetailModel model) {
                Intent intent = new Intent(activity, ProductDetailActivity.class);
                intent.putExtra("barcode", model.getBarcode());
                startActivity(intent);
            }

            @Override
            public void onStar(int pos, ProductDetailModel model) {

            }
        });
        recyclerView.setAdapter(recyclerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                offset = offset + limit;
                mViewModel.loadData();
            }

            @Override
            public boolean isLastPage() {
                return isLast;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
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
