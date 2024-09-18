package com.mobile.seemesave.view.auth;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.greenrobot.eventbus.EventBus;

import com.mobile.seemesave.R;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.view_model.auth.StartedViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartedActivity extends BaseActivity {

    private StartedViewModel mViewModel;

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.layoutDots)
    LinearLayout dotsLayout;
    @BindView(R.id.btnNext)
    LinearLayout btnNext;
    @BindView(R.id.txtNext)
    TextView txtNext;
    @BindView(R.id.btnSkip)
    TextView btnSkip;

    private SliderPagerAdapter sliderPagerAdapter;
    private int[] layouts;
    private TextView[] dots;

    private StartedActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StartedViewModel.class);
        setContentView(R.layout.activity_started);
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
        EventBus.getDefault().unregister(this);
    }

    private void initView() {

        layouts = new int[]{
                R.layout.started_slide1,
                R.layout.started_slide2,
                R.layout.started_slide3,
                R.layout.started_slide4
        };
        addBottomDots(0);

        sliderPagerAdapter = new SliderPagerAdapter();
        viewPager.setAdapter(sliderPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        viewPager.setOffscreenPageLimit(layouts.length - 1);


    }



    @OnClick({R.id.btBack, R.id.btnNext, R.id.btnSkip})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                onBack();
                break;
            case R.id.btnNext:
                int current = getItem(+1);
                if (current < layouts.length) {
                    viewPager.setCurrentItem(current);
                } else {
                    mViewModel.gotoJoin(this);
                }
                break;
            case R.id.btnSkip:
                int currents = getItem(+1);
                if (currents < layouts.length) {
                    mViewModel.gotoJoin(this);
                } else {
                    mViewModel.gotoSign(this);
                }
                break;
        }
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private void onBack() {
        int current = getItem(-1);
        if (current > -1) {
            viewPager.setCurrentItem(current);
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    public class SliderPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public SliderPagerAdapter() {
            super();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

    }



    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            switch (position) {
                case 3:
                    txtNext.setText(getString(R.string.txt_join));
                    btnSkip.setText(getString(R.string.txt_sign_in));
                    break;
                default:
                    txtNext.setText(getString(R.string.txt_next));
                    btnSkip.setText(getString(R.string.txt_skip));
                    break;
            }

        }

        @Override
        public void onPageScrolled(int position, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };
}