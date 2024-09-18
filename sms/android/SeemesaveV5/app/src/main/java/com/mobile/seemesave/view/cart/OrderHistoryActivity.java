package com.mobile.seemesave.view.cart;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.MainPagerAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.utils.CustomViewPager;
import com.mobile.seemesave.view_model.cart.CartViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.nikartm.support.ImageBadgeView;

public class OrderHistoryActivity extends BaseActivity {
    private CartViewModel mViewModel;


    @BindView(R.id.ic_deliver_cart)
    ImageBadgeView ic_deliver_cart;
    @BindView(R.id.bottom_deliver_cart)
    View bottom_deliver_cart;

    @BindView(R.id.ic_shopping_cart)
    ImageBadgeView ic_shopping_cart;
    @BindView(R.id.bottom_shopping_cart)
    View bottom_shopping_cart;

    @BindView(R.id.viewPager)
    CustomViewPager viewPager;
    private MainPagerAdapter viewPagerAdapter;
    private OrderHistoryActivity activity;
    private Fragment deliverFragment, shoppingFragment;
    int selectedTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        setContentView(R.layout.activity_order_history);
        ButterKnife.bind(this);
        activity = this;
        initView();
    }

    private void initView() {
        setFragmentView();

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
    private void setFragmentView() {
        deliverFragment = DeliverHistoryFragment.newInstance();
        shoppingFragment = ShoppingHistoryFragment.newInstance();
        viewPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(deliverFragment);
        viewPagerAdapter.addFrag(shoppingFragment);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.disableScroll(true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("page:1", position + "");
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("page:3", state + "");
            }
        });
    }
    private void showTab(int pos) {
        if (pos == 0) {
            ic_deliver_cart.setColorFilter(getResources().getColor(R.color.black_color));
            bottom_deliver_cart.setVisibility(View.VISIBLE);
            ic_shopping_cart.setColorFilter(getResources().getColor(R.color.md_grey_500));
            bottom_shopping_cart.setVisibility(View.INVISIBLE);
            ((DeliverHistoryFragment) deliverFragment).loadData();
        } else if (pos == 1) {
            ic_deliver_cart.setColorFilter(getResources().getColor(R.color.md_grey_500));
            bottom_deliver_cart.setVisibility(View.INVISIBLE);
            ic_shopping_cart.setColorFilter(getResources().getColor(R.color.black_color));
            bottom_shopping_cart.setVisibility(View.VISIBLE);
            ((ShoppingHistoryFragment) shoppingFragment).loadData();
        }
        viewPager.setCurrentItem(pos);
    }
    @OnClick({R.id.btBack, R.id.li_deliver_cart, R.id.li_shopping_cart, })
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.li_deliver_cart:
                selectedTab = 0;
                showTab(0);
                viewPager.setCurrentItem(selectedTab);
                break;
            case R.id.li_shopping_cart:
                selectedTab = 1;
                showTab(1);
                viewPager.setCurrentItem(selectedTab);
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
