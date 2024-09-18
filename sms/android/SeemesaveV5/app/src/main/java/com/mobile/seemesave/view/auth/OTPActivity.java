package com.mobile.seemesave.view.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.UserModel;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.auth.OTPViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OTPActivity extends BaseActivity {
    private OTPViewModel mViewModel;
    @BindView(R.id.otp_enedit_code1)
    EditText editENCode1;
    @BindView(R.id.otp_enedit_code2)
    EditText editENCode2;
    @BindView(R.id.otp_enedit_code3)
    EditText editENCode3;
    @BindView(R.id.otp_enedit_code4)
    EditText editENCode4;
    @BindView(R.id.otp_enedit_code5)
    EditText editENCode5;
    @BindView(R.id.otp_enedit_code6)
    EditText editENCode6;
    @BindView(R.id.txtDigit)
    TextView txtDigit;
    @BindView(R.id.btnNext)
    LinearLayout btnNext;
    @BindView(R.id.btnNextInvisible)
    LinearLayout btnNextInvisible;

    private OTPActivity activity;
    private String strOTP = "";
    private boolean isMobile = true;
    private String email = "";
    private String countryCode = "";
    private String phoneNumber = "";
    private String user_id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(OTPViewModel.class);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);
        activity = this;

        isMobile = getIntent().getBooleanExtra("isMobile", true);
        email = getIntent().getStringExtra("email");
        countryCode = getIntent().getStringExtra("countryCode");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        user_id = getIntent().getStringExtra("user_id");
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
        editENCode1.addTextChangedListener(textwatcher1);
        editENCode2.addTextChangedListener(textwatcher2);
        editENCode3.addTextChangedListener(textwatcher3);
        editENCode4.addTextChangedListener(textwatcher4);
        editENCode5.addTextChangedListener(textwatcher5);
        editENCode6.addTextChangedListener(textwatcher6);
        enableActive(false);
        String mask = "";
        try {
            if (isMobile) {
                mask = phoneNumber.replaceAll("\\w(?=\\w{4})", "*");
                mask = getString(R.string.slide_4_desc) + "your number " + mask;
            } else {
                mask = email.substring(0, 1)  + "******@" + email.split("@")[1];
                mask = getString(R.string.slide_4_desc) + "your email " + mask;
            }
            txtDigit.setText(String.format(java.util.Locale.US,"%s",mask));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void enableActive(boolean flag) {
        if (flag) {
            btnNext.setVisibility(View.VISIBLE);
            btnNextInvisible.setVisibility(View.GONE);
        } else {
            btnNext.setVisibility(View.GONE);
            btnNextInvisible.setVisibility(View.VISIBLE);
        }
    }
    private boolean validActive(String verifyCode) {
        boolean noErrorFound = true;

        if (verifyCode.isEmpty()) {
            String strMsg = "Please enter verification code.";
            Toast.makeText(activity, strMsg, Toast.LENGTH_SHORT).show();
            noErrorFound = false;
        }
        return noErrorFound;
    }

    private void onSend() {
        if (validActive(strOTP)) {
            apiCallForVerifyCode();
            enableActive(false);
        }
    }
    private void apiCallForVerifyCode() {
        showLoadingDialog();
        JsonObject json = new JsonObject();
        json.addProperty("id", user_id);
        json.addProperty("activation_code", strOTP);

        Ion.with(this)
                .load("PUT", G.REGISTER)
                .addHeader("Content-Type", "application/json")
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        if (e != null) {
                            Toast.makeText(activity, R.string.verification_fail, Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                Gson gson = new Gson();
                                if (jsonObject.getBoolean("status")) {
                                    G.user = gson.fromJson(jsonObject.getJSONObject("data").getString("user"), UserModel.class);
                                    moveToUpdatePass();

                                } else {
                                    Toast.makeText(activity, R.string.verification_fail, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException jsonException) {
                                Toast.makeText(activity, R.string.verification_fail, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

    }
    private void moveToUpdatePass() {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        intent.putExtra("user_id", user_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    @OnClick({R.id.btBack, R.id.btnNext})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.btnNext:
                onSend();
                break;
        }
    }

    TextWatcher textwatcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() == 1) {
                editENCode2.requestFocus();
            }
        }
    };
    TextWatcher textwatcher2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.toString().length() == 0) {
                editENCode1.requestFocus();
                enableActive(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() == 1) {
                editENCode3.requestFocus();
            }
        }
    };
    TextWatcher textwatcher3 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.toString().length() == 0) {
                editENCode2.requestFocus();
                enableActive(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() == 1) {
                editENCode4.requestFocus();
            }
        }
    };
    TextWatcher textwatcher4 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.toString().length() == 0) {
                editENCode3.requestFocus();
                enableActive(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() == 1) {
                editENCode5.requestFocus();
            }
        }
    };
    TextWatcher textwatcher5 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.toString().length() == 0) {
                editENCode4.requestFocus();
                enableActive(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() == 1) {
                editENCode6.requestFocus();
            }
        }
    };
    TextWatcher textwatcher6 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.toString().length() == 0) {
                editENCode5.requestFocus();
                enableActive(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() == 1) {
                strOTP = editENCode1.getText().toString().trim() + editENCode2.getText().toString().trim() + editENCode3.getText().toString().trim() + editENCode4.getText().toString().trim() + editENCode5.getText().toString().trim() + editENCode6.getText().toString().trim();
                if (strOTP.length() < 6) {
                    enableActive(false);
                } else {
                    enableActive(true);
                }
            }
        }
    };
}
