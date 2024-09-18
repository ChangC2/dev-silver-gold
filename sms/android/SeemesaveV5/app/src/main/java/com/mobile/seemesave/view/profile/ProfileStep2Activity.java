package com.mobile.seemesave.view.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.instagram.InsGallery;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.mobile.seemesave.R;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.UserModel;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.profile.EditProfileViewModel;
import com.mobile.seemesave.widget.imagePicker.GlideCacheEngine;
import com.mobile.seemesave.widget.imagePicker.GlideEngine;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileStep2Activity extends BaseActivity {

    @BindView(R.id.imgBack)
    ImageView imgBack;

    @BindView(R.id.editFName)
    EditText editFName;

    @BindView(R.id.editLName)
    EditText editLName;

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

    @BindView(R.id.txtName)
    TextView txtName;

    public static final int REQUEST_CODE = 100;
    private String mediaPath1 = "";
    private String mediaPath2 = "";

    ProfileStep2Activity activity;
    private EditProfileViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        G.setLightFullScreen(this);
        mViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);
        setContentView(R.layout.activity_profile_step2);
        ButterKnife.bind(this);
        activity = this;
        initView();
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
        editFName.setText(G.user.getFirst_name());
        editLName.setText(G.user.getLast_name());
        try {
            G.user.setCountryCode(G.user.getCountryCode().replace("+", ""));
            editMobile.setText(G.user.getPhoneNumber());
        }catch (Exception e) {
            editMobile.setText("");
        }
    }
    private void onUpdate() {
        if (G.user.getRegister_with().equalsIgnoreCase("phone") && !G.isValidEmail(editEmail.getText().toString())){
            Toast.makeText(activity, R.string.missing_email, Toast.LENGTH_LONG).show();
            return;
        }

        if (G.user.getRegister_with().equalsIgnoreCase("email") && TextUtils.isEmpty(editMobile.getText())){
            Toast.makeText(activity, R.string.missing_phone, Toast.LENGTH_LONG).show();
            return;
        }

        showLoadingDialog();
        String token = G.pref.getString("token" , "");
        Builders.Any.B builder = Ion.with(this)
                .load("PUT", G.GetUserDetailUrl);
        builder.addHeader("Authorization", "Bearer " + token);

        if (!TextUtils.isEmpty(mediaPath1)){
            File file = new File(mediaPath1);
            builder.setMultipartFile("new_image_url", file);
        }

        if (!TextUtils.isEmpty(mediaPath2)){
            File file = new File(mediaPath2);
            builder.setMultipartFile("new_image_url_bg", file);
        }

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
                                    Toast.makeText(activity, R.string.save_success, Toast.LENGTH_LONG).show();
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
    private void onCoverImage() {
        InsGallery.setCurrentTheme(InsGallery.THEME_STYLE_DARK);
        InsGallery.openGalleryForSingleImage(this, GlideEngine.createGlideEngine(), GlideCacheEngine.createCacheEngine(), new OnResultCallbackListener() {
            @Override
            public void onResult(List result) {
                if (result.size() > 0){
                    LocalMedia media = (LocalMedia) result.get(0);
                    if (media.isCut() && !media.isCompressed()) {
                        mediaPath2 = media.getCutPath();
                    } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                        mediaPath2 = media.getCompressPath();
                    } else if (PictureMimeType.isHasVideo(media.getMimeType()) && !TextUtils.isEmpty(media.getCoverPath())) {
                        mediaPath2 = media.getCoverPath();
                    } else {
                        mediaPath2 = media.getPath();
                    }
                    Glide.with(ProfileStep2Activity.this)
                            .load(new File(mediaPath2)) // Uri of the picture
                            .into(imgBg);
                }
            }

            @Override
            public void onCancel() {
            }
        });
    }
    private void onProfileImage() {
        InsGallery.setCurrentTheme(InsGallery.THEME_STYLE_DARK);
        InsGallery.openGalleryForSingleImage(this, GlideEngine.createGlideEngine(), GlideCacheEngine.createCacheEngine(), new OnResultCallbackListener() {
            @Override
            public void onResult(List result) {
                if (result.size() > 0){
                    LocalMedia media = (LocalMedia) result.get(0);
                    if (media.isCut() && !media.isCompressed()) {
                        mediaPath1 = media.getCutPath();
                    } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                        mediaPath1 = media.getCompressPath();
                    } else if (PictureMimeType.isHasVideo(media.getMimeType()) && !TextUtils.isEmpty(media.getCoverPath())) {
                        mediaPath1 = media.getCoverPath();
                    } else {
                        mediaPath1 = media.getPath();
                    }
                    Glide.with(ProfileStep2Activity.this)
                            .load(new File(mediaPath1)) // Uri of the picture
                            .into(imgProfile);
                }
            }

            @Override
            public void onCancel() {
            }
        });
    }
    @OnClick({R.id.btnUpdate, R.id.img_profile, R.id.imgBack, R.id.imgPhotoEdit, R.id.btnEditCover})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.img_profile:
            case R.id.imgPhotoEdit:
                onProfileImage();
                break;
            case R.id.btnEditCover:
                onCoverImage();
                break;
            case R.id.btnUpdate:
                onUpdate();
                break;
        }
    }
}
