package com.mobile.seemesave.view.menu;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.mobile.seemesave.R;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.view_model.menu.ContactUsViewModel;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactUsNewActivity extends BaseActivity {

    private ContactUsViewModel mViewModel;
    private ContactUsNewActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ContactUsViewModel.class);
        setContentView(R.layout.activity_contact_us_new);
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

    @OnClick({R.id.imgBack, R.id.btnPhone, R.id.btnEmail, R.id.btnSpeak})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnPhone:
                mViewModel.goPhone(activity);
                break;
            case R.id.btnEmail:
                mViewModel.goEmail(activity);
                break;
            case R.id.btnSpeak:
                mViewModel.goSpeak(activity);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}