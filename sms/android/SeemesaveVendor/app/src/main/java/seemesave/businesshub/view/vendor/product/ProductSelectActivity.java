package seemesave.businesshub.view.vendor.product;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.ProductKindAdapter;
import seemesave.businesshub.adapter.ProductSelectAdapter;
import seemesave.businesshub.api.product.ProductApi;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.model.common.BaseInfoModel;
import seemesave.businesshub.model.common.ProductDetailModel;
import seemesave.businesshub.model.common.ProductModel;
import seemesave.businesshub.model.common.ProductOneModel;
import seemesave.businesshub.model.common.ProductSelectTypeModel;
import seemesave.businesshub.model.res.BaseInfoRes;
import seemesave.businesshub.model.res.ProductListRes;
import seemesave.businesshub.sqlite.DatabaseQueryClass;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.utils.GsonUtils;
import seemesave.businesshub.view_model.vendor.product.ProductSelectViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductSelectActivity extends BaseActivity {
    private ProductSelectViewModel mViewModel;
    private ProductSelectActivity activity;

    @BindView(R.id.txtTitle)
    TextView txtTitle;

    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.productKindRecyclerView)
    RecyclerView productKindRecyclerView;
    private ProductKindAdapter productKindAdapter;
    private ArrayList<ProductSelectTypeModel> productKindList = new ArrayList<>();

    @BindView(R.id.lytSupplier)
    RelativeLayout lytSupplier;

    @BindView(R.id.spinnerSupplier)
    Spinner spinnerSupplier;
    private ArrayAdapter<BaseInfoModel> supplierAdapter;
    private ArrayList<BaseInfoModel> supplierList = new ArrayList<>();

    @BindView(R.id.edtSearch)
    EditText edtSearch;

    @BindView(R.id.edtPrice)
    TextInputEditText edtPrice;

    @BindView(R.id.lytQty)
    TextInputLayout lytQty;
    @BindView(R.id.edtQty)
    TextInputEditText edtQty;

    @BindView(R.id.lytEmpty)
    LinearLayout lytEmpty;
    @BindView(R.id.productRecyclerView)
    RecyclerView productRecyclerView;
    private ProductSelectAdapter productSelectAdapter;
    private ArrayList<ProductDetailModel> productList = new ArrayList<>();

    private String selType = "single";
    private int selSupplier = -1;
    private String keyword = "";
    private int offset = 0;
    private int limit = 10;
    private boolean isLoading = false;
    private boolean isLast = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProductSelectViewModel.class);
        setContentView(R.layout.activity_product_select);
        ButterKnife.bind(this);
        activity = this;
        selType = getIntent().getStringExtra("type");

        initView();
    }

    private void initView() {
        nestedScrollView.setNestedScrollingEnabled(false);
        initParam();
        productKindList.clear();
        if (App.getPortalType()) {
            productKindList.add(new ProductSelectTypeModel("My Products", true));
            productKindList.add(new ProductSelectTypeModel("Liked Products", false));
            productKindList.add(new ProductSelectTypeModel("Wish List", false));
            productKindList.add(new ProductSelectTypeModel("Supplier Search", false));
        } else {
            productKindList.add(new ProductSelectTypeModel("Liked Products that were not promoted", false));
            productKindList.add(new ProductSelectTypeModel("Wish List", false));
        }
        productList.clear();
        if (App.getPortalType()) {
            loadSupplierData();
        }
        setTypeRecycler();
        setProductAdapter();
        if (selType.equalsIgnoreCase("single")) {
            txtTitle.setText(R.string.txt_single_product);
        } else if (selType.equalsIgnoreCase("combo")) {
            txtTitle.setText(R.string.txt_combo_product);
        } else if (selType.equalsIgnoreCase("buyget")) {
            txtTitle.setText(R.string.txt_buyget_product);
        } else if (selType.equalsIgnoreCase("promote")) {
            txtTitle.setText(R.string.txt_pay_to_promote_products);
        }
        edtSearch.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    keyword = edtSearch.getText().toString().toLowerCase();
                    G.hideSoftKeyboard(activity);
                    getProduct();
                    return true;
                }
                return false;
            }
        });
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
                int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));
                if (diff == 0 && !isLoading && !isLast) {
                    isLoading = true;
                    offset = offset + limit;
                    mViewModel.setIsBusy(true);
                    getProduct();
                }

            }
        });
        getProduct();
        if (!App.getPortalType()) {
            lytQty.setVisibility(View.GONE);
        }
    }

    private void initParam() {
        offset = 0;
        isLast = false;
        isLoading = false;
    }

    private void loadSupplierData() {
        supplierList.clear();
        try {
            String data = DatabaseQueryClass.getInstance().getData(App.getUserID(), "BaseInfo", "");
            if (!TextUtils.isEmpty(data)) {
                BaseInfoRes localRes = GsonUtils.getInstance().fromJson(data, BaseInfoRes.class);
                supplierList.addAll(localRes.getSupplierList());
                setSupplierAdapter();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setTypeRecycler() {
        productKindAdapter = new ProductKindAdapter(activity, productKindList, new ProductKindAdapter.ProductSelectRecyclerListener() {
            @Override
            public void onItemClicked(int pos, ProductSelectTypeModel model) {
                productKindList.get(pos).setChecked(!productKindList.get(pos).isChecked());
                productKindAdapter.setData(productKindList);
                productKindRecyclerView.scrollToPosition(pos);
                if (App.getPortalType()) {
                    if (pos == 3) {
                        if (productKindList.get(pos).isChecked()) {
                            lytSupplier.setVisibility(View.VISIBLE);
                        } else {
                            lytSupplier.setVisibility(View.GONE);
                        }
                    } else {
                        initParam();
                        getProduct();
                    }
                } else {
                    initParam();
                    getProduct();
                }
            }

        });
        productKindRecyclerView.setAdapter(productKindAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        productKindRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void getProduct() {
        if (App.getPortalType()) {
            boolean is_mine = productKindList.get(0).isChecked();
            boolean is_wish = productKindList.get(1).isChecked();
            boolean is_liked = productKindList.get(2).isChecked();
            boolean is_supplier = productKindList.get(3).isChecked();
            ProductApi.getPromotionProduct(is_mine, is_wish, is_liked, is_supplier? selSupplier : -1, keyword, offset, limit);
        } else {
            boolean is_liked = productKindList.get(0).isChecked();
            boolean is_wish = productKindList.get(1).isChecked();
            ProductApi.getPromotionProduct(false, is_wish, is_liked, -1, keyword, offset, limit);
        }
    }

    private void setProductAdapter() {
        productSelectAdapter = new ProductSelectAdapter(activity, productList, selType, new ProductSelectAdapter.ProductSelectRecyclerListener() {

            @Override
            public void onItemClicked(int pos, ProductDetailModel model) {
                ArrayList<ProductDetailModel> tmpList = new ArrayList<>();
                tmpList.clear();
                if (selType.equalsIgnoreCase("single")) {
                    for (int i = 0; i < productList.size(); i++) {
                        ProductDetailModel productDetailModel = new ProductDetailModel();
                        productDetailModel = productList.get(i);
                        if (i == pos) {
                            productDetailModel.setCheck(true);
                        } else {
                            productDetailModel.setCheck(false);
                        }
                        tmpList.add(productDetailModel);
                    }
                } else {
                    for (int i = 0; i < productList.size(); i++) {
                        ProductDetailModel productDetailModel = new ProductDetailModel();
                        productDetailModel = productList.get(i);
                        if (i == pos) {
                            productDetailModel.setCheck(!productDetailModel.isCheck());
                        }
                        tmpList.add(productDetailModel);
                    }
                }
                productSelectAdapter.setData(tmpList);
            }

            @Override
            public void onPlus(int pos, ProductDetailModel model) {
            }

            @Override
            public void onMinus(int pos, ProductDetailModel model) {

            }

            @Override
            public void onSwitch(int pos, ProductDetailModel model) {

            }
        });
        productRecyclerView.setAdapter(productSelectAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        productRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setSupplierAdapter() {
        spinnerSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selSupplier = supplierList.get(position).getId();
                getProduct();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        supplierAdapter = new ArrayAdapter<BaseInfoModel>(activity, R.layout.custom_spinner, supplierList);
        supplierAdapter.setDropDownViewResource(R.layout.custom_spinner_combo);
        spinnerSupplier.setAdapter(supplierAdapter);
        spinnerSupplier.setSelection(getSupplierIndex());
    }

    private int getSupplierIndex() {
        for (int i = 0; i < supplierList.size(); i++) {
            if (supplierList.get(i).getId() == selSupplier) {
                return i;
            }
        }
        return -1;
    }
    private void onConfirm() {
        if (TextUtils.isEmpty(Objects.requireNonNull(edtPrice.getText()).toString()) || Float.parseFloat(edtPrice.getText().toString()) == 0.0) {
            Toast.makeText(activity, R.string.msg_input_price, Toast.LENGTH_LONG).show();
            return;
        }
        if (App.getPortalType()) {
            if (TextUtils.isEmpty(Objects.requireNonNull(edtQty.getText()).toString()) || Integer.parseInt(edtQty.getText().toString()) == 0) {
                Toast.makeText(activity, R.string.msg_input_inventory, Toast.LENGTH_LONG).show();
                return;
            }
        }
        ArrayList<ProductDetailModel> tmpProductList = new ArrayList<>();
        tmpProductList.clear();
        for (int i = 0; i < productList.size(); i ++) {
            if (productList.get(i).isCheck()) {
                tmpProductList.add(productList.get(i));
            }
        }
        if (selType.equalsIgnoreCase("single")) {
            if (tmpProductList.size() == 0) {
                Toast.makeText(activity, R.string.msg_select_product, Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            if (tmpProductList.size() == 0 || (tmpProductList.size() == 1 && tmpProductList.get(0).getProduct_count() < 2)) {
                Toast.makeText(activity, R.string.msg_select_more_product, Toast.LENGTH_LONG).show();
                return;
            }
        }
        ProductOneModel productOneModel = new ProductOneModel();
        productOneModel.setPrice(edtPrice.getText().toString());
        if (App.getPortalType()) {
            productOneModel.setStock(Integer.parseInt(edtQty.getText().toString()));
        }
        productOneModel.setBarcode(tmpProductList.get(0).getBarcode());
        productOneModel.setImageUrl(tmpProductList.get(0).getThumbnail_image());
        productOneModel.setTitle(tmpProductList.get(0).getBrand());
        productOneModel.setDescription(tmpProductList.get(0).getDescription());
        productOneModel.setUnit(tmpProductList.get(0).getUnit());
        productOneModel.setPack_size(tmpProductList.get(0).getPackSize());
        productOneModel.setCount(tmpProductList.get(0).getProduct_count());
        productOneModel.setVariant_string(tmpProductList.get(0).getVariant_string());
        productOneModel.setPay_to_promote(0);
        productOneModel.setCheck(true);
        if (selType.equalsIgnoreCase("single")) {
            productOneModel.setProduct_type("SingleProduct");
        } else {
            ArrayList<ProductModel> tmpCombos = new ArrayList<>();
            tmpCombos.clear();
            for (int i = 0; i < tmpProductList.size(); i ++) {
                ProductModel productModel = new ProductModel();
                productModel.setProductDetail(tmpProductList.get(i));
                productModel.setQuantity(tmpProductList.get(i).getProduct_count());
                tmpCombos.add(productModel);
            }
            productOneModel.setComboDeals(tmpCombos);
            productOneModel.setProduct_type("ComboDeal");
        }

        Gson gson = new Gson();
        String productJson = gson.toJson(productOneModel);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("type", selType);
        resultIntent.putExtra("product", productJson);
        setResult(500, resultIntent);
        finish();

    }
    @OnClick({R.id.btBack, R.id.btnConfirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                onConfirm();
                break;
            case R.id.btBack:
                finish();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessProductList(ProductListRes res) {
        if (res.isStatus()) {
            if (offset == 0 && res.getProductList().size() == 0) {
                productList.clear();
                productSelectAdapter.setData(productList);
                lytEmpty.setVisibility(View.VISIBLE);
                return;
            } else {
                lytEmpty.setVisibility(View.GONE);
                if (offset == 0) {
                    productList.clear();
                }
                if (res.getProductList().size() < limit) {
                    isLast = true;
                }
                productList.addAll(res.getProductList());
                productSelectAdapter.setData(productList);
                isLoading = false;
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
