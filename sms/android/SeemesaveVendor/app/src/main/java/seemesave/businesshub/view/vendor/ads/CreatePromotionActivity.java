package seemesave.businesshub.view.vendor.ads;

import static seemesave.businesshub.utils.ParseUtil.getBuyGetProductsFromPromotion;
import static seemesave.businesshub.utils.ParseUtil.getComboProductsFromPromotion;
import static seemesave.businesshub.utils.ParseUtil.getSingleProductsFromPromotion;
import static seemesave.businesshub.utils.TimeUtils.getDateDiff;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
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
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;

import seemesave.businesshub.R;
import seemesave.businesshub.adapter.OneSelectionAdapter;
import seemesave.businesshub.adapter.PlacesAutoCompleteAdapter;
import seemesave.businesshub.adapter.ProductPromotionAdapter;
import seemesave.businesshub.api.ads.AdsApi;
import seemesave.businesshub.api.common.CommonApi;
import seemesave.businesshub.api.store.StoreApi;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.model.common.CurrencyModel;
import seemesave.businesshub.model.common.ProductModel;
import seemesave.businesshub.model.common.ProductOneModel;
import seemesave.businesshub.model.common.PromotionModel;
import seemesave.businesshub.model.common.StoreModel;
import seemesave.businesshub.model.res.BaseInfoRes;
import seemesave.businesshub.model.res.PromotionRes;
import seemesave.businesshub.model.res.RateListRes;
import seemesave.businesshub.model.res.StoreListRes;
import seemesave.businesshub.sqlite.DatabaseQueryClass;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.utils.GsonUtils;
import seemesave.businesshub.view.vendor.product.ProductBuyGetSelectActivity;
import seemesave.businesshub.view.vendor.product.ProductPromoteSelectActivity;
import seemesave.businesshub.view.vendor.product.ProductSelectActivity;
import seemesave.businesshub.widget.imagePicker.GlideCacheEngine;
import seemesave.businesshub.widget.imagePicker.GlideEngine;

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

public class CreatePromotionActivity extends BaseActivity implements PlacesAutoCompleteAdapter.ClickListener {
    private CreatePromotionActivity activity;

    @BindView(R.id.ckCollect)
    CheckBox ckCollect;
    @BindView(R.id.ckDeliver)
    CheckBox ckDeliver;

    @BindView(R.id.imgCreate)
    RoundedImageView imgCreate;

