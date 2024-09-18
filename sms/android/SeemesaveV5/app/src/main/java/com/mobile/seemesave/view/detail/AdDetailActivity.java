package com.mobile.seemesave.view.detail;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.AdBrandAdapter;
import com.mobile.seemesave.adapter.ProductOneAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.model.common.PostModel;
import com.mobile.seemesave.model.common.ProductOneModel;
import com.mobile.seemesave.model.common.PromotionModel;
import com.mobile.seemesave.model.common.TrendingBrandModel;
import com.mobile.seemesave.model.res.CartRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.detail.AdDetailViewModel;
import com.mobile.seemesave.view_model.detail.PromotionDetailViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdDetailActivity extends BaseActivity {
    private AdDetailViewModel mViewModel;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.imgProduct)
    RoundedImageView imgProduct;
    @BindView(R.id.tvAdName)
    TextView tvAdName;
    @BindView(R.id.tvAdDesc)
    TextView tvAdDesc;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPeriod)
    TextView tvPeriod;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.li_empty)
    LinearLayout li_empty;

    private AdDetailActivity activity;

    private int id = 0;
    private String feed_type = "";
    private boolean isDeliver = false;
    private FeedModel feedInfo = new FeedModel();
    private ArrayList<TrendingBrandModel> brandList = new ArrayList<>();
    private AdBrandAdapter adBrandAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AdDetailViewModel.class);
        setContentView(R.layout.activity_ad_detail);
        ButterKnife.bind(this);
        activity = this;
        id = getIntent().getIntExtra("id", 0);
        feed_type = getIntent().getStringExtra("feed_type");
        isDeliver = getIntent().getBooleanExtra("isDeliver", false);

        initView();
    }

    private void initView() {
        nestedScrollView.setNestedScrollingEnabled(false);
        brandList.clear();
        setRecycler();
        mViewModel.setFeedType(feed_type + "_" + id);

        try {
            String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "HomeAdvert", feed_type + "_" + id);
            if (TextUtils.isEmpty(local_data)) {
                mViewModel.setIsBusy(true);
            } else {
                mViewModel.loadLocalData();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mViewModel.loadData(id);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                mViewModel.setIsBusy(true);
                mViewModel.loadData(id);
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
        mViewModel.getBrandList().observe(this, info -> {
            brandList.clear();
            if (info != null && info.size() == 0) {
                li_empty.setVisibility(View.VISIBLE);
                return;
            } else {
                li_empty.setVisibility(View.GONE);
                brandList.addAll(info);
                adBrandAdapter.setData(brandList);
            }
        });

        mViewModel.getFeedInfo().observe(this, info -> {
            if (info != null && info.getSubMedia() != null) {
                feedInfo = info;
                showView();
            }
        });

    }
    private void showView() {
        if (feedInfo.getSubMedia().size() > 0) {
            Glide.with(activity)
                    .load(feedInfo.getSubMedia().get(0).getMedia())
                    .centerCrop()
                    .into(imgProduct);
        }

        tvAdName.setText(feedInfo.getTitle());
        tvAdDesc.setText(feedInfo.getDescription());
        tvName.setText(feedInfo.getTitle());
        tvPeriod.setText(feedInfo.getStart_date() + "~" + feedInfo.getEnd_date());

    }
    private void setRecycler() {
        adBrandAdapter = new AdBrandAdapter(activity, brandList, new AdBrandAdapter.AdBrandAdapterRecyclerListener() {

            @Override
            public void onItemClicked(int pos, TrendingBrandModel model) {
                int brand_id = model.getId();
                Intent intent = new Intent(activity, BrandDetailActivity.class);
                intent.putExtra("id", brand_id);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adBrandAdapter);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(activity, 3);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @OnClick({R.id.btBack, R.id.imgView})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.imgView:
                Intent intent = new Intent(activity, VideoPlayerActivity.class);
                intent.putExtra("post_id", Integer.valueOf(feedInfo.getId()));
                startActivity(intent);
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
