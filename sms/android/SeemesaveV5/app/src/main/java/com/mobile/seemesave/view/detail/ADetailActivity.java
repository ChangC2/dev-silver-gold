package com.mobile.seemesave.view.detail;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.mobile.seemesave.R;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.view_model.detail.AViewModel;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ADetailActivity extends BaseActivity {
    private AViewModel mViewModel;


    private ADetailActivity activity;

    private boolean is_click_collect = true;
    private int brand_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AViewModel.class);
        setContentView(R.layout.a_detail_template);
        ButterKnife.bind(this);
        activity = this;
        initView();
    }

    private void initView() {
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
