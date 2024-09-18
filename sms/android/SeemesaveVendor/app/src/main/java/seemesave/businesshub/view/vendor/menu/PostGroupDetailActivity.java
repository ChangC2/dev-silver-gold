package seemesave.businesshub.view.vendor.menu;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.PostGroupDetailAdapter;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.model.common.PostGroupModel;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostGroupDetailActivity extends BaseActivity {

    private PostGroupDetailActivity activity;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.title)
    TextView tvTitle;
    @BindView(R.id.txtType)
    TextView txtType;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtDesc)
    TextView txtDesc;
    @BindView(R.id.btnShare)
    LinearLayout btnShare;
    @BindView(R.id.imgProduct)
    RoundedImageView imgProduct;
    @BindView(R.id.imgPlay)
    ImageView imgPlay;
    @BindView(R.id.imgDownload)
    ImageView imgDownload;
    @BindView(R.id.imgAttach)
    ImageView imgAttach;
    @BindView(R.id.imgSend)
    ImageView imgSend;
    @BindView(R.id.edtMessage)
    EditText edtMessage;

    private PostGroupDetailAdapter postGroupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_group_detail);
        ButterKnife.bind(this);
        activity = this;
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initView() {
        tvTitle.setText("Our New Promotion is Coming Soon");
        txtType.setText("Media's");
        txtName.setText(String.format(Locale.US, "Created by : %1$s", "Devalnd"));
        txtDesc.setText("Don't miss it guys, we are sure you will love it, Thank you");

        Glide.with(activity)
                .load("https://seemesave-web.fra1.digitaloceanspaces.com/media/newsfeed_media/Gaint-hyper-2_HCpmXPn.png")
                .centerCrop()
                .placeholder(R.drawable.ic_me)
                .into(imgProduct);

        setRecycler();
    }
    private void setRecycler() {
        ArrayList<PostGroupModel> list = new ArrayList<>();
        list.add(new PostGroupModel(1, "https://seemesave-web.fra1.digitaloceanspaces.com/media/newsfeed_media/Gaint-hyper-2_HCpmXPn.png", "Devland", "Our New Promotion is Coming Soon", "Don't miss it guys, we are sure you will love it."));
        list.add(new PostGroupModel(2, "https://seemesave-web.fra1.digitaloceanspaces.com/media/newsfeed_media/Yarona-Cash--Carry_3_5SZ4Lyg.png", "Devland", "Our New Promotion is Coming Soon", "Don't miss it guys, we are sure you will love it."));
        list.add(new PostGroupModel(3, "https://seemesave-web.fra1.digitaloceanspaces.com/media/newsfeed_media/Epping_BlYqO0c.png", "Devland", "Our New Promotion is Coming Soon", "Don't miss it guys, we are sure you will love it."));
        postGroupAdapter = new PostGroupDetailAdapter(activity, list, new PostGroupDetailAdapter.PostGroupRecyclerListener() {

            @Override
            public void onItemClicked(int pos, PostGroupModel model) {

            }
        });
        recyclerView.setAdapter(postGroupAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
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