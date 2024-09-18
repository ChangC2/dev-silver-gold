package com.mobile.seemesave.view.auth;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.mobile.seemesave.R;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.res.LoginRes;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.main.MainActivity;
import com.mobile.seemesave.view_model.auth.LoginViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    private LoginViewModel mViewModel;
    @BindView(R.id.edtNumber)
    TextInputEditText edtNumber;
    @BindView(R.id.edtEmail)
    TextInputEditText edtEmail;
    @BindView(R.id.edtPassword)
    TextInputEditText edtPassword;
    @BindView(R.id.btnForgotPassword)
    Button btnForgotPassword;
    @BindView(R.id.country_picker)
    CountryCodePicker countryCodePicker;
    @BindView(R.id.toggoleBtn)
    ToggleButton toggoleBtn;

    @BindView(R.id.ckMobile)
    CheckBox ckMobile;
    @BindView(R.id.ckEmail)
    CheckBox ckEmail;
    @BindView(R.id.lytMobile)
    LinearLayout lytMobile;
    @BindView(R.id.lytEmail)
    LinearLayout lytEmail;

    private boolean isMobile = true;
    private String countryCodeStr = "27";
    private LoginActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        activity = this;
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mViewModel.getIsBusy().observe(this, isBusy -> {
            if (isBusy) {
                G.showLoading(activity);
            } else {
                G.hideLoading();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
//        edtEmail.setText("jin.lu@seemesave.com");
//        edtPassword.setText("123456");

//        edtEmail.setText("cai.xian@seemesave.com");
//        edtPassword.setText("cai123456");

        btnForgotPassword.setPaintFlags(btnForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        ckMobile.setOnClickListener(view -> onCheckMobile(true));
        ckEmail.setOnClickListener(view -> onCheckMobile(false));
        toggoleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else{
                    edtPassword.setInputType(129);
                }
            }
        });

        countryCodePicker.setOnCountryChangeListener(() -> {
            countryCodeStr = countryCodePicker.getSelectedCountryCode();
        });
    }
    private void onCheckMobile(boolean is_mobile){
        isMobile = is_mobile;
        if (isMobile) {
            lytMobile.setVisibility(View.VISIBLE);
            lytEmail.setVisibility(View.GONE);
            ckMobile.setChecked(true);
            ckEmail.setChecked(false);
        } else {
            lytMobile.setVisibility(View.GONE);
            lytEmail.setVisibility(View.VISIBLE);
            ckMobile.setChecked(false);
            ckEmail.setChecked(true);
        }
    }
    @OnClick({R.id.btBack, R.id.btLogin, R.id.btnSignup, R.id.btnForgotPassword})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.btLogin:
                mViewModel.loginClicked(this, isMobile, edtEmail.getText().toString(), countryCodeStr, edtNumber.getText().toString(), edtPassword.getText().toString());
                break;
            case R.id.btnSignup:
                mViewModel.gotoSignup(this);
                break;
            case R.id.btnForgotPassword:
                mViewModel.gotoForgotPassword(this);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessLogin(LoginRes res) {
        if (res.isStatus()) {
            G.user = res.getUser();
            G.location = new Location(LocationManager.GPS_PROVIDER);
            G.location.setLatitude(G.user.getLatitude());
            G.location.setLongitude(G.user.getLongitude());
            G.initUserInfo(res.getUser(), isMobile, edtPassword.getText().toString(), res.getToken(), true);
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            if (!res.getMessage().equalsIgnoreCase("")) {
                Toast.makeText(activity, res.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}