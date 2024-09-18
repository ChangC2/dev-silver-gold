package com.mobile.seemesave.view.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.FavouriteTabAdapter;
import com.mobile.seemesave.adapter.MainPagerAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.FavouriteTabModel;
import com.mobile.seemesave.utils.CustomViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendDetailActivity extends BaseActivity {
    private FriendDetailActivity activity;

    @BindView(R.id.imgUser)
    CircleImageView imgUser;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private FavouriteTabAdapter recyclerAdapter;
    private MainPagerAdapter viewPagerAdapter;
    private Fragment productFragment, brandFragment, storeFragment, postFragment;

    private int friend_id = -1;

    private String user_image = "", user_name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        ButterKnife.bind(this);
        activity = this;
        friend_id = getIntent().getIntExtra("friend_id", -1);
        user_image = getIntent().getStringExtra("image");
        user_name = getIntent().getStringExtra("name");
        if (friend_id == -1) {
            finish();
        }
        initView();
    }

    private void initView() {
        Glide.with(activity)
                .load(user_image)
                .fitCenter()
                .placeholder(R.drawable.ic_avatar)
                .into(imgUser);
        txtTitle.setText(user_name);

        setFragmentView();
        ArrayList<FavouriteTabModel> list = new ArrayList<>();
        list.add(new FavouriteTabModel(1, "Products"));
        list.add(new FavouriteTabModel(2, "Brands"));
        list.add(new FavouriteTabModel(3, "Stores"));
        list.add(new FavouriteTabModel(4, "Posts"));
        recyclerAdapter = new FavouriteTabAdapter(activity, list, new FavouriteTabAdapter.FavouriteTabRecyclerListener() {
            @Override
            public void onItemClicked(int pos, FavouriteTabModel model) {
                recyclerView.scrollToPosition(pos);
                viewPager.setCurrentItem(pos);
            }

        });
        recyclerAdapter.setSelected_index(0);
        recyclerView.setAdapter(recyclerAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    private void setFragmentView() {
        productFragment = FriendProductFragment.newInstance(friend_id);
        brandFragment = FriendBrandFragment.newInstance();
        storeFragment = FriendStoreFragment.newInstance();
        postFragment = FriendPostFragment.newInstance();
        viewPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(productFragment);
        viewPagerAdapter.addFrag(brandFragment);
        viewPagerAdapter.addFrag(storeFragment);
        viewPagerAdapter.addFrag(postFragment);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.disableScroll(true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("page:1", position + "");
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("page:2", position + "");
                switch (position) {
                    case 0:
                        ((FriendProductFragment) productFragment).loadData(friend_id);
                        break;
                    case 1:
                        ((FriendBrandFragment) brandFragment).loadData(friend_id);
                        break;
                    case 2:
                        ((FriendStoreFragment) storeFragment).loadData(friend_id);
                        break;
                    case 3:
                        ((FriendPostFragment) postFragment).loadData(friend_id);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @OnClick({R.id.imgUser, R.id.btBack})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.imgUser:
                startActivity(new Intent(activity, EditProfileActivity.class));
                break;
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
