package com.mobile.seemesave.view.checkout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.lifecycle.ViewModelProvider;

import com.mobile.seemesave.R;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.view_model.checkout.CheckoutFinishViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckoutFinishActivity extends BaseActivity {

    private CheckoutFinishViewModel mViewModel;
    private CheckoutFinishActivity activity;

    @BindView(R.id.imgBack)
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CheckoutFinishViewModel.class);
        setContentView(R.layout.activity_checkout_finish);
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

    }
    private void showBadgeFromLocal() {

    }


    @OnClick({R.id.imgBack})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
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