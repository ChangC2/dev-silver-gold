package seemesave.businesshub.view.supplier.promote;

import static seemesave.businesshub.utils.TimeUtils.getDateDiff;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.ProductPromotionAdapter;
import seemesave.businesshub.adapter.StoreSelectAdapter;
import seemesave.businesshub.api.promote.PromoteApi;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.model.common.CurrencyModel;
import seemesave.businesshub.model.common.NotificationModel;
import seemesave.businesshub.model.common.ProductModel;
import seemesave.businesshub.model.common.ProductOneModel;
import seemesave.businesshub.model.common.StoreModel;
import seemesave.businesshub.model.res.BaseInfoRes;
import seemesave.businesshub.model.res.PromoteBaseInfoRes;
import seemesave.businesshub.sqlite.DatabaseQueryClass;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.utils.GsonUtils;
import seemesave.businesshub.view.vendor.product.ProductBuyGetSelectActivity;
import seemesave.businesshub.view.vendor.product.ProductSelectActivity;

public class CreatePaytoPromoteActivity extends BaseActivity {

    private CreatePaytoPromoteActivity activity;



    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txtStep)
    TextView txtStep;
    @BindView(R.id.imgStep)
    ImageView imgStep;
    @BindView(R.id.lytSlide1)
    LinearLayout lytSlide1;
    @BindView(R.id.lytSlide2)
    LinearLayout lytSlide2;
    @BindView(R.id.lytSlide3)
    LinearLayout lytSlide3;
    @BindView(R.id.lytSlide4)
    LinearLayout lytSlide4;
    @BindView(R.id.btnBack)
    LinearLayout btnBack;
    @BindView(R.id.btnNext)
    LinearLayout btnNext;


    @BindView(R.id.spinnerCurrency)
    Spinner spinnerCurrency;
    private ArrayAdapter<CurrencyModel> currencyAdapter;
    private ArrayList<CurrencyModel> currencyList = new ArrayList<>();
    private String selCurrency = "ZAR";

    @BindView(R.id.rvStore)
    RecyclerView rvStore;
    private StoreSelectAdapter storeSelectAdapter;
    private ArrayList<StoreModel> storeList = new ArrayList<>();
    private ArrayList<StoreModel> selStoreList = new ArrayList<>();

    @BindView(R.id.rdPromotion)
    RadioButton rdPromotion;
    @BindView(R.id.rdMissDeal)
    RadioButton rdMissDeal;
    @BindView(R.id.rdBestDeal)
    RadioButton rdBestDeal;
    @BindView(R.id.rdExDeal)
    RadioButton rdExDeal;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

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

    @BindView(R.id.imgCreate)
    RoundedImageView imgCreate;

    @BindView(R.id.imgPlay)
    ImageView imgPlay;

    @BindView(R.id.lytMedia)
    LinearLayout lytMedia;
    @BindView(R.id.edtTitle)
    TextInputEditText edtTitle;
    @BindView(R.id.edtDescription)
    TextInputEditText edtDescription;
    @BindView(R.id.edtStart)
    TextInputEditText edtStart;
    @BindView(R.id.edtEnd)
    TextInputEditText edtEnd;
    @BindView(R.id.lytFinal)
    LinearLayout lytFinal;
    @BindView(R.id.ckPrice)
    CheckBox ckPrice;
    @BindView(R.id.txtDesc)
    TextView txtDesc;

    final Calendar myCalendar = Calendar.getInstance();
    private String mediaPath = "";
    private String contentType = "image/jpeg";
    private String mType = "";

    private int selType = 0;
    private float rate = 0;
    private int curStep = 0;
    private float totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_paytopromote);
        ButterKnife.bind(this);
        activity = this;
        getLocalCurrencyList();
        initView();
        showLoadingDialog();
        PromoteApi.getPromoteBaseInfo();
    }


    private void initView() {
        storeList.clear();
        selStoreList.clear();
        singleList.clear();
        comboList.clear();
        buyList.clear();
        setStoreSelectAdapter();
        setSingleAdapter();
        setComboAdapter();
        setBuyAdapter();
        rdPromotion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selType = 1;
                }
            }
        });

    }
    private void setSingleAdapter() {
        singleAdapter = new ProductPromotionAdapter(activity, singleList, false, new ProductPromotionAdapter.ProductPromotionRecyclerListener() {
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
        comboAdapter = new ProductPromotionAdapter(activity, comboList, false, new ProductPromotionAdapter.ProductPromotionRecyclerListener() {
            @Override
            public void onItemClicked(int pos, ProductOneModel model) {
                comboList.remove(pos);
                comboAdapter.setData(comboList);
            }
        });
        comboView.setAdapter(comboAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        comboView.setLayoutManager(linearLayoutManager);
        comboIndicator.attachToRecyclerView(comboView);
    }

    private void setBuyAdapter() {
        buyAdapter = new ProductPromotionAdapter(activity, buyList, false, new ProductPromotionAdapter.ProductPromotionRecyclerListener() {
            @Override
            public void onItemClicked(int pos, ProductOneModel model) {
                buyList.remove(pos);
                buyAdapter.setData(buyList);
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
                setStoreAdapter();
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

    private void setStoreSelectAdapter() {
        storeSelectAdapter = new StoreSelectAdapter(activity, selStoreList, new StoreSelectAdapter.StoreSelectRecyclerListener() {
            @Override
            public void onItemClicked(int pos, StoreModel model) {
                ArrayList<StoreModel> tmpList = new ArrayList<>();
                tmpList.clear();
                for (int i = 0; i < selStoreList.size(); i++) {
                    StoreModel storeModel = new StoreModel();
                    storeModel = selStoreList.get(i);
                    if (i == pos) {
                        storeModel.setCheck(!storeModel.isCheck());
                    }
                    tmpList.add(storeModel);
                }
                storeSelectAdapter.setData(tmpList);
            }

        });
        rvStore.setAdapter(storeSelectAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rvStore.setLayoutManager(linearLayoutManager);
    }
    private void setStoreAdapter() {
        selStoreList.clear();
        for (int i = 0; i < storeList.size(); i ++) {
            if (storeList.get(i).getCurrency().getIso().equalsIgnoreCase(selCurrency)) {
                StoreModel model = new StoreModel();
                model.setId(storeList.get(i).getId());
                model.setName(storeList.get(i).getName());
                model.setCheck(false);
                model.setAddress(storeList.get(i).getAddress());
                model.setDescription(storeList.get(i).getDescription());
                model.setLogo(storeList.get(i).getLogo());
                selStoreList.add(model);
            }
        }
        storeSelectAdapter.setData(selStoreList);
    }
    private void onNext() {
        if (validation()) {
            if (curStep == 3) {
                onCreatePaytoPromote();
            } else {
                curStep++;
                setView();
            }
        }
    }

    private boolean validation() {
        boolean isValid = true;
        switch (curStep) {
            case 0:
                int selCount = 0;
                for (int i = 0; i < selStoreList.size(); i++) {
                    if (selStoreList.get(i).isCheck()) {
                        selCount ++;
                    }
                }
                if (selCount == 0) {
                    isValid = false;
                    Toast.makeText(activity, R.string.msg_select_store, Toast.LENGTH_LONG).show();
                }
                break;
            case 1:
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    isValid = false;
                    Toast.makeText(activity, R.string.msg_select_type, Toast.LENGTH_LONG).show();
                } else {
                    RadioButton radioSexButton=(RadioButton)findViewById(selectedId);
                    String selTypeStr = radioSexButton.getText().toString();
                    switch (selTypeStr) {
                        case "Promotion":
                            selType = 1;
                            break;
                        case "Can't Miss Deal":
                            selType = 2;
                            break;
                        case "Best Deal":
                            selType = 3;
                            break;
                        case "Exclusive Deal":
                            selType = 4;
                            break;
                    }
                }
                break;
            case 2:
                if (singleList.size() == 0 && comboList.size() == 0 && buyList.size() == 0) {
                    isValid = false;
                    Toast.makeText(activity, R.string.msg_select_product, Toast.LENGTH_LONG).show();
                }
                break;
            case 3:
                if (TextUtils.isEmpty(edtTitle.getText().toString()) || TextUtils.isEmpty(edtDescription.getText().toString()) || TextUtils.isEmpty(edtStart.getText().toString()) ||
                TextUtils.isEmpty(edtEnd.getText().toString()) || !ckPrice.isChecked()) {
                    Toast.makeText(activity, R.string.txt_msg_finish_info, Toast.LENGTH_LONG).show();
                    isValid = false;
                } else {
                    String start_date = edtStart.getText().toString();
                    String end_date = edtEnd.getText().toString();
                    @SuppressLint("SimpleDateFormat") int dateDifference = (int) getDateDiff(new SimpleDateFormat("yyyy-MM-dd"), start_date, end_date);
                    if (dateDifference < 0) {
                        Toast.makeText(activity, R.string.msg_select_end_date, Toast.LENGTH_LONG).show();
                        isValid = false;
                    }
                }
                break;
        }
        return isValid;
    }

    private void onBack() {
        if (curStep == 0) return;
        curStep--;
        setView();
    }

    private void setView() {
        switch (curStep) {
            case 0:
                txtStep.setText(getString(R.string.txt_store_select));
                imgStep.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_add_store_step1));
                btnBack.setVisibility(View.GONE);
                btnNext.setVisibility(View.VISIBLE);
                lytSlide1.setVisibility(View.VISIBLE);
                lytSlide2.setVisibility(View.GONE);
                lytSlide3.setVisibility(View.GONE);
                lytSlide4.setVisibility(View.GONE);
                break;
            case 1:
                txtStep.setText(getString(R.string.txt_type_select));
                imgStep.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_add_store_step2));
                btnBack.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.VISIBLE);
                lytSlide1.setVisibility(View.GONE);
                lytSlide2.setVisibility(View.VISIBLE);
                lytSlide3.setVisibility(View.GONE);
                lytSlide4.setVisibility(View.GONE);
                break;
            case 2:
                txtStep.setText(getString(R.string.txt_select_products));
                imgStep.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_add_store_step3));
                btnBack.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.VISIBLE);
                lytSlide1.setVisibility(View.GONE);
                lytSlide2.setVisibility(View.GONE);
                lytSlide3.setVisibility(View.VISIBLE);
                lytSlide4.setVisibility(View.GONE);
                break;
            case 3:
                txtStep.setText(getString(R.string.txt_finalizing));
                imgStep.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_add_store_step4));
                btnBack.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.VISIBLE);
                lytSlide1.setVisibility(View.GONE);
                lytSlide2.setVisibility(View.GONE);
                lytSlide3.setVisibility(View.GONE);
                lytSlide4.setVisibility(View.VISIBLE);
                break;

        }
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

                String start_date = edtStart.getText().toString();
                String end_date = edtEnd.getText().toString();
                if (!TextUtils.isEmpty(start_date) && !TextUtils.isEmpty(end_date)) {
                    @SuppressLint("SimpleDateFormat") int dateDifference = (int) getDateDiff(new SimpleDateFormat("yyyy-MM-dd"), start_date, end_date);
                    if (dateDifference >= 0) {
                        calcPrice();
                    }
                }

            }

        };
        new DatePickerDialog(activity, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void calcPrice() {
        String start_date = edtStart.getText().toString();
        String end_date = edtEnd.getText().toString();
        if (!TextUtils.isEmpty(start_date) && !TextUtils.isEmpty(end_date)) {
            @SuppressLint("SimpleDateFormat") int dateDifference = (int) getDateDiff(new SimpleDateFormat("yyyy-MM-dd"), start_date, end_date);
            if (dateDifference >= 0) {
                float tmpPrice = 0;
                for (int i = 0; i < singleList.size(); i ++) {
                    tmpPrice += Float.parseFloat(singleList.get(i).getPrice());
                }
                for (int i = 0; i < comboList.size(); i ++) {
                    tmpPrice += Float.parseFloat(comboList.get(i).getPrice());
                }
                for (int i = 0; i < buyList.size(); i ++) {
                    tmpPrice += Float.parseFloat(buyList.get(i).getPrice());
                }
                tmpPrice = tmpPrice * (dateDifference + 1);
                int storeCount = 0;
                for (int i = 0; i < selStoreList.size(); i++) {
                    if (selStoreList.get(i).isCheck()) {
                        storeCount ++;
                    }
                }
                tmpPrice = storeCount * tmpPrice;
                float price1 = tmpPrice * rate;
                float price2 = tmpPrice * (1 - rate);
                String descStr = String.format(java.util.Locale.US, "%s %s %.2f %s %s %.2f %s", "SeeMeSave will retain maximum", selCurrency.equalsIgnoreCase("ZAR") ? "R": "$", price1, "and the selected stores will receive maximum", selCurrency.equalsIgnoreCase("ZAR") ? "R": "$", price2, "for the duration of the Pay to Promote period.");
                lytFinal.setVisibility(View.VISIBLE);
                txtDesc.setText(descStr);
                String ckStr = String.format(java.util.Locale.US, "I will pay maximum %s %.2f on %s",  selCurrency.equalsIgnoreCase("ZAR") ? "R": "$", tmpPrice, end_date);
                ckPrice.setText(ckStr);
                totalPrice = tmpPrice;
            }
        }
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

    private void onSelectProduct(String type) {
        Intent i;
        if (type.equalsIgnoreCase("buyget")) {
            i = new Intent(activity, ProductBuyGetSelectActivity.class);
        } else {
            i = new Intent(activity, ProductSelectActivity.class);
        }
        i.putExtra("type", type);
        startActivityIfNeeded(i, 500);
    }
    private void onCreatePaytoPromote() {


        String singleString = "";
        for (int i = 0; i < singleList.size(); i++) {
            //barcode,quantity:price/barcode,quantity:price
            ProductOneModel singleProduct = singleList.get(i);
            singleString += singleProduct.getBarcode() + "," + "1" + ":" + singleProduct.getPrice() + "/";
        }

        String comboString = "";
        for (int i = 0; i < comboList.size(); i++) {
            ProductOneModel comboProduct = comboList.get(i);
            //barcode,quantity&barcode,quantity:price/barcode,quantity&barcode,quantity:price
            String oneComboString = "";
            oneComboString = comboProduct.getComboDeals().get(0).getProductDetail().getBarcode() + "," + comboProduct.getComboDeals().get(0).getProductDetail().getProduct_count();
            for (int j = 1; j < comboProduct.getComboDeals().size(); j++) {
                ProductModel comboDeal = comboProduct.getComboDeals().get(j);
                oneComboString += "&" + comboDeal.getProductDetail().getBarcode() + "," + comboDeal.getProductDetail().getProduct_count();
            }
            comboString += oneComboString + ":" + comboProduct.getPrice() + "/";
        }

        String buy1get1String = "";
        for (int i = 0; i < buyList.size(); i++) {
            ProductOneModel buyGetProduct = buyList.get(i);
            //barcode,quantity&barcode,quantity:price%barcode,quantity:price/barcode,quantity&barcode,quantity:price%barcode,quantity:price
            String oneBuyString = "";
            oneBuyString = buyGetProduct.getBuyList().get(0).getProductDetail().getBarcode() + "," + buyGetProduct.getBuyList().get(0).getProductDetail().getProduct_count();
            for (int j = 1; j < buyGetProduct.getBuyList().size(); j++) {
                ProductModel buyDeal = buyGetProduct.getBuyList().get(j);
                oneBuyString += "&" + buyDeal.getProductDetail().getBarcode() + "," + buyDeal.getProductDetail().getProduct_count();
            }

            String oneGetString = "";
            oneGetString = buyGetProduct.getGetList().get(0).getProductDetail().getBarcode() + "," + buyGetProduct.getGetList().get(0).getProductDetail().getProduct_count();
            for (int j = 1; j < buyGetProduct.getGetList().size(); j++) {
                ProductModel getDeal = buyGetProduct.getGetList().get(j);
                oneGetString += "&" + getDeal.getProductDetail().getBarcode() + "," + getDeal.getProductDetail().getProduct_count();
            }

            buy1get1String += oneBuyString + "%" + oneGetString + ":" + buyGetProduct.getPrice() + "/";
        }

        String title = edtTitle.getText().toString();
        String description = edtDescription.getText().toString();
        String start_date = edtStart.getText().toString();
        String end_date = edtEnd.getText().toString();
        String store_id_list = "";
        for (int i = 0; i < selStoreList.size(); i ++) {
            if (selStoreList.get(i).isCheck()) {
                store_id_list += selStoreList.get(i).getId() + ",";
            }
        }
        store_id_list = store_id_list.substring(0, store_id_list.length() - 1);
        showLoadingDialog();
        Builders.Any.B builder;

        builder = Ion.with(activity).load("POST", G.CreatePaytoPromote);

        builder.addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken());
        if (!mediaPath.equalsIgnoreCase("")) {
            builder.setMultipartFile("media", contentType, new File(mediaPath));
        }
        builder.setMultipartParameter("title", title)
                .setMultipartParameter("description", description)
                .setMultipartParameter("start_date", start_date)
                .setMultipartParameter("end_date", end_date)
                .setMultipartParameter("store_id_list", store_id_list)
                .setMultipartParameter("promotion_type", String.valueOf(selType))
                .setMultipartParameter("currency", selCurrency)
                .setMultipartParameter("singleString", singleString)
                .setMultipartParameter("comboString", comboString)
                .setMultipartParameter("buy1get1String", buy1get1String);
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
                                    Toast.makeText(activity, R.string.msg_success_created, Toast.LENGTH_LONG).show();
                                    EventBus.getDefault().post("CreatedPaytoPromote");
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

    @OnClick({R.id.btBack, R.id.btnNext, R.id.btnBack, R.id.btnSingle, R.id.btnCombo, R.id.btnBuyGet, R.id.imgCreate, R.id.edtStart, R.id.edtEnd})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.btnNext:
                onNext();
                break;
            case R.id.btnBack:
                onBack();
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
            case R.id.imgCreate:
                openCamera();
                break;
            case R.id.edtStart:
                showDatePickDlg("start");
                break;
            case R.id.edtEnd:
                showDatePickDlg("end");
                break;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessBase(PromoteBaseInfoRes res) {
        hideLoadingDialog();
        if (res.isStatus() && res.getStoreList().size() > 0) {
            storeList.clear();
            storeList.addAll(res.getStoreList());
            rate = res.getRate();
            setStoreAdapter();
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 500 && resultCode == 500 && data != null) {
            Gson gson = new Gson();
            ProductOneModel product = gson.fromJson(data.getStringExtra("product"), ProductOneModel.class);
            String type = data.getStringExtra("type");
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
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            if (returnValue.size() > 0) {
                mediaPath = returnValue.get(0);
                Glide.with(activity)
                        .load(new File(mediaPath))
                        .into(imgCreate);

            }

        }
    }
}