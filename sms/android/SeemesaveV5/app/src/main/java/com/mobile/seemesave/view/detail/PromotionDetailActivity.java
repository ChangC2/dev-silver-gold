package com.mobile.seemesave.view.detail;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.ProductCategoryAdapter;
import com.mobile.seemesave.adapter.ProductCategoryDetailAdapter;
import com.mobile.seemesave.adapter.ProductOneAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.ProductCategoryModel;
import com.mobile.seemesave.model.common.ProductOneModel;
import com.mobile.seemesave.model.common.PromotionModel;
import com.mobile.seemesave.model.common.PromotionOneModel;
import com.mobile.seemesave.model.res.CartRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.seeall.AllProductCategoryDetailActivity;
import com.mobile.seemesave.view_model.detail.PromotionDetailViewModel;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PromotionDetailActivity extends BaseActivity {
    private PromotionDetailViewModel mViewModel;

    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
//    @BindView(R.id.swipe_refresh)
//    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.imgProduct)
    RoundedImageView imgProduct;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.txtTitle)
    TextView txtTitle;

    @BindView(R.id.lytCombo)
    LinearLayout lytCombo;
    @BindView(R.id.comboIndicator)
    IndefinitePagerIndicator comboIndicator;
    @BindView(R.id.lytBuyGet)
    LinearLayout lytBuyGet;
    @BindView(R.id.buyGetIndicator)
    IndefinitePagerIndicator buyGetIndicator;
    @BindView(R.id.recyclerComboView)
    RecyclerView recyclerComboView;
    @BindView(R.id.recyclerBuyGetView)
    RecyclerView recyclerBuyGetView;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.period)
    TextView period;
    @BindView(R.id.imgView)
    ImageView imgView;
    @BindView(R.id.lytProduct)
    RelativeLayout lytProduct;

    @BindView(R.id.recyclerTabView)
    RecyclerView recyclerTabView;

    @BindView(R.id.recyclerSingleView)
    RecyclerView recyclerSingleView;
    private PromotionDetailActivity activity;

    private int id = 0;
    private String media = "";
    private String brand = "";
    private String feed_type = "";
    private boolean isDeliver = false;
    private PromotionModel promotionOneModel = new PromotionModel();
    private PromotionOneModel promotionInfo = new PromotionOneModel();
    private ArrayList<ProductOneModel> productList = new ArrayList<>();
    private ArrayList<ProductOneModel> singleProductList = new ArrayList<>();
    private ArrayList<ProductOneModel> comboProductList = new ArrayList<>();
    private ArrayList<ProductOneModel> buyGetProductList = new ArrayList<>();
    private ArrayList<ProductCategoryModel> categoryList = new ArrayList<>();

    private ProductOneAdapter comboProductAdapter;
    private ProductOneAdapter buyGetProductAdapter;

    private ProductCategoryAdapter productCategoryTabAdapter;
    private ProductCategoryDetailAdapter productCategoryDetailAdapter;


    private int offset = 0;
    private int limit = 20;
    private boolean isLoading = false;


    private boolean disableDetailView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PromotionDetailViewModel.class);
        setContentView(R.layout.activity_promotion_detail);
        ButterKnife.bind(this);
        activity = this;
        id = getIntent().getIntExtra("id", 0);
        Log.e("pid:", id + "");
        if (getIntent().hasExtra("media")) {
            media = getIntent().getStringExtra("media");
        }
        if (getIntent().hasExtra("brand")) {
            brand = getIntent().getStringExtra("brand");
        }
        if (getIntent().hasExtra("feed_type")) {
            feed_type = getIntent().getStringExtra("feed_type");
        }
        if (getIntent().hasExtra("isDeliver")) {
            isDeliver = getIntent().getBooleanExtra("isDeliver", false);
        }
        if (getIntent().hasExtra("disable")) {
            disableDetailView = true;
        }
        initView();
    }

    private void initView() {
        nestedScrollView.setNestedScrollingEnabled(false);


        productList.clear();
        singleProductList.clear();
        comboProductList.clear();
        buyGetProductList.clear();
        categoryList.clear();

        setComboRecycler();
        setBuyGetRecycler();
        setCategoryTabRecycler();
        setCategoryDetailRecycler();
        mViewModel.setFeedType(feed_type + "_" + id);

        try {
            String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "PromotionDetail", feed_type + "_" + id);
            if (TextUtils.isEmpty(local_data)) {
                mViewModel.setIsBusy(true);
            } else {
                mViewModel.loadLocalData();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mViewModel.loadData(id);
    }

    private void initParam() {
        offset = 0;
        mViewModel.setOffset(offset);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mViewModel.getIsBusy().observe(this, isBusy -> {
            if (isBusy) {
                showLoadingDialog();
            } else {
                hideLoadingDialog();
            }
        });
        mViewModel.getPromotionInfo().observe(this, info -> {
            if (info != null && info.getComboProductList() != null) {
                if (info.getComboProductList().size() == 0) {
                    comboProductList.clear();
                    comboProductAdapter.setData(comboProductList, isDeliver ? 1 : 0);
                    lytCombo.setVisibility(View.GONE);
                } else {
                    lytCombo.setVisibility(View.VISIBLE);
                    comboProductList.addAll(info.getComboProductList());
                    comboProductAdapter.setData(comboProductList, isDeliver ? 1 : 0);
                }
            }
            if (info != null && info.getBuyGetProductList() != null) {
                if (info.getBuyGetProductList().size() == 0) {
                    buyGetProductList.clear();
                    buyGetProductAdapter.setData(buyGetProductList, isDeliver ? 1 : 0);
                    lytBuyGet.setVisibility(View.GONE);
                } else {
                    lytBuyGet.setVisibility(View.VISIBLE);
                    buyGetProductList.addAll(info.getBuyGetProductList());
                    buyGetProductAdapter.setData(buyGetProductList, isDeliver ? 1 : 0);
                }
            }
            if (info != null && info.getCategories() != null) {
                categoryList.clear();
                categoryList.addAll(info.getCategories());
                productCategoryTabAdapter.setData(categoryList);
                productCategoryDetailAdapter.setData(categoryList);
            }
        });

        mViewModel.getData().observe(this, info -> {
            if (info != null && info.getFeed_type() != null) {
                promotionOneModel = info;
                if (info.getSubMedia().size() > 0) {
                    media = info.getSubMedia().get(0).getMedia();
                }
                brand = info.getTitle();
                feed_type = info.getFeed_type();
                if (!getIntent().hasExtra("isDeliver")) {
                    isDeliver = info.isIs_deliver();
                }
                showView();
            }
        });
        mViewModel.getPromotionInfo().observe(this, info -> {
            if (info != null && info.getFeed_type() != null) {
                G.promotionInfo = info;
            }
        });

    }

    private void showView() {
        Glide.with(activity)
                .load(media)
                .into(imgProduct);
        tvName.setText(brand);

        if (feed_type.equalsIgnoreCase("CantMissDeal")) {
            txtTitle.setText(getString(R.string.txt_cant_miss_deal));
        } else if (feed_type.equalsIgnoreCase("ClickCollectDeal")) {
            txtTitle.setText(getString(R.string.txt_click_collect));
        } else if (feed_type.equalsIgnoreCase("VendorPromotion")) {
            txtTitle.setText(getString(R.string.txt_deal_special));
        } else if (feed_type.equalsIgnoreCase("FeaturedStore")) {
            txtTitle.setText(getString(R.string.txt_featured_store));
        } else if (feed_type.equalsIgnoreCase("ExclusiveDeal")) {
            txtTitle.setText(getString(R.string.txt_exclusive_deal));
        } else {
            txtTitle.setText(getString(R.string.txt_click_deliver));
        }
        if (disableDetailView) {
            txtTitle.setText(getString(R.string.txt_best_selling));
            imgView.setVisibility(View.GONE);
            lytProduct.setVisibility(View.GONE);
        } else {
            imgView.setVisibility(View.VISIBLE);
            lytProduct.setVisibility(View.VISIBLE);
        }
        title.setText(promotionOneModel.getTitle());
        period.setText(promotionOneModel.getStart_date() + "~" + promotionOneModel.getEnd_date());
    }

    private void setComboRecycler() {
        comboProductAdapter = new ProductOneAdapter(activity, comboProductList, false, isDeliver ? 1 : 0, new ProductOneAdapter.ProductOneRecyclerListener() {
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
        recyclerComboView.setAdapter(comboProductAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recyclerComboView.setLayoutManager(linearLayoutManager);
        comboIndicator.attachToRecyclerView(recyclerComboView);
    }

    private void setBuyGetRecycler() {
        buyGetProductAdapter = new ProductOneAdapter(activity, buyGetProductList, false, isDeliver ? 1 : 0, new ProductOneAdapter.ProductOneRecyclerListener() {
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
        recyclerBuyGetView.setAdapter(buyGetProductAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recyclerBuyGetView.setLayoutManager(linearLayoutManager);
        buyGetIndicator.attachToRecyclerView(recyclerBuyGetView);
    }

    private void setCategoryTabRecycler() {
        productCategoryTabAdapter = new ProductCategoryAdapter(activity, categoryList, new ProductCategoryAdapter.ProductCategoryAdapterRecyclerListener() {
            @Override
            public void onItemClicked(int pos, ProductCategoryModel model) {
                for (int i = 0; i < categoryList.size(); i++) {
                    categoryList.get(i).setCheck(i == pos);
                }

                productCategoryTabAdapter.setData(categoryList);
                productCategoryDetailAdapter.setData(categoryList);

                recyclerSingleView.getLayoutManager().scrollToPosition(pos);

            }
        });
        recyclerTabView.setAdapter(productCategoryTabAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recyclerTabView.setLayoutManager(linearLayoutManager);
    }
    private void setCategoryDetailRecycler() {
        productCategoryDetailAdapter = new ProductCategoryDetailAdapter(activity, categoryList,isDeliver, new ProductCategoryDetailAdapter.ProductCategoryDetailAdapterRecyclerListener() {
            @Override
            public void onItemClicked(int pos, ProductCategoryModel model) {
                Intent i = new Intent(activity, AllProductCategoryDetailActivity.class);
                i.putExtra("category_name", model.getTitle());
                i.putExtra("category_id", model.getId());
                i.putExtra("promotion_id", G.promotionInfo.getId());
                i.putExtra("is_deliver", isDeliver);
                startActivity(i);
            }
        });
        recyclerSingleView.setAdapter(productCategoryDetailAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerSingleView.setLayoutManager(linearLayoutManager);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessCart(CartRes res) {
        if (res.isStatus()) {
            Toast.makeText(activity, !isDeliver ? R.string.add_success_shopping_cart : R.string.add_success_deliver_cart, Toast.LENGTH_LONG).show();
        } else {
            if (!res.getMessage().equalsIgnoreCase("")) {
                Toast.makeText(activity, res.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @OnClick({R.id.btBack, R.id.imgView})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.imgView:
                Intent intent = new Intent(activity, VideoPlayerActivity.class);
                intent.putExtra("post_id", Integer.valueOf(promotionOneModel.getId()));
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
