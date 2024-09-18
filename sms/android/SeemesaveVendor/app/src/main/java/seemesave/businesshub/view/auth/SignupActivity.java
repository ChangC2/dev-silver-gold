package seemesave.businesshub.view.auth;

import static seemesave.businesshub.utils.SystemUtils.isValidEmail;
import static seemesave.businesshub.utils.SystemUtils.isValidMobile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.instagram.InsGallery;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import seemesave.businesshub.R;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.model.req.LoginReq;
import seemesave.businesshub.model.req.SignupReq;
import seemesave.businesshub.model.res.LoginRes;
import seemesave.businesshub.model.res.SignupRes;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.view.supplier.main.MainSupplierActivity;
import seemesave.businesshub.view.vendor.main.MainActivity;
import seemesave.businesshub.view_model.auth.SignupViewModel;
import seemesave.businesshub.widget.imagePicker.GlideCacheEngine;
import seemesave.businesshub.widget.imagePicker.GlideEngine;

public class SignupActivity extends BaseActivity {
    private SignupViewModel mViewModel;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.txtStep)
    TextView txtStep;

    @BindView(R.id.imgStep)
    ImageView imgStep;


    private CountryCodePicker countryCodePicker;
    private TextInputEditText edtNumber;
    private TextInputEditText edtEmail;
    private TextInputEditText edtPassword;
    private CheckBox ckLoginMobile;
    private CheckBox ckLoginEmail;
    private LinearLayout btnNext1;
    private ToggleButton toggoleBtn;
    private LinearLayout lytMobile, lytEmail;
    private TextView btnSignup;

    private RadioButton rdVendor;
    private RadioButton rdSupplier;
    private LinearLayout btnNext2;
    private TextView txtTypeLabel;
    private TextView txtTypeInfo;
    private LinearLayout lytType;


    private CircleImageView imgProfile;
    private ImageView imgPhotoEdit;
    private TextInputEditText edtBusinessName;
    private TextInputEditText edtBusinessAddress;
    private TextInputEditText edtBusinessEmail;
    private TextInputEditText edtBusinessWebsite;
    private CountryCodePicker countryCodePickerBusiness;
    private TextInputEditText edtBusinessNumber;
    private LinearLayout btnNext3;

    private TextView txtAgreement;
    private CheckBox ckTerm;
    private Button txtTerm;
    private LinearLayout btnNext4;


    private boolean isLogin = false;
    private boolean selectedType = false, isVendor = false;
    private boolean finishedInfo = false;
    private boolean isConfirm = false;
    private String vendorProfile = "";

    private String businessName = "";
    private String businessAddress = "";
    private String businessEmail = "";
    private String businessMobileCountry = "";
    private String businessMobileNumber = "";
    private String businessWebsite = "";

    private SliderPagerAdapter sliderPagerAdapter;
    private int[] layouts;
    private TextView[] dots;
    private SparseArray<View> mCacheView;
    private boolean isBuy = true, isMobile = true;
    private String strOTP = "";
    private SignupActivity activity;

    private int user_id = -1;

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
        mViewModel.getUserData().observe(this, data -> {
            if (data != null && data.getToken() != "") {
                user_id = data.getUser().getId();
            }
        });
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
                    ckLoginMobile = view.findViewById(R.id.ckMobile);
                    ckLoginEmail = view.findViewById(R.id.ckEmail);
                    lytMobile = view.findViewById(R.id.lytMobile);
                    lytEmail = view.findViewById(R.id.lytEmail);
                    edtNumber = view.findViewById(R.id.edtNumber);
                    countryCodePicker = view.findViewById(R.id.country_picker);
                    edtEmail = view.findViewById(R.id.edtEmail);
                    edtPassword = view.findViewById(R.id.edtPassword);
                    toggoleBtn = view.findViewById(R.id.toggoleBtn);
                    btnNext1 = view.findViewById(R.id.btnNext1);
                    btnSignup = view.findViewById(R.id.btnSignup);
                    break;
                case 1:
                    rdVendor = view.findViewById(R.id.rdVendor);
                    rdSupplier = view.findViewById(R.id.rdSupplier);
                    txtTypeLabel = view.findViewById(R.id.txtTypeLabel);
                    txtTypeInfo = view.findViewById(R.id.txtTypeInfo);
                    lytType = view.findViewById(R.id.lytType);
                    btnNext2 = view.findViewById(R.id.btnNext2);
                    break;
                case 2:
                    imgProfile = view.findViewById(R.id.imgProfile);
                    imgPhotoEdit = view.findViewById(R.id.imgPhotoEdit);
                    edtBusinessName = view.findViewById(R.id.editBName);
                    edtBusinessAddress = view.findViewById(R.id.editBAddress);
                    edtBusinessEmail = view.findViewById(R.id.editBEmail);
                    edtBusinessNumber = view.findViewById(R.id.editBMobile);
                    countryCodePickerBusiness = view.findViewById(R.id.country_picker_business);
                    edtBusinessWebsite = view.findViewById(R.id.editLink);
                    btnNext3 = view.findViewById(R.id.btnNext3);
                    break;
                case 3:
                    txtAgreement = view.findViewById(R.id.txtAgreement);
                    ckTerm = view.findViewById(R.id.ckTerm);
                    txtTerm = view.findViewById(R.id.txtTerm);
                    btnNext4 = view.findViewById(R.id.btnNext4);
                    break;
            }
        }

        ckLoginMobile.setOnClickListener(view -> onCheckMobile(true));
        ckLoginEmail.setOnClickListener(view -> onCheckMobile(false));
        toggoleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    edtPassword.setInputType(129);
                }
            }
        });
        rdVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVendor = true;
                selectedType = true;
                txtTypeLabel.setText(R.string.txt_vendor);
                txtTypeInfo.setText(R.string.txt_vendor_info);
                lytType.setVisibility(View.VISIBLE);
                rdSupplier.setChecked(false);
                rdVendor.setChecked(true);
            }
        });
        rdSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVendor = false;
                selectedType = true;
                txtTypeLabel.setText(R.string.txt_supplier);
                txtTypeInfo.setText(R.string.txt_supplier_info);
                lytType.setVisibility(View.VISIBLE);
                rdVendor.setChecked(false);
                rdSupplier.setChecked(true);
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(G.UserAppPlayStoreUrl)));
            }
        });
        btnNext1.setOnClickListener(view -> onLogin());
        btnNext2.setOnClickListener(view -> goInfo());
        btnNext3.setOnClickListener(view -> goConfirm());
        btnNext4.setOnClickListener(view -> onSignup());
        imgPhotoEdit.setOnClickListener(view -> onProfileImage());
        txtTerm.setOnClickListener(view -> onTerm());

    }

    private void onSignup() {
        if (ckTerm.isChecked()) {
            File vendorLogo = new File(vendorProfile);
            RequestBody vendorLogoFile = RequestBody.create(MediaType.parse("multipart/form-data"), vendorLogo);
            MultipartBody.Part uploadFilePart = MultipartBody.Part.createFormData("new_logo", vendorLogo.getName(), vendorLogoFile);

            SignupReq req = new SignupReq();
            req.setUser_id(RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(user_id)));
            req.setNew_logo(uploadFilePart);
            req.setDest_portal(RequestBody.create(MediaType.parse("multipart/form-data"), isVendor ? "vendor" : "supplier"));
            req.setName(RequestBody.create(MediaType.parse("multipart/form-data"), businessName));
            req.setEmail(RequestBody.create(MediaType.parse("multipart/form-data"), businessEmail));
            req.setPhone(RequestBody.create(MediaType.parse("multipart/form-data"), businessMobileCountry + businessMobileNumber));
            req.setAddress(RequestBody.create(MediaType.parse("multipart/form-data"), businessAddress));
            req.setWebsite(RequestBody.create(MediaType.parse("multipart/form-data"), businessWebsite));
            mViewModel.onSignup(req);
        } else {
            showMessages(R.string.txt_msg_term);
        }
    }

    private void onTerm() {
        ckTerm.setChecked(true);
        G.openUrlBrowser(activity, isVendor ? G.vendor_privacy_url : G.supplier_privacy_url);
    }

    private void goConfirm() {
        if (TextUtils.isEmpty(vendorProfile) ||
                TextUtils.isEmpty(edtBusinessName.getText().toString().trim()) ||
                TextUtils.isEmpty(edtBusinessAddress.getText().toString().trim()) ||
                TextUtils.isEmpty(edtBusinessNumber.getText().toString().trim()) ||
                TextUtils.isEmpty(edtBusinessEmail.getText().toString().trim())
        ) {
            finishedInfo = false;
            showMessages(R.string.txt_msg_finish_info);
        } else if (!isValidEmail(edtBusinessEmail.getText().toString().trim())) {
            finishedInfo = false;
            showMessages(R.string.msg_valid_email);
        } else if (!isValidMobile(edtBusinessNumber.getText().toString().trim())) {
            finishedInfo = false;
            showMessages(R.string.msg_valid_phone);
        } else {
            businessName = edtBusinessName.getText().toString();
            businessAddress = edtBusinessAddress.getText().toString();
            businessEmail = edtBusinessEmail.getText().toString();
            businessMobileNumber = edtBusinessNumber.getText().toString();
            businessMobileCountry = countryCodePickerBusiness.getSelectedCountryCode();
            businessWebsite = edtBusinessWebsite.getText().toString();
            finishedInfo = true;
            viewPager.setCurrentItem(3);
        }
    }

    private void goInfo() {
        if (selectedType) {
            viewPager.setCurrentItem(2);
        } else {
            showMessages(R.string.txt_msg_select_portal);
        }
    }

    private void onProfileImage() {
        InsGallery.setCurrentTheme(InsGallery.THEME_STYLE_DARK);
        InsGallery.openGalleryForSingleImage(this, GlideEngine.createGlideEngine(), GlideCacheEngine.createCacheEngine(), new OnResultCallbackListener() {
            @Override
            public void onResult(List result) {
                if (result.size() > 0) {
                    LocalMedia media = (LocalMedia) result.get(0);
                    if (media.isCut() && !media.isCompressed()) {
                        vendorProfile = media.getCutPath();
                    } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                        vendorProfile = media.getCompressPath();
                    } else if (PictureMimeType.isHasVideo(media.getMimeType()) && !TextUtils.isEmpty(media.getCoverPath())) {
                        vendorProfile = media.getCoverPath();
                    } else {
                        vendorProfile = media.getPath();
                    }
                    Glide.with(activity)
                            .load(new File(vendorProfile)) // Uri of the picture
                            .into(imgProfile);
                }
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private void onLogin() {
        if (isMobile) {
            if (TextUtils.isEmpty(edtNumber.getText().toString()) || TextUtils.isEmpty(edtPassword.getText().toString())) {
                Toast.makeText(activity, R.string.msg_valid_number, Toast.LENGTH_LONG).show();
                return;
            } else {
                if (!isValidMobile(edtNumber.getText().toString())) {
                    Toast.makeText(activity, R.string.msg_valid_phone, Toast.LENGTH_LONG).show();
                    return;
                }
            }
        } else {
            if (TextUtils.isEmpty(edtEmail.getText().toString()) || TextUtils.isEmpty(edtPassword.getText().toString())) {
                Toast.makeText(activity, R.string.msg_empty_email, Toast.LENGTH_LONG).show();
                return;
            } else {
                if (!isValidEmail(edtEmail.getText().toString())) {
                    Toast.makeText(activity, R.string.msg_valid_email, Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
        LoginReq req = new LoginReq();
        req.setEmail(edtEmail.getText().toString().trim());
        req.setCountryCode(countryCodePicker.getSelectedCountryCode());
        req.setPhoneNumber(edtNumber.getText().toString());
        req.setPassword(edtPassword.getText().toString());
        req.setRegister_with(isMobile ? "Phone" : "Email");
        mViewModel.onLogin(req);
    }

    private void onCheckMobile(boolean is_mobile) {
        isMobile = is_mobile;
        if (isMobile) {
            lytMobile.setVisibility(View.VISIBLE);
            lytEmail.setVisibility(View.GONE);
            ckLoginMobile.setChecked(true);
            ckLoginEmail.setChecked(false);
        } else {
            lytMobile.setVisibility(View.GONE);
            lytEmail.setVisibility(View.VISIBLE);
            ckLoginMobile.setChecked(false);
            ckLoginEmail.setChecked(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserLoginComplete(@NonNull LoginRes res) {
        if (res.isStatus()) {
            if (res.getOwns().getSuppliers().size() != 0 && res.getOwns().getVendors().size() != 0) {
                showMessages(R.string.txt_msg_already_portal);
            } else {
                isLogin = true;
                if (res.getOwns().getVendors().size() != 0) {
                    rdVendor.setEnabled(false);
                }
                if (res.getOwns().getSuppliers().size() != 0) {
                    rdSupplier.setEnabled(false);
                }
                viewPager.setCurrentItem(1);
            }
        } else {
            showMessages(res.getMessage());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSignupComplete(@NonNull SignupRes res) {
        if (res.isStatus()) {
            App.initPortalInfo(res.getToken(), res.getUser(), res.getPortal(), edtPassword.getText().toString(), isVendor);
            Intent intent;
            if (isVendor) {
                intent = new Intent(activity, MainActivity.class);
            } else {
                intent = new Intent(activity, MainSupplierActivity.class);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            if (G.isNetworkAvailable(activity)) {
                showMessages(res.getMessage());
            } else {
                showMessages(R.string.msg_connection_fail);
            }
        }
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void addBottomDots(int cur_page) {
        switch (cur_page) {
            case 0:
                txtStep.setText(getString(R.string.txt_signup_step1));
                imgStep.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_add_store_step1));
                break;
            case 1:
                txtStep.setText(getString(R.string.txt_signup_step2));
                imgStep.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_add_store_step2));
                break;
            case 2:
                if (isVendor) {
                    txtStep.setText(getString(R.string.txt_signup_step3_0));
                } else {
                    txtStep.setText(getString(R.string.txt_signup_step3_1));
                }

                imgStep.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_add_store_step3));
                break;
            case 3:
                if (isVendor) {
                    txtStep.setText(getString(R.string.txt_signup_step4_0));
                    txtAgreement.setText(R.string.txt_vendor_agree_info);
                    txtTerm.setText(R.string.txt_vendor_terms);
                } else {
                    txtStep.setText(getString(R.string.txt_signup_step4_1));
                    txtAgreement.setText(R.string.txt_supplier_agree_info);
                    txtTerm.setText(R.string.txt_supplier_terms);
                }

                imgStep.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_add_store_step4));
                break;
        }
    }

    @OnClick({R.id.btBack})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                onBack();
                break;
        }
    }

    private void onBack() {
        int current = getItem(-1);
        if (current > -1) {
            viewPager.setCurrentItem(current);
        } else {
            finish();
        }
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

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            switch (position) {
                case 0:

                    break;
                case 1:
                    if (!isLogin) {
                        viewPager.setCurrentItem(0);
                        showMessages(R.string.txt_msg_login);
                    }
                    break;
                case 2:
                    if (!selectedType) {
                        viewPager.setCurrentItem(1);
                        showMessages(R.string.txt_msg_select_portal);
                    }
                    break;
                case 3:
                    if (!finishedInfo) {
                        viewPager.setCurrentItem(2);
                        showMessages(R.string.txt_msg_finish_info);

                    }
                    break;

            }

        }

        @Override
        public void onPageScrolled(int position, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };
}