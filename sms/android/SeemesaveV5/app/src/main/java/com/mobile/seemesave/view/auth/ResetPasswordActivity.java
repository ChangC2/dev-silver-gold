package com.mobile.seemesave.view.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.auth.ResetPasswordViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPasswordActivity   extends BaseActivity {
    private ResetPasswordViewModel mViewModel;
    @BindView(R.id.edtPassword)
    TextInputEditText edtPassword;
    @BindView(R.id.toggoleBtn)
    ToggleButton toggoleBtn;

    private ResetPasswordActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        activity = this;
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    }

    private void initView() {
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
    }
    private void onChange() {
        if (TextUtils.isEmpty(edtPassword.getText())){
            Toast.makeText(this, R.string.missing_param, Toast.LENGTH_LONG).show();
            return;
        }
        showLoadingDialog();
        Ion.with(this)
                .load("PUT", G.ForgotPassUrl)
                .setBodyParameter("id", getIntent().getStringExtra("user_id"))
                .setBodyParameter("password", edtPassword.getText().toString().trim())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        showLoadingDialog();
                        Toast.makeText(activity, R.string.update_success, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(activity, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
    }
    @OnClick({R.id.btBack, R.id.btnChange})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.btnChange:
                onChange();
                break;
        }
    }

}
