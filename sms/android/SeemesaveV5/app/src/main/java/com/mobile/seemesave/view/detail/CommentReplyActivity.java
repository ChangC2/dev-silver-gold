package com.mobile.seemesave.view.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.CommentReplyAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.MCommon;
import com.mobile.seemesave.utils.G;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentReplyActivity extends BaseActivity {

    @BindView(R.id.recylerReplies)
    RecyclerView recylerReplies;

    @BindView(R.id.imgBack)
    ImageView imgBack;

    @BindView(R.id.imgPhoto)
    ImageView imgPhoto;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtDesc)
    TextView txtDesc;

    @BindView(R.id.btnSend)
    ImageView btnSend;

    @BindView(R.id.editComment)
    EditText editComment;

    @BindView(R.id.imgUser)
    ImageView imgUser;

    CommentReplyActivity activity;

    MCommon comment = new MCommon();

    private CommentReplyAdapter repliesAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, CommentReplyActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_reply);
        ButterKnife.bind(this);
        activity = this;
        Gson gson  = new Gson();
        comment = gson.fromJson(getIntent().getStringExtra("comment"),  MCommon.class);
        initUI();
    }

    private void initUI() {
        Glide.with(activity)
                .load(G.user.getImage_url())
                .fitCenter()
                .into(imgUser);

        Glide.with(activity)
                .load(comment.getImageUrl())
                .fitCenter()
                .into(imgPhoto);
        txtName.setText(comment.getName());
        txtDesc.setText(comment.getDescription());

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
                apiCallForSendComment();
                MCommon reply = new MCommon();
                reply.setName(G.user.getFirst_name() + " " + G.user.getLast_name());
                reply.setImageUrl(G.user.getImage_url());
                reply.setDescription(editComment.getText().toString());
                comment.getSubItems().add(reply);
                if (G.commentClickListener != null){
                    Gson gson = new Gson();
                    String json = gson.toJson(reply);
                    G.commentClickListener.onClick(json);
                }
                G.hideSoftKeyboard(activity);
            }
        });
        setRecycler();
    }


    private void setRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recylerReplies.setLayoutManager(linearLayoutManager);
        repliesAdapter = new CommentReplyAdapter(activity, comment.getSubItems(), null);
        recylerReplies.setAdapter(repliesAdapter);
    }

    //***************************************//
    //             API Call Method           //
    //***************************************//

    void apiCallForSendComment(){
        JsonObject json = new JsonObject();
        json.addProperty("review_id", comment.getId());
        json.addProperty("comment", editComment.getText().toString().trim());
        String token = G.pref.getString("token" , "");
        G.showLoading(activity);
        Ion.with(activity)
                .load("PUT", G.ReplyCommentsUrl)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        G.hideLoading();
                        setRecycler();
                        editComment.setText("");
                    }
                });
    }
}