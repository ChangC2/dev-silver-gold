package seemesave.businesshub.view.vendor.ads;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.instagram.InsGallery;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.makeramen.roundedimageview.RoundedImageView;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.PlacesAutoCompleteAdapter;
import seemesave.businesshub.api.ads.AdsApi;
import seemesave.businesshub.api.common.CommonApi;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.model.common.CurrencyModel;
import seemesave.businesshub.model.common.PostModel;
import seemesave.businesshub.model.res.BaseInfoRes;
import seemesave.businesshub.model.res.PostRes;
import seemesave.businesshub.model.res.RateListRes;
import seemesave.businesshub.sqlite.DatabaseQueryClass;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.utils.GsonUtils;
import seemesave.businesshub.widget.imagePicker.GlideCacheEngine;
import seemesave.businesshub.widget.imagePicker.GlideEngine;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateStoryActivity extends BaseActivity {
    private CreateStoryActivity activity;

    @BindView(R.id.txtTitle)
    TextView txtTitle;

    @BindView(R.id.imgCreate)
    RoundedImageView imgCreate;

    @BindView(R.id.imgPlay)
    ImageView imgPlay;

    @BindView(R.id.edtTitle)
    TextInputEditText edtTitle;

    @BindView(R.id.edtDescription)
    TextInputEditText edtDescription;

    @BindView(R.id.spinnerBudget)
    Spinner spinnerBudget;
    private ArrayAdapter<CurrencyModel> budgetAdapter;
    private ArrayList<CurrencyModel> budgetList = new ArrayList<>();
    private String selBudget = "ZAR";
    private double dailyAmount = 10;

    @BindView(R.id.txtPrice)
    TextView txtPrice;
    @BindView(R.id.txtPrice1)
    TextView txtPrice1;
    @BindView(R.id.txtPrice2)
    TextView txtPrice2;
    @BindView(R.id.txtPrice3)
    TextView txtPrice3;
    @BindView(R.id.txtPrice4)
    TextView txtPrice4;
    @BindView(R.id.seekbar)
    SeekBar seekbar;

    private RateListRes rateInfo = new RateListRes();
    private double currency_rate = 1.0;
    private boolean isVideo = false;
    private String mediaPath = "";
    private String contentType = "image/jpeg";
    private String mType = "";

    private int post_id = -1;
    private String adsType = "VendorStory";
    private static final int REQUEST_CODE = 100;
    private boolean is_active = false;
    private int max_media_count = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_story);
        ButterKnife.bind(this);
        activity = this;
        post_id = getIntent().getIntExtra("id", -1);
        initView();
    }

    private void initView() {
        budgetList.clear();
        seekbar.setMax(900);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                initProgress(progress);
            }
        });
        getLocalCurrencyList();
        CommonApi.getRateList(selBudget);

        if (post_id != -1) {
            showLoadingDialog();
            AdsApi.getStoryDetail(post_id);
        }

    }

    private void initData(PostModel model) {
        txtTitle.setText(getString(R.string.txt_edit_story));

        if (model.getMedia_Type().equalsIgnoreCase("Image")) {
            Glide.with(activity)
                    .load(model.getMedia()) // Uri of the picture
                    .into(imgCreate);
        }
        is_active = model.isActive();
        edtTitle.setText(model.getTitle());
        edtDescription.setText(model.getDescription());
        spinnerBudget.setSelection(getBudgetIndex());
        dailyAmount = model.getPaymentInfo().getToday_amount();
        spinnerBudget.setEnabled(false);
        seekbar.setEnabled(false);

    }



    private void initProgress(int progress) {
        int step = 1;
        double min = 10;
        if (progress <= 890) {
            dailyAmount = min + ((progress - 1) * step) + 1;
        } else {
            dailyAmount = progress * step;
        }
        dailyAmount = dailyAmount * currency_rate;
        setBudgetAmount();
    }

    private void setBudgetAmount() {
        txtPrice.setText(String.format(java.util.Locale.US, "%.2f", Float.valueOf(String.valueOf(dailyAmount))));
        if (rateInfo != null && rateInfo.isStatus()) {
            float rateVal = rateInfo.getRateList().getStory();
            float cpc = rateInfo.getCpcValue();
            float cpl = rateInfo.getCplValue();
            float cpm = rateInfo.getCpmValue();
            float cph = rateInfo.getCphValue();
            float cpr = rateInfo.getCprValue();
            double price1_min = dailyAmount / ((cpc + cpl + cpm / 50) * rateVal);
            double price1_max = dailyAmount / (cpm / 50 * rateVal);
            double price2_min = dailyAmount / ((cpc + cpl + cph + cpr) * rateVal);
            double price2_max = dailyAmount / (cpl * rateVal);
            if (price1_min > 100) {
                txtPrice1.setText(String.format(java.util.Locale.US, "Estimated %.2f K", price1_min / 1000));
            } else {
                txtPrice1.setText(String.format(java.util.Locale.US, "Estimated %.2f", price1_min));
            }
            if (price1_max > 100) {
                txtPrice2.setText(String.format(java.util.Locale.US, "%.2f K", price1_max / 1000));
            } else {
                txtPrice2.setText(String.format(java.util.Locale.US, "%.2f", price1_max));
            }
            txtPrice3.setText(String.format(java.util.Locale.US, "Estimated %.2f", price2_min));
            txtPrice4.setText(String.format(java.util.Locale.US, "%.2f", price2_max));

        }
    }


    private void getLocalCurrencyList() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(App.getUserID(), "BaseInfo", "");
            if (!TextUtils.isEmpty(data)) {
                BaseInfoRes localRes = GsonUtils.getInstance().fromJson(data, BaseInfoRes.class);
                budgetList.addAll(localRes.getCurrencyList());
                setBudgetAdapter();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void setBudgetAdapter() {
        spinnerBudget.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selBudget = budgetList.get(position).getIso();
                if (selBudget.equalsIgnoreCase("ZAR")) {
                    currency_rate = 1;
                    seekbar.setMax(900);
                } else {
                    currency_rate = 0.06;
                    seekbar.setMax(1000);
                }
                showLoadingDialog();
                CommonApi.getRateList(selBudget);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        budgetAdapter = new ArrayAdapter<CurrencyModel>(activity, R.layout.custom_spinner, budgetList);
        budgetAdapter.setDropDownViewResource(R.layout.custom_spinner_combo);
        spinnerBudget.setAdapter(budgetAdapter);
        spinnerBudget.setSelection(getBudgetIndex());
    }

    private int getBudgetIndex() {
        for (int i = 0; i < budgetList.size(); i++) {
            if (budgetList.get(i).getIso().equalsIgnoreCase(selBudget)) {
                return i;
            }
        }
        return -1;
    }



    private void onCreateAds() {
        if (TextUtils.isEmpty(mediaPath)) {
            Toast.makeText(activity, R.string.msg_select_media, Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edtTitle.getText().toString())) {
            Toast.makeText(activity, R.string.msg_valid_info, Toast.LENGTH_LONG).show();
            return;
        }

        String title = edtTitle.getText().toString();
        String description = edtDescription.getText().toString();

        double total_money = dailyAmount;


        showLoadingDialog();
        Ion.with(activity)
                .load(G.CreateStory)
                .addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken())
                .setMultipartFile("media", contentType, new File(mediaPath))
                .setMultipartParameter("title", title)
                .setMultipartParameter("description", description)
                .setMultipartParameter("total_money", String.valueOf(total_money))
                .setMultipartParameter("currency", selBudget)
                .asString()
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
                                    Toast.makeText(activity, "Story " + getString(R.string.msg_created_successfully), Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(activity, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException jsonException) {
                            }
                        }
                    }
                });

    }

    private void onEditAds() {
        if (TextUtils.isEmpty(edtTitle.getText().toString())) {
            Toast.makeText(activity, R.string.msg_valid_info, Toast.LENGTH_LONG).show();
            return;
        }
        String title = edtTitle.getText().toString();
        String description = edtDescription.getText().toString();
        showLoadingDialog();
        Builders.Any.B builder = Ion.with(activity)
                .load("PUT", G.UpdateStory);
        builder.addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken());
        builder.setMultipartParameter("id", String.valueOf(post_id))
                .setMultipartParameter("title", title)
                .setMultipartParameter("description", description)
                .setMultipartParameter("active", String.valueOf(is_active));
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
                                    Toast.makeText(activity, "Story " + getString(R.string.msg_updated_successfully), Toast.LENGTH_LONG).show();
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

    private void onSelectMedia() {
        InsGallery.setCurrentTheme(InsGallery.THEME_STYLE_DARK);
        InsGallery.openGalleryForPost(activity, GlideEngine.createGlideEngine(), GlideCacheEngine.createCacheEngine(), new OnResultCallbackListener() {
            @Override
            public void onResult(List result) {
                if (result.size() > 0) {
                    LocalMedia media = (LocalMedia) result.get(0);


                    if (media.isCut() && !media.isCompressed()) {
                        mediaPath = media.getCutPath();
                    } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                        mediaPath = media.getCompressPath();
                    } else if (PictureMimeType.isHasVideo(media.getMimeType())) {
                        if (TextUtils.isEmpty(media.getCompressPath()))
                            mediaPath = media.getPath();
                        else
                            mediaPath = media.getCompressPath();
                    } else {
                        mediaPath = media.getPath();
                    }

                    if (PictureMimeType.isHasVideo(media.getMimeType())) {
                        mType = "video";
                        contentType = "video/mp4";
                    } else {
                        mType = "image";
                        contentType = "image/jpeg";
                    }
                    imgPlay.setVisibility(mType.equalsIgnoreCase("video") ? View.VISIBLE : View.GONE);

                    Glide.with(activity)
                            .load(new File(mediaPath)) // Uri of the picture
                            .into(imgCreate);
                }
            }

            @Override
            public void onCancel() {
            }
        });
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
                            onSelectMedia();
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
    @OnClick({R.id.btBack, R.id.btnCreate, R.id.imgCreate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCreate:
                if (post_id == -1) {
                    onCreateAds();
                } else {
                    onEditAds();
                }
                break;
            case R.id.imgCreate:
                if (post_id == -1) {
                    openCamera();
                }
                break;
            case R.id.btBack:
                finish();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessRateInfo(RateListRes res) {
        hideLoadingDialog();
        if (res.isStatus()) {
            rateInfo.setRateList(res.getRateList());
            rateInfo.setCpcValue(res.getCpcValue());
            rateInfo.setCphValue(res.getCphValue());
            rateInfo.setCplValue(res.getCplValue());
            rateInfo.setCprValue(res.getCprValue());
            rateInfo.setCurrency(res.getCurrency());
            rateInfo.setCpmValue(res.getCpmValue());
            rateInfo.setStatus(true);
            setBudgetAmount();
        } else {
            rateInfo.setStatus(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessAdsDetail(PostRes res) {
        hideLoadingDialog();
        if (res.isStatus()) {
            initData(res.getData());
        } else {
            if (!TextUtils.isEmpty(res.getMessage())) {
                Toast.makeText(activity, res.getMessage(), Toast.LENGTH_LONG).show();    
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
