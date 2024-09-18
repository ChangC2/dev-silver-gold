package com.mobile.seemesave.view.detail;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.mobile.seemesave.R;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.listener.SimpleGestureFilter;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.detail.StoryPlayerViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoryPlayerActivity extends BaseActivity {
    private StoryPlayerViewModel mViewModel;


    private StoryPlayerActivity activity;

    @BindView(R.id.pager)
    ViewPager2 pager;
    @BindView(R.id.my_view)
    RelativeLayout myView;
    int selectedIndex = 0;
    MyPagerAdapter pagerAdapter;

    private GestureDetector mDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StoryPlayerViewModel.class);
        G.setLightFullScreen(this);
        setContentView(R.layout.activity_story_player);
        ButterKnife.bind(this);
        activity = this;
        selectedIndex = getIntent().getIntExtra("selected_index", 0);


        initView();

    }

    private void initView() {

        ViewPager2.PageTransformer pageTransformer = new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                new CubeOutTransformer().transformPage(page, position);
            }
        };
        pager.setPageTransformer(pageTransformer);
        pagerAdapter = new MyPagerAdapter(this);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(selectedIndex);
        pager.setClipChildren(false);
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

    }

    @OnClick({R.id.btnBack})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                finish();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    private class MyPagerAdapter extends FragmentStateAdapter {
        public MyPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            StoryFragment fragment = StoryFragment.newInstance();
            fragment.releaseMusic();


            //                    for (Fragment f : fragments) {
//                        if (f != fragment){
//                            getSupportFragmentManager().beginTransaction().remove(f).commit();
//                        }
//                    }
            fragment.activity = activity;
            fragment.story = G.stories.get(position);
            return fragment;
        }

        @Override
        public int getItemCount() {
            return G.stories.size();
        }
    }


}
