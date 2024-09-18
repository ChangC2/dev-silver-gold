package seemesave.businesshub.view.supplier.menu;

import static seemesave.businesshub.utils.SystemUtils.isValidEmail;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import seemesave.businesshub.R;
import seemesave.businesshub.api.common.CommonApi;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.model.common.CurrencyModel;
import seemesave.businesshub.model.res.BaseInfoRes;
import seemesave.businesshub.model.res.PortalProfileInfoRes;
import seemesave.businesshub.sqlite.DatabaseQueryClass;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.utils.GsonUtils;

public class SupplierProfileActivity extends BaseActivity {

    private SupplierProfileActivity activity;

    @BindView(R.id.imgProfile)
    CircleImageView imgProfile;
    @BindView(R.id.imgPhotoEdit)
    ImageView imgPhotoEdit;
    @BindView(R.id.edtName)
    TextInputEditText edtName;
    @BindView(R.id.edtAddress)
    TextInputEditText edtAddress;
    @BindView(R.id.edtEmail)
    TextInputEditText edtEmail;
    @BindView(R.id.edtLink)
    TextInputEditText edtLink;
    @BindView(R.id.edtPhone)
    TextInputEditText edtPhone;

    @BindView(R.id.spinnerCurrency)
    Spinner spinnerCurrency;
    private ArrayAdapter<CurrencyModel> currencyAdapter;
    private ArrayList<CurrencyModel> currencyList = new ArrayList<>();
    private String selCurrency = "ZAR";

    private String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_profile);
        ButterKnife.bind(this);
        activity = this;
        showLoadingDialog();
        getLocalCurrencyList();
        CommonApi.getPortalProfile();
    }

    private void initData(PortalProfileInfoRes res) {
        Glide.with(activity)
                .load(res.getData().getLogo())
                .centerCrop()
                .placeholder(R.drawable.ic_store_default)
                .into(imgProfile);
        edtName.setText(res.getData().getName());
//        edtAddress.setText(res.get);
        edtEmail.setText(res.getData().getEmail());
        edtPhone.setText(res.getData().getContact_number());
        edtLink.setText(res.getData().getWebsite());
        selCurrency = res.getData().getCurrency().getIso();
        spinnerCurrency.setSelection(getCurrencyIndex());

    }
    private void getLocalCurrencyList() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(App.getUserID(), "BaseInfo", "");
            if (!TextUtils.isEmpty(data)) {
                BaseInfoRes localRes = GsonUtils.getInstance().fromJson(data, BaseInfoRes.class);
                currencyList.addAll(localRes.getCurrencyList());
                setCurrencyAdapter();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void setCurrencyAdapter() {
        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selCurrency = currencyList.get(position).getIso();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        currencyAdapter = new ArrayAdapter<CurrencyModel>(activity, R.layout.custom_spinner, currencyList);
        currencyAdapter.setDropDownViewResource(R.layout.custom_spinner_combo);
        spinnerCurrency.setAdapter(currencyAdapter);
        spinnerCurrency.setSelection(getCurrencyIndex());
    }
    private int getCurrencyIndex() {
        for (int i = 0; i < currencyList.size(); i++) {
            if (currencyList.get(i).getIso().equalsIgnoreCase(selCurrency)) {
                return i;
            }
        }
        return -1;
    }
    private void openCamera() {
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Options options = Options.init()
                                    .setRequestCode(100)                                  //Request code for activity results
                                    .setCount(1)                                                   //Number of images to restict selection count
                                    .setFrontfacing(true)                                         //Front Facing camera on start
                                    .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                                    .setMode(Options.Mode.Picture)                                     //Option to select only pictures or videos or both
                                    .setVideoDurationLimitinSeconds(60)                            //Duration for video recording
                                    .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT);    //Orientaion

                            Pix.start(activity, options);
                        } else {
                            Toast.makeText(activity, "Camera & Read, Write permissions are needed to take a media.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    private void onUpdate() {

        String name = edtName.getText().toString();
        String address = edtAddress.getText().toString();
        String email = edtEmail.getText().toString();
        String website = edtLink.getText().toString();
        String number = edtPhone.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(number) || TextUtils.isEmpty(email)) {
            Toast.makeText(activity, R.string.txt_msg_finish_info, Toast.LENGTH_LONG).show();
            return;
        }
        if (!isValidEmail(email)) {
            Toast.makeText(activity, R.string.msg_valid_email, Toast.LENGTH_LONG).show();
            return;
        }
        showLoadingDialog();
        Builders.Any.B builder = Ion.with(activity)
                .load("PUT", G.UpdateSupplierPortalProfile);
        builder.addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken());
        if (!TextUtils.isEmpty(imagePath)) {
            File file = new File(imagePath);
            builder.setMultipartFile("new_logo", file);
        }
        builder.setMultipartParameter("name", name)
                .setMultipartParameter("email", email)
                .setMultipartParameter("website", website)
                .setMultipartParameter("address", address)
                .setMultipartParameter("phone", number)
                .setMultipartParameter("currency", selCurrency);
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

    @OnClick({R.id.btBack, R.id.imgPhotoEdit, R.id.btnUpdate})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.imgPhotoEdit:
                openCamera();
                break;
            case R.id.btBack:
                finish();
                break;
            case R.id.btnUpdate:
                onUpdate();
                break;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onApiPortalProfileInfo(PortalProfileInfoRes res) {
        hideLoadingDialog();
        if (res.isStatus()) {
            if (res.getData() != null && res.getData().getBalance().size() > 0) {
                initData(res);
            }
            String data = new Gson().toJson(res, new TypeToken<PortalProfileInfoRes>() {
            }.getType());
            DatabaseQueryClass.getInstance().insertData(
                    App.getUserID(),
                    "PortalProfileInfo",
                    data,
                    "",
                    ""
            );
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            if (returnValue.size() > 0) {
                imagePath = returnValue.get(0);
                Glide.with(activity)
                        .load(new File(imagePath))
                        .centerCrop()
                        .into(imgProfile);

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