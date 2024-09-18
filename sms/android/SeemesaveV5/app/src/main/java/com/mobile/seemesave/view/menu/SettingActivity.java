package com.mobile.seemesave.view.menu;

import static com.mobile.seemesave.utils.G.ADDRESS_PICKER_REQUEST;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;
import com.mobile.seemesave.R;
import com.mobile.seemesave.api.user.UserApi;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.menu.SettingViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    private SettingViewModel mViewModel;
    private SettingActivity activity;

    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.swNotification)
    SwitchCompat swNotification;
    @BindView(R.id.swAudio)
    SwitchCompat swAudio;
    @BindView(R.id.swLocation)
    SwitchCompat swLocation;
    @BindView(R.id.swCamera)
    SwitchCompat swCamera;
    @BindView(R.id.swPhone)
    SwitchCompat swPhone;
    @BindView(R.id.txtAddress)
    TextView txtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingViewModel.class);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        activity = this;
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

    @Override
    protected void onResume() {
        super.onResume();
        String address = G.getAddress();
        txtAddress.setText(address);
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            swCamera.setChecked(true);
        } else {
            swCamera.setChecked(false);
        }
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            swLocation.setChecked(true);
        } else {
            swLocation.setChecked(false);
        }
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            swPhone.setChecked(true);
        } else {
            swPhone.setChecked(false);
        }
    }

    private void initView() {
        if (G.getAudioStatus().equalsIgnoreCase("true")) {
            swAudio.setChecked(true);
        } else {
            swAudio.setChecked(false);
        }
        swAudio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                G.saveAudioStatus(b);
            }
        });
        swCamera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    requestPermissions(
                            new String[]
                                    {Manifest.permission.CAMERA}
                            , 100);
                } else {
                    goToSetting();
                }

            }
        });
        swLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    requestPermissions(
                            new String[]
                                    {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}
                            , 200);
                } else {
                    goToSetting();
                }

            }
        });
        swPhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    requestPermissions(
                            new String[]
                                    {Manifest.permission.RECORD_AUDIO}
                            , 300);
                } else {
                    goToSetting();
                }

            }
        });
    }

    private void goToSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @OnClick({R.id.imgBack, R.id.li_edit})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.li_edit:
                Intent intent = new Intent(activity, LocationGoogleActivity.class);
                startActivityForResult(intent, ADDRESS_PICKER_REQUEST);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    swCamera.setChecked(true);
                } else {
                    swCamera.setChecked(false);
                }
            }
        }
        if (requestCode == 200) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    swLocation.setChecked(true);
                } else {
                    swLocation.setChecked(false);
                }
            }
        }
        if (requestCode == 300) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    swPhone.setChecked(true);
                } else {
                    swPhone.setChecked(false);
                }
            }

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra("fullAddress") != null) {
                    // String address = data.getStringExtra(MapUtility.ADDRESS);
                    double currentLatitude = data.getDoubleExtra("lat", 0.0);
                    double currentLongitude = data.getDoubleExtra("lon", 0.0);
                    G.user.setLatitude(currentLatitude);
                    G.user.setLongitude(currentLongitude);
                    G.user.setAddress(data.getStringExtra("fullAddress"));
                    G.saveUserAddress(currentLatitude, currentLongitude, data.getStringExtra("fullAddress"));
                    JsonObject json = new JsonObject();
                    json.addProperty("latitude", G.user.getLatitude());
                    json.addProperty("longitude", G.user.getLongitude());
                    UserApi.updateLocation(json);
                    Log.e("location_update:", "2");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}