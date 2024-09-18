package com.mobile.seemesave.view.auth;

import static com.mobile.seemesave.utils.SystemUtils.isValidEmail;
import static com.mobile.seemesave.utils.SystemUtils.isValidMobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.api.common.CommonApi;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.UserModel;
import com.mobile.seemesave.model.req.SignupReq;
import com.mobile.seemesave.model.res.SignupRes;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.main.MainActivity;
import com.mobile.seemesave.view_model.auth.SignupViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends BaseActivity {
    private SignupViewModel mViewModel;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.layoutDots)
    LinearLayout dotsLayout;
    @BindView(R.id.btnNext)
    LinearLayout btnNext;
    @BindView(R.id.btnNextInvisible)
    LinearLayout btnNextInvisible;

    private TextInputEditText edtName;
    private TextInputEditText edtSurname;
    private CountryCodePicker countryCodePicker;
    private TextInputEditText edtNumber;
    private TextInputEditText edtEmail;
    private TextInputEditText edtPassword;

    private CheckBox ckBuy;
    private CheckBox ckSell;
    private CheckBox ckMobile;
    private CheckBox ckEmail;
    private Button btnResend;
    private ToggleButton toggoleBtn;
    private LinearLayout lytMobile, lytEmail;

    private TextView txtDigit;
    private EditText editENCode1;
    private EditText editENCode2;
    private EditText editENCode3;
    private EditText editENCode4;
    private EditText editENCode5;
    private EditText editENCode6;


    private SliderPagerAdapter sliderPagerAdapter;
    private int[] layouts;
    private TextView[] dots;
    private SparseArray<View> mCacheView;
    private boolean isBuy = false, isMobile = true;
    private String strOTP = "";
    private SignupActivity activity;
    int user_id = -1;
    private boolean isTerm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SignupViewModel.class);
        setContentView(R.layout.activity_signup);
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
                showLoadingDialog();
            } else {
                hideLoadingDialog();
            }
        });
        CommonApi.getAddress();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {

        mCacheView = new SparseArray<>();
        mCacheView.clear();

        layouts = new int[]{
                R.layout.signup_slide1,
                R.layout.signup_slide2,
                R.layout.signup_slide3,
                R.layout.signup_slide4
        };
        addBottomDots(0);

        sliderPagerAdapter = new SliderPagerAdapter();
        viewPager.setAdapter(sliderPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        viewPager.setOffscreenPageLimit(layouts.length - 1);

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < layouts.length; i++) {
            View view = layoutInflater.inflate(layouts[i], null, false);
            mCacheView.put(i, view);
            switch (i) {
                case 0:
                    ckBuy = view.findViewById(R.id.ckBuy);
                    ckSell = view.findViewById(R.id.ckSell);
                    view.findViewById(R.id.txtTerms).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isTerm = true;
                            G.openUrlBrowser(activity, "https://www.seemesave.com/terms");
                        }
                    });

                    view.findViewById(R.id.txtPrivacy).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isTerm = true;
                            G.openUrlBrowser(activity, "https://www.seemesave.com/privacy");
                        }
                    });
                    break;
                case 1:
                    edtName = view.findViewById(R.id.edtName);
                    edtSurname = view.findViewById(R.id.edtSurname);
                    break;
                case 2:
                    ckEmail = view.findViewById(R.id.ckEmail);
                    ckMobile = view.findViewById(R.id.ckMobile);
                    lytMobile = view.findViewById(R.id.lytMobile);
                    lytEmail = view.findViewById(R.id.lytEmail);
                    edtNumber = view.findViewById(R.id.edtNumber);
                    countryCodePicker = view.findViewById(R.id.country_picker);
                    edtEmail = view.findViewById(R.id.edtEmail);
                    edtPassword = view.findViewById(R.id.edtPassword);
                    toggoleBtn = view.findViewById(R.id.toggoleBtn);
                    break;
                case 3:
                    txtDigit = view.findViewById(R.id.txtDigit);
                    btnResend = view.findViewById(R.id.btnResend);
                    editENCode1 = view.findViewById(R.id.otp_enedit_code1);
                    editENCode2 = view.findViewById(R.id.otp_enedit_code2);
                    editENCode3 = view.findViewById(R.id.otp_enedit_code3);
                    editENCode4 = view.findViewById(R.id.otp_enedit_code4);
                    editENCode5 = view.findViewById(R.id.otp_enedit_code5);
                    editENCode6 = view.findViewById(R.id.otp_enedit_code6);
                    break;
            }
        }

        ckBuy.setOnClickListener(view -> onCheckBuySell(false));
        ckSell.setOnClickListener(view -> onCheckBuySell(true));
        ckMobile.setOnClickListener(view -> onCheckMobile(true));
        ckEmail.setOnClickListener(view -> onCheckMobile(false));
        toggoleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    edtPassword.setInputType(129);
                }
            }
        });
        btnResend.setPaintFlags(btnResend.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        editENCode1.addTextChangedListener(textwatcher1);
        editENCode2.addTextChangedListener(textwatcher2);
        editENCode3.addTextChangedListener(textwatcher3);
        editENCode4.addTextChangedListener(textwatcher4);
        editENCode5.addTextChangedListener(textwatcher5);
        editENCode6.addTextChangedListener(textwatcher6);
        btnResend.setOnClickListener(view -> onResendOtp());

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

    private void onCheckBuySell(boolean is_buy) {
        isBuy = is_buy;
        if (isBuy) {
            ckBuy.setChecked(true);
            ckSell.setChecked(false);
        } else {
            ckBuy.setChecked(false);
            ckSell.setChecked(true);
        }
    }

    private void onCheckMobile(boolean is_mobile) {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessSignup(SignupRes res) {
        if (res.isStatus()) {
            user_id = res.getId();
            viewPager.setCurrentItem(3);
        } else {
            showMessages(res.getMessage());
        }
    }

    private void onSignUp() {
        SignupReq req = new SignupReq();
        req.setFirst_name(edtName.getText().toString().trim());
        req.setLast_name(edtSurname.getText().toString().trim());
        req.setEmail(edtEmail.getText().toString().trim());
        req.setCountryCode(countryCodePicker.getSelectedCountryCode());
        req.setPhoneNumber(edtNumber.getText().toString());
        req.setPassword(edtPassword.getText().toString());
        req.setRegister_with(isMobile ? "phone" : "email");
        req.setLatitude(G.user.getLatitude());
        req.setLongitude(G.user.getLongitude());
        req.setIs_trader(isBuy);
        mViewModel.onSignup(req);

    }
    private void onSendOtp() {
        if (user_id == -1) {
            Toast.makeText(activity, R.string.msg_failed_signup, Toast.LENGTH_LONG).show();
            return;
        }
        apiCallForVerifyCode();

    }
    private void onResendOtp() {
        onSendOtp();
    }
    void apiCallForVerifyCode() {
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
                                    G.location = new Location(LocationManager.GPS_PROVIDER);
                                    G.location.setLatitude(G.user.getLatitude());
                                    G.location.setLongitude(G.user.getLongitude());
                                    G.initUserInfo(G.user, isMobile, edtPassword.getText().toString(), jsonObject.getJSONObject("data").getString("token"), true);
                                    Intent intent = new Intent(activity, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

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
    @OnClick({R.id.btBack, R.id.btnNext, R.id.btnLogin})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.btnNext:
                int current = getItem(+1);
                if (current < layouts.length - 1) {
                    viewPager.setCurrentItem(current);
                    break;
                } else if (current == layouts.length - 1) {
                    onSignUp();
                    break;
                } else {
                    onSendOtp();
                    break;
                }
            case R.id.btnLogin:
                mViewModel.gotoLogin(this);
                break;
        }
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private void onBack() {
//        int current = getItem(-1);
//        if (current == dots.length)
//        if (current > -1) {
//            viewPager.setCurrentItem(current);
//        } else {
//            finish();
//        }
        finish();
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    public class SliderPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public SliderPagerAdapter() {
            super();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mCacheView.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

    }

    private void showOTP() {
        String mask = "";
        if (isMobile) {
            mask = edtNumber.getText().toString().replaceAll("\\w(?=\\w{4})", "*");
            mask = getString(R.string.slide_4_desc) + "your number " + mask;
        } else {
            mask = edtEmail.getText().toString().substring(0, 1) + "******@" + edtEmail.getText().toString().split("@")[1];
            mask = getString(R.string.slide_4_desc) + "your email " + mask;
        }
        txtDigit.setText(String.format(java.util.Locale.US, "%s", mask));
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position == 3) {
                enableActive(false);
                editENCode6.setText("");
                editENCode5.setText("");
                editENCode4.setText("");
                editENCode3.setText("");
                editENCode2.setText("");
                editENCode1.setText("");
            } else {
                enableActive(true);
            }
//            if (position == 1) {
//                if (!isTerm) {
//                    viewPager.setCurrentItem(0);
//                    Toast.makeText(activity, R.string.msg_confirm_term, Toast.LENGTH_LONG).show();
//                }
//            } else
            if (position == 2) {
                if (TextUtils.isEmpty(edtName.getText().toString()) || TextUtils.isEmpty(edtSurname.getText().toString())) {
                    viewPager.setCurrentItem(1);
                    Toast.makeText(activity, R.string.msg_valid_name, Toast.LENGTH_LONG).show();
                }
            } else if (position == 3) {
                if (isMobile) {
                    if (TextUtils.isEmpty(edtNumber.getText().toString()) || TextUtils.isEmpty(edtPassword.getText().toString())) {
                        Toast.makeText(activity, R.string.msg_valid_number, Toast.LENGTH_LONG).show();
                        viewPager.setCurrentItem(2);
                        return;
                    } else {
                        if (!isValidMobile(edtNumber.getText().toString())) {
                            Toast.makeText(activity, R.string.msg_valid_phone, Toast.LENGTH_LONG).show();
                            viewPager.setCurrentItem(2);
                            return;
                        }
                    }
                } else {
                    if (TextUtils.isEmpty(edtEmail.getText().toString()) || TextUtils.isEmpty(edtPassword.getText().toString())) {
                        Toast.makeText(activity, R.string.msg_empty_email, Toast.LENGTH_LONG).show();
                        viewPager.setCurrentItem(2);
                        return;
                    } else {
                        if (!isValidEmail(edtEmail.getText().toString())) {
                            Toast.makeText(activity, R.string.msg_valid_email, Toast.LENGTH_LONG).show();
                            viewPager.setCurrentItem(2);
                            return;
                        }
                    }
                }
                showOTP();
            }

        }

        @Override
        public void onPageScrolled(int position, float arg1, int arg2) {
            Log.e("scroll:", position + "");

//            CURRENT_PAGE = arg0;
//            if (CURRENT_PAGE == 2) {
//
//            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            //Log.e("scroll:", arg0 + "");
        }
    };
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