package com.mobile.seemesave.view.bookmark;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.AllCanMissAdapter;
import com.mobile.seemesave.adapter.FeedAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.detail.PromotionDetailActivity;
import com.mobile.seemesave.view.detail.VideoPlayerActivity;
import com.mobile.seemesave.view.seeall.AllAdvertiseActivity;
import com.mobile.seemesave.view_model.seeall.BookAllPostViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookAllPostsActivity extends BaseActivity {
    private BookAllPostViewModel mViewModel;
    private BookAllPostsActivity activity;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.li_empty)
    LinearLayout li_empty;
    @BindView(R.id.txtTitle)
    TextView txtTitle;

    private FeedAdapter recyclerAdapter;
    ArrayList<FeedModel> dealList = new ArrayList<>();
    private String category_id = "";
    private String category_name = "";
    private int offset = 0;
    private int limit = 20;
    private boolean isLoading = false;
    private boolean isLast = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BookAllPostViewModel.class);
        setContentView(R.layout.activity_book_all_posts);
        ButterKnife.bind(this);
        activity = this;
        category_id = getIntent().getStringExtra("category_id");
        category_name = getIntent().getStringExtra("category_name");
        mViewModel.setCategory_id(category_id);
        initView();
    }

    private void initView() {
        initParam();
        if (!category_name.equalsIgnoreCase("")) {
            txtTitle.setText(category_name);
        }
        nestedScrollView.setNestedScrollingEnabled(false);
        setRecycler();
        try {
            String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "BookmarkPost", String.valueOf(category_id));
            if (TextUtils.isEmpty(local_data)) {
                mViewModel.setIsBusy(true);
            } else {
                mViewModel.loadLocalData();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mViewModel.loadData();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                initParam();
                mViewModel.setIsBusy(true);
                mViewModel.loadData();
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
                    mViewModel.loadData();
                }

            }
        });
    }
    private void initParam() {
        offset = 0;
        dealList.clear();
        mViewModel.setOffset(offset);
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
        mViewModel.getDealList().observe(this, list -> {
            if (offset == 0 && list.size() == 0) {
                li_empty.setVisibility(View.VISIBLE);
                return;
            } else {
                li_empty.setVisibility(View.GONE);
                if (offset == 0) {
                    dealList.clear();
                }
                if (list.size() < limit) {
                    isLast = true;
                }
                dealList.addAll(list);
                recyclerAdapter.setData(dealList);
                isLoading = false;
            }
        });

    }

    private void setRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new FeedAdapter(activity, dealList, new FeedAdapter.FeedRecyclerListener() {
            @Override
            public void onItemClicked(int pos, FeedModel model) {
                startFeedDetail(model);
            }

            @Override
            public void onOption(int pos, FeedModel model) {
                showOptionDlg(pos);
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
                    detailFragment.show(getSupportFragmentManager(), "AddBookMarkBottomSheet");
                }
            }
        });
        recyclerView.setAdapter(recyclerAdapter);
    }
    void apiCallForDeleteBookmark(int post_id, int position) {
        if (G.isNetworkAvailable(activity)) {
            Ion.with(activity)
                    .load("DELETE", G.RemoveFeedBookmarkUrl)
                    .addHeader("Authorization", "Bearer " + G.pref.getString("token", ""))
                    .setBodyParameter("feed_id", String.valueOf(post_id))
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            FeedModel item;
                            item = dealList.get(position);
                            item.setMarked(false);
                            dealList.set(position, item);
                            if (recyclerAdapter != null) {
                                recyclerAdapter.notifyDataSetChanged();
                            }

                        }
                    });
        } else {
            Toast.makeText(activity, R.string.msg_offline, Toast.LENGTH_LONG).show();
        }
    }
    void apiCallForSetBookmark(int post_id, String category_id, int position) {
        if (G.isNetworkAvailable(activity) && !category_id.equalsIgnoreCase("-1")) {
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
                                    item = dealList.get(position);
                                    item.setMarked(true);
                                    dealList.set(position, item);
                                    if (recyclerAdapter != null) {
                                        recyclerAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    Toast.makeText(activity, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException jsonException) {
                                Toast.makeText(activity, R.string.connection_fail, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(activity, R.string.connection_fail, Toast.LENGTH_LONG).show();
        }
    }
    private void startFeedDetail(FeedModel model) {
        int id = model.getId();
        Intent intent;
        if (model.getMedia_type().equalsIgnoreCase("Image")) {
            intent = new Intent(activity, PromotionDetailActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("media", model.getMediaList().get(0));
            intent.putExtra("feed_type", model.getFeed_type());
            intent.putExtra("brand", model.getTitle());
        } else {
            intent = new Intent(activity, VideoPlayerActivity.class);
            intent.putExtra("url", model.getMediaList().get(0));
            intent.putExtra("post_id", id);
        }
        startActivity(intent);
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private void showOptionDlg(int pos) {
        View dialogView = getLayoutInflater().inflate(R.layout.dlg_option, null);
        final android.app.AlertDialog dlg = new android.app.AlertDialog.Builder(activity)
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
        ImageView imgFollow = dialogView.findViewById(R.id.imgFollow);
        TextView txtFollow = dialogView.findViewById(R.id.txtFollow);

        FeedModel model = dealList.get(pos);
        if (model.isLike()) {
            imgFav.setImageDrawable(getDrawable(R.drawable.ic_heart));
            txtFav.setText(getString(R.string.txt_remove_favourite));
        } else {
            imgFav.setImageDrawable(getDrawable(R.drawable.ic_unheart));
            txtFav.setText(getString(R.string.txt_add_favourite));
        }
        if (model.isFollow()) {
            imgFollow.setImageDrawable(getDrawable(R.drawable.ic_delete_user));
            txtFollow.setText(getString(R.string.txt_unfollow));
        } else {
            imgFollow.setImageDrawable(getDrawable(R.drawable.ic_add_user));
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
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }else{
                    Toast.makeText(activity, "There is no email application installed.", Toast.LENGTH_SHORT).show();
                }
                dlg.dismiss();
            }
        });
        lytView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFeedDetail(finalModel);
                dlg.dismiss();
            }
        });
        lytFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (G.isNetworkAvailable(activity)) {
                    apiCallForPPL("CPL", String.valueOf(finalModel.getId()));
                    setLikeUI(pos);
                    String token = G.pref.getString("token" , "");
                    Ion.with(activity)
                            .load(G.SetFeedLikeUrl)
                            .addHeader("Authorization", "Bearer " + token)
                            .setBodyParameter("newsfeed_id", String.valueOf(finalModel.getId()))
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {

                                }
                            });
                } else {
                    Toast.makeText(activity, R.string.msg_offline, Toast.LENGTH_LONG).show();
                }
                dlg.dismiss();
            }
        });
        lytFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (G.isNetworkAvailable(activity)) {
                    setFollowUI(pos);
                    String token = G.pref.getString("token" , "");
                    Ion.with(activity)
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
                    Toast.makeText(activity, R.string.msg_offline, Toast.LENGTH_LONG).show();
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
    private void setLikeUI(int pos) {
        dealList.get(pos).setLikeCount(dealList.get(pos).isLike() ? dealList.get(pos).getLikeCount() - 1 : dealList.get(pos).getLikeCount() + 1);
        dealList.get(pos).setLike(!dealList.get(pos).isLike());
        recyclerAdapter.setData(dealList);
    }
    private void setFollowUI(int pos) {
        dealList.get(pos).setFollow(!dealList.get(pos).isFollow());
        recyclerAdapter.setData(dealList);
    }
    private void apiCallForPPL(String type, String ids){
        String token = G.pref.getString("token" , "");
        String url = String.format(java.util.Locale.US,G.FeedPPLUrl, type, ids);
        Ion.with(activity)
                .load(url)
                .addHeader("Authorization", "Bearer " + token)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
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
