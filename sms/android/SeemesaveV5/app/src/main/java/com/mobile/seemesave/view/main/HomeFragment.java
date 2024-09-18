package com.mobile.seemesave.view.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.AdAdapter;
import com.mobile.seemesave.adapter.ExclusiveDealAdapter;
import com.mobile.seemesave.adapter.FeaturedBrandAdapter;
import com.mobile.seemesave.adapter.FeedAdapter;
import com.mobile.seemesave.adapter.HomeBestDealsAdapter;
import com.mobile.seemesave.adapter.ProductOneAdapter;
import com.mobile.seemesave.adapter.DiscoverAdapter;
import com.mobile.seemesave.adapter.FeaturedStoreAdapter;
import com.mobile.seemesave.adapter.MissDealAdapter;
import com.mobile.seemesave.adapter.SpecialDealAdapter;
import com.mobile.seemesave.adapter.StoreCategoryAdapter;
import com.mobile.seemesave.adapter.TrendingBrandAdapter;
import com.mobile.seemesave.base.BaseFragment;
import com.mobile.seemesave.model.common.ProductOneModel;
import com.mobile.seemesave.model.common.DiscoverModel;
import com.mobile.seemesave.model.common.PromotionModel;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.model.common.StoreCategoryModel;
import com.mobile.seemesave.model.common.TrendingBrandModel;
import com.mobile.seemesave.model.res.CartRes;
import com.mobile.seemesave.model.res.CommonRes;
import com.mobile.seemesave.model.res.HomeRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.bookmark.BookMarkAddBottomSheet;
import com.mobile.seemesave.view.detail.AdDetailActivity;
import com.mobile.seemesave.view.detail.BrandDetailActivity;
import com.mobile.seemesave.view.detail.PromotionDetailActivity;
import com.mobile.seemesave.view.detail.StoreCategoryDetailActivity;
import com.mobile.seemesave.view.detail.StoreDetailActivity;
import com.mobile.seemesave.view.detail.StoryPlayerActivity;
import com.mobile.seemesave.view.detail.VideoPlayerActivity;
import com.mobile.seemesave.view.seeall.AllAdvertiseActivity;
import com.mobile.seemesave.view.seeall.AllBestSellingActivity;
import com.mobile.seemesave.view.seeall.AllCanMissActivity;
import com.mobile.seemesave.view.seeall.AllDealActivity;
import com.mobile.seemesave.view.seeall.AllExclusiveActivity;
import com.mobile.seemesave.view.seeall.AllFeaturedBrandActivity;
import com.mobile.seemesave.view.seeall.AllFeaturedStoreActivity;
import com.mobile.seemesave.view.seeall.AllStoreCategoryActivity;
import com.mobile.seemesave.view_model.main.HomeFragViewModel;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment {
    private HomeFragViewModel mViewModel;
    private View mFragView;
    @BindView(R.id.imgTop)
    ImageView imgTop;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.li_discover)
    LinearLayout li_discover;
    @BindView(R.id.li_miss)
    LinearLayout li_miss;
    @BindView(R.id.li_special)
    LinearLayout li_special;
    @BindView(R.id.li_brand)
    LinearLayout li_brand;

    @BindView(R.id.li_store_category)
    LinearLayout li_store_category;
    @BindView(R.id.li_store)
    LinearLayout li_store;
    @BindView(R.id.li_best_selling)
    LinearLayout li_best_selling;

    @BindView(R.id.li_exclusive)
    LinearLayout li_exclusive;

    @BindView(R.id.li_featured_brand)
    LinearLayout li_featured_brand;

    @BindView(R.id.li_advertise)
    LinearLayout li_advertise;

    @BindView(R.id.btnCollect)
    LinearLayout btnCollect;
    @BindView(R.id.btnDeliver)
    LinearLayout btnDeliver;

    @BindView(R.id.txt_collect)
    TextView txtCollect;
    @BindView(R.id.txt_deliver)
    TextView txtDeliver;

    @BindView(R.id.li_recent_post)
    LinearLayout li_recent_post;
    @BindView(R.id.li_update_post)
    LinearLayout li_update_post;

    @BindView(R.id.txt_recent_post)
    TextView txt_recent_post;
    @BindView(R.id.txt_update_post)
    TextView txt_update_post;

    @BindView(R.id.bottom_recent_post)
    View bottom_recent_post;
    @BindView(R.id.bottom_update_post)
    View bottom_update_post;

    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private DiscoverAdapter discoverAdapter;

    @BindView(R.id.recyclerMissView)
    RecyclerView recyclerMissView;
    private MissDealAdapter missDealAdapter;

    @BindView(R.id.recyclerSpecialView)
    RecyclerView recyclerSpecialView;
    private SpecialDealAdapter specialDealAdapter;

    @BindView(R.id.recyclerExclusiveView)
    RecyclerView recyclerExclusiveView;
    private ExclusiveDealAdapter exclusiveDealAdapter;

    @BindView(R.id.recyclerBrandView)
    RecyclerView recyclerBrandView;
    private TrendingBrandAdapter trendingBrandAdapter;

    @BindView(R.id.recyclerStoreCategoryView)
    RecyclerView recyclerStoreCategoryView;
    private StoreCategoryAdapter storeCategoryAdapter;
    @BindView(R.id.recyclerStoreView)
    RecyclerView recyclerStoreView;
    private FeaturedStoreAdapter featuredStoreAdapter;

    @BindView(R.id.recyclerSellingView)
    RecyclerView recyclerSellingView;
    private HomeBestDealsAdapter bestSellingAdapter;

    @BindView(R.id.recyclerRecentPost)
    RecyclerView recyclerRecentPost;
    private FeedAdapter postRecentAdapter;

    @BindView(R.id.recyclerPopularPost)
    RecyclerView recyclerPopularPost;
    private FeedAdapter postPopularAdapter;

    @BindView(R.id.recyclerFeaturedBrandView)
    RecyclerView recyclerFeaturedBrandView;
    private FeaturedBrandAdapter featuredBrandAdapter;

    @BindView(R.id.recyclerAdView)
    RecyclerView recyclerAdView;
    private AdAdapter advertiseAdapter;

    @BindView(R.id.pagerIndicator)
    IndefinitePagerIndicator pagerIndicator;

    @BindView(R.id.lytEnd)
    LinearLayout lytEnd;

    ArrayList<DiscoverModel> discoverList = new ArrayList<>();
    ArrayList<FeedModel> postRecentList = new ArrayList<>();
    ArrayList<FeedModel> postPopularList = new ArrayList<>();
    ArrayList<TrendingBrandModel> brandList = new ArrayList<>();
    ArrayList<StoreCategoryModel> storeCategoryList = new ArrayList<>();
    ArrayList<FeedModel> storeList = new ArrayList<>();
    ArrayList<ProductOneModel> sellingList = new ArrayList<>();
    ArrayList<PromotionModel> mBestSellingList = new ArrayList<>();
    ArrayList<FeedModel> specialDealList = new ArrayList<>();
    ArrayList<PromotionModel> exclusiveDealList = new ArrayList<>();
    ArrayList<FeedModel> missDealList = new ArrayList<>();
    ArrayList<FeedModel> featuredBrandList = new ArrayList<>();
    ArrayList<FeedModel> advertiseList = new ArrayList<>();

    private int offset = 0;
    private int limit = 10;
    private boolean isLoading = false;
    private boolean isLast = false;

    private boolean is_deliver = false;
    private boolean isRecent = true;
    Context ctx;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        ctx = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(HomeFragViewModel.class);
        mFragView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, mFragView);
        initView();
        return mFragView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
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
        mViewModel.getDiscoverList().observe(this, list -> {
            discoverList.clear();
            if (list.size() == 0) {
                li_discover.setVisibility(View.GONE);
            } else {
                li_discover.setVisibility(View.VISIBLE);
                discoverList.addAll(list);
                discoverAdapter.setData(discoverList);
            }
        });
        mViewModel.getBrandList().observe(this, list -> {
            brandList.clear();
            if (list.size() == 0) {
                li_brand.setVisibility(View.GONE);
            } else {
                li_brand.setVisibility(View.VISIBLE);
                brandList.addAll(list);
                trendingBrandAdapter.setData(brandList);
            }
        });
        mViewModel.getStoreCategoryList().observe(this, list -> {
            storeCategoryList.clear();
            G.storeCategoryList.clear();
            if (list.size() == 0) {
                li_store_category.setVisibility(View.GONE);
            } else {
                li_store_category.setVisibility(View.VISIBLE);
                storeCategoryList.addAll(list);
                G.storeCategoryList.addAll(list);
                storeCategoryAdapter.setData(storeCategoryList);
            }
        });
        mViewModel.getStoreList().observe(this, list -> {
            storeList.clear();
            if (list.size() == 0) {
                li_store.setVisibility(View.GONE);
                return;
            } else {
                li_store.setVisibility(View.VISIBLE);
                storeList.addAll(list);
                featuredStoreAdapter.setData(storeList);
            }
        });
        mViewModel.getSpecialDealList().observe(this, list -> {
            specialDealList.clear();
            if (list.size() == 0) {
                li_special.setVisibility(View.GONE);
                return;
            } else {
                li_special.setVisibility(View.VISIBLE);
                specialDealList.addAll(list);
                specialDealAdapter.setData(specialDealList);
            }
        });
        mViewModel.getMissDealList().observe(this, list -> {
            missDealList.clear();
            if (list.size() == 0) {
                li_miss.setVisibility(View.GONE);
                return;
            } else {
                li_miss.setVisibility(View.VISIBLE);
                missDealList.addAll(list);
                missDealAdapter.setData(missDealList);
            }
        });
        mViewModel.getExclusiveDealList().observe(this, list -> {
            exclusiveDealList.clear();
            if (list.size() == 0) {
                li_exclusive.setVisibility(View.GONE);
                return;
            } else {
                li_exclusive.setVisibility(View.VISIBLE);
                exclusiveDealList.addAll(list);
                exclusiveDealAdapter.setData(exclusiveDealList);
            }
        });
        mViewModel.getFeaturedBrandList().observe(this, list -> {
            featuredBrandList.clear();
            if (list.size() == 0) {
                li_featured_brand.setVisibility(View.GONE);
                return;
            } else {
                li_featured_brand.setVisibility(View.VISIBLE);
                featuredBrandList.addAll(list);
                featuredBrandAdapter.setData(featuredBrandList);
            }
        });
        mViewModel.getAdList().observe(this, list -> {
            advertiseList.clear();
            if (list.size() == 0) {
                li_advertise.setVisibility(View.GONE);
                return;
            } else {
                li_advertise.setVisibility(View.VISIBLE);
                advertiseList.addAll(list);
                advertiseAdapter.setData(advertiseList);
            }
        });

        mViewModel.getBestSellingList().observe(this, list -> {
            mBestSellingList.clear();
            if (list.size() != 0) {
                mBestSellingList.addAll(list);
            }
        });

        mViewModel.getPostPopularList().observe(this, list -> {
            postPopularList.clear();
            if (list.size() == 0) {
                showEmptyPostView();
            } else {
                hideEmptyPostView();
                postPopularList.addAll(list);
                postPopularAdapter.setData(postPopularList);
            }
        });

        mViewModel.getPostRecentList().observe(this, list -> {
            if (offset == 0 && list.size() == 0) {
                showEmptyPostView();
            } else {
                hideEmptyPostView();
                if (offset == 0) {
                    postRecentList.clear();
                }
                if (list.size() < limit) {
                    isLast = true;
                    lytEnd.setVisibility(View.VISIBLE);
                }
                postRecentList.addAll(list);
                postRecentAdapter.setData(postRecentList);
                isLoading = false;
            }
        });

