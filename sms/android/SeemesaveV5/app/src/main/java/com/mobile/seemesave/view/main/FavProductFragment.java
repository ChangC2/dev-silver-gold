package com.mobile.seemesave.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.ProductTopAdapter;
import com.mobile.seemesave.base.BaseFragment;
import com.mobile.seemesave.listener.pagination.PaginationScrollListener;
import com.mobile.seemesave.model.common.ProductDetailModel;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.detail.ProductDetailActivity;
import com.mobile.seemesave.view_model.main.FavProductFragViewModel;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavProductFragment extends BaseFragment {
    private FavProductFragViewModel mViewModel;
    private View mFragView;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private ProductTopAdapter recyclerAdapter;

    @BindView(R.id.li_empty)
    LinearLayout li_empty;

    private int offset = 0;
    private int limit = 20;
    private boolean isLoading = false;
    private boolean isLast = false;
    private boolean firstLoading = true;
    private ArrayList<ProductDetailModel> productList = new ArrayList<>();
    public FavProductFragment() {
    }

    public static FavProductFragment newInstance() {
        FavProductFragment fragment = new FavProductFragment();
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
        mViewModel = new ViewModelProvider(this).get(FavProductFragViewModel.class);
        mFragView = inflater.inflate(R.layout.fragment_fav_product, container, false);
        ButterKnife.bind(this, mFragView);
        initView();
        return mFragView;
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
                } else {
                    isLast = false;
                }
                productList.addAll(list);
                recyclerAdapter.setData(productList);
                isLoading = false;
            }
        });
        try {
            String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "FavProduct", "");
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
    public void loadData() {
        if (!firstLoading) {
            mViewModel.loadData();
            firstLoading = true;
        }
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
        recyclerAdapter = new ProductTopAdapter(getActivity(), productList, new ProductTopAdapter.ProductTopRecyclerListener() {
            @Override
            public void onItemClicked(int pos, ProductDetailModel model) {
            }

            @Override
            public void onViewPromotion(int pos, ProductDetailModel model) {
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra("barcode", model.getBarcode());
                startActivity(intent);
            }

            @Override
            public void onStar(int pos, ProductDetailModel model) {

            }
        });
        recyclerView.setAdapter(recyclerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                offset = offset + limit;
                mViewModel.setOffset(offset);
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

}
