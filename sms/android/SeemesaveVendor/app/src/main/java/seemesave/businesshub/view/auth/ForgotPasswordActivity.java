package seemesave.businesshub.view.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import seemesave.businesshub.R;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.model.res.LoginRes;
import seemesave.businesshub.view_model.auth.ForgotPasswordViewModel;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);
        setContentView(R.layout.activity_forgot_password);
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
    @OnClick({R.id.btBack, R.id.btSend, R.id.btnSignup})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.btSend:
                mViewModel.sendClicked(this, isMobile, edtEmail.getText().toString(), countryCodePicker.getSelectedCountryCode(), edtNumber.getText().toString());
                break;
            case R.id.btnSignup:
                mViewModel.gotoSignup(this);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCompleteLogin(LoginRes res) {
//        if (res.isResult()) {
//            Intent intent = new Intent(LoginActivity.this, ConnectDeviceActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        } else {
//            showMessages(res.getMessage());
//        }
    }
}
