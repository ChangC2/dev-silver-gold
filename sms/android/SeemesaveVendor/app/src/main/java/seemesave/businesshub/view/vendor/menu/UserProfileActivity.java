package seemesave.businesshub.view.vendor.menu;

import static seemesave.businesshub.utils.SystemUtils.isValidEmail;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbb20.CountryCodePicker;
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

import de.hdodenhof.circleimageview.CircleImageView;
import seemesave.businesshub.R;
import seemesave.businesshub.api.common.CommonApi;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import seemesave.businesshub.model.common.TimeZoneModel;
import seemesave.businesshub.model.common.UserModel;
import seemesave.businesshub.model.res.BaseInfoRes;
import seemesave.businesshub.model.res.UserInfoRes;
import seemesave.businesshub.sqlite.DatabaseQueryClass;
import seemesave.businesshub.utils.DialogUtils;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.utils.GsonUtils;

public class UserProfileActivity extends BaseActivity {

    private UserProfileActivity activity;

    @BindView(R.id.imgUser)
    CircleImageView imgUser;

    @BindView(R.id.txtName)
    TextView txtName;

    @BindView(R.id.txtMobile)
    TextView txtMobile;

    @BindView(R.id.txtEmail)
    TextView txtEmail;

    @BindView(R.id.editFName)
    TextInputEditText editFName;

    @BindView(R.id.editLName)
    TextInputEditText editLName;

    @BindView(R.id.editEmail)
    TextInputEditText editEmail;

    @BindView(R.id.editMobile)
    TextInputEditText editMobile;

    @BindView(R.id.editOldPass)
    TextInputEditText editOldPass;

    @BindView(R.id.editNewPass)
    TextInputEditText editNewPass;

    @BindView(R.id.country_picker)
    CountryCodePicker countryCodePicker;

    @BindView(R.id.togPass)
    ToggleButton togPass;
    @BindView(R.id.togRePass)
    ToggleButton togRePass;

    @BindView(R.id.spinnerTimezone)
    Spinner spinnerTimezone;
    ArrayAdapter<TimeZoneModel> spinnerTimezoneAdapter;
    ArrayList<TimeZoneModel> arrayTimezone = new ArrayList<>();

    private String selTimezone = "";
    private int selTimeOffset = 0;

    private String imagePath = "";

