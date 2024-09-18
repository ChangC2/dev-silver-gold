package seemesave.businesshub.view.vendor.menu;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.makeramen.roundedimageview.RoundedImageView;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.OneSelectionAdapter;
import seemesave.businesshub.adapter.ProductVariantSelectAdapter;
import seemesave.businesshub.adapter.VariantsAdapter;
import seemesave.businesshub.api.common.CommonApi;
import seemesave.businesshub.api.product.ProductApi;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.listener.RecyclerClickListener;
import seemesave.businesshub.model.common.BaseInfoModel;
import seemesave.businesshub.model.common.MCommon;
import seemesave.businesshub.model.common.ProductDetailModel;
import seemesave.businesshub.model.res.BaseInfoRes;
import seemesave.businesshub.model.res.ProductRes;
import seemesave.businesshub.model.res.UserInfoRes;
import seemesave.businesshub.sqlite.DatabaseQueryClass;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.utils.GsonUtils;

public class CreateProductActivity extends BaseActivity {

    private CreateProductActivity activity;
    private SelectProductItemFragment selectProductItemFragment;

    @BindView(R.id.txtTitle)
    TextView txtTitle;

    @BindView(R.id.txtCreate)
    TextView txtCreate;

    @BindView(R.id.imgProduct)
    ImageView imgProduct;

    @BindView(R.id.edtBarcode)
    TextInputEditText edtBarcode;

    @BindView(R.id.edtDescription)
    TextInputEditText edtDescription;

    @BindView(R.id.edtSupplierCode)
    TextInputEditText edtSupplierCode;

    @BindView(R.id.spinnerBrand)
    TextView spinnerBrand;
    private ArrayList<BaseInfoModel> brandList = new ArrayList<>();
    private int selBrand = -1;

    @BindView(R.id.spinnerPack)
    TextView spinnerPack;
    private ArrayList<BaseInfoModel> packList = new ArrayList<>();
    private int selPack = -1;

    @BindView(R.id.spinnerUnit)
    TextView spinnerUnit;
    private ArrayList<BaseInfoModel> unitList = new ArrayList<>();
    private int selUnit = -1;

    @BindView(R.id.spinnerCategory)
    TextView spinnerCategory;
    private ArrayList<BaseInfoModel> categoryList = new ArrayList<>();
    private int selCategory = -1;

    @BindView(R.id.spinnerSupplier)
    TextView spinnerSupplier;
    private ArrayList<BaseInfoModel> supplierList = new ArrayList<>();
    private int selSupplier = -1;

    @BindView(R.id.recyclerVariantView)
    RecyclerView recyclerVariantView;
    @BindView(R.id.variantIndicator)
    IndefinitePagerIndicator variantIndicator;
    ProductVariantSelectAdapter variantSelectAdapter;
    private ArrayList<ProductDetailModel> variantProductList = new ArrayList<>();

    @BindView(R.id.edtTag)
    TextInputEditText edtTag;
    @BindView(R.id.tagRecyclerView)
    RecyclerView tagRecyclerView;
    private OneSelectionAdapter tagAdapter;
    private ArrayList<String> tagList = new ArrayList<>();

