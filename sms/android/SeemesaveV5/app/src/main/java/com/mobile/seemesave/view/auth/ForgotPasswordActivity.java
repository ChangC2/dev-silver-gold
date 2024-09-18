package com.mobile.seemesave.view.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.res.LoginRes;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.auth.ForgotPasswordViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity  extends BaseActivity {
    private ForgotPasswordViewModel mViewModel;
    @BindView(R.id.edtNumber)
    TextInputEditText edtNumber;
    @BindView(R.id.edtEmail)
    TextInputEditText edtEmail;
    @BindView(R.id.country_picker)
    CountryCodePicker countryCodePicker;
    @BindView(R.id.lytMobile)
    LinearLayout lytMobile;
    @BindView(R.id.lytEmail)
    LinearLayout lytEmail;
    @BindView(R.id.ckMobile)
    CheckBox ckMobile;
    @BindView(R.id.ckEmail)
    CheckBox ckEmail;

    private boolean isMobile = true;
    ForgotPasswordActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);
        setContentView(R.layout.activity_forgot_password);
        activity = this;
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mViewModel.getIsBusy().observe(this, isBusy -> {
            if (isBusy) {
                showLoadingDialog();
            } else {
                hideLoadingDialog();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        ckMobile.setOnClickListener(view -> onCheckMobile(true));
        ckEmail.setOnClickListener(view -> onCheckMobile(false));
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
    private void onSend() {
        if (isMobile) {
            apiCallForSendbyMobile();
        } else {
            apiCallForSendByEmail();
        }
    }
    void apiCallForSendByEmail() {
        if (TextUtils.isEmpty(edtEmail.getText())){
            Toast.makeText(this, R.string.missing_param, Toast.LENGTH_LONG).show();
            return;
        }
        showLoadingDialog();
        String url = G.ForgotPassUrl + "?email=" + edtEmail.getText().toString().trim() +"&register_with=email";
        Ion.with(this)
                .load(url)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getBoolean("status")){
                                String userId = jsonObject.getJSONObject("data").getString("id");
                                mViewModel.moveToOTP(activity, isMobile, edtEmail.getText().toString(), countryCodePicker.getSelectedCountryCode(), edtNumber.getText().toString(), userId);
                            }else{
                                Toast.makeText(activity, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException jsonException) {
                            Toast.makeText(activity, R.string.connection_fail, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    void apiCallForSendbyMobile() {
        if (TextUtils.isEmpty(edtNumber.getText().toString())){
            Toast.makeText(this, R.string.missing_param, Toast.LENGTH_LONG).show();
            return;
        }
        showLoadingDialog();
        String url = G.ForgotPassUrl + "?phoneNumber=" + edtNumber.getText().toString() + "&register_with=phone" + "&countryCode=" + countryCodePicker.getSelectedCountryCode();
        Ion.with(this)
                .load(url)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getBoolean("status")){
                                String userId = jsonObject.getJSONObject("data").getString("id");
                                mViewModel.moveToOTP(activity, isMobile, edtEmail.getText().toString(), countryCodePicker.getSelectedCountryCode(), edtNumber.getText().toString(), userId);
                            }else{
                                Toast.makeText(activity, R.string.connection_fail, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException jsonException) {
                            Toast.makeText(activity, R.string.connection_fail, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    @OnClick({R.id.btBack, R.id.btSend, R.id.btnSignup})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.btSend:
                onSend();
                break;
            case R.id.btnSignup:
                mViewModel.gotoSignup(this);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessLogin(LoginRes res) {
//        if (res.isResult()) {
//            Intent intent = new Intent(LoginActivity.this, ConnectDeviceActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        } else {
//            showMessages(res.getMessage());
//        }
    }
}
