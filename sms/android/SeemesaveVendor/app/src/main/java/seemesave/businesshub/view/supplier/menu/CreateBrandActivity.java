package seemesave.businesshub.view.supplier.menu;

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
import seemesave.businesshub.model.common.BrandModel;
import seemesave.businesshub.model.common.MCommon;
import seemesave.businesshub.model.common.ProductDetailModel;
import seemesave.businesshub.model.res.BaseInfoRes;
import seemesave.businesshub.model.res.ProductRes;
import seemesave.businesshub.model.res.UserInfoRes;
import seemesave.businesshub.sqlite.DatabaseQueryClass;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.utils.GsonUtils;
import seemesave.businesshub.view.vendor.menu.SelectProductItemFragment;

public class CreateBrandActivity extends BaseActivity {

    private CreateBrandActivity activity;
    private SelectProductItemFragment selectProductItemFragment;

    @BindView(R.id.txtTitle)
    TextView txtTitle;

    @BindView(R.id.txtCreate)
    TextView txtCreate;

    @BindView(R.id.imgProduct)
    ImageView imgProduct;

    @BindView(R.id.edtName)
    TextInputEditText edtName;

    @BindView(R.id.edtTag)
    TextInputEditText edtTag;
    @BindView(R.id.tagRecyclerView)
    RecyclerView tagRecyclerView;
    private OneSelectionAdapter tagAdapter;
    private ArrayList<String> tagList = new ArrayList<>();

    private String imagePath = "";
    private BrandModel selBrand = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_brand);
        ButterKnife.bind(this);
        activity = this;
        initView();
        if (getIntent().hasExtra("brand")) {
            txtTitle.setText(getString(R.string.txt_update_brand));
            txtCreate.setText(getString(R.string.update));
            Gson gson = new Gson();
            selBrand = gson.fromJson(getIntent().getStringExtra("brand"), BrandModel.class);
            initData();
        }

    }

    private void initView() {
        tagList.clear();
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
        if (selBrand == null && TextUtils.isEmpty(imagePath)) {
            Toast.makeText(activity, R.string.msg_select_image, Toast.LENGTH_LONG).show();
            return;
        }
        String name = edtName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(activity, R.string.msg_input_brand_name, Toast.LENGTH_LONG).show();
            return;
        }
        String tag_list = tagList.size() == 0 ? "" : tagList.get(0);
        if (tagList.size() > 1) {
            for (int i = 1; i < tagList.size(); i++) {
                tag_list += "," + tagList.get(i);
            }
        }

        showLoadingDialog();
        Builders.Any.B builder;

        if (selBrand == null) {
            builder = Ion.with(activity).load("POST", G.CreateBrand);
        } else {
            builder = Ion.with(activity).load("PUT", G.UpdateBrand);
        }

        builder.addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken());
        if (!TextUtils.isEmpty(imagePath)) {
            File file = new File(imagePath);
            builder.setMultipartFile("new_media", file);
        }
        if (selBrand != null) {
            builder.setMultipartParameter("id", String.valueOf(selBrand.getId()));
        }
        builder.setMultipartParameter("name", name)
                .setMultipartParameter("tag_list", tag_list);
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
                                    if (selBrand == null) {
                                        Toast.makeText(activity, R.string.msg_success_created, Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(activity, R.string.msg_updated_successfully, Toast.LENGTH_LONG).show();
                                    }
                                    LocalBroadcastManager.getInstance(activity).sendBroadcast(new Intent("refresh_brand"));
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
    private void initData() {
        edtName.setText(selBrand.getName());
        Glide.with(activity)
                .load(selBrand.getMedia())
                .centerCrop()
                .into(imgProduct);
        if (!TextUtils.isEmpty(selBrand.getTagString_list())) {
            if (selBrand.getTagList().contains(",")) {
                ArrayList<String> tagItems = new ArrayList<String>(Arrays.asList(selBrand.getTagString_list().split(",")));
                tagList.addAll(tagItems);
            } else {
                tagList.add(selBrand.getTagString_list());
            }
            tagAdapter.setData(tagList);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick({R.id.btBack, R.id.imgPhotoEdit, R.id.btnCreate})
    public void onClickButtons(View view) {
        switch (view.getId()) {
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
