package seemesave.businesshub.view.vendor.menu;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import seemesave.businesshub.R;
import seemesave.businesshub.api.common.CommonApi;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.model.common.BankDetailModel;
import seemesave.businesshub.model.res.BankDetailRes;
import seemesave.businesshub.sqlite.DatabaseQueryClass;
import seemesave.businesshub.utils.G;

public class BankDetailActivity extends BaseActivity {

    private BankDetailActivity activity;


    @BindView(R.id.edtName)
    TextInputEditText edtName;
    @BindView(R.id.edtNumber)
    TextInputEditText edtNumber;
    @BindView(R.id.edtBarcode)
    TextInputEditText edtBarcode;
    @BindView(R.id.edtBranch)
    TextInputEditText edtBranch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_detail);
        ButterKnife.bind(this);
        activity = this;
        showLoadingDialog();
        CommonApi.getBankDetail();
    }

    private void initData(BankDetailModel model) {
        edtName.setText(model.getAccount_name());
        edtNumber.setText(model.getAccount_number());
        edtBarcode.setText(model.getBranch_code());
        edtBranch.setText(model.getBranch_code());

    }

    private void onUpdate() {

        String name = edtName.getText().toString();
        String number = edtNumber.getText().toString();
        String barcode = edtBarcode.getText().toString();
        String branch = edtBranch.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(number) || TextUtils.isEmpty(barcode) || TextUtils.isEmpty(branch)) {
            Toast.makeText(activity, R.string.txt_msg_finish_info, Toast.LENGTH_LONG).show();
            return;
        }
        showLoadingDialog();
        Builders.Any.B builder = Ion.with(activity)
                .load("PUT", G.UpdateBankDetail);
        builder.addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken());
        builder.setBodyParameter("account_name", name)
                .setBodyParameter("account_number", number)
                .setBodyParameter("branch_code", barcode)
                .setBodyParameter("branch_name", branch);
        builder.asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        if (e != null) {
                            Toast.makeText(activity, R.string.msg_something_wrong, Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("status")) {
                                    Toast.makeText(activity, R.string.msg_updated_successfully, Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(activity, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException jsonException) {
                                Toast.makeText(activity, R.string.msg_something_wrong, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    @OnClick({R.id.btBack, R.id.btnUpdate})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.btnUpdate:
                onUpdate();
                break;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onApiDetailInfo(BankDetailRes res) {
        hideLoadingDialog();
        if (res.isStatus()) {
            if (res.getData() != null) {
                initData(res.getData());
                String data = new Gson().toJson(res, new TypeToken<BankDetailRes>() {
                }.getType());
                DatabaseQueryClass.getInstance().insertData(
                        App.getUserID(),
                        "BankDetail",
                        data,
                        "",
                        ""
                );
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}