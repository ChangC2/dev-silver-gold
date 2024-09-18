package seemesave.businesshub.view.vendor.detail;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.StoreCommentReplyAdapter;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.model.common.MCommon;
import seemesave.businesshub.utils.G;

public class StoreCommentReplyActivity  extends BaseActivity {

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

    StoreCommentReplyActivity activity;

    MCommon comment = new MCommon();

    private StoreCommentReplyAdapter repliesAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_comments_reply);
        ButterKnife.bind(this);
        activity = this;
        Gson gson  = new Gson();
        comment = gson.fromJson(getIntent().getStringExtra("comment"),  MCommon.class);
        initUI();
    }

    private void initUI() {
        Glide.with(activity)
                .load(App.getUserImage())
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
                reply.setName(App.getUserFirstName() + " " + App.getUserLastName());
                reply.setImageUrl(App.getUserImage());
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
        repliesAdapter = new StoreCommentReplyAdapter(activity, comment.getSubItems(), null);
        recylerReplies.setAdapter(repliesAdapter);
    }

    //***************************************//
    //             API Call Method           //
    //***************************************//

    void apiCallForSendComment(){
        JsonObject json = new JsonObject();
        json.addProperty("reviewId", comment.getId());
        json.addProperty("comment", editComment.getText().toString().trim());
        G.showLoading(activity);
        Ion.with(activity)
                .load("PUT", G.SetStoreReviewUrl)
                .addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken())
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