    private UserModel userModel = new UserModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        activity = this;
        initView();
        showLoadingDialog();
        getLocalCurrencyList();
        CommonApi.getUserInfo();
    }
    private void initView() {
        togPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editOldPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else{
                    editOldPass.setInputType(129);
                }
            }
        });
        togRePass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editNewPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else{
                    editNewPass.setInputType(129);
                }
            }
        });
    }
    private void getLocalCurrencyList() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(App.getUserID(), "BaseInfo", "");
            if (!TextUtils.isEmpty(data)) {
                BaseInfoRes localRes = GsonUtils.getInstance().fromJson(data, BaseInfoRes.class);
                arrayTimezone.addAll(localRes.getTimezoneList());
                setTimezoneSpinner();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void setTimezoneSpinner() {
        spinnerTimezone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selTimezone = arrayTimezone.get(position).getTime_zone();
                selTimeOffset = arrayTimezone.get(position).getTime_offset();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerTimezoneAdapter = new ArrayAdapter<TimeZoneModel>(this, R.layout.custom_spinner, arrayTimezone);
        spinnerTimezoneAdapter.setDropDownViewResource(R.layout.custom_spinner_combo);
        spinnerTimezone.setAdapter(spinnerTimezoneAdapter);
        spinnerTimezone.setSelection(getTimezoneIndex());
    }

    private int getTimezoneIndex(){
        for(int i=0; i<arrayTimezone.size(); i++){
            if(arrayTimezone.get(i).getTime_zone().equalsIgnoreCase(selTimezone)){
                return i;
            }
        }
        return -1;
    }
    private void initData() {
        Glide.with(this)
                .load(userModel.getImage_url())
                .centerCrop()
                .placeholder(R.drawable.ic_avatar)
                .into(imgUser);

        txtName.setText(String.format(java.util.Locale.US,"%s %s", userModel.getFirst_name(), userModel.getLast_name()));
        txtEmail.setText(userModel.getEmail());
        txtMobile.setText(String.format(java.util.Locale.US,"+%s %s", userModel.getCountryCode(), userModel.getPhoneNumber()));
        editEmail.setText(userModel.getEmail());
        editFName.setText(userModel.getFirst_name());
        editLName.setText(userModel.getLast_name());
        try {
            countryCodePicker.setCountryForPhoneCode(Integer.valueOf(userModel.getCountryCode()));
        }catch (Exception e){
            countryCodePicker.setCountryForPhoneCode(27);
        }
        editMobile.setText(userModel.getPhoneNumber());

        if (userModel.getRegister_with().equalsIgnoreCase("Email")) {
            editEmail.setEnabled(false);
        }else {
            countryCodePicker.setEnabled(false);
            editMobile.setEnabled(false);
        }

        selTimezone = userModel.getTime_zone();
        selTimeOffset = userModel.getTime_offset();
        spinnerTimezone.setSelection(getTimezoneIndex());
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
        if (userModel == null) return;
        if (TextUtils.isEmpty(editFName.getText())
                || TextUtils.isEmpty(editLName.getText())){
            Toast.makeText(activity, R.string.msg_missing_userinfo, Toast.LENGTH_LONG).show();
            return;
        }
        if (userModel.getRegister_with().equalsIgnoreCase("Phone") && !isValidEmail(editEmail.getText().toString())){
            Toast.makeText(activity, R.string.msg_missing_userinfo, Toast.LENGTH_LONG).show();
            return;
        }

        if (userModel.getRegister_with().equalsIgnoreCase("Email") && TextUtils.isEmpty(editMobile.getText().toString())){
            Toast.makeText(activity, R.string.msg_missing_userinfo, Toast.LENGTH_LONG).show();
            return;
        }



        Builders.Any.B builder = Ion.with(this)
                .load("POST", G.UpdateUserProfile);
        builder.addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken());

        builder.setMultipartParameter("first_name", editFName.getText().toString().trim())
                .setMultipartParameter("last_name", editLName.getText().toString().trim())
                .setMultipartParameter("time_zone", selTimezone)
                .setMultipartParameter("time_offset", String.valueOf(selTimeOffset))
                .setMultipartParameter("countryCode", countryCodePicker.getSelectedCountryCode())
                .setMultipartParameter("phoneNumber", editMobile.getText().toString().trim())
                .setMultipartParameter("email", editEmail.getText().toString().trim());

        if (!TextUtils.isEmpty(editNewPass.getText().toString())) {
            builder.setMultipartParameter("new_password", editNewPass.getText().toString().trim())
                    .setMultipartParameter("old_password", editOldPass.getText().toString().trim());

        }

        if (!TextUtils.isEmpty(imagePath)){
            File file = new File(imagePath);
            builder.setMultipartFile("new_image_url", file);
        }

        showLoadingDialog();
        builder.asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        if (e != null){
                            Toast.makeText(activity, R.string.msg_something_wrong, Toast.LENGTH_LONG).show();
                        }else{
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("status")){
                                    Gson gson = new Gson();
                                    UserModel resUser = gson.fromJson(jsonObject.getString("data"), UserModel.class);
                                    App.initPortalInfo("", resUser, null, editNewPass.getText().toString(), true);
                                    Toast.makeText(activity, getString(R.string.msg_updated_successfully), Toast.LENGTH_LONG).show();
                                    finish();
                                }else{
                                    Toast.makeText(activity, R.string.msg_something_wrong, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException jsonException) {
                                Toast.makeText(activity, R.string.msg_something_wrong, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
    private void apiCallForDeleteAccount() {
//        showLoadingDialog();
//        Ion.with(activity)
//                .load("DELETE", G.DeleteUser)
//                .addHeader("Authorization", App.getToken())
//                .addHeader("Content-Language", App.getPortalToken())
//                .asString()
//                .setCallback(new FutureCallback<String>() {
//                    @Override
//                    public void onCompleted(Exception e, String result) {
//                        hideLoadingDialog();
//                        if (result == null) return;
//                        JSONObject jsonObject = null;
//                        try {
//                            jsonObject = new JSONObject(result);
//                            if (jsonObject.getBoolean("status")){
//                                G.logout();
//                                Intent intent = new Intent(activity, LoginActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                finish();
//                            }
//                        } catch (JSONException jsonException) {
//                            jsonException.printStackTrace();
//                        }
//                    }
//                });
    }
    private void onDeleteAccount() {
        DialogUtils.showConfirmDialogWithListener(activity, getString(R.string.txt_delete_confirmation), getString(R.string.txt_confirm_delete_account), getString(R.string.txt_yes), getString(R.string.txt_no),
                (dialog, which) -> apiCallForDeleteAccount(),
                (dialog, which) -> dialog.dismiss());
    }

    @OnClick({R.id.btBack, R.id.imgPhotoEdit, R.id.btnUpdate, R.id.btnDeleteAccount})
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
            case R.id.btnDeleteAccount:
                break;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onApiProfileInfo(UserInfoRes res) {
        hideLoadingDialog();
        if (res.isStatus()) {
            userModel = res.getUserInfo();
            initData();
            String data = new Gson().toJson(res, new TypeToken<UserInfoRes>() {
            }.getType());
            DatabaseQueryClass.getInstance().insertData(
                    App.getUserID(),
                    "UserProfileInfo",
                    data,
                    String.valueOf(App.getPortalType()),
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
                        .into(imgUser);

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