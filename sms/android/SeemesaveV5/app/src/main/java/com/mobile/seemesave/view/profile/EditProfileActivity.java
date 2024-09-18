package com.mobile.seemesave.view.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.mobile.seemesave.R;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.CurrencyModel;
import com.mobile.seemesave.model.common.TimeZoneModel;
import com.mobile.seemesave.model.common.UserModel;
import com.mobile.seemesave.service.GpsTracker;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.auth.LoginActivity;
import com.mobile.seemesave.view_model.profile.EditProfileViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileActivity extends BaseActivity {

    @BindView(R.id.imgBack)
    ImageView imgBack;

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

    @BindView(R.id.btnUpdate)
    LinearLayout btnUpdate;

    @BindView(R.id.img_profile)
    ImageView imgProfile;

    @BindView(R.id.imgBg)
    ImageView imgBg;

    @BindView(R.id.btnLogout)
    ImageView btnLogout;

    @BindView(R.id.btnDeleteAccount)
    LinearLayout btnDeleteAccount;

    @BindView(R.id.txtContentLocation)
    TextView txtContentLocation;

    @BindView(R.id.lytEmail)
    LinearLayout lytEmail;

    @BindView(R.id.lytMobile)
    LinearLayout lytMobile;

    @BindView(R.id.txtName)
    TextView txtName;

    @BindView(R.id.btnStep1)
    LinearLayout btnStep1;

    @BindView(R.id.btnStep2)
    LinearLayout btnStep2;

    @BindView(R.id.btnStep3)
    LinearLayout btnStep3;

    @BindView(R.id.btnStep4)
    LinearLayout btnStep4;

    @BindView(R.id.txtComplete)
    TextView txtComplete;
    @BindView(R.id.togPass)
    ToggleButton togPass;
    @BindView(R.id.togRePass)
    ToggleButton togRePass;


    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @BindView(R.id.switchGPS)
    Switch switchGPS;

    @BindView(R.id.country_picker)
    CountryCodePicker countryCodePicker;

    @BindView(R.id.spinnerTimezone)
    Spinner spinnerTimezone;

    @BindView(R.id.spinnerCurrency)
    Spinner spinnerCurrency;


    private boolean isCompletedStep1 = false;
    private boolean isCompletedStep2 = false;
    private boolean isCompletedStep3 = false;
    private boolean isCompletedStep4 = false;

    public static final int REQUEST_CODE = 100;
    private String mediaPath1 = "";
    private String mediaPath2 = "";

    private String selTimezone = "";
    private int selTimeOffset = 0;
    private int selCurrencyId = -1;

    ArrayList<TimeZoneModel> arrayTimezone = new ArrayList<>();
    ArrayList<CurrencyModel> arrayCurrency = new ArrayList<>();
    ArrayAdapter<TimeZoneModel> spinnerTimezoneAdapter;
    ArrayAdapter<CurrencyModel> spinnerCurrencyAdapter;

    private int mediaType = 0;

    EditProfileActivity activity;
    private EditProfileViewModel mViewModel;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        G.setLightFullScreen(this);
        mViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        activity = this;

        arrayTimezone.clear();
        arrayCurrency.clear();
        apiCallForGetBaseInfo(true);
        mViewModel.loadBaseInfo();

    }
    @Override
    public void onStart() {
        super.onStart();
        mViewModel.getIsBusy().observe(this, isBusy -> {
            if (isBusy){
                showLoadingDialog();
            }else {
                hideLoadingDialog();
            }
        });
        mViewModel.getCurrencyList().observe(this, list -> {
            arrayCurrency.clear();
            if (list.size() == 0) {
                return;
            } else {
                arrayCurrency.addAll(list);
                setCurrencySpinner();
            }
        });
        mViewModel.getTimezoneList().observe(this, list -> {
            arrayTimezone.clear();
            if (list.size() == 0) {
                return;
            } else {
                arrayTimezone.addAll(list);
                setTimezoneSpinner();
            }
        });
        initView();
    }
    @Override
    public void onStop() {
        super.onStop();
    }
    private void initView() {

        if (G.user.getAddress().isEmpty()){
            txtContentLocation.setText("Please set your address.");
        }else{
            txtContentLocation.setText(G.user.getAddress());
        }
        Glide.with(this)
                .load(G.user.getImage_url())
                .centerCrop()
                .placeholder(R.drawable.ic_avatar)
                .into(imgProfile);
        Glide.with(activity)
                .load(G.user.getBg_image_url())
                .centerCrop()
                .into(imgBg);
        txtName.setText(String.format(java.util.Locale.US,"%s %s", G.user.getFirst_name(), G.user.getLast_name()));
        editEmail.setText(G.user.getEmail());
        editFName.setText(G.user.getFirst_name());
        editLName.setText(G.user.getLast_name());
        try {
            countryCodePicker.setCountryForPhoneCode(Integer.valueOf(G.user.getCountryCode()));
        }catch (Exception e){
            countryCodePicker.setCountryForPhoneCode(27);
        }
        editMobile.setText(G.user.getPhoneNumber());

        if (G.user.getRegister_with().equalsIgnoreCase("email")) {
            editEmail.setEnabled(false);
        }else {
            countryCodePicker.setEnabled(false);
            editMobile.setEnabled(false);
        }
        switchGPS.setChecked(G.pref.getBoolean("use_gps", false));
        switchGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GpsTracker gpsTracker = new GpsTracker(activity);
                if (switchGPS.isChecked() && gpsTracker.canGetLocation() && gpsTracker.getLocation() != null){
                    G.location = gpsTracker.getLocation();
                    switchGPS.setChecked(true);
                    G.editor.putBoolean("use_gps", true);
                }else{
                    switchGPS.setChecked(false);
                    G.editor.putBoolean("use_gps", false);
                }
                G.editor.apply();
            }
        });

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
        setStepCount();
    }

    private void setStepCount(){
        int step = 0;
        Log.e("address:", G.user.getAddress());
        if (!TextUtils.isEmpty(G.user.getEmail())
                && !TextUtils.isEmpty(G.user.getAddress())
                && G.user.getAge() > 0
                && !TextUtils.isEmpty(G.user.getBio())
                && !TextUtils.isEmpty(G.user.getGender())){
            step++;
            isCompletedStep1 = true;
        }

        if (!TextUtils.isEmpty(G.user.getImage_url())
                && !TextUtils.isEmpty(G.user.getBg_image_url())){
            step++;
            isCompletedStep2 = true;
        }

        if (G.user.getFollowedStoreCount() > 0 && G.user.getLikedProductCount() > 0){
            step++;
            isCompletedStep3 = true;
        }

        if (G.user.getFriendCount() > 0 && G.pref.getBoolean("sync_contacts", false)){
            step++;
            isCompletedStep4 = true;
        }
        txtComplete.setText(String.format(Locale.US, "%d OF 4", step));
        findViewById(R.id.imgCheck1).setVisibility(isCompletedStep1 ? View.VISIBLE : View.INVISIBLE);
        findViewById(R.id.imgCheck2).setVisibility(isCompletedStep2 ? View.VISIBLE : View.INVISIBLE);
        findViewById(R.id.imgCheck3).setVisibility(isCompletedStep3 ? View.VISIBLE : View.INVISIBLE);
        findViewById(R.id.imgCheck4).setVisibility(isCompletedStep4 ? View.VISIBLE : View.INVISIBLE);
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

    private void setCurrencySpinner() {
        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selCurrencyId = arrayCurrency.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerCurrencyAdapter = new ArrayAdapter<CurrencyModel>(this, R.layout.custom_spinner, arrayCurrency);
        spinnerCurrencyAdapter.setDropDownViewResource(R.layout.custom_spinner_combo);
        spinnerCurrency.setAdapter(spinnerCurrencyAdapter);
        spinnerCurrency.setSelection(getCurrencyIndex());
    }

    private int getCurrencyIndex(){
        for(int i=0; i<arrayCurrency.size(); i++){
            if(arrayCurrency.get(i).getId() == selCurrencyId){
                return i;
            }
        }
        return -1;
    }
    private void onUpdate() {
        if (TextUtils.isEmpty(editFName.getText())
                || TextUtils.isEmpty(editLName.getText())){
            Toast.makeText(activity, R.string.msg_missing_userinfo, Toast.LENGTH_LONG).show();
            return;
        }
        if (G.user.getRegister_with().equalsIgnoreCase("phone") && !G.isValidEmail(editEmail.getText().toString())){
            Toast.makeText(activity, R.string.msg_missing_userinfo, Toast.LENGTH_LONG).show();
            return;
        }

        if (G.user.getRegister_with().equalsIgnoreCase("email") && TextUtils.isEmpty(editMobile.getText().toString())){
            Toast.makeText(activity, R.string.msg_missing_userinfo, Toast.LENGTH_LONG).show();
            return;
        }



        String token = G.getToken();
        Builders.Any.B builder = Ion.with(this)
                .load("PUT", G.GetUserDetailUrl);
        builder.addHeader("Authorization", token);

        builder.setMultipartParameter("first_name", editFName.getText().toString().trim())
                .setMultipartParameter("last_name", editLName.getText().toString().trim())
                .setMultipartParameter("currency", String.valueOf(selCurrencyId))
                .setMultipartParameter("time_zone", selTimezone)
                .setMultipartParameter("time_offset", String.valueOf(selTimeOffset))
                .setMultipartParameter("countryCode", countryCodePicker.getSelectedCountryCode())
                .setMultipartParameter("phoneNumber", editMobile.getText().toString().trim())
                .setMultipartParameter("email", editEmail.getText().toString().trim());

        if (!TextUtils.isEmpty(editNewPass.getText().toString())) {
            builder.setMultipartParameter("new_password", editNewPass.getText().toString().trim())
                    .setMultipartParameter("old_password", editOldPass.getText().toString().trim());

        }

        if (!TextUtils.isEmpty(mediaPath1)){
            File file = new File(mediaPath1);
            builder.setMultipartFile("new_image_url", file);
        }

        if (!TextUtils.isEmpty(mediaPath2)){
            File file = new File(mediaPath2);
            builder.setMultipartFile("new_image_url_bg", file);
        }
        showLoadingDialog();
        builder.asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        if (e != null){
                            Toast.makeText(activity, R.string.connection_fail, Toast.LENGTH_LONG).show();
                        }else{
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("status")){
                                    Gson gson = new Gson();
                                    G.user = gson.fromJson(jsonObject.getString("data"), UserModel.class);
                                    G.initUserInfo(G.user, G.getRegisterType(), editNewPass.getText().toString(), "", false);
                                    Toast.makeText(activity, getString(R.string.save_success), Toast.LENGTH_LONG).show();
                                    finish();
                                }else{
                                    Toast.makeText(activity, R.string.connection_fail, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException jsonException) {
                                Toast.makeText(activity, R.string.connection_fail, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
    private void apiCallForDeleteAccount() {
        String token = G.pref.getString("token" , "");
        showLoadingDialog();
        Ion.with(activity)
                .load("DELETE", G.DeleteUser)
                .addHeader("Authorization", "Bearer " + token)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        if (result == null) return;
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            if (jsonObject.getBoolean("status")){
                                G.logout();
                                Intent intent = new Intent(activity, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                });
    }
    private void onDeleteAccount() {
        View dialogView = activity.getLayoutInflater().inflate(R.layout.dlg_exit, null);

        final android.app.AlertDialog dlg = new android.app.AlertDialog.Builder(activity)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dlg.setCanceledOnTouchOutside(true);
        TextView ok = dialogView.findViewById(R.id.ok);
        TextView cancel = dialogView.findViewById(R.id.cancel);
        TextView confirm_txt = dialogView.findViewById(R.id.confirm_txt);
        confirm_txt.setText(R.string.txt_confirm_delete_account);
        cancel.setVisibility(View.VISIBLE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiCallForDeleteAccount();
                dlg.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        dlg.setCanceledOnTouchOutside(false);
        dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dlg.show();
    }
    private void onLogout() {
        View dialogView = activity.getLayoutInflater().inflate(R.layout.dlg_exit, null);

        final android.app.AlertDialog dlg = new android.app.AlertDialog.Builder(activity)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dlg.setCanceledOnTouchOutside(true);
        final TextView ok = dialogView.findViewById(R.id.ok);
        final TextView cancel = dialogView.findViewById(R.id.cancel);
        cancel.setVisibility(View.VISIBLE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                G.logout();
                Intent intent = new Intent(activity, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                dlg.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        dlg.setCanceledOnTouchOutside(false);
        dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dlg.show();
    }

    @OnClick({R.id.btnStep1, R.id.btnStep2, R.id.btnStep3, R.id.btnStep4, R.id.btnUpdate, R.id.btnDeleteAccount, R.id.imgBack, R.id.btnLogout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStep1:
                mViewModel.goStep1(activity);
                break;
            case R.id.btnStep2:
                mViewModel.goStep2(activity);
                break;
            case R.id.btnStep3:
                mViewModel.goStep3(activity);
                break;
            case R.id.btnStep4:
                mViewModel.goStep4(activity);
                break;
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnUpdate:
                onUpdate();
                break;
            case R.id.btnDeleteAccount:
                onDeleteAccount();
                break;
            case R.id.btnLogout:
                onLogout();
                break;
        }
    }

    void apiCallForGetBaseInfo(boolean showLoading) {
        String token = G.pref.getString("token" , "");
        Ion.with(this)
                .load(G.GetUserDetailUrl)
                .addHeader("Authorization", "Bearer " + token)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e == null){
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("status")){
                                    Gson gson = new Gson();
                                    G.user = gson.fromJson(jsonObject.getString("data"), UserModel.class);
                                    G.location = new Location(LocationManager.GPS_PROVIDER);
                                    G.location.setLatitude(G.user.getLatitude());
                                    G.location.setLongitude(G.user.getLongitude());
                                    G.initUserInfo(G.user, G.getRegisterType(), "", "", false);
                                    selCurrencyId = G.user.getCurrency().getId();
                                    selTimezone = G.user.getTime_zone();
                                    selTimeOffset = G.user.getTime_offset();
                                }
                            } catch (JSONException jsonException) {
                            }
                        }
                    }
                });
    }
}