    @BindView(R.id.imgPlay)
    ImageView imgPlay;

    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.lytMedia)
    LinearLayout lytMedia;
    @BindView(R.id.lytTitle)
    TextInputLayout lytTitle;
    @BindView(R.id.lytDescription)
    RelativeLayout lytDescription;

    @BindView(R.id.edtTitle)
    TextInputEditText edtTitle;

    @BindView(R.id.edtDescription)
    TextInputEditText edtDescription;

    @BindView(R.id.lytLocation)
    LinearLayout lytLocation;
    @BindView(R.id.edtLocation)
    TextInputEditText edtLocation;

    @BindView(R.id.edtTag)
    TextInputEditText edtTag;

    @BindView(R.id.edtStart)
    TextInputEditText edtStart;

    @BindView(R.id.edtEnd)
    TextInputEditText edtEnd;

    @BindView(R.id.ckPrivate)
    CheckBox ckPrivate;

    @BindView(R.id.places_recycler_view)
    RecyclerView placeRecyclerView;

    @BindView(R.id.tagRecyclerView)
    RecyclerView tagRecyclerView;

    @BindView(R.id.areaRecyclerView)
    RecyclerView areaRecyclerView;


    @BindView(R.id.spinnerCurrency)
    Spinner spinnerCurrency;
    private ArrayAdapter<CurrencyModel> currencyAdapter;
    private ArrayList<CurrencyModel> currencyList = new ArrayList<>();
    private String selCurrency = "ZAR";

    @BindView(R.id.txtStoreEmpty)
    TextView txtStoreEmpty;
    @BindView(R.id.spinnerStore)
    Spinner spinnerStore;
    private ArrayAdapter<StoreModel> storeAdapter;

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

    @BindView(R.id.lytSingle)
    LinearLayout lytSingle;
    @BindView(R.id.lytCombo)
    LinearLayout lytCombo;
    @BindView(R.id.lytBuy)
    LinearLayout lytBuy;

    @BindView(R.id.singleView)
    RecyclerView singleView;
    @BindView(R.id.singleIndicator)
    IndefinitePagerIndicator singleIndicator;
    private ProductPromotionAdapter singleAdapter;
    private ArrayList<ProductOneModel> singleList = new ArrayList<>();

    @BindView(R.id.comboView)
    RecyclerView comboView;
    @BindView(R.id.comboIndicator)
    IndefinitePagerIndicator comboIndicator;
    private ProductPromotionAdapter comboAdapter;
    private ArrayList<ProductOneModel> comboList = new ArrayList<>();


    @BindView(R.id.buyView)
    RecyclerView buyView;
    @BindView(R.id.buyIndicator)
    IndefinitePagerIndicator buyIndicator;
    private ProductPromotionAdapter buyAdapter;
    private ArrayList<ProductOneModel> buyList = new ArrayList<>();

    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    final Calendar myCalendar = Calendar.getInstance();

    private ArrayList<StoreModel> storeList = new ArrayList<>();
    private ArrayList<StoreModel> storeAllList = new ArrayList<>();
    private ArrayList<StoreModel> storeBothList = new ArrayList<>();
    private ArrayList<StoreModel> storeCollectList = new ArrayList<>();
    private ArrayList<StoreModel> storeDeliverList = new ArrayList<>();
    private int selStore = -1;
    private String filePath = "";

    private RateListRes rateInfo = new RateListRes();
    private double currency_rate = 1.0;
    private ArrayList<String> tagList = new ArrayList<>();
    private ArrayList<String> areaList = new ArrayList<>();
    private ArrayList<String> locationList = new ArrayList<>();
    private OneSelectionAdapter tagAdapter;
    private OneSelectionAdapter areaAdapter;
    private boolean isDeliver = false;
    private String mediaPath = "";
    private String contentType = "image/jpeg";
    private String mType = "";

    private int promotion_id = -1;
    private String adsType = "VendorPromotion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_promotion);
        ButterKnife.bind(this);
        activity = this;
        promotion_id = getIntent().getIntExtra("id", -1);
        adsType = getIntent().getStringExtra("adsType");
        initView();
    }

    private void initView() {
        setUpPlaceAutoComplete();
        spinnerStore.setVisibility(View.GONE);
        txtStoreEmpty.setVisibility(View.VISIBLE);
        currencyList.clear();
        storeList.clear();
        storeCollectList.clear();
        storeDeliverList.clear();
        tagList.clear();
        areaList.clear();
        locationList.clear();
        singleList.clear();
        comboList.clear();
        buyList.clear();
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
        ckCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ckDeliver.isChecked() && !ckCollect.isChecked()) {
                    return;
                }
                setCollectType(false, false);
            }
        });
        ckDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ckDeliver.isChecked() && !ckCollect.isChecked()) {
                    return;
                }
                setCollectType(true, false);
            }
        });

        switch (adsType) {
            case "CantMissDeal":
                if (promotion_id == -1) {
                    txtTitle.setText(getString(R.string.txt_create_cantmissdeal));
                } else {
                    txtTitle.setText(getString(R.string.txt_edit_cantmissdeal));
                }
                ckPrivate.setVisibility(View.GONE);
                break;
            case "BestDeal":
                if (promotion_id == -1) {
                    txtTitle.setText(getString(R.string.txt_create_bestdeal));
                } else {
                    txtTitle.setText(getString(R.string.txt_edit_bestdeal));
                }
                lytMedia.setVisibility(View.GONE);
                lytTitle.setVisibility(View.GONE);
                lytDescription.setVisibility(View.GONE);
                lytLocation.setVisibility(View.GONE);
                ckPrivate.setVisibility(View.GONE);
                break;
            case "ExclusiveDeal":
                if (promotion_id == -1) {
                    txtTitle.setText(getString(R.string.txt_create_exclusivedeal));
                } else {
                    txtTitle.setText(getString(R.string.txt_edit_exclusivedeal));
                }
                lytLocation.setVisibility(View.GONE);
                ckPrivate.setVisibility(View.GONE);
                break;
            case "VendorPromotion":
                if (promotion_id == -1) {
                    txtTitle.setText(getString(R.string.txt_create_promotion));
                } else {
                    txtTitle.setText(getString(R.string.txt_edit_promotion));
                }
                break;
        }
        setTagAdapter();
        setAreaAdapter();
        setSingleAdapter();
        setComboAdapter();
        setBuyAdapter();
        getLocalCurrencyList();
        StoreApi.getStoreList(0, 1000, "");
        CommonApi.getRateList(selBudget);


    }

    private void initData(PromotionModel model) {

        if (model.getSubMedia().size() > 0) {
            if (model.getSubMedia().get(0).getMedia_Type().equalsIgnoreCase("Image")) {
                Glide.with(activity)
                        .load(model.getSubMedia().get(0).getMedia()) // Uri of the picture
                        .into(imgCreate);
            }
        }
        edtTitle.setText(model.getTitle());
        edtDescription.setText(model.getDescription());
        edtStart.setText(model.getStart_date());
        edtEnd.setText(model.getEnd_date());
        selStore = model.getStores().get(0).getId();
        selCurrency = model.getPaymentInfo().getCurrency().getIso();
        ckPrivate.setChecked(model.isPrivate());
        setCollectType(model.isIs_deliver(), true);
        spinnerStore.setSelection(getStoreIndex());

        spinnerCurrency.setSelection(getCurrencyIndex());
        for (int i = 0; i < model.getLocationList().size(); i++) {
            areaList.add(model.getLocationList().get(i).getFull_address());
            locationList.add(model.getLocationList().get(i).getPlace_id());
            areaAdapter.setData(areaList);
        }
        if (!TextUtils.isEmpty(model.getTagList())) {
            ArrayList<String> tagItems = new ArrayList<String>(Arrays.asList(model.getTagList().split(",")));
            tagList.addAll(tagItems);
            tagAdapter.setData(tagList);
        }

        if (model.getSingleProducts().size() == 0) {
            lytSingle.setVisibility(View.GONE);
        } else {
            singleList.addAll(getSingleProductsFromPromotion(model.getSingleProducts()));
            singleAdapter.setData(singleList);
            lytSingle.setVisibility(View.VISIBLE);
        }
        if (model.getComboDeals().size() == 0) {
            lytCombo.setVisibility(View.GONE);
        } else {
            comboList.addAll(getComboProductsFromPromotion(model.getComboDeals()));
            comboAdapter.setData(comboList);
            lytCombo.setVisibility(View.VISIBLE);
        }
        if (model.getBuy1Get1FreeDeals().size() == 0) {
            lytBuy.setVisibility(View.GONE);
        } else {
            buyList.addAll(getBuyGetProductsFromPromotion(model.getBuy1Get1FreeDeals()));
            buyAdapter.setData(buyList);
            lytBuy.setVisibility(View.VISIBLE);
        }

        ckCollect.setEnabled(false);
        ckDeliver.setEnabled(false);
        edtStart.setEnabled(false);
        edtEnd.setEnabled(false);
        spinnerStore.setEnabled(false);
        spinnerCurrency.setEnabled(false);
        spinnerBudget.setEnabled(false);
        ckPrivate.setEnabled(false);
        seekbar.setEnabled(false);
    }

    private void setCollectType(boolean type, boolean init) {
        if (type) {
            if (!adsType.equalsIgnoreCase("VendorPromotion")) {
                ckCollect.setChecked(false);
                ckDeliver.setChecked(true);
            }
        } else {
            if (!adsType.equalsIgnoreCase("VendorPromotion")) {
                ckCollect.setChecked(true);
                ckDeliver.setChecked(false);
            }
        }
        setStoreList();

        isDeliver = type;
        if (!init) {
            Toast.makeText(activity, R.string.msg_collect_select_clear, Toast.LENGTH_LONG).show();
            selStore = -1;
            singleList.clear();
            comboList.clear();
            buyList.clear();
            singleAdapter.setData(singleList);
            comboAdapter.setData(comboList);
            buyAdapter.setData(buyList);
            spinnerStore.setVisibility(View.GONE);
            txtStoreEmpty.setVisibility(View.VISIBLE);
        }
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

    private void setStoreList() {
        if (storeAdapter != null) {
            storeList.clear();
            if (ckCollect.isChecked() && ckDeliver.isChecked()) {
                for (int i = 0; i < storeBothList.size(); i++) {
                    if (storeBothList.get(i).getCurrency().getIso().equalsIgnoreCase(selCurrency)) {
                        storeList.add(storeBothList.get(i));
                    }
                }
            } else if (ckCollect.isChecked()) {
                for (int i = 0; i < storeCollectList.size(); i++) {
                    if (storeCollectList.get(i).getCurrency().getIso().equalsIgnoreCase(selCurrency)) {
                        storeList.add(storeCollectList.get(i));
                    }
                }
            } else if (ckDeliver.isChecked()) {
                for (int i = 0; i < storeDeliverList.size(); i++) {
                    if (storeDeliverList.get(i).getCurrency().getIso().equalsIgnoreCase(selCurrency)) {
                        storeList.add(storeDeliverList.get(i));
                    }
                }
            }
            storeAdapter.notifyDataSetChanged();
        }
    }
    private void setBudgetAmount() {
        txtPrice.setText(String.format(java.util.Locale.US, "%.2f", Float.valueOf(String.valueOf(dailyAmount))));
        if (rateInfo != null && rateInfo.isStatus()) {
            float rateVal = 0;
            if (adsType.equalsIgnoreCase("VendorPromotion")) {
                rateVal = rateInfo.getRateList().getVendorpromotion();
            } else if (adsType.equalsIgnoreCase("ExclusiveDeal")) {
                rateVal = rateInfo.getRateList().getExclusivedeal();
            } else if (adsType.equalsIgnoreCase("BestDeal")) {
                rateVal = rateInfo.getRateList().getBestdeal();
            } else if (adsType.equalsIgnoreCase("CantMissDeal")) {
                rateVal = rateInfo.getRateList().getCantmissdeal();
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

    private void setSingleAdapter() {
        singleAdapter = new ProductPromotionAdapter(activity, singleList, promotion_id == -1 ? false : true, new ProductPromotionAdapter.ProductPromotionRecyclerListener() {
            @Override
            public void onItemClicked(int pos, ProductOneModel model) {
                singleList.remove(pos);
                singleAdapter.setData(singleList);

            }
        });
        singleView.setAdapter(singleAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        singleView.setLayoutManager(linearLayoutManager);
        singleIndicator.attachToRecyclerView(singleView);
    }

    private void setComboAdapter() {
        comboAdapter = new ProductPromotionAdapter(activity, comboList, promotion_id == -1 ? false : true, new ProductPromotionAdapter.ProductPromotionRecyclerListener() {
            @Override
            public void onItemClicked(int pos, ProductOneModel model) {
                comboList.remove(pos);
                comboAdapter.setData(comboList);
                comboIndicator.refreshDrawableState();
            }
        });
        comboView.setAdapter(comboAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        comboView.setLayoutManager(linearLayoutManager);
        comboIndicator.attachToRecyclerView(comboView);
    }

    private void setBuyAdapter() {
        buyAdapter = new ProductPromotionAdapter(activity, buyList, promotion_id == -1 ? false : true, new ProductPromotionAdapter.ProductPromotionRecyclerListener() {
            @Override
            public void onItemClicked(int pos, ProductOneModel model) {
                buyList.remove(pos);
                buyAdapter.setData(buyList);
                buyIndicator.refreshDrawableState();
            }
        });
        buyView.setAdapter(buyAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        buyView.setLayoutManager(linearLayoutManager);
        buyIndicator.attachToRecyclerView(buyView);
    }

    private void getLocalCurrencyList() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(App.getUserID(), "BaseInfo", "");
            if (!TextUtils.isEmpty(data)) {
                BaseInfoRes localRes = GsonUtils.getInstance().fromJson(data, BaseInfoRes.class);
                currencyList.addAll(localRes.getCurrencyList());
                budgetList.addAll(localRes.getCurrencyList());
                setCurrencyAdapter();
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

    private void setCurrencyAdapter() {
        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selCurrency = currencyList.get(position).getIso();
                setStoreList();

                selStore = -1;
                singleList.clear();
                comboList.clear();
                buyList.clear();
                singleAdapter.setData(singleList);
                comboAdapter.setData(comboList);
                buyAdapter.setData(buyList);
                spinnerStore.setVisibility(View.GONE);
                txtStoreEmpty.setVisibility(View.VISIBLE);
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
                txtStoreEmpty.setVisibility(View.GONE);
                spinnerStore.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selStore = -1;
                txtStoreEmpty.setVisibility(View.VISIBLE);
                spinnerStore.setVisibility(View.GONE);
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

    private void onSelectProduct(String type) {
        if (selStore == -1) {
            Toast.makeText(activity, R.string.msg_select_store, Toast.LENGTH_LONG).show();
            return;
        }
        Intent i;
        if (type.equalsIgnoreCase("buyget")) {
            i = new Intent(activity, ProductBuyGetSelectActivity.class);
            i.putExtra("type", type);
        } else if (type.equalsIgnoreCase("promote")) {
            int selType = 1;
            switch (adsType) {
                case "BestDeal":
                    selType = 3;
                    break;
                case "ExclusiveDeal":
                    selType = 4;
                    break;
                case "CantMissDeal":
                    selType = 2;
                    break;
                case "VendorPromotion":
                    selType = 1;
                    break;
            }
            i = new Intent(activity, ProductPromoteSelectActivity.class);
            i.putExtra("store", selStore);
            i.putExtra("type", selType);
        } else {
            i = new Intent(activity, ProductSelectActivity.class);
            i.putExtra("type", type);
        }
        startActivityIfNeeded(i, 500);
    }

    private void onCreateAds() {
        String place_id_list = "";
        switch (adsType) {
            case "BestDeal":
                if (TextUtils.isEmpty(edtStart.getText().toString()) || TextUtils.isEmpty(edtEnd.getText().toString())) {
                    Toast.makeText(activity, R.string.msg_valid_info, Toast.LENGTH_LONG).show();
                    return;
                }
                break;
            case "ExclusiveDeal":
                if (TextUtils.isEmpty(edtStart.getText().toString()) || TextUtils.isEmpty(edtEnd.getText().toString())) {
                    Toast.makeText(activity, R.string.msg_valid_info, Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(mediaPath)) {
                    Toast.makeText(activity, R.string.msg_select_media, Toast.LENGTH_LONG).show();
                    return;
                }
                break;
            case "CantMissDeal":
            case "VendorPromotion":
                if (TextUtils.isEmpty(edtTitle.getText().toString()) || TextUtils.isEmpty(edtStart.getText().toString()) || TextUtils.isEmpty(edtEnd.getText().toString())) {
                    Toast.makeText(activity, R.string.msg_valid_info, Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(mediaPath)) {
                    Toast.makeText(activity, R.string.msg_select_media, Toast.LENGTH_LONG).show();
                    return;
                }
                if (locationList.size() == 0) {
                    Toast.makeText(activity, R.string.msg_select_location, Toast.LENGTH_LONG).show();
                    return;
                }
                place_id_list = locationList.get(0);
                for (int i = 1; i < locationList.size(); i++) {
                    place_id_list += "," + locationList.get(i);
                }

                break;
        }


        String start_date = edtStart.getText().toString();
        String end_date = edtEnd.getText().toString();
        @SuppressLint("SimpleDateFormat") int dateDifference = (int) getDateDiff(new SimpleDateFormat("yyyy-MM-dd"), start_date, end_date);
        if (dateDifference < 0) {
            Toast.makeText(activity, R.string.msg_select_end_date, Toast.LENGTH_LONG).show();
            return;
        }

        if (singleList.size() == 0 && comboList.size() == 0 && buyList.size() == 0) {
            Toast.makeText(activity, R.string.msg_select_product, Toast.LENGTH_LONG).show();
            return;
        }

        String title = edtTitle.getText().toString();
        String description = edtDescription.getText().toString();

        double total_money = dailyAmount * (dateDifference + 1);
        boolean is_private = ckPrivate.isChecked();
        boolean need_data = true;
        String tag_list = tagList.size() == 0 ? "" : tagList.get(0);
        if (tagList.size() > 1) {
            for (int i = 1; i < tagList.size(); i++) {
                tag_list += "," + tagList.get(i);
            }
        }
        String singleString = "", pay_singleString = "";
        for (int i = 0; i < singleList.size(); i++) {
            ProductOneModel singleProduct = singleList.get(i);
            if (singleProduct.getPay_to_promote() == 0) {
                //barcode,quantity;variant1,variant2:price,stock/
                singleString += singleProduct.getBarcode() + "," + "1" + ";" + singleProduct.getVariant_string() + ":" + singleProduct.getPrice() + "," + singleProduct.getStock() + "/";
            } else if (singleProduct.getPay_to_promote() == 1) {
                pay_singleString += singleProduct.getProduct_id() + ":" + singleProduct.getPrice() + "," + singleProduct.getStock() + "/";
            }
        }

        String comboString = "", pay_comboString = "";
        for (int i = 0; i < comboList.size(); i++) {
            ProductOneModel comboProduct = comboList.get(i);
            if (comboProduct.getPay_to_promote() == 0) {
                //barcode,quantity;variant1,variant2&barcode,quantity;variant1,variant2:price,stock/
                String oneComboString = "";
                oneComboString = comboProduct.getComboDeals().get(0).getProductDetail().getBarcode() + "," + comboProduct.getComboDeals().get(0).getQuantity() + ";" + comboProduct.getComboDeals().get(0).getProductDetail().getVariant_string();
                for (int j = 1; j < comboProduct.getComboDeals().size(); j++) {
                    ProductModel comboDeal = comboProduct.getComboDeals().get(j);
                    oneComboString += "&" + comboDeal.getProductDetail().getBarcode() + "," + comboDeal.getProductDetail().getProduct_count() + ";" + comboDeal.getProductDetail().getVariant_string();
                }
                comboString += oneComboString + ":" + comboProduct.getPrice() + "," + comboProduct.getStock() + "/";
            } else if (comboProduct.getPay_to_promote() == 1) {
                pay_comboString += comboProduct.getProduct_id() + ":" + comboProduct.getPrice() + "," + comboProduct.getStock() + "/";
            }
        }

        String buy1get1String = "", pay_buy1get1String = "";
        for (int i = 0; i < buyList.size(); i++) {
            ProductOneModel buyGetProduct = buyList.get(i);
            if (buyGetProduct.getPay_to_promote() == 0) {
                //barcode,quantity;variant1,variant2&barcode,quantity;variant1,variant2%barcode,quantity;variant1,variant2:price,stock
                String oneBuyString = "";
                oneBuyString = buyGetProduct.getBuyList().get(0).getProductDetail().getBarcode() + "," + buyGetProduct.getBuyList().get(0).getProductDetail().getProduct_count() + ";" + buyGetProduct.getBuyList().get(0).getProductDetail().getVariant_string();
                for (int j = 1; j < buyGetProduct.getBuyList().size(); j++) {
                    ProductModel buyDeal = buyGetProduct.getBuyList().get(j);
                    oneBuyString += "&" + buyDeal.getProductDetail().getBarcode() + "," + buyDeal.getQuantity() + ";" + buyDeal.getProductDetail().getVariant_string();
                }

                String oneGetString = "";
                oneGetString = buyGetProduct.getGetList().get(0).getProductDetail().getBarcode() + "," + buyGetProduct.getGetList().get(0).getQuantity() + ";" + buyGetProduct.getGetList().get(0).getProductDetail().getVariant_string();
                for (int j = 1; j < buyGetProduct.getGetList().size(); j++) {
                    ProductModel getDeal = buyGetProduct.getGetList().get(j);
                    oneGetString += "&" + getDeal.getProductDetail().getBarcode() + "," + getDeal.getQuantity() + ";" + getDeal.getProductDetail().getVariant_string();
                }

                buy1get1String += oneBuyString + "%" + oneGetString + ":" + buyGetProduct.getPrice() + "," + buyGetProduct.getStock() + "/";
            } else if (buyGetProduct.getPay_to_promote() == 1) {
                pay_buy1get1String += buyGetProduct.getProduct_id() + ":" + buyGetProduct.getPrice() + "," + buyGetProduct.getStock() + "/";
            }
        }
        showLoadingDialog();


        Builders.Any.B builder = Ion.with(this)
                .load(G.CreatePromotion)
                .addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken());

        if (!adsType.equalsIgnoreCase("BestDeal")) {
            builder.setMultipartFile("sub_media", contentType, new File(mediaPath));
        }

        builder.setMultipartParameter("promotion_type", adsType)
                .setMultipartParameter("title", title)
                .setMultipartParameter("description", description)
                .setMultipartParameter("tag_list", tag_list)
                .setMultipartParameter("place_id_list", place_id_list)
                .setMultipartParameter("start_date", start_date)
                .setMultipartParameter("end_date", end_date)
                .setMultipartParameter("store_id", String.valueOf(selStore))
                .setMultipartParameter("total_money", String.valueOf(total_money))
                .setMultipartParameter("currency", selCurrency)
                .setMultipartParameter("is_private", String.valueOf(is_private))
                .setMultipartParameter("is_deliver", String.valueOf(isDeliver))
                .setMultipartParameter("need_data", String.valueOf(need_data))
                .setMultipartParameter("singleString", singleString)
                .setMultipartParameter("comboString", comboString)
                .setMultipartParameter("buy1get1String", buy1get1String)
                .setMultipartParameter("pay_singleString", pay_singleString)
                .setMultipartParameter("pay_comboString", pay_comboString)
                .setMultipartParameter("pay_buy1get1String", pay_buy1get1String)
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
                                    Toast.makeText(activity, adsType + " " + getString(R.string.msg_created_successfully), Toast.LENGTH_LONG).show();
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

    private void onCreateAdsMultiple(String collectType) {
        String place_id_list = "";
        if (TextUtils.isEmpty(edtTitle.getText().toString()) || TextUtils.isEmpty(edtStart.getText().toString()) || TextUtils.isEmpty(edtEnd.getText().toString())) {
            Toast.makeText(activity, R.string.msg_valid_info, Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(mediaPath)) {
            Toast.makeText(activity, R.string.msg_select_media, Toast.LENGTH_LONG).show();
            return;
        }
        if (locationList.size() == 0) {
            Toast.makeText(activity, R.string.msg_select_location, Toast.LENGTH_LONG).show();
            return;
        }
        place_id_list = locationList.get(0);
        for (int i = 1; i < locationList.size(); i++) {
            place_id_list += "," + locationList.get(i);
        }


        String start_date = edtStart.getText().toString();
        String end_date = edtEnd.getText().toString();
        @SuppressLint("SimpleDateFormat") int dateDifference = (int) getDateDiff(new SimpleDateFormat("yyyy-MM-dd"), start_date, end_date);
        if (dateDifference < 0) {
            Toast.makeText(activity, R.string.msg_select_end_date, Toast.LENGTH_LONG).show();
            return;
        }

        if (singleList.size() == 0 && comboList.size() == 0 && buyList.size() == 0) {
            Toast.makeText(activity, R.string.msg_select_product, Toast.LENGTH_LONG).show();
            return;
        }

        String title = edtTitle.getText().toString();
        String description = edtDescription.getText().toString();

        double total_money = dailyAmount * (dateDifference + 1);
        boolean is_private = ckPrivate.isChecked();
        boolean need_data = true;
        String tag_list = tagList.size() == 0 ? "" : tagList.get(0);
        if (tagList.size() > 1) {
            for (int i = 1; i < tagList.size(); i++) {
                tag_list += "," + tagList.get(i);
            }
        }
        String singleString = "", pay_singleString = "";
        for (int i = 0; i < singleList.size(); i++) {
            ProductOneModel singleProduct = singleList.get(i);
            if (singleProduct.getPay_to_promote() == 0) {
                //barcode,quantity;variant1,variant2:price,stock/
                singleString += singleProduct.getBarcode() + "," + "1" + ";" + singleProduct.getVariant_string() + ":" + singleProduct.getPrice() + "," + singleProduct.getStock() + "/";
            } else if (singleProduct.getPay_to_promote() == 1) {
                pay_singleString += singleProduct.getProduct_id() + ":" + singleProduct.getPrice() + "," + singleProduct.getStock() + "/";
            }
        }

        String comboString = "", pay_comboString = "";
        for (int i = 0; i < comboList.size(); i++) {
            ProductOneModel comboProduct = comboList.get(i);
            if (comboProduct.getPay_to_promote() == 0) {
                //barcode,quantity;variant1,variant2&barcode,quantity;variant1,variant2:price,stock/
                String oneComboString = "";
                oneComboString = comboProduct.getComboDeals().get(0).getProductDetail().getBarcode() + "," + comboProduct.getComboDeals().get(0).getQuantity() + ";" + comboProduct.getComboDeals().get(0).getProductDetail().getVariant_string();
                for (int j = 1; j < comboProduct.getComboDeals().size(); j++) {
                    ProductModel comboDeal = comboProduct.getComboDeals().get(j);
                    oneComboString += "&" + comboDeal.getProductDetail().getBarcode() + "," + comboDeal.getProductDetail().getProduct_count() + ";" + comboDeal.getProductDetail().getVariant_string();
                }
                comboString += oneComboString + ":" + comboProduct.getPrice() + "," + comboProduct.getStock() + "/";
            } else if (comboProduct.getPay_to_promote() == 1) {
                pay_comboString += comboProduct.getProduct_id() + ":" + comboProduct.getPrice() + "," + comboProduct.getStock() + "/";
            }
        }

        String buy1get1String = "", pay_buy1get1String = "";
        for (int i = 0; i < buyList.size(); i++) {
            ProductOneModel buyGetProduct = buyList.get(i);
            if (buyGetProduct.getPay_to_promote() == 0) {
                //barcode,quantity;variant1,variant2&barcode,quantity;variant1,variant2%barcode,quantity;variant1,variant2:price,stock
                String oneBuyString = "";
                oneBuyString = buyGetProduct.getBuyList().get(0).getProductDetail().getBarcode() + "," + buyGetProduct.getBuyList().get(0).getProductDetail().getProduct_count() + ";" + buyGetProduct.getBuyList().get(0).getProductDetail().getVariant_string();
                for (int j = 1; j < buyGetProduct.getBuyList().size(); j++) {
                    ProductModel buyDeal = buyGetProduct.getBuyList().get(j);
                    oneBuyString += "&" + buyDeal.getProductDetail().getBarcode() + "," + buyDeal.getQuantity() + ";" + buyDeal.getProductDetail().getVariant_string();
                }

                String oneGetString = "";
                oneGetString = buyGetProduct.getGetList().get(0).getProductDetail().getBarcode() + "," + buyGetProduct.getGetList().get(0).getQuantity() + ";" + buyGetProduct.getGetList().get(0).getProductDetail().getVariant_string();
                for (int j = 1; j < buyGetProduct.getGetList().size(); j++) {
                    ProductModel getDeal = buyGetProduct.getGetList().get(j);
                    oneGetString += "&" + getDeal.getProductDetail().getBarcode() + "," + getDeal.getQuantity() + ";" + getDeal.getProductDetail().getVariant_string();
                }

                buy1get1String += oneBuyString + "%" + oneGetString + ":" + buyGetProduct.getPrice() + "," + buyGetProduct.getStock() + "/";
            } else if (buyGetProduct.getPay_to_promote() == 1) {
                pay_buy1get1String += buyGetProduct.getProduct_id() + ":" + buyGetProduct.getPrice() + "," + buyGetProduct.getStock() + "/";
            }
        }
        showLoadingDialog();


        Builders.Any.B builder = Ion.with(this)
                .load(G.CreatePromotion)
                .addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken());

        if (!adsType.equalsIgnoreCase("BestDeal")) {
            builder.setMultipartFile("sub_media", contentType, new File(mediaPath));
        }

        builder.setMultipartParameter("promotion_type", adsType)
                .setMultipartParameter("title", title)
                .setMultipartParameter("description", description)
                .setMultipartParameter("tag_list", tag_list)
                .setMultipartParameter("place_id_list", place_id_list)
                .setMultipartParameter("start_date", start_date)
                .setMultipartParameter("end_date", end_date)
                .setMultipartParameter("store_id", String.valueOf(selStore))
                .setMultipartParameter("total_money", String.valueOf(total_money))
                .setMultipartParameter("currency", selCurrency)
                .setMultipartParameter("is_private", String.valueOf(is_private))
                .setMultipartParameter("is_deliver", collectType.equalsIgnoreCase("deliver") ? "true" : "false")
                .setMultipartParameter("need_data", String.valueOf(need_data))
                .setMultipartParameter("singleString", singleString)
                .setMultipartParameter("comboString", comboString)
                .setMultipartParameter("buy1get1String", buy1get1String)
                .setMultipartParameter("pay_singleString", pay_singleString)
                .setMultipartParameter("pay_comboString", pay_comboString)
                .setMultipartParameter("pay_buy1get1String", pay_buy1get1String)
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
                                    Toast.makeText(activity, adsType + " " + getString(R.string.msg_created_successfully), Toast.LENGTH_LONG).show();
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
        if (adsType.equalsIgnoreCase("CantMissDeal") || adsType.equalsIgnoreCase("VendorPromotion")) {
            if (TextUtils.isEmpty(edtTitle.getText().toString())) {
                Toast.makeText(activity, R.string.msg_valid_info, Toast.LENGTH_LONG).show();
                return;
            }
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
                .load("PUT", G.UpdatePromotion);
        builder.addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken());
        if (!adsType.equalsIgnoreCase("BestDeal")) {
            if (!TextUtils.isEmpty(mediaPath)) {
                File file = new File(mediaPath);
                builder.setMultipartFile("sub_media", file);
            }
        }
        if (adsType.equalsIgnoreCase("CantMissDeal") || adsType.equalsIgnoreCase("VendorPromotion")) {
            builder.setMultipartParameter("title", title);
        }
        builder.setMultipartParameter("id", String.valueOf(promotion_id))
                .setMultipartParameter("description", description)
                .setMultipartParameter("tag_list", tag_list)
                .setMultipartParameter("place_id_list", place_id_list);
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
                                    Toast.makeText(activity, adsType + " " + getString(R.string.msg_updated_successfully), Toast.LENGTH_LONG).show();
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

    @OnClick({R.id.btBack, R.id.txtStoreEmpty, R.id.btnCreate, R.id.imgCreate, R.id.imgDelete, R.id.edtStart, R.id.edtEnd, R.id.btnSingle, R.id.btnCombo, R.id.btnBuyGet, R.id.btnPayToPromote})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtStoreEmpty:
                spinnerStore.performClick();
                if (isDeliver) {
                    if (storeDeliverList.size() > 0) {
                        selStore = storeDeliverList.get(0).getId();
                    }
                } else {
                    if (storeCollectList.size() > 0) {
                        selStore = storeCollectList.get(0).getId();
                    }
                }
                txtStoreEmpty.setVisibility(View.GONE);
                spinnerStore.setVisibility(View.VISIBLE);
                break;
            case R.id.btnCreate:
                if (promotion_id == -1) {
                    if (adsType.equalsIgnoreCase("VendorPromotion")) {
                        if (ckDeliver.isChecked() && ckCollect.isChecked()) {
                            String place_id_list = "";
                            if (TextUtils.isEmpty(edtTitle.getText().toString()) || TextUtils.isEmpty(edtStart.getText().toString()) || TextUtils.isEmpty(edtEnd.getText().toString())) {
                                Toast.makeText(activity, R.string.msg_valid_info, Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (TextUtils.isEmpty(mediaPath)) {
                                Toast.makeText(activity, R.string.msg_select_media, Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (locationList.size() == 0) {
                                Toast.makeText(activity, R.string.msg_select_location, Toast.LENGTH_LONG).show();
                                return;
                            }
                            place_id_list = locationList.get(0);
                            for (int i = 1; i < locationList.size(); i++) {
                                place_id_list += "," + locationList.get(i);
                            }


                            String start_date = edtStart.getText().toString();
                            String end_date = edtEnd.getText().toString();
                            @SuppressLint("SimpleDateFormat") int dateDifference = (int) getDateDiff(new SimpleDateFormat("yyyy-MM-dd"), start_date, end_date);
                            if (dateDifference < 0) {
                                Toast.makeText(activity, R.string.msg_select_end_date, Toast.LENGTH_LONG).show();
                                return;
                            }

                            if (singleList.size() == 0 && comboList.size() == 0 && buyList.size() == 0) {
                                Toast.makeText(activity, R.string.msg_select_product, Toast.LENGTH_LONG).show();
                                return;
                            }
                            onCreateAdsMultiple("collect");
                            onCreateAdsMultiple("deliver");
                        } else {
                            onCreateAds();
                        }
                    } else {
                        onCreateAds();
                    }
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
            case R.id.imgCreate:
                if (promotion_id == -1) {
                    openCamera();
                }
                break;
            case R.id.btnSingle:
                onSelectProduct("single");
                break;
            case R.id.btnCombo:
                onSelectProduct("combo");
                break;
            case R.id.btnBuyGet:
                onSelectProduct("buyget");
                break;
            case R.id.btnPayToPromote:
                onSelectProduct("promote");
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
            for (int i = 0; i < res.getData().size(); i++) {
                if (res.getData().get(i).isIs_click_collect()) {
                    storeCollectList.add(res.getData().get(i));
                }
                if (res.getData().get(i).isIs_click_deliver()) {
                    storeDeliverList.add(res.getData().get(i));
                }
                if (res.getData().get(i).isIs_click_collect() && res.getData().get(i).isIs_click_deliver()) {
                    storeBothList.add(res.getData().get(i));
                }
            }
            storeAllList.addAll(res.getData());
            if (ckCollect.isChecked() && ckDeliver.isChecked()) {
                for (int i = 0; i < storeBothList.size(); i++) {
                    if (storeBothList.get(i).getCurrency().getIso().equalsIgnoreCase(selCurrency)) {
                        storeList.add(storeBothList.get(i));
                    }
                }
            } else if (ckCollect.isChecked()) {
                for (int i = 0; i < storeCollectList.size(); i++) {
                    if (storeCollectList.get(i).getCurrency().getIso().equalsIgnoreCase(selCurrency)) {
                        storeList.add(storeCollectList.get(i));
                    }
                }
            } else if (ckDeliver.isChecked()) {
                for (int i = 0; i < storeDeliverList.size(); i++) {
                    if (storeDeliverList.get(i).getCurrency().getIso().equalsIgnoreCase(selCurrency)) {
                        storeList.add(storeDeliverList.get(i));
                    }
                }
            }

            setStoreAdapter();
            if (promotion_id != -1) {
                txtStoreEmpty.setVisibility(View.GONE);
                spinnerStore.setVisibility(View.VISIBLE);
                showLoadingDialog();
                AdsApi.getAdsDetail(adsType, promotion_id);
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessAdsDetail(PromotionRes res) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 500 && resultCode == 500 && data != null) {
            Gson gson = new Gson();
            ProductOneModel product = gson.fromJson(data.getStringExtra("product"), ProductOneModel.class);
            String type = data.getStringExtra("type");
            if (adsType.equalsIgnoreCase("BestDeal") || adsType.equalsIgnoreCase("ExclusiveDeal")) {
                if (type.equalsIgnoreCase("single")) {
                    singleList.add(product);
                    singleAdapter.setData(singleList);
                    if (singleList.size() == 0) {
                        lytSingle.setVisibility(View.GONE);
                    } else {
                        lytSingle.setVisibility(View.VISIBLE);
                    }
                    comboList.clear();
                    comboAdapter.setData(comboList);
                    lytCombo.setVisibility(View.GONE);
                    buyList.clear();
                    buyAdapter.setData(buyList);
                    lytBuy.setVisibility(View.GONE);
                } else if (type.equalsIgnoreCase("combo")) {
                    comboList.add(product);
                    comboAdapter.setData(comboList);
                    if (comboList.size() == 0) {
                        lytCombo.setVisibility(View.GONE);
                    } else {
                        lytCombo.setVisibility(View.VISIBLE);
                    }
                    singleList.clear();
                    singleAdapter.setData(singleList);
                    lytSingle.setVisibility(View.GONE);
                    buyList.clear();
                    buyAdapter.setData(buyList);
                    lytBuy.setVisibility(View.GONE);
                } else {
                    buyList.add(product);
                    buyAdapter.setData(buyList);
                    if (buyList.size() == 0) {
                        lytBuy.setVisibility(View.GONE);
                    } else {
                        lytBuy.setVisibility(View.VISIBLE);
                    }
                    singleList.clear();
                    singleAdapter.setData(singleList);
                    lytSingle.setVisibility(View.GONE);
                    comboList.clear();
                    comboAdapter.setData(comboList);
                    lytCombo.setVisibility(View.GONE);
                }
            } else {
                if (type.equalsIgnoreCase("single")) {
                    singleList.add(product);
                    singleAdapter.setData(singleList);
                    if (singleList.size() == 0) {
                        lytSingle.setVisibility(View.GONE);
                    } else {
                        lytSingle.setVisibility(View.VISIBLE);
                    }
                } else if (type.equalsIgnoreCase("combo")) {
                    comboList.add(product);
                    comboAdapter.setData(comboList);
                    if (comboList.size() == 0) {
                        lytCombo.setVisibility(View.GONE);
                    } else {
                        lytCombo.setVisibility(View.VISIBLE);
                    }
                } else {
                    buyList.add(product);
                    buyAdapter.setData(buyList);
                    if (buyList.size() == 0) {
                        lytBuy.setVisibility(View.GONE);
                    } else {
                        lytBuy.setVisibility(View.VISIBLE);
                    }
                }
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
