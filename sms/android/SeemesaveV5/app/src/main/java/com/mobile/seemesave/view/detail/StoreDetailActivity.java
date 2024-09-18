package com.mobile.seemesave.view.detail;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.PromotionOneAdapter;
import com.mobile.seemesave.adapter.StoreDetailAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.ProductOneModel;
import com.mobile.seemesave.model.common.PromotionModel;
import com.mobile.seemesave.model.common.PromotionOneModel;
import com.mobile.seemesave.model.common.StoreModel;
import com.mobile.seemesave.model.res.CartRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.detail.StoreDetailViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoreDetailActivity extends BaseActivity {
    private StoreDetailViewModel mViewModel;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.li_empty)
    LinearLayout li_empty;
    @BindView(R.id.imgProduct)
    RoundedImageView imgProduct;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.txtPhone)
    TextView txtPhone;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private StoreDetailAdapter storeDetailAdapter;

    private StoreDetailActivity activity;

    private StoreModel storeInfo;
    private ArrayList<PromotionModel> promotionList = new ArrayList<>();
    private int store_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StoreDetailViewModel.class);
        setContentView(R.layout.activity_store_detail);
        ButterKnife.bind(this);
        activity = this;
        store_id = getIntent().getIntExtra("id", 0);
        initView();
    }

    private void initView() {
        promotionList.clear();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                mViewModel.setIsBusy(true);
                mViewModel.loadData(store_id);
            }
        });
        setRecycler();
        mViewModel.setStoreId(store_id);
        try {
            String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "StoreDetail", String.valueOf(store_id));
            if (TextUtils.isEmpty(local_data)) {
                mViewModel.setIsBusy(true);
            } else {
                mViewModel.loadLocalData();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mViewModel.loadData(store_id);
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
        mViewModel.getStoreInfo().observe(this, info -> {
            storeInfo = info;
            Glide.with(activity)
                    .load(storeInfo.getLogo())
                    .centerCrop()
                    .into(imgProduct);
            tvName.setText(storeInfo.getName());
            txtAddress.setText(storeInfo.getAddress());
            txtPhone.setText(TextUtils.isEmpty(storeInfo.getContact_number()) ? "None" : storeInfo.getContact_number());
        });
        
        mViewModel.getPromotionList().observe(this, list -> {
            promotionList.clear();
            if (list.size() == 0) {
                li_empty.setVisibility(View.VISIBLE);
            } else {
                li_empty.setVisibility(View.GONE);
                promotionList.addAll(list);
            }
            storeDetailAdapter.setData(promotionList);
        });
    }
    private void setRecycler() {
        storeDetailAdapter = new StoreDetailAdapter(activity, promotionList, new StoreDetailAdapter.StoreDetailRecyclerListener() {

            @Override
            public void onItemClicked(int pos, PromotionModel model) {
                
            }
        });
        recyclerView.setAdapter(storeDetailAdapter);
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
    public void onStop() {
        super.onStop();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
