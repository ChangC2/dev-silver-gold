package seemesave.businesshub.view.vendor.ads;

import static seemesave.businesshub.utils.TimeUtils.getDateDiff;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.FilePart;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.makeramen.roundedimageview.RoundedImageView;

import seemesave.businesshub.R;
import seemesave.businesshub.adapter.ImageSelectAdapter;
import seemesave.businesshub.adapter.OneSelectionAdapter;
import seemesave.businesshub.adapter.PlacesAutoCompleteAdapter;
import seemesave.businesshub.api.ads.AdsApi;
import seemesave.businesshub.api.brand.BrandApi;
import seemesave.businesshub.api.common.CommonApi;
import seemesave.businesshub.api.store.StoreApi;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.model.common.BrandModel;
import seemesave.businesshub.model.common.CurrencyModel;
import seemesave.businesshub.model.common.PostModel;
import seemesave.businesshub.model.common.StoreModel;
import seemesave.businesshub.model.res.BaseInfoRes;
import seemesave.businesshub.model.res.BrandListRes;
import seemesave.businesshub.model.res.CommonRes;
import seemesave.businesshub.model.res.PostRes;
import seemesave.businesshub.model.res.RateListRes;
import seemesave.businesshub.model.res.StoreListRes;
import seemesave.businesshub.sqlite.DatabaseQueryClass;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.utils.GsonUtils;
import seemesave.businesshub.widget.ActionSheet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreatePostActivity extends BaseActivity implements PlacesAutoCompleteAdapter.ClickListener {
    private CreatePostActivity activity;

    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.ckImage)
    CheckBox ckImage;
    @BindView(R.id.ckVideo)
    CheckBox ckVideo;


    @BindView(R.id.mediaRecyclerView)
    RecyclerView mediaRecyclerView;
    private ImageSelectAdapter imageAdapter;
    private ArrayList<String> imageList = new ArrayList<>();

    @BindView(R.id.imgVideo)
    RoundedImageView imgVideo;

    @BindView(R.id.edtTitle)
    TextInputEditText edtTitle;

    @BindView(R.id.edtDescription)
    TextInputEditText edtDescription;

    @BindView(R.id.edtLocation)
    TextInputEditText edtLocation;

    @BindView(R.id.edtTag)
    TextInputEditText edtTag;

    @BindView(R.id.edtStart)
    TextInputEditText edtStart;

    @BindView(R.id.edtEnd)
    TextInputEditText edtEnd;

    @BindView(R.id.places_recycler_view)
    RecyclerView placeRecyclerView;

    @BindView(R.id.tagRecyclerView)
    RecyclerView tagRecyclerView;

    @BindView(R.id.areaRecyclerView)
    RecyclerView areaRecyclerView;

    @BindView(R.id.txtStore)
    TextView txtStore;
    @BindView(R.id.spinnerStore)
    Spinner spinnerStore;
    private ArrayAdapter<StoreModel> storeAdapter;
    private ArrayAdapter<BrandModel> brandAdapter;

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


    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    final Calendar myCalendar = Calendar.getInstance();

    private ArrayList<StoreModel> storeList = new ArrayList<>();
    private int selStore = -1;
    private ArrayList<BrandModel> brandList = new ArrayList<>();
    private int selBrand = -1;
    private String filePath = "";

    private RateListRes rateInfo = new RateListRes();
    private double currency_rate = 1.0;
    private ArrayList<String> tagList = new ArrayList<>();
    private ArrayList<String> areaList = new ArrayList<>();
    private ArrayList<String> locationList = new ArrayList<>();
    private OneSelectionAdapter tagAdapter;
    private OneSelectionAdapter areaAdapter;
    private boolean isVideo = false;
    private String mediaPath = "";
    private String contentType = "image/jpeg";
    private String mType = "";

    private int post_id = -1;
    private String adsType = "VendorPost";
    private static final int REQUEST_CODE = 100;
    private boolean is_active = false;
    private int max_media_count = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        ButterKnife.bind(this);
        activity = this;
        post_id = getIntent().getIntExtra("id", -1);
        initView();
    }

    private void initView() {
        setUpPlaceAutoComplete();
        imageList.clear();
        storeList.clear();
        brandList.clear();
        tagList.clear();
        areaList.clear();
        locationList.clear();
        budgetList.clear();
        edtTag.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    tagList.add(edtTag.getText().toString().trim());
                    tagAdapter.setData(tagList);
                    edtTag.setText("");
                    G.hideSoftKeyboard(activity);
                    return true;
                }
                return false;
            }
        });
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
        ckImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMediaType(false);
            }
        });
        ckVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMediaType(true);
            }
        });
        setImageRecycler();
        setTagAdapter();
        setAreaAdapter();
        getLocalCurrencyList();
        if (App.getPortalType()) {
            StoreApi.getStoreList(0, 1000, "");
            txtStore.setText(getString(R.string.txt_store));
        } else {
            BrandApi.getBrandList(0, 1000, "");
            txtStore.setText(getString(R.string.txt_brand));
        }
        CommonApi.getRateList(selBudget);

        if (post_id != -1) {
            showLoadingDialog();
            AdsApi.getPostDetail(post_id);
        }

    }

    private void initData(PostModel model) {
        txtTitle.setText(getString(R.string.txt_edit_post));
        if (model.getSubMedia().size() > 0) {
            if (model.getSubMedia().get(0).getMedia_Type().equalsIgnoreCase("Video")) {
                Glide.with(activity)
                        .load(model.getSubMedia().get(0).getMedia()) // Uri of the picture
                        .into(imgVideo);
                setMediaType(true);
            } else {
                imageList.clear();
                for (int i = 0; i < model.getSubMedia().size(); i++) {
                    imageList.add(model.getSubMedia().get(i).getMedia());
                }
                imageAdapter.setData(imageList, false);
                setMediaType(false);
            }
        }
        is_active = model.isActive();
        edtTitle.setText(model.getTitle());
        edtDescription.setText(model.getDescription());
        edtStart.setText(model.getStart_date());
        edtEnd.setText(model.getEnd_date());
        if (App.getPortalType()) {
            selStore = model.getStore().getId();
            spinnerStore.setSelection(getStoreIndex());
        } else {
            selBrand = model.getBrand().getId();
            spinnerStore.setSelection(getBrandIndex());
        }

        for (int i = 0; i < model.getLocationList().size(); i++) {
            areaList.add(model.getLocationList().get(i).getFull_address());
            locationList.add(model.getLocationList().get(i).getPlace_id());
            areaAdapter.setData(areaList);
        }
        if (!TextUtils.isEmpty(model.getTagList())) {
            if (model.getTagList().contains(",")) {
                ArrayList<String> tagItems = new ArrayList<String>(Arrays.asList(model.getTagList().split(",")));
                tagList.addAll(tagItems);
            } else {
                tagList.add(model.getTagList());
            }
            tagAdapter.setData(tagList);
        }
        selBudget = model.getPaymentInfo().getCurrency().getIso();
        spinnerBudget.setSelection(getBudgetIndex());
        dailyAmount = model.getPaymentInfo().getToday_amount();
        ckImage.setEnabled(false);
        ckVideo.setEnabled(false);
        edtStart.setEnabled(false);
        edtEnd.setEnabled(false);
        spinnerStore.setEnabled(false);
        spinnerBudget.setEnabled(false);
        seekbar.setEnabled(false);

    }

    private void setMediaType(boolean type) {
        if (type) {
            ckImage.setChecked(false);
            ckVideo.setChecked(true);
            imgVideo.setVisibility(View.VISIBLE);
            mediaRecyclerView.setVisibility(View.GONE);
        } else {
            ckImage.setChecked(true);
            ckVideo.setChecked(false);
            imgVideo.setVisibility(View.GONE);
            mediaRecyclerView.setVisibility(View.VISIBLE);
        }
        isVideo = type;
    }

    private void setImageRecycler() {
        imageList.add("");
        imageAdapter = new ImageSelectAdapter(activity, imageList, new ImageSelectAdapter.ImageSelectRecyclerListener() {

            @Override
            public void onItemClicked(int pos, String model) {
                if (post_id == -1) {
                    for (int i = 0; i < imageList.size(); i++) {
                        if (imageList.get(i).equalsIgnoreCase(model)) {
                            imageList.remove(i);
                        }
                    }
                    imageAdapter.setData(imageList, true);
                }
            }

            @Override
            public void onItemAdd(int pos, String model) {
                onSelectPhoto();
            }
        });
        mediaRecyclerView.setAdapter(imageAdapter);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(activity, 4);
        mediaRecyclerView.setLayoutManager(linearLayoutManager);
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
            float rateVal = 0;
            if (App.getPortalType()) {
                rateVal = rateInfo.getRateList().getVendorpost();
            } else {
                rateVal = rateInfo.getRateList().getSupplierpost();
            }
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

    private void setTagAdapter() {
        tagAdapter = new OneSelectionAdapter(activity, tagList, new OneSelectionAdapter.OneSelectionRecyclerListener() {
            @Override
            public void onItemClicked(int pos, String model) {
                tagList.remove(pos);
                tagAdapter.setData(tagList);
            }

        });
        tagRecyclerView.setAdapter(tagAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        tagRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setAreaAdapter() {
        areaAdapter = new OneSelectionAdapter(activity, areaList, new OneSelectionAdapter.OneSelectionRecyclerListener() {
            @Override
            public void onItemClicked(int pos, String model) {
                areaList.remove(pos);
                locationList.remove(pos);
                areaAdapter.setData(areaList);
            }

        });
        areaRecyclerView.setAdapter(areaAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        areaRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                mAutoCompleteAdapter.getFilter().filter(s.toString());
                if (placeRecyclerView.getVisibility() == View.GONE) {
                    placeRecyclerView.setVisibility(View.VISIBLE);
                }
            } else {
                if (placeRecyclerView.getVisibility() == View.VISIBLE) {
                    placeRecyclerView.setVisibility(View.GONE);
                }
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    private void setUpPlaceAutoComplete() {
        Places.initialize(this, getResources().getString(R.string.google_maps_key));
        edtLocation.addTextChangedListener(filterTextWatcher);
        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this);
        placeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAutoCompleteAdapter.setClickListener(this);
        placeRecyclerView.setAdapter(mAutoCompleteAdapter);
        mAutoCompleteAdapter.notifyDataSetChanged();
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

    private void setStoreAdapter() {
        spinnerStore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selStore = storeList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        storeAdapter = new ArrayAdapter<StoreModel>(activity, R.layout.custom_spinner, storeList);
        storeAdapter.setDropDownViewResource(R.layout.custom_spinner_combo);
        spinnerStore.setAdapter(storeAdapter);
        spinnerStore.setSelection(getStoreIndex());
    }

    private int getStoreIndex() {
        for (int i = 0; i < storeList.size(); i++) {
            if (storeList.get(i).getId() == selStore) {
                return i;
            }
        }
        return -1;
    }

    private void setBrandAdapter() {
        spinnerStore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selBrand = brandList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        brandAdapter = new ArrayAdapter<BrandModel>(activity, R.layout.custom_spinner, brandList);
        brandAdapter.setDropDownViewResource(R.layout.custom_spinner_combo);
        spinnerStore.setAdapter(brandAdapter);
        spinnerStore.setSelection(getBrandIndex());
    }

    private int getBrandIndex() {
        for (int i = 0; i < brandList.size(); i++) {
            if (brandList.get(i).getId() == selBrand) {
                return i;
            }
        }
        return -1;
    }

    private void showDatePickDlg(String type) {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                String selMonth = String.valueOf(monthOfYear + 1);
                if (monthOfYear < 9) {
                    selMonth = "0" + selMonth;
                }
                String selDay = String.valueOf(dayOfMonth);
                if (dayOfMonth < 10) {
                    selDay = "0" + selDay;
                }
                String selDate = String.format(java.util.Locale.US, "%s-%s-%s", year, selMonth, selDay);
                if (type.equalsIgnoreCase("start")) {
                    edtStart.setText(selDate);
                } else {
                    edtEnd.setText(selDate);
                }
            }

        };
        new DatePickerDialog(activity, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void onSelectPhoto() {
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @SuppressLint("IntentReset")
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Options options = Options.init()
                                .setRequestCode(REQUEST_CODE)                                  //Request code for activity results
                                .setCount(8)                                                   //Number of images to restict selection count
                                .setFrontfacing(true)                                         //Front Facing camera on start
                                .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                                .setMode(Options.Mode.Picture)                                     //Option to select only pictures or videos or both
                                .setVideoDurationLimitinSeconds(60)                            //Duration for video recording
                                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT);    //Orientaion

                        Pix.start(activity, options);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(activity, "Camera permission is needed to take a photo.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Log.e("Dexter", "There was an error: " + error.toString());
                    }
                }).check();

    }

    private void onSelectVideo() {
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Options options = Options.init()
                                    .setRequestCode(REQUEST_CODE)                                  //Request code for activity results
                                    .setCount(1)                                                   //Number of images to restict selection count
                                    .setFrontfacing(true)                                         //Front Facing camera on start
                                    .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                                    .setMode(Options.Mode.Video)                                     //Option to select only pictures or videos or both
                                    .setVideoDurationLimitinSeconds(3600)                            //Duration for video recording
                                    .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT);    //Orientaion

                            Pix.start(activity, options);
                        } else {
                            Toast.makeText(activity, "Camera & Write permissions are needed to take a photo.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }


    private void onCreateAds() {
        if (isVideo) {
            if (TextUtils.isEmpty(mediaPath)) {
                Toast.makeText(activity, R.string.msg_select_video, Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            if (imageList.size() < 2) {
                Toast.makeText(activity, R.string.msg_select_image, Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (TextUtils.isEmpty(edtTitle.getText().toString()) || TextUtils.isEmpty(edtStart.getText().toString()) || TextUtils.isEmpty(edtEnd.getText().toString())) {
            Toast.makeText(activity, R.string.msg_valid_info, Toast.LENGTH_LONG).show();
            return;
        }
        if (locationList.size() == 0) {
            Toast.makeText(activity, R.string.msg_select_location, Toast.LENGTH_LONG).show();
            return;
        }
        if (App.getPortalType()) {
            if (selStore == -1) {
                Toast.makeText(activity, R.string.msg_select_store, Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            if (selBrand == -1) {
                Toast.makeText(activity, R.string.msg_select_brand, Toast.LENGTH_LONG).show();
                return;
            }
        }
        String start_date = edtStart.getText().toString();
        String end_date = edtEnd.getText().toString();
        @SuppressLint("SimpleDateFormat") int dateDifference = (int) getDateDiff(new SimpleDateFormat("yyyy-MM-dd"), start_date, end_date);
        if (dateDifference < 0) {
            Toast.makeText(activity, R.string.msg_select_end_date, Toast.LENGTH_LONG).show();
            return;
        }

        String place_id_list = locationList.get(0);
        for (int i = 1; i < locationList.size(); i++) {
            place_id_list += "," + locationList.get(i);
        }
        String title = edtTitle.getText().toString();
        String description = edtDescription.getText().toString();

        double total_money = dailyAmount * (dateDifference + 1);
        String tag_list = tagList.size() == 0 ? "" : tagList.get(0);
        if (tagList.size() > 1) {
            for (int i = 1; i < tagList.size(); i++) {
                tag_list += "," + tagList.get(i);
            }
        }

        ArrayList<Part> fileParts = new ArrayList<>();

        if (isVideo) {
            Part part = new FilePart("sub_media", new File(mediaPath));
            fileParts.add(part);
        } else {
            for (int i = 0; i < imageList.size() - 1; i++) {
                Part part = new FilePart("sub_media", new File(imageList.get(i)));
                fileParts.add(part);
            }
        }
        showLoadingDialog();

        Builders.Any.B builder = Ion.with(activity)
                .load("POST", G.CreatePost);
        builder.addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken());
        builder.setMultipartParameter("title", title)
                .setMultipartParameter("description", description)
                .setMultipartParameter("tag_list", tag_list)
                .setMultipartParameter("place_id_list", place_id_list)
                .setMultipartParameter("start_date", start_date)
                .setMultipartParameter("end_date", end_date);
        if (App.getPortalType()) {
            builder.setMultipartParameter("store_id", String.valueOf(selStore));
        } else {
            builder.setMultipartParameter("brand_id", String.valueOf(selBrand));
        }
        builder.setMultipartParameter("total_money", String.valueOf(total_money))
                .setMultipartParameter("currency", selBudget)
                .addMultipartParts(fileParts);

        builder
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
                                    Toast.makeText(activity, "Post " + getString(R.string.msg_success_created), Toast.LENGTH_LONG).show();
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
        if (locationList.size() == 0) {
            Toast.makeText(activity, R.string.msg_select_location, Toast.LENGTH_LONG).show();
            return;
        }
        String place_id_list = locationList.get(0);
        for (int i = 1; i < locationList.size(); i++) {
            place_id_list += "," + locationList.get(i);
        }
        String title = edtTitle.getText().toString();
        String description = edtDescription.getText().toString();
        String tag_list = tagList.size() == 0 ? "" : tagList.get(0);
        if (tagList.size() > 1) {
            for (int i = 1; i < tagList.size(); i++) {
                tag_list += "," + tagList.get(i);
            }
        }
        showLoadingDialog();
        Builders.Any.B builder = Ion.with(activity)
                .load("PUT", G.UpdatePost);
        builder.addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken());
        builder.setMultipartParameter("id", String.valueOf(post_id))
                .setMultipartParameter("title", title)
                .setMultipartParameter("description", description)
                .setMultipartParameter("tag_list", tag_list)
                .setMultipartParameter("place_id_list", place_id_list)
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
                                    Toast.makeText(activity, "Post " + getString(R.string.msg_updated_successfully), Toast.LENGTH_LONG).show();
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

    @OnClick({R.id.btBack, R.id.btnCreate, R.id.imgDelete, R.id.edtStart, R.id.edtEnd, R.id.imgVideo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCreate:
                if (post_id == -1) {
                    onCreateAds();
                } else {
                    onEditAds();
                }
                break;
            case R.id.edtStart:
                showDatePickDlg("start");
                break;
            case R.id.edtEnd:
                showDatePickDlg("end");
                break;
            case R.id.imgVideo:
                onSelectVideo();
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
    public void onSuccessStoreList(StoreListRes res) {
        if (res.isStatus()) {
            storeList.addAll(res.getData());
            setStoreAdapter();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessBrandList(BrandListRes res) {
        if (res.isStatus()) {
            brandList.addAll(res.getData());
            setBrandAdapter();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessCreateAds(CommonRes res) {
        hideLoadingDialog();
        if (res.isStatus()) {
            if (!TextUtils.isEmpty(res.getMessage())) {
                Toast.makeText(activity, res.getMessage(), Toast.LENGTH_LONG).show();
            }
            finish();
        } else {
            if (!TextUtils.isEmpty(res.getMessage())) {
                Toast.makeText(activity, res.getMessage(), Toast.LENGTH_LONG).show();
            }
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
    public void click(Place place) {
        areaList.add(place.getName());
        locationList.add(place.getId());
        areaAdapter.setData(areaList);
        placeRecyclerView.setVisibility(View.GONE);
        edtLocation.setText("");
        G.hideSoftKeyboard(activity);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            if (returnValue.size() > 0) {
                if (isVideo) {
                    mediaPath = returnValue.get(0);
                    Glide.with(activity)
                            .load(new File(mediaPath))
                            .centerCrop()
                            .placeholder(R.drawable.ic_me)
                            .into(imgVideo);
                } else {
                    for (int i = 0; i < imageList.size(); i++) {
                        if (TextUtils.isEmpty(imageList.get(i))) {
                            imageList.remove(i);
                        }
                    }
                    imageList.addAll(returnValue);
                    imageList.add("");
                    imageAdapter.setData(imageList, true);
                }
            }

        }
    }
}
