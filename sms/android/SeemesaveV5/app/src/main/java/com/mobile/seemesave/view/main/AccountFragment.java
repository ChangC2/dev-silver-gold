package com.mobile.seemesave.view.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.instagram.InsGallery;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.BookmarkAdapter;
import com.mobile.seemesave.adapter.BrandTopAdapter;
import com.mobile.seemesave.adapter.FeedAdapter;
import com.mobile.seemesave.adapter.PostAdapter;
import com.mobile.seemesave.adapter.ProductTopAdapter;
import com.mobile.seemesave.adapter.StoreTopAdapter;
import com.mobile.seemesave.base.BaseFragment;
import com.mobile.seemesave.listener.RecyclerClickListener;
import com.mobile.seemesave.model.common.BookmarkModel;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.model.common.PostModel;
import com.mobile.seemesave.model.common.ProductDetailModel;
import com.mobile.seemesave.model.common.StoreModel;
import com.mobile.seemesave.model.common.TrendingBrandModel;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.bookmark.BookAllPostsActivity;
import com.mobile.seemesave.view.bookmark.BookMarkAddBottomSheet;
import com.mobile.seemesave.view.detail.PromotionDetailActivity;
import com.mobile.seemesave.view.detail.VideoPlayerActivity;
import com.mobile.seemesave.view_model.main.AccountFragViewModel;
import com.mobile.seemesave.widget.imagePicker.GlideCacheEngine;
import com.mobile.seemesave.widget.imagePicker.GlideEngine;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountFragment extends BaseFragment {
    private AccountFragViewModel mViewModel;
    private View mFragView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.imgUser)
    ImageView imgUser;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtEmail)
    TextView txtEmail;
    @BindView(R.id.txtPhone)
    TextView txtPhone;
    @BindView(R.id.txtLike)
    TextView txtLike;
    @BindView(R.id.txtFriend)
    TextView txtFriend;
    @BindView(R.id.txtFollowing)
    TextView txtFollowing;
    @BindView(R.id.txtDashboard)
    TextView txtDashboard;
    @BindView(R.id.txtBookmark)
    TextView txtBookmark;
    @BindView(R.id.txtPost)
    TextView txtPost;

    @BindView(R.id.lytDashboard)
    LinearLayout lytDashboard;
    @BindView(R.id.lytPost)
    LinearLayout lytPost;
    @BindView(R.id.lytFeed)
    LinearLayout lytFeed;

    //dashboard
    @BindView(R.id.li_store)
    LinearLayout li_store;
    @BindView(R.id.li_brand)
    LinearLayout li_brand;
    @BindView(R.id.li_product)
    LinearLayout li_product;
    @BindView(R.id.lytDashboardEmpty)
    LinearLayout lytDashboardEmpty;

    @BindView(R.id.recyclerStoreView)
    RecyclerView recyclerStoreView;
    @BindView(R.id.storeIndicator)
    IndefinitePagerIndicator storeIndicator;
    StoreTopAdapter storeTopAdapter;

    @BindView(R.id.recyclerBrandView)
    RecyclerView recyclerBrandView;
    @BindView(R.id.brandIndicator)
    IndefinitePagerIndicator brandIndicator;
    BrandTopAdapter brandTopAdapter;

    @BindView(R.id.recyclerProductView)
    RecyclerView recyclerProductView;
    @BindView(R.id.productIndicator)
    IndefinitePagerIndicator productIndicator;

    ProductTopAdapter productTopAdapter;

    private ArrayList<StoreModel> storeList = new ArrayList<>();
    private ArrayList<TrendingBrandModel> brandList = new ArrayList<>();
    private ArrayList<ProductDetailModel> productList = new ArrayList<>();

    //post
    @BindView(R.id.recyclerPostView)
    RecyclerView recyclerPostView;
    @BindView(R.id.lytPostEmpty)
    LinearLayout lytPostEmpty;
    private FeedAdapter postAdapter;
    private ArrayList<FeedModel> postList = new ArrayList<>();
    private int post_offset = 0;
    private int post_limit = 10;
    private boolean post_isLoading = false;
    private boolean post_isLast = false;

    //feed
    @BindView(R.id.recyclerFeedView)
    RecyclerView recyclerFeedView;
    @BindView(R.id.lytFeedEmpty)
    LinearLayout lytFeedEmpty;
    private ArrayList<BookmarkModel> bookmarkList = new ArrayList<>();
    private int feed_offset = 0;
    private int feed_limit = 10;
    private boolean feed_isLoading = false;
    private boolean feed_isLast = false;
    private BookmarkAdapter bookmarkCategoryAdapter;
    private String local_book_data = null;

    private boolean click_post_tab = false, click_feed_tab = false;
    private int curPage = 0;

    public AccountFragment() {
    }

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
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
        mViewModel = new ViewModelProvider(this).get(AccountFragViewModel.class);
        mFragView = inflater.inflate(R.layout.fragment_account, container, false);
        try {
            local_book_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "BookmarkCategory", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        mViewModel.getTopStoreList().observe(this, list -> {
            storeList.clear();
            if (list.size() == 0) {
                li_store.setVisibility(View.GONE);
                return;
            } else {
                li_store.setVisibility(View.VISIBLE);
                storeList.addAll(list);
                storeTopAdapter.setData(storeList);
            }
        });
        mViewModel.getTopBrandList().observe(this, list -> {
            brandList.clear();
            if (list.size() == 0) {
                li_brand.setVisibility(View.GONE);
                return;
            } else {
                li_brand.setVisibility(View.VISIBLE);
                brandList.addAll(list);
                brandTopAdapter.setData(brandList);
            }
        });
        mViewModel.getTopProductList().observe(this, list -> {
            productList.clear();
            if (list.size() == 0) {
                li_product.setVisibility(View.GONE);
                return;
            } else {
                li_product.setVisibility(View.VISIBLE);
                productList.addAll(list);
                productTopAdapter.setData(productList);
            }
        });
        mViewModel.getEmptyDashboard().observe(this, flag -> {
            if (flag) {
                lytDashboardEmpty.setVisibility(View.VISIBLE);
            } else {
                lytDashboardEmpty.setVisibility(View.GONE);
            }
        });
        mViewModel.getPostList().observe(this, list -> {
            if (post_offset == 0 && list.size() == 0) {
                lytPostEmpty.setVisibility(View.VISIBLE);
                return;
            } else {
                lytPostEmpty.setVisibility(View.GONE);
                if (post_offset == 0) {
                    postList.clear();
                }
                if (list.size() < post_limit) {
                    post_isLast = true;
                }
                postList.addAll(list);
                postAdapter.setData(postList);
                post_isLoading = false;
            }
        });


        if (curPage == 1) {
            try {
                String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "AccountPost", "");
                if (TextUtils.isEmpty(local_data)) {
                    if (G.isNetworkAvailable(getActivity())) {
                        mViewModel.setIsBusy(true);
                    }
                } else {
                    mViewModel.loadLocalPostData();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (G.isNetworkAvailable(getActivity())) {
                mViewModel.loadPostData();
            }
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                if (G.isNetworkAvailable(getActivity())) {
                    if (curPage == 0) {
                        mViewModel.setIsBusy(true);
                        mViewModel.loadDashboardData();
                    } else if (curPage == 1) {
                        post_offset = 0;
                        post_isLoading = false;
                        post_isLast = false;
                        mViewModel.setPost_offset(0);
                        mViewModel.setIsBusy(true);
                        mViewModel.loadPostData();
                    } else {
                        loadBookmarkData(true);
                    }
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initView() {
        Glide.with(getActivity())
                .load(G.user.getImage_url())
                .placeholder(R.drawable.ic_avatar)
                .fitCenter()
                .into(imgUser);
        txtName.setText(G.user.getFirst_name() + " " + G.user.getLast_name());
        txtEmail.setText(G.user.getEmail());
        txtPhone.setText("+" + G.user.getCountryCode() + " " + G.user.getPhoneNumber());
        txtLike.setText(String.valueOf(G.user.getLikedProductCount()));
        txtFollowing.setText(String.valueOf(G.user.getFollowedStoreCount()));
        txtFriend.setText(String.valueOf(G.user.getFriendCount()));
        setPage(0);

        brandList.clear();
        storeList.clear();
        productList.clear();
        postList.clear();
        bookmarkList.clear();
        setStoreRecycler();
        setBrandRecycler();
        setProductRecycler();
        setPostRecycler();
        setFeedRecycler();

        nestedScrollView.setNestedScrollingEnabled(false);
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (!G.isNetworkAvailable(getActivity())) {
                    return;
                }
                View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
                int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));
                if (curPage == 1) {
                    if (diff == 0 && !post_isLoading && !post_isLast) {
                        post_isLoading = true;
                        post_offset = post_offset + post_limit;
                        mViewModel.setPost_offset(post_offset);
                        mViewModel.setIsBusy(true);
                        mViewModel.loadPostData();
                    }
                } else if (curPage == 1) {
                    if (diff == 0 && !feed_isLoading && !feed_isLast) {
                        feed_isLoading = true;
                        feed_offset = feed_offset + feed_limit;
                        mViewModel.setFeed_offset(feed_offset);
                        if (local_book_data == null) {
                            loadBookmarkData(true);
                        } else {
                            loadBookmarkData(false);
                        }

                    }
                }

            }
        });
        try {
            String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "AccountDashboard", "");
            if (TextUtils.isEmpty(local_data)) {
                if (G.isNetworkAvailable(getActivity())) {
                    mViewModel.setIsBusy(true);
                }
            } else {
                mViewModel.loadLocalDashboardData();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (G.isNetworkAvailable(getActivity())) {
            mViewModel.loadDashboardData();
        }
        loadDataFromLocal();
    }

    private void setPage(int pos) {
        curPage = pos;
        switch (pos) {
            case 0:
                txtDashboard.setTextColor(ContextCompat.getColor(getContext(), R.color.main_blue_color));
                txtBookmark.setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_500));
                txtPost.setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_500));
                lytDashboard.setVisibility(View.VISIBLE);
                lytPost.setVisibility(View.GONE);
                lytFeed.setVisibility(View.GONE);
                nestedScrollView.smoothScrollTo(0, 0);
                nestedScrollView.fullScroll(View.FOCUS_UP);
                break;
            case 1:
                txtPost.setTextColor(ContextCompat.getColor(getContext(), R.color.main_blue_color));
                txtBookmark.setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_500));
                txtDashboard.setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_500));
                lytDashboard.setVisibility(View.GONE);
                lytPost.setVisibility(View.VISIBLE);
                lytFeed.setVisibility(View.GONE);
                if (!click_post_tab) {
                    try {
                        String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "AccountPost", "");
                        if (TextUtils.isEmpty(local_data)) {
                            if (G.isNetworkAvailable(getActivity())) {
                                mViewModel.setIsBusy(true);
                            }
                        } else {
                            mViewModel.loadLocalPostData();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (G.isNetworkAvailable(getActivity())) {
                        mViewModel.loadPostData();
                    }
                    click_post_tab = true;
                }
                nestedScrollView.smoothScrollTo(0, 0);
                nestedScrollView.fullScroll(View.FOCUS_UP);
                break;
            case 2:
                txtBookmark.setTextColor(ContextCompat.getColor(getContext(), R.color.main_blue_color));
                txtDashboard.setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_500));
                txtPost.setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_500));
                lytDashboard.setVisibility(View.GONE);
                lytPost.setVisibility(View.GONE);
                lytFeed.setVisibility(View.VISIBLE);
                if (local_book_data == null) {
                    loadBookmarkData(true);
                } else {
                    loadBookmarkData(false);
                }
                nestedScrollView.smoothScrollTo(0, 0);
                nestedScrollView.fullScroll(View.FOCUS_UP);
                break;
        }
    }

    private void loadBookmarkData(boolean show_load) {
        if (G.isNetworkAvailable(getContext())) {
            String token = G.pref.getString("token", "");
            String url = G.GetBookmarkCategory;
            if (show_load)
                showLoadingDialog();
            Ion.with(getActivity())
                    .load(url)
                    .addHeader("Authorization", "Bearer " + token)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            if (show_load)
                                hideLoadingDialog();
                            if (e == null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.getBoolean("status")) {
                                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("data");
                                        DatabaseQueryClass.getInstance().insertData(
                                                G.getUserID(),
                                                "BookmarkCategory",
                                                jsonArray.toString(),
                                                "",
                                                ""
                                        );
                                        parseData(jsonArray);
                                    } else {
                                        bookmarkList = new ArrayList<>();
                                        bookmarkCategoryAdapter.setDatas(bookmarkList);
                                    }
                                } catch (JSONException jsonException) {
                                    bookmarkList = new ArrayList<>();
                                    bookmarkCategoryAdapter.setDatas(bookmarkList);
                                }
                            }
                        }
                    });
        }
    }

    private void loadDataFromLocal() {
        try {
            if (local_book_data != null) {
                JSONArray local_book_array = new JSONArray(local_book_data);
                parseData(local_book_array);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseData(JSONArray jsonArray) {
        try {
            if (jsonArray.length() == 0) {
                ArrayList<BookmarkModel> newPosts = new ArrayList<>();
                BookmarkModel item = new BookmarkModel();
                item.setId("");
                item.setName("Add new");
                item.setItem_type("add");
                item.setCover_image("");
                newPosts.add(item);
                bookmarkCategoryAdapter.setDatas(newPosts);
                bookmarkList.clear();
                feed_isLoading = false;
                bookmarkList.addAll(newPosts);
            } else {
                ArrayList<BookmarkModel> newPosts = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    BookmarkModel item = new BookmarkModel();
                    JSONArray thumbnailList = null;
                    thumbnailList = jsonArray.getJSONObject(i).getJSONArray("thumbnailList");

                    item.setId(jsonArray.getJSONObject(i).getString("id"));
                    item.setName(jsonArray.getJSONObject(i).getString("name"));
                    item.setItem_type("main");
                    String cover_image = "";

                    if (thumbnailList.length() > 0) {
                        JSONArray subImageArry = thumbnailList.getJSONObject(0).getJSONArray("SubMedia");
                        if (subImageArry.length() > 0) {
                            if (subImageArry.getJSONObject(0).getString("Media_Type").equalsIgnoreCase("Image")) {
                                cover_image = subImageArry.getJSONObject(0).getString("Media");
                            } else {
                                cover_image = subImageArry.getJSONObject(0).getString("VideoThumb");
                            }
                        }
                    }
                    item.setCover_image(cover_image);
                    newPosts.add(item);
                }
                BookmarkModel all_item = new BookmarkModel();
                all_item.setId("");
                all_item.setName("All Posts");
                all_item.setItem_type("all");
                if (jsonArray.length() > 3) {
                    all_item.setCover_image(newPosts.get(0).getCover_image());
                    all_item.setCover_image2(newPosts.get(1).getCover_image());
                    all_item.setCover_image3(newPosts.get(2).getCover_image());
                    all_item.setCover_image4(newPosts.get(3).getCover_image());
                } else if (jsonArray.length() == 3) {
                    all_item.setCover_image(newPosts.get(0).getCover_image());
                    all_item.setCover_image2(newPosts.get(1).getCover_image());
                    all_item.setCover_image3(newPosts.get(2).getCover_image());
                } else if (jsonArray.length() == 2) {
                    all_item.setCover_image(newPosts.get(0).getCover_image());
                    all_item.setCover_image2(newPosts.get(1).getCover_image());
                } else if (jsonArray.length() == 1) {
                    all_item.setCover_image(newPosts.get(0).getCover_image());
                }

                bookmarkList.clear();
                bookmarkList.add(all_item);
                bookmarkList.addAll(newPosts);

                BookmarkModel new_item = new BookmarkModel();
                new_item.setId("");
                new_item.setName("Add new");
                new_item.setItem_type("add");
                new_item.setCover_image("");

                bookmarkList.add(new_item);

                bookmarkCategoryAdapter.setDatas(bookmarkList);
                feed_isLoading = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setStoreRecycler() {
        storeTopAdapter = new StoreTopAdapter(getActivity(), storeList, new StoreTopAdapter.StoreTopRecyclerListener() {
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
        recyclerStoreView.setAdapter(storeTopAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerStoreView.setLayoutManager(linearLayoutManager);
        storeIndicator.attachToRecyclerView(recyclerStoreView);
    }

    private void setBrandRecycler() {
        brandTopAdapter = new BrandTopAdapter(getActivity(), brandList, new BrandTopAdapter.BrandTopRecyclerListener() {

            @Override
            public void onItemClicked(int pos, TrendingBrandModel model) {

            }

            @Override
            public void onStar(int pos, TrendingBrandModel model) {

            }
        });
        recyclerBrandView.setAdapter(brandTopAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerBrandView.setLayoutManager(linearLayoutManager);
        brandIndicator.attachToRecyclerView(recyclerBrandView);
    }

    private void setProductRecycler() {
        productTopAdapter = new ProductTopAdapter(getActivity(), productList, new ProductTopAdapter.ProductTopRecyclerListener() {


            @Override
            public void onItemClicked(int pos, ProductDetailModel model) {

            }

            @Override
            public void onViewPromotion(int pos, ProductDetailModel model) {
            }

            @Override
            public void onStar(int pos, ProductDetailModel model) {

            }
        });
        recyclerProductView.setAdapter(productTopAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerProductView.setLayoutManager(linearLayoutManager);
        productIndicator.attachToRecyclerView(recyclerProductView);
    }

    private void setPostRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerPostView.setLayoutManager(linearLayoutManager);
        postAdapter = new FeedAdapter(getActivity(), postList, new FeedAdapter.FeedRecyclerListener() {


            @Override
            public void onItemClicked(int pos, FeedModel model) {
                int id = model.getId();
                Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
                intent.putExtra("url", model.getMedia());
                intent.putExtra("post_id", id);
                Gson gson = new Gson();
                String buysJson = gson.toJson(postList);
                intent.putExtra("feed_data", buysJson);
                startActivity(intent);
            }

            @Override
            public void onOption(int pos, FeedModel model) {
                showOptionPostDlg(pos);
            }

            @Override
            public void onBookmark(int pos, FeedModel model) {
                if (model.isMarked()) {
                    apiCallForDeleteBookmark(model.getId(), pos);
                } else {
                    BookMarkAddBottomSheet detailFragment = BookMarkAddBottomSheet.newInstance((cID, pID) -> {
                        apiCallForSetBookmark(pID, cID, pos);
                    });
                    detailFragment.post_id = model.getId();
                    detailFragment.show(getActivity().getSupportFragmentManager(), "AddBookMarkBottomSheet");
                }
            }
        });
        recyclerPostView.setAdapter(postAdapter);
    }

    void apiCallForDeleteBookmark(int post_id, int position) {
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
                            item = postList.get(position);
                            item.setMarked(false);
                            postList.set(position, item);
                            if (postAdapter != null) {
                                postAdapter.notifyDataSetChanged();
                            }

                        }
                    });
        } else {
            Toast.makeText(getActivity(), R.string.msg_offline, Toast.LENGTH_LONG).show();
        }
    }

    void apiCallForSetBookmark(int post_id, String category_id, int position) {
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
                                    item = postList.get(position);
                                    item.setMarked(true);
                                    postList.set(position, item);
                                    if (postAdapter != null) {
                                        postAdapter.notifyDataSetChanged();
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

    private void setFeedRecycler() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false);
        recyclerFeedView.setLayoutManager(layoutManager);
        RecyclerClickListener listener = new RecyclerClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (v.getId() == R.id.li_add) {
                    Dialog dialog = new Dialog(getActivity(), R.style.DialogTheme);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    dialog.setContentView(R.layout.dlg_add_bookmark_category);
                    EditText eName = (EditText) dialog.findViewById(R.id.editName);

                    dialog.findViewById(R.id.btnCreate).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = eName.getText().toString();
                            if (TextUtils.isEmpty(name)) {
                                Toast.makeText(getActivity(), R.string.missing_param, Toast.LENGTH_LONG).show();
                                return;
                            }

                            apiCallCreateCategory(name);
                            dialog.dismiss();
                        }
                    });

                    dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                } else if (v.getId() == R.id.rm_close) {
                    apiCallDeleteCategory(position);
                } else if (v.getId() == R.id.li_all) {
                    Intent intent = new Intent(getActivity(), BookAllPostsActivity.class);
                    intent.putExtra("category_id", "");
                    intent.putExtra("category_name", "");
                    startActivity(intent);
                } else if (v.getId() == R.id.rm_image) {
                    Intent intent = new Intent(getActivity(), BookAllPostsActivity.class);
                    intent.putExtra("category_id", bookmarkList.get(position).getId());
                    intent.putExtra("category_name", bookmarkList.get(position).getName());
                    startActivity(intent);
                }

            }

            @Override
            public void onClick(View v, int position, int type) {
            }
        };
        bookmarkCategoryAdapter = new BookmarkAdapter(getActivity(), bookmarkList, listener, "bottom");
        recyclerFeedView.setAdapter(bookmarkCategoryAdapter);
    }

    void apiCallCreateCategory(String name) {
        if (G.isNetworkAvailable(getContext())) {
            JsonObject json = new JsonObject();
            json.addProperty("name", name);
            String token = G.pref.getString("token", "");
            G.showLoading(getActivity());
            Ion.with(this)
                    .load("POST", G.CreateBookmarkCategory)
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("Content-Type", "application/json")
                    .setJsonObjectBody(json)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            G.hideLoading();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("status")) {
                                    G.hideSoftKeyboard(getActivity());
                                    loadBookmarkData(false);
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

    void apiCallDeleteCategory(int position) {
        Ion.with(getActivity())
                .load("DELETE", G.DeleteBookmarkCategory)
                .addHeader("Authorization", "Bearer " + G.pref.getString("token", ""))
                .setBodyParameter("category_id", bookmarkList.get(position).getId())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        bookmarkList.remove(position);
                        if (bookmarkCategoryAdapter != null)
                            bookmarkCategoryAdapter.notifyDataSetChanged();
                    }
                });

    }

    private void apiCallForPPL(String type, String ids) {
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

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showOptionPostDlg(int pos) {
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dlg_option, null);
        final android.app.AlertDialog dlg = new android.app.AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dlg.setCanceledOnTouchOutside(true);
        ImageView imgClose = dialogView.findViewById(R.id.imgClose);
        LinearLayout lytView = dialogView.findViewById(R.id.lytView);
        LinearLayout lytFav = dialogView.findViewById(R.id.lytFav);
        LinearLayout lytFollow = dialogView.findViewById(R.id.lytFollow);
        LinearLayout lytBooking = dialogView.findViewById(R.id.lytBooking);
        ImageView imgFav = dialogView.findViewById(R.id.imgFav);
        TextView txtFav = dialogView.findViewById(R.id.txtFav);
        lytFollow.setVisibility(View.GONE);
        ImageView imgFollow = dialogView.findViewById(R.id.imgFollow);
        TextView txtFollow = dialogView.findViewById(R.id.txtFollow);
        LinearLayout lytRemove = dialogView.findViewById(R.id.lytRemove);
        lytRemove.setVisibility(View.VISIBLE);
        FeedModel model = postList.get(pos);
        if (model.isLike()) {
            imgFav.setImageDrawable(getContext().getDrawable(R.drawable.ic_heart));
            txtFav.setText(getString(R.string.txt_remove_favourite));
        } else {
            imgFav.setImageDrawable(getContext().getDrawable(R.drawable.ic_unheart));
            txtFav.setText(getString(R.string.txt_add_favourite));
        }
        if (model.isFollow()) {
            imgFollow.setImageDrawable(getContext().getDrawable(R.drawable.ic_delete_user));
            txtFollow.setText(getString(R.string.txt_unfollow));
        } else {
            imgFollow.setImageDrawable(getContext().getDrawable(R.drawable.ic_add_user));
            txtFollow.setText(getString(R.string.txt_follow));
        }
        FeedModel finalModel = model;
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
                }else{
                    Toast.makeText(getActivity(), "There is no email application installed.", Toast.LENGTH_SHORT).show();
                }
                dlg.dismiss();
            }
        });
        lytView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = model.getId();
                Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
                intent.putExtra("url", model.getMedia());
                intent.putExtra("post_id", id);
                startActivity(intent);
                dlg.dismiss();
            }
        });
        lytFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (G.isNetworkAvailable(getActivity())) {
                    apiCallForPPL("CPL", String.valueOf(finalModel.getId()));
                    setPostLikeUI(pos);
                    String token = G.pref.getString("token", "");
                    Ion.with(getActivity())
                            .load(G.SetFeedLikeUrl)
                            .addHeader("Authorization", "Bearer " + token)
                            .setBodyParameter("newsfeed_id", String.valueOf(finalModel.getId()))
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {

                                }
                            });
                }
                dlg.dismiss();
            }
        });

        lytRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (G.isNetworkAvailable(getActivity())) {
                    Ion.with(getActivity())
                            .load("DELETE", G.DeletePostUrl)
                            .addHeader("Authorization", "Bearer " + G.pref.getString("token", ""))
                            .setBodyParameter("post_id", String.valueOf(model.getId()))
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {
                                }
                            });
                    postList.remove(pos);
                    postAdapter.setData(postList);
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

    private void setPostLikeUI(int pos) {
        postList.get(pos).setLikeCount(postList.get(pos).isLike() ? postList.get(pos).getLikeCount() - 1 : postList.get(pos).getLikeCount() + 1);
        postList.get(pos).setLike(!postList.get(pos).isLike());
        postAdapter.setData(postList);
    }

    private void setPostFollowUI(int pos) {
        postList.get(pos).setFollow(!postList.get(pos).isFollow());
        postAdapter.setData(postList);
    }

    private void addPost() {
        boolean flag_read = check_Allpermission();

        if (flag_read) {
            InsGallery.setCurrentTheme(InsGallery.THEME_STYLE_DARK);
            InsGallery.openGalleryForPost(getActivity(), GlideEngine.createGlideEngine(), GlideCacheEngine.createCacheEngine(), new OnResultCallbackListener() {
                @Override
                public void onResult(List result) {
                    if (result.size() > 0) {
                        LocalMedia media = (LocalMedia) result.get(0);

                        String mediaPath = "";
                        String contentType = "image/jpeg";
                        String mType = "";

                        if (media.isCut() && !media.isCompressed()) {
                            mediaPath = media.getCutPath();
                        } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                            mediaPath = media.getCompressPath();
                        } else if (PictureMimeType.isHasVideo(media.getMimeType())) {
                            if (TextUtils.isEmpty(media.getCompressPath()))
                                mediaPath = media.getPath();
                            else
                                mediaPath = media.getCompressPath();
                        } else {
                            mediaPath = media.getPath();
                        }

                        if (PictureMimeType.isHasVideo(media.getMimeType())) {
                            mType = "video";
                            contentType = "video/mp4";
                        } else {
                            mType = "image";
                            contentType = "image/jpeg";
                        }
                        Intent intent = new Intent(getActivity(), AddMediaActivity.class);
                        intent.putExtra("mediaPath", mediaPath);
                        intent.putExtra("contentType", contentType);
                        intent.putExtra("mType", mType);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancel() {
                }
            });
        }
    }

    private boolean check_Allpermission() {

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            try {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO},
                        G.permission_Read_data);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

        return false;
    }

    @OnClick({R.id.imgEdit, R.id.imgMenu, R.id.txtDashboard, R.id.txtBookmark, R.id.txtPost, R.id.lytLike, R.id.lytFriend, R.id.lytFollowing, R.id.imgAddPost})
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.imgEdit:
                mViewModel.goEditProfile(getActivity());
                break;
            case R.id.imgMenu:
                mViewModel.goMenu(getActivity());
                break;
            case R.id.txtDashboard:
                setPage(0);
                break;
            case R.id.txtPost:
                setPage(1);
                break;
            case R.id.txtBookmark:
                setPage(2);
                break;
            case R.id.lytLike:
                mViewModel.goProductLike(getActivity());
                break;
            case R.id.lytFollowing:
                mViewModel.goStoreFollow(getActivity());
                break;
            case R.id.lytFriend:
                mViewModel.goFriend(getActivity());
                break;
            case R.id.imgAddPost:
                addPost();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == G.permission_Read_data && grantResults.length > 0) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Tap again", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == G.permission_write_data && grantResults.length > 0) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Tap again", Toast.LENGTH_LONG).show();
            }
        }
    }
}