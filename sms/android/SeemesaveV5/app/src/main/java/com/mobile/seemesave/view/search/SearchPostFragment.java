package com.mobile.seemesave.view.search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.FeedAdapter;
import com.mobile.seemesave.base.BaseFragment;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.detail.PromotionDetailActivity;
import com.mobile.seemesave.view.detail.VideoPlayerActivity;
import com.mobile.seemesave.view_model.main.FavPostFragViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchPostFragment extends BaseFragment {
    private FavPostFragViewModel mViewModel;
    private View mFragView;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private FeedAdapter recyclerAdapter;

    @BindView(R.id.li_empty)
    LinearLayout li_empty;

    private ArrayList<FeedModel> postList = new ArrayList<>();
    private int offset = 0;
    private int limit = 20;
    private boolean isLoading = false;
    private boolean isLast = false;
    private boolean firstLoading = false;
    public SearchPostFragment() {
    }

    public static SearchPostFragment newInstance() {
        SearchPostFragment fragment = new SearchPostFragment();
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
        mViewModel = new ViewModelProvider(this).get(FavPostFragViewModel.class);
        mFragView = inflater.inflate(R.layout.fragment_fav_post, container, false);
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
        mViewModel.getPostList().observe(this, list -> {
            if (offset == 0 && list.size() == 0) {
                li_empty.setVisibility(View.VISIBLE);
                return;
            } else {
                ((SearchActivity)getActivity()).setSearchKey();
                li_empty.setVisibility(View.GONE);
                if (offset == 0) {
                    postList.clear();
                }
                if (list.size() < limit) {
                    isLast = true;
                }
                postList.addAll(list);
                recyclerAdapter.setData(postList);
                isLoading = false;
            }
        });
    }
    public void loadData(String key) {
        if (TextUtils.isEmpty(key)) return;
        mViewModel.loadDataSearch(key);
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    private void initView() {
        postList.clear();
        setRecycler();
    }

    private void setRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new FeedAdapter(getActivity(), postList, new FeedAdapter.FeedRecyclerListener() {
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

            }
        });
        recyclerView.setAdapter(recyclerAdapter);
    }
    private void startFeedDetail(FeedModel model) {
        int id = model.getId();
        Intent intent;
        if (model.getMedia_type().equalsIgnoreCase("Image")) {
            intent = new Intent(getActivity(), PromotionDetailActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("media", model.getMediaList().get(0));
            intent.putExtra("feed_type", model.getFeed_type());
            intent.putExtra("brand", model.getTitle());
        } else {
            intent = new Intent(getActivity(), VideoPlayerActivity.class);
            intent.putExtra("url", model.getMediaList().get(0));
            intent.putExtra("post_id", id);
        }
        startActivity(intent);
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private void showOptionDlg(int pos) {
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
        ImageView imgFollow = dialogView.findViewById(R.id.imgFollow);
        TextView txtFollow = dialogView.findViewById(R.id.txtFollow);
        ImageView imgFav = dialogView.findViewById(R.id.imgFav);
        TextView txtFav = dialogView.findViewById(R.id.txtFav);
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
        FeedModel finalModel = model;
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
                if (G.isNetworkAvailable(getActivity())) {
                    apiCallForPPL("CPL", String.valueOf(finalModel.getId()));
                    setLikeUI(pos);
                    String token = G.pref.getString("token" , "");
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
        lytFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (G.isNetworkAvailable(getActivity())) {
                    setFollowUI(pos);
                    String token = G.pref.getString("token" , "");
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
        postList.get(pos).setLikeCount(postList.get(pos).isLike() ? postList.get(pos).getLikeCount() - 1 : postList.get(pos).getLikeCount() + 1);
        postList.get(pos).setLike(!postList.get(pos).isLike());
        recyclerAdapter.setData(postList);
    }
    private void setFollowUI(int pos) {
        postList.get(pos).setFollow(!postList.get(pos).isFollow());
        recyclerAdapter.setData(postList);
    }
    private void apiCallForPPL(String type, String ids){
        String token = G.pref.getString("token" , "");
        String url = String.format(java.util.Locale.US,G.FeedPPLUrl, type, ids);
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
}
