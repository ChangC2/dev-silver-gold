package com.mobile.seemesave.view.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.FeedCommentAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.listener.RecyclerClickListener;
import com.mobile.seemesave.listener.pagination.PaginationScrollListener;
import com.mobile.seemesave.model.common.MCommon;
import com.mobile.seemesave.utils.G;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedCommentActivity extends BaseActivity {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.recylerComments)
    RecyclerView recylerComments;

    @BindView(R.id.btBack)
    ImageView imgBack;

    @BindView(R.id.btnSend)
    ImageView btnSend;

    @BindView(R.id.editComment)
    EditText editComment;

    @BindView(R.id.imgUser)
    ImageView imgUser;

    FeedCommentActivity activity;

    ArrayList<MCommon> comments = new ArrayList<>();

    private FeedCommentAdapter commentsAdapter;
    private int offset = 0;
    private int limit = 20;
    private boolean isLoading = false;
    private boolean isLast = false;
    String promoId = "";
    public static void start(Context context) {
        Intent intent = new Intent(context, FeedCommentActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_comments);
        ButterKnife.bind(this);
        activity = this;
        promoId = getIntent().getStringExtra("promo_id");
        initUI();
    }

    private void initUI() {
        Glide.with(activity)
                .load(G.user.getImage_url())
                .fitCenter()
                .placeholder(R.drawable.ic_avatar)
                .into(imgUser);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                if (G.isNetworkAvailable(activity)) {
                    refreshPage();
                } else {
                    Toast.makeText(activity, R.string.msg_offline, Toast.LENGTH_LONG).show();
                }
            }
        });


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editComment.getText().toString())) {
                    Toast.makeText(activity, R.string.missing_param, Toast.LENGTH_LONG).show();
                    return;
                }
                apiCallForSendComment(editComment.getText().toString());
                G.hideSoftKeyboard(activity);
            }
        });
        setRecycler();
        refreshPage();
    }

    private void refreshPage(){
        initPageNationParams();
        apiCallForGetComments();
    }

    private void initPageNationParams(){
        offset = 0;
        limit = 20;
        isLoading = false;
        isLast = false;
        comments.clear();
        commentsAdapter.notifyDataSetChanged();
    }

    private void setRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recylerComments.setLayoutManager(linearLayoutManager);
        RecyclerClickListener listener = new RecyclerClickListener() {
            @Override
            public void onClick(View v, int position) {
            }

            @Override
            public void onClick(View v, int position, int type) {
            }
        };

        recylerComments.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                offset = offset + limit;
                apiCallForGetComments();
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
        commentsAdapter = new FeedCommentAdapter(activity, comments, listener);
        recylerComments.setAdapter(commentsAdapter);
    }

    //***************************************//
    //             API Call Method           //
    //***************************************//

    void apiCallForGetComments() {
        String token = G.pref.getString("token" , "");

        String url = String.format(java.util.Locale.US,G.GetFeedCommentsUrl, promoId, offset, limit);
        showLoadingDialog();
        Ion.with(activity)
                .load(url)
                .addHeader("Authorization", "Bearer " + token)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        if (e != null){
                            Toast.makeText(activity, R.string.connection_fail, Toast.LENGTH_LONG).show();
                        }else{
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("status")){
                                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("data");
                                    if (offset == 0 && jsonArray.length() == 0){
                                        commentsAdapter.setDatas(new ArrayList<>());
                                    }else {
                                        ArrayList<MCommon> newComments = new ArrayList<>();
                                        for (int i=0;i<jsonArray.length();i++){
                                            MCommon item = new MCommon();
                                            item.setId(jsonArray.getJSONObject(i).getString("id"));
                                            String name = String.format(java.util.Locale.US,"%s %s", jsonArray.getJSONObject(i).getJSONObject("User").getString("first_name"),
                                                    jsonArray.getJSONObject(i).getJSONObject("User").getString("last_name"));
                                            item.setName(name);
                                            item.setImageUrl(jsonArray.getJSONObject(i).getJSONObject("User").getString("image_url"));
                                            item.setDescription(jsonArray.getJSONObject(i).getString("comment"));
                                            item.setCreated(jsonArray.getJSONObject(i).getString("Created"));
                                            JSONArray replyArry = jsonArray.getJSONObject(i).getJSONArray("ReplyList");
                                            for (int j=0;j<replyArry.length();j++){
                                                MCommon reply = new MCommon();
                                                reply.setId(replyArry.getJSONObject(j).getString("id"));
                                                String rname = String.format(java.util.Locale.US,"%s %s", replyArry.getJSONObject(j).getJSONObject("User").getString("first_name"),
                                                        replyArry.getJSONObject(j).getJSONObject("User").getString("last_name"));
                                                reply.setName(rname);
                                                reply.setImageUrl(replyArry.getJSONObject(j).getJSONObject("User").getString("image_url"));
                                                reply.setDescription(replyArry.getJSONObject(j).getString("comment"));
                                                reply.setCreated(replyArry.getJSONObject(j).getString("Created"));
                                                item.getSubItems().add(reply);

                                            }
                                            newComments.add(item);
                                        }
                                        if (newComments.size() < 20){
                                            isLast = true;
                                        }
                                        if (offset == 0){
                                            comments.clear();
                                        }
                                        comments.addAll(newComments);
                                        commentsAdapter.setDatas(comments);
                                        isLoading = false;
                                    }
                                }else{
                                    if (offset == 0){
                                        comments = new ArrayList<>();
                                        commentsAdapter.setDatas(comments);
                                    }else{
                                        isLast = true;
                                        isLoading = false;
                                    }
                                }
                            } catch (JSONException jsonException) {
                                if (offset == 0){
                                    comments = new ArrayList<>();
                                    commentsAdapter.setDatas(comments);
                                }else{
                                    isLast = true;
                                    isLoading = false;
                                }
                            }
                        }
                    }
                });
    }

    void apiCallForSendComment(String comment_str){
        MCommon item = new MCommon();
        if (comments.size() > 0) {
            item.setId(String.valueOf(Integer.parseInt(comments.get(comments.size()-1).getId()) + 1));
            String rname = String.format(java.util.Locale.US,"%s %s", G.user.getFirst_name(),
                    G.user.getLast_name());
            item.setName(rname);
            item.setImageUrl(G.user.getImage_url());
            item.setDescription(comment_str);
        } else {
            item.setId("1");
            String rname = String.format(java.util.Locale.US,"%s %s", G.user.getFirst_name(),
                    G.user.getLast_name());
            item.setName(rname);
            item.setImageUrl(G.user.getImage_url());
            item.setDescription(comment_str);
        }
        comments.add(item);
        commentsAdapter.setDatas(comments);


        JsonObject json = new JsonObject();
        json.addProperty("newsfeed_id", promoId);
        json.addProperty("comment", editComment.getText().toString().trim());
        String token = G.pref.getString("token" , "");
        showLoadingDialog();
        Ion.with(activity)
                .load("POST", G.SetFeedCommentsUrl)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        editComment.setText("");
                        //refreshPage();


                        if (G.clickListener != null)
                            G.clickListener.onClick(true);
                    }
                });
    }
}