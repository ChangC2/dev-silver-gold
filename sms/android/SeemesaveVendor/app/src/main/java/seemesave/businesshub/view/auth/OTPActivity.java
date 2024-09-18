package seemesave.businesshub.view.auth;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import seemesave.businesshub.R;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.view_model.auth.OTPViewModel;
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
        if (isMobile) {
            mask = phoneNumber.replaceAll("\\w(?=\\w{4})", "*");
            mask = getString(R.string.slide_4_desc) + "your number " + mask;
        } else {
            mask = email.substring(0, 1)  + "******@" + email.split("@")[1];
            mask = getString(R.string.slide_4_desc) + "your email " + mask;
        }
        txtDigit.setText(String.format(java.util.Locale.US,"%s",mask));
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

    @OnClick({R.id.btBack, R.id.btnNext})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.btnNext:
                mViewModel.gotoLogin(activity);
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
