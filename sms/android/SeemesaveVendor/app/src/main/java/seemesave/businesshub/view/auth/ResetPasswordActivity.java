package seemesave.businesshub.view.auth;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import seemesave.businesshub.R;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.model.res.LoginRes;
import seemesave.businesshub.view_model.auth.ResetPasswordViewModel;
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
    @OnClick({R.id.btBack, R.id.btnChange})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.btnChange:
                mViewModel.changeClicked(activity, edtPassword.getText().toString());
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
