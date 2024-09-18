package seemesave.businesshub.view.vendor.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import seemesave.businesshub.R;
import seemesave.businesshub.adapter.PostGroupAdapter;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.model.common.PostGroupModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostSupplierActivity extends BaseActivity {

    private PostSupplierActivity activity;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private PostGroupAdapter postGroupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_supplier);
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
        setRecycler();
    }
    private void setRecycler() {
        ArrayList<PostGroupModel> list = new ArrayList<>();
        list.add(new PostGroupModel(1, "https://seemesave-web.fra1.digitaloceanspaces.com/media/newsfeed_media/Gaint-hyper-2_HCpmXPn.png", "Devland", "Our New Promotion is Coming Soon", "Don't miss it guys, we are sure you will love it."));
        list.add(new PostGroupModel(2, "https://seemesave-web.fra1.digitaloceanspaces.com/media/newsfeed_media/Yarona-Cash--Carry_3_5SZ4Lyg.png", "Devland", "Our New Promotion is Coming Soon", "Don't miss it guys, we are sure you will love it."));
        list.add(new PostGroupModel(3, "https://seemesave-web.fra1.digitaloceanspaces.com/media/newsfeed_media/Epping_BlYqO0c.png", "Devland", "Our New Promotion is Coming Soon", "Don't miss it guys, we are sure you will love it."));
        postGroupAdapter = new PostGroupAdapter(activity, list, new PostGroupAdapter.PostGroupRecyclerListener() {

            @Override
            public void onItemClicked(int pos, PostGroupModel model) {
                startActivity(new Intent(activity, PostSupplierDetailActivity.class));
            }
        });
        recyclerView.setAdapter(postGroupAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
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