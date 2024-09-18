package com.mobile.seemesave.view.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.mobile.seemesave.R;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.UserModel;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.profile.EditProfileViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileStep1Activity extends BaseActivity {


    @BindView(R.id.imgBack)
    ImageView imgBack;

    @BindView(R.id.editEmail)
    EditText editEmail;

    @BindView(R.id.editMobile)
    EditText editMobile;

    @BindView(R.id.btnUpdate)
    LinearLayout btnUpdate;

    @BindView(R.id.img_profile)
    ImageView imgProfile;

    @BindView(R.id.imgBg)
    ImageView imgBg;

    @BindView(R.id.txtContentLocation)
    TextView txtContentLocation;

    @BindView(R.id.lytEmail)
    LinearLayout lytEmail;

    @BindView(R.id.lytMobile)
    LinearLayout lytMobile;

    @BindView(R.id.txtName)
    TextView txtName;

    @BindView(R.id.editAge)
    EditText editAge;

    @BindView(R.id.editBio)
    EditText editBio;

    @BindView(R.id.chkMale)
    CheckBox chkMale;

    @BindView(R.id.chkFemale)
    CheckBox chkFemale;

    @BindView(R.id.country_picker)
    CountryCodePicker countryCodePicker;

    ProfileStep1Activity activity;


    private EditProfileViewModel mViewModel;
    private int mediaType = 0;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        G.setLightFullScreen(this);
        mViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);
        setContentView(R.layout.activity_profile_step1);
        ButterKnife.bind(this);
        activity = this;

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
        txtName.setText(String.format(Locale.US,"%s %s", G.user.getFirst_name(), G.user.getLast_name()));
        editEmail.setText(G.user.getEmail());
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
        editAge.setText(String.valueOf(G.user.getAge()));
        editBio.setText(G.user.getBio());
        if (G.user.getGender().equalsIgnoreCase("female")){
            chkFemale.setChecked(true);
            chkMale.setChecked(false);
        }else{
            chkFemale.setChecked(false);
            chkMale.setChecked(true);
        }
    }

    private void onUpdate() {
        if (G.user.getRegister_with().equalsIgnoreCase("phone") && !G.isValidEmail(editEmail.getText().toString())){
            Toast.makeText(activity, R.string.msg_missing_userinfo, Toast.LENGTH_LONG).show();
            return;
        }

        if (G.user.getRegister_with().equalsIgnoreCase("email") && TextUtils.isEmpty(editMobile.getText())){
            Toast.makeText(activity, R.string.msg_missing_userinfo, Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(editAge.getText()) || TextUtils.isEmpty(editBio.getText()) || Integer.parseInt(editAge.getText().toString()) < 1){
            Toast.makeText(activity, R.string.msg_missing_userinfo, Toast.LENGTH_LONG).show();
            return;
        }
        showLoadingDialog();
        String token = G.pref.getString("token" , "");
        Builders.Any.B builder = Ion.with(this)
                .load("PUT", G.GetUserDetailUrl);
        builder.addHeader("Authorization", "Bearer " + token);

        builder.setMultipartParameter("countryCode", countryCodePicker.getSelectedCountryCode())
                .setMultipartParameter("age", editAge.getText().toString().trim())
                .setMultipartParameter("gender", chkMale.isChecked() ? "male" : "female")
                .setMultipartParameter("bio", editBio.getText().toString().trim())
                .setMultipartParameter("phoneNumber", editMobile.getText().toString().trim())
                .setMultipartParameter("email", editEmail.getText().toString().trim());

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
                                    Toast.makeText(activity, getString(R.string.update_success), Toast.LENGTH_LONG).show();
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
    @OnClick({R.id.btnUpdate, R.id.imgBack, R.id.chkMale, R.id.chkFemale})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnUpdate:
                onUpdate();
                break;
            case R.id.chkMale:
                chkMale.setChecked(true);
                chkFemale.setChecked(false);
                break;
            case R.id.chkFemale:
                chkFemale.setChecked(true);
                chkMale.setChecked(false);
                break;

        }
    }
}