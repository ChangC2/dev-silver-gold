package seemesave.businesshub.view.vendor.detail;

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
import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.StoreCommentAdapter;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.listener.RecyclerClickListener;
import seemesave.businesshub.listener.pagination.PaginationScrollListener;
import seemesave.businesshub.model.common.MCommon;
import seemesave.businesshub.utils.G;

public class StoreCommentActivity extends BaseActivity {

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

    StoreCommentActivity activity;

    ArrayList<MCommon> comments = new ArrayList<>();

    private StoreCommentAdapter commentsAdapter;
    private int offset = 0;
    private int limit = 20;
    private boolean isLoading = false;
    private boolean isLast = false;
    String store_id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_reply);
        ButterKnife.bind(this);
        activity = this;
        store_id = getIntent().getStringExtra("store_id");
        initUI();
    }

    private void initUI() {
        Glide.with(activity)
                .load(App.getUserImage())
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
                Intent intent = new Intent(activity, StoreCommentReplyActivity.class);
                Gson gson = new Gson();
                String json = gson.toJson(comments.get(position));
                intent.putExtra("comment", json);
                startActivity(intent);
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
        commentsAdapter = new StoreCommentAdapter(activity, comments, listener);
        recylerComments.setAdapter(commentsAdapter);
    }

    //***************************************//
    //             API Call Method           //
    //***************************************//

    void apiCallForGetComments() {

        String url = String.format(java.util.Locale.US, G.GetStoreReviews, store_id, offset, limit);
        showLoadingDialog();
        Ion.with(activity)
                .load(url)
                .addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        if (e != null){
                            Toast.makeText(activity, R.string.msg_something_wrong, Toast.LENGTH_LONG).show();
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
                                            item.setCreated(jsonArray.getJSONObject(i).getString("created"));
                                            JSONArray replyArry = jsonArray.getJSONObject(i).getJSONArray("ReplyList");
                                            for (int j=0;j<replyArry.length();j++){
                                                MCommon reply = new MCommon();
                                                reply.setId(replyArry.getJSONObject(j).getString("id"));
                                                String rname = String.format(java.util.Locale.US,"%s %s", replyArry.getJSONObject(j).getJSONObject("User").getString("first_name"),
                                                        replyArry.getJSONObject(j).getJSONObject("User").getString("last_name"));
                                                reply.setName(rname);
                                                reply.setImageUrl(replyArry.getJSONObject(j).getJSONObject("User").getString("image_url"));
                                                reply.setDescription(replyArry.getJSONObject(j).getString("comment"));
                                                reply.setCreated(replyArry.getJSONObject(j).getString("created"));
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
//        MCommon item = new MCommon();
//        if (comments.size() > 0) {
//            item.setId(String.valueOf(Integer.parseInt(comments.get(comments.size()-1).getId()) + 1));
//            String rname = String.format(java.util.Locale.US,"%s %s", G.user.getFirst_name(),
//                    G.user.getLast_name());
//            item.setName(rname);
//            item.setImageUrl(G.user.getImage_url());
//            item.setDescription(comment_str);
//        } else {
//            item.setId("1");
//            String rname = String.format(java.util.Locale.US,"%s %s", G.user.getFirst_name(),
//                    G.user.getLast_name());
//            item.setName(rname);
//            item.setImageUrl(G.user.getImage_url());
//            item.setDescription(comment_str);
//        }
//        comments.add(item);
//        commentsAdapter.setDatas(comments);
//
//
//        JsonObject json = new JsonObject();
//        json.addProperty("newsfeed_id", store_id);
//        json.addProperty("comment", editComment.getText().toString().trim());
//        showLoadingDialog();
//        Ion.with(activity)
//                .load("POST", G.SetStoreReviewUrl)
//                .addHeader("Authorization", App.getToken())
//                .addHeader("Content-Language", App.getPortalToken())
//                .addHeader("Content-Type", "application/json")
//                .setJsonObjectBody(json)
//                .asString()
//                .setCallback(new FutureCallback<String>() {
//                    @Override
//                    public void onCompleted(Exception e, String result) {
//                        hideLoadingDialog();
//                        editComment.setText("");
//
//                        if (G.clickListener != null)
//                            G.clickListener.onClick(true);
//                    }
//                });
    }
}