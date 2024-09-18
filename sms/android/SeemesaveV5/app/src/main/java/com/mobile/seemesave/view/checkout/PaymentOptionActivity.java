package com.mobile.seemesave.view.checkout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.lifecycle.ViewModelProvider;

import com.mobile.seemesave.R;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.view_model.checkout.PaymentOptionViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentOptionActivity extends BaseActivity {

    private PaymentOptionViewModel mViewModel;
    private PaymentOptionActivity activity;

    @BindView(R.id.imgBack)
    ImageView imgBack;

    @BindView(R.id.ckMaster)
    ImageView ckMaster;
    @BindView(R.id.ckPaypal)
    ImageView ckPaypal;
    @BindView(R.id.ckMpesa)
    ImageView ckMpesa;
    @BindView(R.id.ckVisa)
    ImageView ckVisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PaymentOptionViewModel.class);
        setContentView(R.layout.activity_payment_option);
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
        checkPayment(-1);
    }
    private void showBadgeFromLocal() {

    }


    @OnClick({R.id.imgBack, R.id.btnNext, R.id.li_master, R.id.li_paypal, R.id.li_mpesa, R.id.li_visa})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnNext:
                mViewModel.goNext(activity);
                break;
            case R.id.li_master:
                checkPayment(0);
                break;
            case R.id.li_paypal:
                checkPayment(1);
                break;
            case R.id.li_mpesa:
                checkPayment(2);
                break;
            case R.id.li_visa:
                checkPayment(3);
                break;
        }
    }

    private void checkPayment(int pos) {
        switch (pos) {
            case 0:
                ckMaster.setVisibility(View.VISIBLE);
                ckPaypal.setVisibility(View.GONE);
                ckMpesa.setVisibility(View.GONE);
                ckVisa.setVisibility(View.GONE);
                break;
            case 1:
                ckMaster.setVisibility(View.GONE);
                ckPaypal.setVisibility(View.VISIBLE);
                ckMpesa.setVisibility(View.GONE);
                ckVisa.setVisibility(View.GONE);
                break;
            case 2:
                ckMaster.setVisibility(View.GONE);
                ckPaypal.setVisibility(View.GONE);
                ckMpesa.setVisibility(View.VISIBLE);
                ckVisa.setVisibility(View.GONE);
                break;
            case 3:
                ckMaster.setVisibility(View.GONE);
                ckPaypal.setVisibility(View.GONE);
                ckMpesa.setVisibility(View.GONE);
                ckVisa.setVisibility(View.VISIBLE);
                break;
            default:
                ckMaster.setVisibility(View.GONE);
                ckPaypal.setVisibility(View.GONE);
                ckMpesa.setVisibility(View.GONE);
                ckVisa.setVisibility(View.GONE);
                break;

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}