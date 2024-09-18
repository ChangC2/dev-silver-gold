package com.mobile.seemesave.view.seeall;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.ProductOneAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.ProductOneModel;
import com.mobile.seemesave.model.common.PromotionOneModel;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.detail.PromotionDetailViewModel;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllProductCategoryDetailActivity extends BaseActivity {

    private PromotionDetailViewModel mViewModel;
    private AllProductCategoryDetailActivity activity;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.li_empty)
    LinearLayout li_empty;

    private int offset = 0;
    private int limit = 20;
    private boolean isLoading = false;
    private boolean isLast = false;

    String category_name = "";
    boolean isDeliver = false;
    int promotion_id = -1, category_id = -1;
    ArrayList<ProductOneModel> productList = new ArrayList<>();


    private ProductOneAdapter singleProductAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PromotionDetailViewModel.class);
        setContentView(R.layout.activity_all_store_category);
        ButterKnife.bind(this);
        activity = this;
        category_name = getIntent().getStringExtra("category_name");
        category_id = getIntent().getIntExtra("category_id", -1);
        promotion_id = getIntent().getIntExtra("promotion_id", -1);
        isDeliver = getIntent().getBooleanExtra("is_deliver", false);
        mViewModel.setPromotionInfo(G.promotionInfo);
        mViewModel.setLocalID(promotion_id + "_" + category_id);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.getIsBusy().observe(this, isBusy -> {
            if (isBusy) {
                showLoadingDialog();
            } else {
                hideLoadingDialog();
            }
        });
        mViewModel.getSingleProductList().observe(this, list -> {
            if (offset == 0 && list.size() == 0) {
                li_empty.setVisibility(View.VISIBLE);
                productList.clear();
                singleProductAdapter.setData(productList, isDeliver ? 1 : 0);
            } else {
                li_empty.setVisibility(View.GONE);
                if (offset == 0) {
                    productList.clear();
                }
                if (list.size() < limit) {
                    isLast = true;
                }
                productList.addAll(list);
                singleProductAdapter.setData(productList, isDeliver ? 1 : 0);
                isLoading = false;
            }
        });
        try {
            String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "PromotionCategoryProduct", promotion_id + "_" + category_id);
            if (TextUtils.isEmpty(local_data)) {
                mViewModel.setIsBusy(true);
            } else {
                mViewModel.loadLocalProductData();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mViewModel.loadProductData(promotion_id, category_id);
    }
    private void initParam() {
        offset = 0;
        mViewModel.setOffset(offset);
        productList.clear();
    }
    private void initView() {
        txtTitle.setText(category_name);
        productList.clear();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                initParam();
                mViewModel.setIsBusy(true);
                mViewModel.loadProductData(promotion_id, category_id);
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
                    mViewModel.loadProductData(promotion_id, category_id);
                }
            }
        });
        setRecycler();
    }
    private void setRecycler() {
        singleProductAdapter = new ProductOneAdapter(activity, productList, false, isDeliver ? 1 : 0, new ProductOneAdapter.ProductOneRecyclerListener() {
            @Override
            public void onRemoveCart(int pos, ProductOneModel model) {

            }

            @Override
            public void onPlus(int pos, ProductOneModel model) {

            }

            @Override
            public void onMinus(int pos, ProductOneModel model) {

            }
        });
        recyclerView.setAdapter(singleProductAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }



    @OnClick({R.id.imgBack})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
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