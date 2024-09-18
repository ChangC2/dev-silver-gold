package com.mobile.seemesave.view.main;

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
import com.mobile.seemesave.adapter.BrandTopAdapter;
import com.mobile.seemesave.base.BaseFragment;
import com.mobile.seemesave.model.common.TrendingBrandModel;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.main.FavBrandFragViewModel;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavBrandFragment extends BaseFragment {
    private FavBrandFragViewModel mViewModel;
    private View mFragView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private BrandTopAdapter recyclerAdapter;

    @BindView(R.id.li_empty)
    LinearLayout li_empty;

    private ArrayList<TrendingBrandModel> brandList = new ArrayList<>();
    private int offset = 0;
    private int limit = 20;
    private boolean isLoading = false;
    private boolean isLast = false;
    private boolean firstLoading = false;
    public FavBrandFragment() {
    }

    public static FavBrandFragment newInstance() {
        FavBrandFragment fragment = new FavBrandFragment();
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
        mViewModel = new ViewModelProvider(this).get(FavBrandFragViewModel.class);
        mFragView = inflater.inflate(R.layout.fragment_fav_brand, container, false);
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
        mViewModel.getBrandList().observe(this, list -> {
            if (offset == 0 && list.size() == 0) {
                li_empty.setVisibility(View.VISIBLE);
                return;
            } else {
                li_empty.setVisibility(View.GONE);
                if (offset == 0) {
                    brandList.clear();
                }
                if (list.size() < limit) {
                    isLast = true;
                }
                brandList.addAll(list);
                recyclerAdapter.setData(brandList);
                isLoading = false;
            }
        });
    }
    public void loadData() {
        if (!firstLoading) {
            try {
                String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "FavBrand", "");
                if (TextUtils.isEmpty(local_data)) {
                    mViewModel.setIsBusy(true);
                } else {
                    mViewModel.loadLocalData();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mViewModel.loadData();
            firstLoading = true;
        }
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    private void initView() {
        brandList.clear();
        setRecycler();
    }
    private void setRecycler() {
        recyclerAdapter = new BrandTopAdapter(getActivity(), brandList, new BrandTopAdapter.BrandTopRecyclerListener() {


            @Override
            public void onItemClicked(int pos, TrendingBrandModel model) {

            }

            @Override
            public void onStar(int pos, TrendingBrandModel model) {

            }
        });
        recyclerView.setAdapter(recyclerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

}
