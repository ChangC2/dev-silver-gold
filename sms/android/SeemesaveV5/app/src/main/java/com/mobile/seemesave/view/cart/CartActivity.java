package com.mobile.seemesave.view.cart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.MainPagerAdapter;
import com.mobile.seemesave.api.user.UserApi;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.AlertCountModel;
import com.mobile.seemesave.model.res.AlertCountRes;
import com.mobile.seemesave.utils.CustomViewPager;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.cart.CartViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.nikartm.support.ImageBadgeView;

public class CartActivity extends BaseActivity {
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
    private CartActivity activity;
    private Fragment deliverFragment, shoppingFragment;
    int selectedTab = 0;
    BroadcastReceiver refreshCallReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "refresh_count":
                    UserApi.getAlertCount();
                    break;
            }
        }
    };
    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("refresh_count");
        LocalBroadcastManager.getInstance(activity).registerReceiver(refreshCallReceiver,
                filter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        activity = this;
        initView();
    }
    private void showBadgeFromLocal() {
        int deliverCount = G.pref.getInt("deliverCount", 0);
        int cartCount = G.pref.getInt("cartCount", 0);
        int listCount = G.pref.getInt("listCount", 0);
        int notificationCount = G.pref.getInt("notificationCount", 0);
        ic_deliver_cart.setBadgeValue(deliverCount);
        ic_shopping_cart.setBadgeValue(cartCount);
    }
    private void initView() {
        registerBroadcast();
        setFragmentView();
        showBadgeFromLocal();

    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mViewModel.getIsBusy().observe(this, isBusy -> {
            if (isBusy) {
                showLoadingDialog();
            } else {
                hideLoadingDialog();
            }
        });
        UserApi.getAlertCount();
    }
    private void setFragmentView() {
        deliverFragment = DeliverCartFragment.newInstance();
        shoppingFragment = ShoppingCartFragment.newInstance();
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
            ic_deliver_cart.setColorFilter(ContextCompat.getColor(activity, R.color.black_color));
            bottom_deliver_cart.setVisibility(View.VISIBLE);
            ic_shopping_cart.setColorFilter(ContextCompat.getColor(activity, R.color.md_grey_500));
            bottom_shopping_cart.setVisibility(View.INVISIBLE);
            ((DeliverCartFragment) deliverFragment).loadData();
        } else if (pos == 1) {
            ic_deliver_cart.setColorFilter(ContextCompat.getColor(activity, R.color.md_grey_500));
            bottom_deliver_cart.setVisibility(View.INVISIBLE);
            ic_shopping_cart.setColorFilter(ContextCompat.getColor(activity, R.color.black_color));
            bottom_shopping_cart.setVisibility(View.VISIBLE);
            ((ShoppingCartFragment) shoppingFragment).loadData();
        }
        viewPager.setCurrentItem(pos);
    }
    @OnClick({R.id.btBack, R.id.li_deliver_cart, R.id.li_shopping_cart, R.id.imgHistory})
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
            case R.id.imgHistory:
                mViewModel.goHistory(activity);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onApiSuccessResult(AlertCountRes res) {
        if (res.isStatus()) {
            AlertCountModel model = res.getData();
            G.setBadgeCount(model.getShopping_list_count(), model.getShopping_cart_count(), model.getNotification_count(), model.getDeliver_cart_count());
            showBadgeFromLocal();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