//        mViewModel.getSellingList().observe(this, list -> {
//            sellingList.clear();
//            if (list.size() == 0) {
//                li_best_selling.setVisibility(View.GONE);
//            } else {
//                li_best_selling.setVisibility(View.VISIBLE);
//                sellingList.addAll(list);
//                bestSellingAdapter.setData(sellingList, is_deliver);
//            }
//        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccess(HomeRes res) {
        if (res.isStatus()) {
            if (offset == 0) {
                String data = new Gson().toJson(res, new TypeToken<HomeRes>() {
                }.getType());
                DatabaseQueryClass.getInstance().insertData(
                        G.getUserID(),
                        "HomeFragment",
                        data,
                        "",
                        ""
                );
            }
        } else {
            if (!res.getMessage().equalsIgnoreCase("")) {
                Toast.makeText(getActivity(), res.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessCart(CartRes res) {
        if (res.isStatus()) {
            Toast.makeText(getActivity(), is_deliver ? R.string.add_success_deliver_cart : R.string.add_success_shopping_cart, Toast.LENGTH_LONG).show();
        } else {
            if (!res.getMessage().equalsIgnoreCase("")) {
                Toast.makeText(getActivity(), res.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessLocation(CommonRes res) {
        if (res.isStatus()) {
            if (res.getPage().equalsIgnoreCase("update_location")) {
                initParam();
                mViewModel.setIsBusy(true);
                mViewModel.loadNewsFeed();
            }
        }
    }

    private void showEmptyPostView() {

    }

    private void hideEmptyPostView() {

    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        initParam();
        nestedScrollView.setNestedScrollingEnabled(false);
        txtName.setText("Hi, " + G.user.getFirst_name());
        setDiscoverRecycler();
        setMissDealRecycler();
        setSpecialDealRecycler();
        setExclusiveDealRecycler();
        setTrendingBrandRecycler();
        setStoreCategoryRecycler();
        setFeaturedStoreRecycler();
        setFeaturedBrandRecycler();
        setAdvertiseRecycler();
        setBestSellingRecycler();
        setPostRecentRecycler();
        setPostPopularRecycler();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                initParam();
                mViewModel.setIsBusy(true);
                mViewModel.loadNewsFeed();
            }
        });
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (isRecent) {
                    View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
                    int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));
                    if (diff == 0 && !isLoading && !isLast) {
                        isLoading = true;
                        offset = offset + limit;
                        mViewModel.setOffset(offset);
                        mViewModel.setIsBusy(true);
                        mViewModel.loadNewsFeed();
                    }
                }
            }
        });
        if (G.user.getLatitude() == 0 && G.user.getLongitude() == 0) {
            Toast.makeText(getActivity(), R.string.msg_location, Toast.LENGTH_LONG).show();
            return;
        }
        try {
            String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "HomeFragment", "");
            if (TextUtils.isEmpty(local_data)) {
                mViewModel.setIsBusy(true);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mViewModel.loadNewsFeed();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initParam() {
        discoverList.clear();
        brandList.clear();
        storeList.clear();
        sellingList.clear();
        mBestSellingList.clear();
        postRecentList.clear();
        postPopularList.clear();
        offset = 0;
        isLast = false;
        isLoading = false;
        mViewModel.setOffset(offset);
        mViewModel.setIsDeliver(is_deliver);
        lytEnd.setVisibility(View.GONE);
    }

    @OnClick({R.id.li_recent_post, R.id.li_update_post, R.id.btnCollect, R.id.btnDeliver, R.id.imgTop, R.id.btnCanMissAll, R.id.btnSpecialAll, R.id.btnExclusiveAll, R.id.btnFeatureBrandAll, R.id.btnFeaturedStoreAll, R.id.btnBestSellingAll, R.id.btnAdAll, R.id.btnStoreCategoryAll})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.li_recent_post:
                setTabPost(0);
                break;
            case R.id.li_update_post:
                setTabPost(1);
                break;
            case R.id.btnCollect:
                setCollectBtn(0);
                break;
            case R.id.btnDeliver:
                setCollectBtn(1);
                break;
            case R.id.imgTop:
                nestedScrollView.scrollTo(0, 0);
                break;
            case R.id.btnCanMissAll:
                Intent intent = new Intent(getActivity(), AllCanMissActivity.class);
                intent.putExtra("isDeliver", is_deliver);
                startActivity(intent);
                break;
            case R.id.btnSpecialAll:
                Intent intent6 = new Intent(getActivity(), AllDealActivity.class);
                intent6.putExtra("isDeliver", is_deliver);
                startActivity(intent6);
                break;
            case R.id.btnExclusiveAll:
                Intent intent1 = new Intent(getActivity(), AllExclusiveActivity.class);
                intent1.putExtra("isDeliver", is_deliver);
                startActivity(intent1);
                break;
            case R.id.btnFeaturedStoreAll:
                Intent intent2 = new Intent(getActivity(), AllFeaturedStoreActivity.class);
                intent2.putExtra("isDeliver", is_deliver);
                startActivity(intent2);
                break;
            case R.id.btnBestSellingAll:
                Intent intent3 = new Intent(getActivity(), AllBestSellingActivity.class);
                intent3.putExtra("isDeliver", is_deliver);
                startActivity(intent3);
                break;
            case R.id.btnAdAll:
                Intent intent4 = new Intent(getActivity(), AllAdvertiseActivity.class);
                intent4.putExtra("isDeliver", is_deliver);
                startActivity(intent4);
                break;
            case R.id.btnFeatureBrandAll:
                Intent intent5 = new Intent(getActivity(), AllFeaturedBrandActivity.class);
                intent5.putExtra("isDeliver", is_deliver);
                startActivity(intent5);
                break;
            case R.id.btnStoreCategoryAll:
                Intent intentAllStoreCategory = new Intent(getActivity(), AllStoreCategoryActivity.class);
                startActivity(intentAllStoreCategory);
                break;
        }
    }

    private void setDiscoverRecycler() {
        discoverAdapter = new DiscoverAdapter(getActivity(), discoverList, new DiscoverAdapter.DiscoverRecyclerListener() {
            @Override
            public void onItemClicked(int pos, DiscoverModel model) {
                Intent intent = new Intent(getActivity(), StoryPlayerActivity.class);
                intent.putExtra("selected_index", pos);
                getActivity().startActivity(intent);
            }

            @Override
            public void onItemAdd(int pos, DiscoverModel model) {

            }
        });
        recyclerView.setAdapter(discoverAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setMissDealRecycler() {
        missDealAdapter = new MissDealAdapter(getActivity(), missDealList, new MissDealAdapter.MissDealRecyclerListener() {
            @Override
            public void onItemClicked(int pos, FeedModel model) {
                showOptionDlg(pos, 2);
            }

        });
        recyclerMissView.setAdapter(missDealAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerMissView.setLayoutManager(linearLayoutManager);
    }

    private void setSpecialDealRecycler() {
        specialDealAdapter = new SpecialDealAdapter(getActivity(), specialDealList, new SpecialDealAdapter.SpecialDealRecyclerListener() {
            @Override
            public void onItemClicked(int pos, FeedModel model) {
                startFeedDetail(specialDealList.get(pos), 3);
            }
        });
        recyclerSpecialView.setAdapter(specialDealAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerSpecialView.setLayoutManager(linearLayoutManager);
    }

    private void setExclusiveDealRecycler() {
        exclusiveDealAdapter = new ExclusiveDealAdapter(getActivity(), exclusiveDealList, new ExclusiveDealAdapter.ExclusiveDealRecyclerListener() {
            @Override
            public void onItemClicked(int pos, PromotionModel model) {
                Intent intent = new Intent(getActivity(), PromotionDetailActivity.class);
                intent.putExtra("id", model.getId());
                intent.putExtra("media", model.getSubMedia().get(0).getMedia());
                intent.putExtra("feed_type", model.getFeed_type());
                intent.putExtra("brand", model.getTitle());
                intent.putExtra("isDeliver", is_deliver);
                startActivity(intent);
            }

        });
        recyclerExclusiveView.setAdapter(exclusiveDealAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerExclusiveView.setLayoutManager(linearLayoutManager);
    }

    private void setTrendingBrandRecycler() {
        trendingBrandAdapter = new TrendingBrandAdapter(getActivity(), brandList, new TrendingBrandAdapter.TrendingBrandRecyclerListener() {
            @Override
            public void onItemClicked(int pos, TrendingBrandModel model) {
                int brand_id = model.getId();
                Intent intent = new Intent(getActivity(), BrandDetailActivity.class);
                intent.putExtra("id", brand_id);
                startActivity(intent);
            }

        });
        recyclerBrandView.setAdapter(trendingBrandAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerBrandView.setLayoutManager(linearLayoutManager);
    }

    private void setStoreCategoryRecycler() {
        storeCategoryAdapter = new StoreCategoryAdapter(getActivity(), storeCategoryList, new StoreCategoryAdapter.StoreCategoryAdapterRecyclerListener() {
            @Override
            public void onItemClicked(int pos, StoreCategoryModel model) {
                Intent intent = new Intent(getActivity(), StoreCategoryDetailActivity.class);
                intent.putExtra("category_id", model.getId());
                intent.putExtra("category_name", model.getName());
                startActivity(intent);
            }
        });
        recyclerStoreCategoryView.setAdapter(storeCategoryAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerStoreCategoryView.setLayoutManager(linearLayoutManager);
    }

    private void setFeaturedStoreRecycler() {
        featuredStoreAdapter = new FeaturedStoreAdapter(getActivity(), storeList, new FeaturedStoreAdapter.FeaturedStoreRecyclerListener() {
            @Override
            public void onItemClicked(int pos, FeedModel model) {
                int store_id = model.getRelated_id();
                if (store_id != 0) {
                    Intent intent = new Intent(getActivity(), StoreDetailActivity.class);
                    intent.putExtra("id", store_id);
                    startActivity(intent);
                }
            }

        });
        recyclerStoreView.setAdapter(featuredStoreAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerStoreView.setLayoutManager(linearLayoutManager);
    }

    private void setFeaturedBrandRecycler() {
        featuredBrandAdapter = new FeaturedBrandAdapter(getActivity(), featuredBrandList, new FeaturedBrandAdapter.FeaturedBrandRecyclerListener() {
            @Override
            public void onItemClicked(int pos, FeedModel model) {
                int brand_id = model.getRelated_id();
                Intent intent = new Intent(getActivity(), BrandDetailActivity.class);
                intent.putExtra("id", brand_id);
                startActivity(intent);
            }
        });
        recyclerFeaturedBrandView.setAdapter(featuredBrandAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerFeaturedBrandView.setLayoutManager(linearLayoutManager);
    }

    private void setAdvertiseRecycler() {
        advertiseAdapter = new AdAdapter(getActivity(), advertiseList, new AdAdapter.AdRecyclerListener() {
            @Override
            public void onItemClicked(int pos, FeedModel model) {
                Intent intent = new Intent(getActivity(), AdDetailActivity.class);
                intent.putExtra("id", model.getId());
                intent.putExtra("feed_type", model.getFeed_type());
                intent.putExtra("isDeliver", is_deliver);
                startActivity(intent);
            }

        });
        recyclerAdView.setAdapter(advertiseAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerAdView.setLayoutManager(linearLayoutManager);
        pagerIndicator.attachToRecyclerView(recyclerAdView);
    }

    private void setBestSellingRecycler() {
        bestSellingAdapter = new HomeBestDealsAdapter(ctx, sellingList, is_deliver, new HomeBestDealsAdapter.ProductCategoryDetailAdapterRecyclerListener() {
            @Override
            public void onItemClicked(int pos, ProductOneModel model) {

            }
        });
        recyclerSellingView.setAdapter(bestSellingAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerSellingView.setLayoutManager(linearLayoutManager);
    }

    private void setPostPopularRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerPopularPost.setLayoutManager(linearLayoutManager);
        postPopularAdapter = new FeedAdapter(getActivity(), postPopularList, new FeedAdapter.FeedRecyclerListener() {

            @Override
            public void onItemClicked(int pos, FeedModel model) {
                startFeedDetail(model, 1);
            }

            @Override
            public void onOption(int pos, FeedModel model) {
                showOptionDlg(pos, 1);
            }

            @Override
            public void onBookmark(int pos, FeedModel model) {
                if (model.isMarked()) {
                    apiCallForDeleteBookmark(model.getId(), pos, "popular");
                } else {
                    BookMarkAddBottomSheet detailFragment = BookMarkAddBottomSheet.newInstance((cID, pID) -> {
                        apiCallForSetBookmark(pID, cID, pos, "popular");
                    });
                    detailFragment.post_id = model.getId();
                    detailFragment.show(getActivity().getSupportFragmentManager(), "AddBookMarkBottomSheet");
                }
            }
        });
        recyclerPopularPost.setAdapter(postPopularAdapter);
    }

    private void setPostRecentRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerRecentPost.setLayoutManager(linearLayoutManager);
        postRecentAdapter = new FeedAdapter(getActivity(), postRecentList, new FeedAdapter.FeedRecyclerListener() {
            @Override
            public void onItemClicked(int pos, FeedModel model) {
                startFeedDetail(model, 0);
            }

            @Override
            public void onOption(int pos, FeedModel model) {

                showOptionDlg(pos, 0);
            }

            @Override
            public void onBookmark(int pos, FeedModel model) {
                if (model.isMarked()) {
                    apiCallForDeleteBookmark(model.getId(), pos, "recent");
                } else {
                    BookMarkAddBottomSheet detailFragment = BookMarkAddBottomSheet.newInstance((cID, pID) -> {
                        apiCallForSetBookmark(pID, cID, pos, "recent");
                    });
                    detailFragment.post_id = model.getId();
                    detailFragment.show(getActivity().getSupportFragmentManager(), "AddBookMarkBottomSheet");
                }


                return;
            }
        });
        recyclerRecentPost.setAdapter(postRecentAdapter);

    }

    private void startFeedDetail(FeedModel model, int type) {
        int id = model.getId();
        Intent intent;
        if (type == 0 || type == 1) {
            intent = new Intent(getActivity(), VideoPlayerActivity.class);
            intent.putExtra("url", model.getMediaList().get(0));
            intent.putExtra("post_id", id);
        } else {
            String media = "";
            if (model.getSubMedia() != null && model.getSubMedia().size() > 0) {
                media = model.getSubMedia().get(0).getMedia();
            }
            intent = new Intent(getActivity(), PromotionDetailActivity.class);
            intent.putExtra("id", model.getId());
            intent.putExtra("media", media);
            intent.putExtra("feed_type", model.getFeed_type());
            intent.putExtra("brand", model.getTitle());
            intent.putExtra("isDeliver", is_deliver);
        }

        startActivity(intent);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showOptionDlg(int pos, int type) {
        //type = 0; postrecent, type =1; popular; type = 2; missdeal, type = 3; specialdeal
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dlg_option, null);
        final android.app.AlertDialog dlg = new android.app.AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dlg.setCanceledOnTouchOutside(true);
        ImageView imgClose = dialogView.findViewById(R.id.imgClose);
        LinearLayout lytView = dialogView.findViewById(R.id.lytView);
        LinearLayout lytFav = dialogView.findViewById(R.id.lytFav);
        LinearLayout lytBooking = dialogView.findViewById(R.id.lytBooking);
        LinearLayout lytFollow = dialogView.findViewById(R.id.lytFollow);
        ImageView imgFav = dialogView.findViewById(R.id.imgFav);
        TextView txtFav = dialogView.findViewById(R.id.txtFav);
        ImageView imgFollow = dialogView.findViewById(R.id.imgFollow);
        TextView txtFollow = dialogView.findViewById(R.id.txtFollow);
        FeedModel model = new FeedModel();
        if (type == 0) {
            model = postRecentList.get(pos);
        } else if (type == 1) {
            model = postPopularList.get(pos);
        } else if (type == 2) {
            model = missDealList.get(pos);
        } else if (type == 3) {
            model = specialDealList.get(pos);
        }
        if (model.isLike()) {
            imgFav.setImageDrawable(getContext().getDrawable(R.drawable.ic_heart));
            txtFav.setText(getString(R.string.txt_remove_favourite));
        } else {
            imgFav.setImageDrawable(getContext().getDrawable(R.drawable.ic_unheart));
            txtFav.setText(getString(R.string.txt_add_favourite));
        }
        String bookingMail = "";
        if (model.getPost_info() != null && !TextUtils.isEmpty(model.getPost_info().getEmail())) {
            lytBooking.setVisibility(View.VISIBLE);
            bookingMail = model.getPost_info().getEmail();
        } else {
            lytBooking.setVisibility(View.GONE);
        }
        String finalBookingMail = bookingMail;
        lytBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "Appointment Booking";
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + finalBookingMail)); // only email apps should handle this
                String[] addresses = {finalBookingMail};
                intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                intent.putExtra(Intent.EXTRA_SUBJECT, text);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "There is no email application installed.", Toast.LENGTH_SHORT).show();
                }
                dlg.dismiss();
            }
        });
        if (model.isFollow()) {
            imgFollow.setImageDrawable(getContext().getDrawable(R.drawable.ic_delete_user));
            if (type > 1) {
                txtFollow.setText(getString(R.string.txt_store_unfollow));
            } else {
                txtFollow.setText(getString(R.string.txt_unfollow));
            }
        } else {
            imgFollow.setImageDrawable(getContext().getDrawable(R.drawable.ic_add_user));
            if (type > 1) {
                txtFollow.setText(getString(R.string.txt_store_follow));
            } else {
                txtFollow.setText(getString(R.string.txt_follow));
            }
        }
        FeedModel finalModel = model;
        lytView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFeedDetail(finalModel, type);
                dlg.dismiss();
            }
        });

        lytFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (G.isNetworkAvailable(getActivity())) {
                    apiCallForPPL("CPL", String.valueOf(finalModel.getId()));
                    setLikeUI(pos, type);
                    String token = G.pref.getString("token", "");
                    Ion.with(getActivity())
                            .load(G.SetFeedLikeUrl)
                            .addHeader("Authorization", "Bearer " + token)
                            .setBodyParameter("newsfeed_id", String.valueOf(finalModel.getId()))
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {
                                    Log.e("sss", "aaa");
                                }
                            });
                }
                dlg.dismiss();
            }
        });
        lytFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (G.isNetworkAvailable(getActivity())) {
                    setFollowUI(pos, type);
                    String token = G.pref.getString("token", "");
                    if (type < 2) {
                        Ion.with(getActivity())
                                .load(G.SetFeedFollowUrl)
                                .addHeader("Authorization", "Bearer " + token)
                                .setBodyParameter("newsfeed_id", String.valueOf(finalModel.getId()))
                                .setBodyParameter("isFollow", String.valueOf(!finalModel.isFollow()))
                                .asString()
                                .setCallback(new FutureCallback<String>() {
                                    @Override
                                    public void onCompleted(Exception e, String result) {
                                    }
                                });
                    } else {
                        Ion.with(getActivity())
                                .load(G.StoreFollowUrl)
                                .addHeader("Authorization", "Bearer " + G.pref.getString("token", ""))
                                .setBodyParameter("id", String.valueOf(finalModel.getRelated_id()))
                                .asString()
                                .setCallback(new FutureCallback<String>() {
                                    @Override
                                    public void onCompleted(Exception e, String result) {
                                    }
                                });

                    }

                }
                dlg.dismiss();
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        dlg.setCanceledOnTouchOutside(false);
        dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dlg.show();
    }

    private void setLikeUI(int pos, int type) {
        if (type == 0) {
            postRecentList.get(pos).setLikeCount(postRecentList.get(pos).isLike() ? postRecentList.get(pos).getLikeCount() - 1 : postRecentList.get(pos).getLikeCount() + 1);
            postRecentList.get(pos).setLike(!postRecentList.get(pos).isLike());
            postRecentAdapter.setData(postRecentList);
        } else if (type == 1) {
            postPopularList.get(pos).setLikeCount(postPopularList.get(pos).isLike() ? postPopularList.get(pos).getLikeCount() - 1 : postPopularList.get(pos).getLikeCount() + 1);
            postPopularList.get(pos).setLike(!postPopularList.get(pos).isLike());
            postPopularAdapter.setData(postPopularList);
        } else if (type == 2) {
            missDealList.get(pos).setLikeCount(missDealList.get(pos).isLike() ? missDealList.get(pos).getLikeCount() - 1 : missDealList.get(pos).getLikeCount() + 1);
            missDealList.get(pos).setLike(!missDealList.get(pos).isLike());
            missDealAdapter.setData(missDealList);
        } else if (type == 3) {
            specialDealList.get(pos).setLikeCount(specialDealList.get(pos).isLike() ? specialDealList.get(pos).getLikeCount() - 1 : specialDealList.get(pos).getLikeCount() + 1);
            specialDealList.get(pos).setLike(!specialDealList.get(pos).isLike());
            specialDealAdapter.setData(specialDealList);
        }

    }

    private void setFollowUI(int pos, int type) {
        if (type == 0) {
            postRecentList.get(pos).setFollow(!postRecentList.get(pos).isFollow());
            postRecentAdapter.setData(postRecentList);
        } else if (type == 1) {
            postPopularList.get(pos).setFollow(!postPopularList.get(pos).isFollow());
            postPopularAdapter.setData(postPopularList);
        } else if (type == 2) {
            missDealList.get(pos).setFollow(!missDealList.get(pos).isFollow());
            missDealAdapter.setData(missDealList);
        } else if (type == 3) {
            specialDealList.get(pos).setFollow(!specialDealList.get(pos).isFollow());
            specialDealAdapter.setData(specialDealList);
        }
    }

    public void apiCallForPPL(String type, String ids) {
        String token = G.pref.getString("token", "");
        String url = String.format(java.util.Locale.US, G.FeedPPLUrl, type, ids);
        Ion.with(getActivity())
                .load(url)
                .addHeader("Authorization", "Bearer " + token)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                    }
                });
    }

    void apiCallForDeleteBookmark(int post_id, int position, String type) {
        if (G.isNetworkAvailable(getActivity())) {
            Ion.with(getActivity())
                    .load("DELETE", G.RemoveFeedBookmarkUrl)
                    .addHeader("Authorization", "Bearer " + G.pref.getString("token", ""))
                    .setBodyParameter("feed_id", String.valueOf(post_id))
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            FeedModel item;
                            if (type.equalsIgnoreCase("recent")) {
                                item = postRecentList.get(position);
                                item.setMarked(false);
                                postRecentList.set(position, item);
                                if (postRecentAdapter != null) {
                                    postRecentAdapter.notifyDataSetChanged();
                                }
                            } else {
                                item = postPopularList.get(position);
                                item.setMarked(false);
                                postPopularList.set(position, item);
                                if (postPopularAdapter != null) {
                                    postPopularAdapter.notifyDataSetChanged();
                                }
                            }

                        }
                    });
        } else {
            Toast.makeText(getActivity(), R.string.connection_fail, Toast.LENGTH_LONG).show();
        }
    }

    void apiCallForSetBookmark(int post_id, String category_id, int position, String type) {
        if (G.isNetworkAvailable(getActivity()) && !category_id.equalsIgnoreCase("-1")) {
            JsonObject json = new JsonObject();
            json.addProperty("category_id", category_id);
            json.addProperty("feed_id", post_id);
            String token = G.pref.getString("token", "");
            showLoadingDialog();
            Ion.with(this)
                    .load("POST", G.SetFeedBookmarkUrl)
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("Content-Type", "application/json")
                    .setJsonObjectBody(json)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            hideLoadingDialog();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("status")) {
                                    FeedModel item;
                                    if (type.equalsIgnoreCase("recent")) {
                                        item = postRecentList.get(position);
                                        item.setMarked(true);
                                        postRecentList.set(position, item);
                                        if (postRecentAdapter != null) {
                                            postRecentAdapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        item = postPopularList.get(position);
                                        item.setMarked(true);
                                        postPopularList.set(position, item);
                                        if (postPopularAdapter != null) {
                                            postPopularAdapter.notifyDataSetChanged();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException jsonException) {
                                Toast.makeText(getActivity(), R.string.connection_fail, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getActivity(), R.string.connection_fail, Toast.LENGTH_LONG).show();
        }
    }

    private void setTabPost(int tab) {
        switch (tab) {
            case 0:
                isRecent = true;
                txt_recent_post.setTextColor(getResources().getColor(R.color.main_blue_color));
                bottom_recent_post.setVisibility(View.VISIBLE);
                txt_update_post.setTextColor(getResources().getColor(R.color.grey_dark));
                bottom_update_post.setVisibility(View.INVISIBLE);
                recyclerRecentPost.setVisibility(View.VISIBLE);
                recyclerPopularPost.setVisibility(View.GONE);
                break;
            case 1:
                isRecent = false;
                txt_recent_post.setTextColor(getResources().getColor(R.color.grey_dark));
                bottom_recent_post.setVisibility(View.INVISIBLE);
                txt_update_post.setTextColor(getResources().getColor(R.color.main_blue_color));
                bottom_update_post.setVisibility(View.VISIBLE);
                recyclerRecentPost.setVisibility(View.GONE);
                recyclerPopularPost.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setCollectBtn(int tab) {
        switch (tab) {
            case 0:
                btnCollect.setBackground(getResources().getDrawable(R.drawable.bk_blue_rect_20));
                txtCollect.setTextColor(getResources().getColor(R.color.white_color));
                btnDeliver.setBackground(getResources().getDrawable(R.drawable.bk_white_rect_20));
                txtDeliver.setTextColor(getResources().getColor(R.color.grey_dark));
                mViewModel.setIsDeliver(true);
                is_deliver = false;
                initParam();
                mViewModel.setIsBusy(true);
                mViewModel.loadNewsFeed();
                break;
            case 1:
                btnDeliver.setBackground(getResources().getDrawable(R.drawable.bk_blue_rect_20));
                txtDeliver.setTextColor(getResources().getColor(R.color.white_color));
                btnCollect.setBackground(getResources().getDrawable(R.drawable.bk_white_rect_20));
                txtCollect.setTextColor(getResources().getColor(R.color.grey_dark));
                mViewModel.setIsDeliver(false);
                is_deliver = true;
                initParam();
                mViewModel.setIsBusy(true);
                mViewModel.loadNewsFeed();
                break;
        }
    }

}