    private String imagePath = "";
    private String selBarcode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        ButterKnife.bind(this);
        activity = this;
        selBarcode = getIntent().getStringExtra("barcode");
        getLocalData();
        initView();
        if (!TextUtils.isEmpty(selBarcode)) {
            txtTitle.setText(getString(R.string.txt_update_product));
            txtCreate.setText(getString(R.string.update));
            showLoadingDialog();
            ProductApi.getPortalProductDetail(selBarcode);
            apiCallForGetVariantsFromBarcode();
        }
    }

    private void initView() {
        tagList.clear();
        variantProductList.clear();
        spinnerBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDlg(getString(R.string.txt_brand), brandList);
            }
        });
        spinnerUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDlg(getString(R.string.txt_unit), unitList);
            }
        });
        spinnerSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.getPortalType()) {
                    showSelectDlg(getString(R.string.txt_supplier), supplierList);
                }
            }
        });
        spinnerCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDlg(getString(R.string.txt_category), categoryList);
            }
        });
        spinnerPack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDlg(getString(R.string.txt_pack_size), packList);
            }
        });
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
        setVariantRecycler();
        setTagAdapter();
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

    private void getLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(App.getUserID(), "BaseInfo", "");
            if (!TextUtils.isEmpty(data)) {
                BaseInfoRes localRes = GsonUtils.getInstance().fromJson(data, BaseInfoRes.class);
                brandList.addAll(localRes.getBrandList());
                packList.addAll(localRes.getPackList());
                unitList.addAll(localRes.getUnitList());
                categoryList.addAll(localRes.getCategoryList());
                supplierList.addAll(localRes.getSupplierList());
                if (!App.getPortalType()) {
                    showLoadingDialog();
                    CommonApi.getUserInfo();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setVariantRecycler() {
        variantSelectAdapter = new ProductVariantSelectAdapter(activity, variantProductList, new ProductVariantSelectAdapter.ProductVariantSelectRecyclerListener() {

            @Override
            public void onItemClicked(int pos, ProductDetailModel model) {
                variantProductList.remove(pos);
                variantSelectAdapter.setData(variantProductList);
            }
        });
        recyclerVariantView.setAdapter(variantSelectAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recyclerVariantView.setLayoutManager(linearLayoutManager);
        variantIndicator.attachToRecyclerView(recyclerVariantView);
    }

    private void showSelectDlg(String type, ArrayList<BaseInfoModel> dataList) {
        selectProductItemFragment = new SelectProductItemFragment(activity, type, dataList, new SelectProductItemFragment.BottomFragListener() {
            @Override
            public void onDismiss() {

            }

        });
        selectProductItemFragment.setCancelable(true);
        selectProductItemFragment.show(getSupportFragmentManager(), selectProductItemFragment.getTag());
    }

    void apiCallForGetVariantsFromBarcode() {
        if (G.isNetworkAvailable(activity)) {
            String url = String.format(java.util.Locale.US, G.GetProductVariantUrl, selBarcode);
            Ion.with(activity)
                    .load(url)
                    .addHeader("Authorization", App.getToken())
                    .addHeader("Content-Language", App.getPortalToken())
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            if (e != null) {
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.getBoolean("status")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject variantInfo = jsonArray.getJSONObject(i);
                                            ProductDetailModel variant = new ProductDetailModel();
                                            variant.setBarcode(variantInfo.getString("barcode"));
                                            variant.setThumbnail_image(variantInfo.getString("thumbnail_image"));
                                            variant.setBrand(variantInfo.getString("Brand"));
                                            variant.setDescription(variantInfo.getString("description"));
                                            variant.setPackSize(variantInfo.getString("PackSize"));
                                            variant.setCategory(variantInfo.getString("Category"));
                                            variant.setUnit(variantInfo.getString("Unit"));

                                            variant.setCheck(true);
                                            variantProductList.add(variant);
                                        }
                                        variantSelectAdapter.setData(variantProductList);
                                    }
                                } catch (JSONException jsonException) {
                                }
                            }
                        }
                    });
        } else {
            Toast.makeText(activity, R.string.msg_offline, Toast.LENGTH_LONG).show();
        }

    }

    void apiCallForGetVariants(String supplier_id) {
        if (G.isNetworkAvailable(activity)) {
            ArrayList<MCommon> variants = new ArrayList<>();
            showLoadingDialog();
            String url = String.format(java.util.Locale.US, G.GetProductUrl, supplier_id);
            Ion.with(activity)
                    .load(url)
                    .addHeader("Authorization", App.getToken())
                    .addHeader("Content-Language", App.getPortalToken())
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            hideLoadingDialog();
                            if (e != null) {
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.getBoolean("status")) {
                                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject variantInfo = jsonArray.getJSONObject(i);
                                            MCommon variant = new MCommon();
                                            variant.setBarcode(variantInfo.getString("barcode"));
                                            variant.setImageUrl(variantInfo.getString("thumbnail_image"));
                                            variant.setName(variantInfo.getString("Brand"));
                                            variant.setDescription(variantInfo.getString("description"));
                                            variant.setPackSize(variantInfo.getString("PackSize"));
                                            variant.setFull_desc(variantInfo.getString("Category"));
                                            variant.setUnit(variantInfo.getString("Unit"));
                                            boolean variant_flag = false;
                                            for (int j = 0; j < variantProductList.size(); j++) {
                                                if (variantProductList.get(j).getBarcode().equalsIgnoreCase(variantInfo.getString("barcode"))) {
                                                    variant_flag = true;
                                                    break;
                                                }
                                            }
                                            variant.setCheck(variant_flag);
                                            variants.add(variant);
                                        }
                                        if (variants.size() > 0) {
                                            showVariants(variants);
                                        } else {
                                            Toast.makeText(activity, R.string.txt_msg_no_variants, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                } catch (JSONException jsonException) {
                                }
                            }
                        }
                    });
        } else {
            Toast.makeText(activity, R.string.msg_offline, Toast.LENGTH_LONG).show();
        }

    }

    private void showVariants(ArrayList<MCommon> variants) {
        Dialog dialog = new Dialog(activity, R.style.DialogTheme);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dlg_variant_select);
        dialog.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < variants.size(); i++) {
                    if (variants.get(i).isCheck()) {
                        ProductDetailModel productDetailModel = new ProductDetailModel();
                        productDetailModel.setBarcode(variants.get(i).getBarcode());
                        productDetailModel.setBrand(variants.get(i).getName());
                        productDetailModel.setDescription(variants.get(i).getDescription());
                        productDetailModel.setPackSize(variants.get(i).getPackSize());
                        productDetailModel.setUnit(variants.get(i).getUnit());
                        productDetailModel.setCategory(variants.get(i).getFull_desc());
                        productDetailModel.setThumbnail_image(variants.get(i).getImageUrl());
                        variantProductList.add(productDetailModel);
                    }
                }
                variantSelectAdapter.setData(variantProductList);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.imgClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.select_product);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = dialog.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerClickListener mListner = new RecyclerClickListener() {
            @Override
            public void onClick(View v, int vPosition) {

            }

            @Override
            public void onClick(View v, int position, int type) {

            }
        };
        VariantsAdapter variantsAdapter = new VariantsAdapter(activity, variants, mListner);
        recyclerView.setAdapter(variantsAdapter);
        dialog.show();
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

    private void onCreateProduct() {
        String barcode = edtBarcode.getText().toString();
        String description = edtDescription.getText().toString();
        String supplierCode = edtSupplierCode.getText().toString();
        String tag_list = tagList.size() == 0 ? "" : tagList.get(0);
        if (tagList.size() > 1) {
            for (int i = 1; i < tagList.size(); i++) {
                tag_list += "," + tagList.get(i);
            }
        }
        if (TextUtils.isEmpty(barcode) || TextUtils.isEmpty(description) || selBrand == -1 || selUnit == -1 || selCategory == -1 || selSupplier == -1 || selPack == -1) {
            Toast.makeText(activity, R.string.txt_msg_finish_info, Toast.LENGTH_LONG).show();
            return;
        }
        showLoadingDialog();
        Builders.Any.B builder;

        if (TextUtils.isEmpty(selBarcode)) {
            builder = Ion.with(activity).load("POST", G.CreateProduct);
        } else {
            builder = Ion.with(activity).load("PUT", G.UpdateProduct);
        }

        builder.addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken());
        if (!TextUtils.isEmpty(imagePath)) {
            File file = new File(imagePath);
            builder.setMultipartFile("new_media", file);
        }
        builder.setMultipartParameter("barcode", barcode)
                .setMultipartParameter("supplier_id", String.valueOf(selSupplier))
                .setMultipartParameter("description", description)
                .setMultipartParameter("brand_id", String.valueOf(selBrand))
                .setMultipartParameter("pack_size_id", String.valueOf(selPack))
                .setMultipartParameter("unit_id", String.valueOf(selUnit))
                .setMultipartParameter("category_id", String.valueOf(selCategory))
                .setMultipartParameter("weight", "1")
                .setMultipartParameter("supplier_product_code", supplierCode)
                .setMultipartParameter("tag_list", tag_list)
                .setMultipartParameter("need_data", "true");
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
                                    if (TextUtils.isEmpty(selBarcode)) {
                                        Toast.makeText(activity, R.string.msg_success_created, Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(activity, R.string.msg_updated_successfully, Toast.LENGTH_LONG).show();
                                    }
                                    if (variantProductList.size() > 0) {
                                        String variants = variantProductList.get(0).getBarcode();
                                        for (int j = 1; j < variantProductList.size(); j++) {
                                            variants += "," + variantProductList.get(j).getBarcode();
                                        }
                                        setVariantToProduct(barcode, variants);
                                    } else {
                                        LocalBroadcastManager.getInstance(activity).sendBroadcast(new Intent("refresh_product"));
                                        finish();
                                    }
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

    private void setVariantToProduct(String barcode, String variants) {
        showLoadingDialog();
        Builders.Any.B builder = Ion.with(activity)
                .load("POST", G.CreateVariant);
        builder.addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken());
        builder.setBodyParameter("barcode", barcode)
                .setBodyParameter("variants", variants);
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
                                    LocalBroadcastManager.getInstance(activity).sendBroadcast(new Intent("refresh_product"));
                                    finish();
                                } else {
                                    Toast.makeText(activity, "Something went wrong on creating variants", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException jsonException) {
                                Toast.makeText(activity, R.string.msg_something_wrong, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
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
                        .into(imgProduct);

            }

        }
    }
    private void initData(ProductDetailModel detailModel) {
        edtBarcode.setText(selBarcode);
        edtBarcode.setEnabled(false);
        edtDescription.setText(detailModel.getDescription());
        Glide.with(activity)
                .load(detailModel.getThumbnail_image())
                .centerCrop()
                .into(imgProduct);
        edtSupplierCode.setText(detailModel.getSupplier_product_code());
        if (!TextUtils.isEmpty(detailModel.getTag_list())) {
            if (detailModel.getTag_list().contains(",")) {
                ArrayList<String> tagItems = new ArrayList<String>(Arrays.asList(detailModel.getTag_list().split(",")));
                tagList.addAll(tagItems);
            } else {
                tagList.add(detailModel.getTag_list());
            }
            tagAdapter.setData(tagList);
        }
        selBrand = detailModel.getBrand_id();
        selPack = detailModel.getPack_size_id();
        selUnit = detailModel.getUnit_id();
        selCategory = detailModel.getCategory_id();
        selSupplier = detailModel.getSupplier_id();
        spinnerBrand.setText(getBrandName());
        spinnerUnit.setText(getUnitName());
        spinnerCategory.setText(getCategoryName());
        spinnerSupplier.setText(getSupplierName());
        spinnerPack.setText(getPackName());
    }
    private String getBrandName() {
        String name = "";
        for (int i = 0; i < brandList.size(); i ++) {
            if (brandList.get(i).getId() == selBrand) {
                name = brandList.get(i).getName();
            }
        }
        return name;
    }
    private String getUnitName() {
        String name = "";
        for (int i = 0; i < unitList.size(); i ++) {
            if (unitList.get(i).getId() == selUnit) {
                name = unitList.get(i).getName();
            }
        }
        return name;
    }
    private String getPackName() {
        String name = "";
        for (int i = 0; i < packList.size(); i ++) {
            if (packList.get(i).getId() == selPack) {
                name = packList.get(i).getName();
            }
        }
        return name;
    }
    private String getCategoryName() {
        String name = "";
        for (int i = 0; i < categoryList.size(); i ++) {
            if (categoryList.get(i).getId() == selCategory) {
                name = categoryList.get(i).getTitle();
            }
        }
        return name;
    }
    private String getSupplierName() {
        String name = "";
        for (int i = 0; i < supplierList.size(); i ++) {
            if (supplierList.get(i).getId() == selSupplier) {
                name = supplierList.get(i).getName();
            }
        }
        return name;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectItem(BaseInfoModel model) {
        switch (model.getType()) {
            case "Brand":
                spinnerBrand.setText(model.getName());
                selBrand = model.getId();
                break;
            case "Pack Size":
                spinnerPack.setText(model.getName());
                selPack = model.getId();
                break;
            case "Unit":
                spinnerUnit.setText(model.getName());
                selUnit = model.getId();
                break;
            case "Category":
                spinnerCategory.setText(model.getTitle());
                selCategory = model.getId();
                break;
            case "Supplier":
                spinnerSupplier.setText(model.getName());
                selSupplier = model.getId();
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessProductDetail(ProductRes res) {
        hideLoadingDialog();
        if (res.isStatus()) {
            initData(res.getData());
        } else {
            Toast.makeText(activity,res.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onApiProfileInfo(UserInfoRes res) {
        hideLoadingDialog();
        if (res.isStatus()) {
            spinnerSupplier.setText(res.getPortalInfo().getName());
            selSupplier = res.getPortalInfo().getId();
            spinnerSupplier.setEnabled(false);
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

    @OnClick({R.id.btBack, R.id.imgPhotoEdit, R.id.btnCreate, R.id.txtAddVariant})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.txtAddVariant:
                if (selSupplier == -1) {
                    Toast.makeText(activity, R.string.msg_select_supplier, Toast.LENGTH_LONG).show();
                    return;
                }
                apiCallForGetVariants(String.valueOf(selSupplier));
                break;
            case R.id.imgPhotoEdit:
                openCamera();
                break;
            case R.id.btBack:
                finish();
                break;
            case R.id.btnCreate:
                onCreateProduct();
                break;

        }
    }
}
