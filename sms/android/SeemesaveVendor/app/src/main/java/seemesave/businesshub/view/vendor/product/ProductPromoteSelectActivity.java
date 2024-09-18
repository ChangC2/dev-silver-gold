package seemesave.businesshub.view.vendor.product;

import static seemesave.businesshub.utils.ParseUtil.getProductsFromPromote;
import static seemesave.businesshub.utils.ParseUtil.getSuppliersFromPromote;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.PromoteSelectAdapter;
import seemesave.businesshub.api.promote.PromoteApi;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.model.common.ProductOneModel;
import seemesave.businesshub.model.common.SupplierOneModel;
import seemesave.businesshub.model.res.ProductPromoteListRes;
import seemesave.businesshub.view_model.vendor.product.ProductSelectViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductPromoteSelectActivity extends BaseActivity {
    private ProductSelectViewModel mViewModel;
    private ProductPromoteSelectActivity activity;

    @BindView(R.id.txtTitle)
    TextView txtTitle;

    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    @BindView(R.id.spinnerSupplier)
    Spinner spinnerSupplier;
    private ArrayAdapter<SupplierOneModel> supplierAdapter;
    private ArrayList<SupplierOneModel> supplierList = new ArrayList<>();

    @BindView(R.id.edtPrice)
    TextInputEditText edtPrice;

    @BindView(R.id.edtQty)
    TextInputEditText edtQty;

    @BindView(R.id.lytEmpty)
    LinearLayout lytEmpty;
    @BindView(R.id.productRecyclerView)
    RecyclerView productRecyclerView;
    private PromoteSelectAdapter productSelectAdapter;
    private ArrayList<ProductOneModel> productList = new ArrayList<>();
    private ArrayList<ProductOneModel> srcProductList = new ArrayList<>();

    private int selType = 1;
    private int selStore = -1;
    private int selSupplier = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProductSelectViewModel.class);
        setContentView(R.layout.activity_promote_select);
        ButterKnife.bind(this);
        activity = this;
        selType = getIntent().getIntExtra("type", 1);
        selStore = getIntent().getIntExtra("store", -1);

        initView();
    }

    private void initView() {
        nestedScrollView.setNestedScrollingEnabled(false);
        productList.clear();
        srcProductList.clear();
        supplierList.clear();
        setProductAdapter();
        setSupplierAdapter();
        txtTitle.setText(R.string.txt_pay_to_promote_products);
        getProduct();
    }

    private void getProduct() {
        PromoteApi.getPromoteProduct(selType, selStore);
    }

    private void setProductAdapter() {
        productSelectAdapter = new PromoteSelectAdapter(activity, productList, "promote", new PromoteSelectAdapter.PromoteProductRecyclerListener() {

            @Override
            public void onItemClicked(int pos, ProductOneModel model) {

            }

            @Override
            public void onPlus(int pos, ProductOneModel model) {
            }

            @Override
            public void onMinus(int pos, ProductOneModel model) {

            }

            @Override
            public void onSwitch(int pos, ProductOneModel model) {

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
                setProduct(selSupplier);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        supplierAdapter = new ArrayAdapter<SupplierOneModel>(activity, R.layout.custom_spinner, supplierList);
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
        if (TextUtils.isEmpty(Objects.requireNonNull(edtQty.getText()).toString()) || Integer.parseInt(edtQty.getText().toString()) == 0) {
            Toast.makeText(activity, R.string.msg_input_inventory, Toast.LENGTH_LONG).show();
            return;
        }
        ArrayList<ProductOneModel> tmpProductList = new ArrayList<>();
        tmpProductList.clear();
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).isCheck()) {
                tmpProductList.add(productList.get(i));
            }
        }
        if (tmpProductList.size() == 0) {
            Toast.makeText(activity, R.string.msg_select_product, Toast.LENGTH_LONG).show();
            return;
        }
        ProductOneModel productOneModel = tmpProductList.get(0);
        productOneModel.setPrice(edtPrice.getText().toString());
        productOneModel.setStock(Integer.parseInt(edtQty.getText().toString()));
        productOneModel.setPay_to_promote(1);
        String proType = "";
        if (tmpProductList.get(0).getProduct_type().equalsIgnoreCase("SingleProduct")) {
            proType = "single";
        } else if (tmpProductList.get(0).getProduct_type().equalsIgnoreCase("ComboDeal")) {
            proType = "combo";
        } else if (tmpProductList.get(0).getProduct_type().equalsIgnoreCase("Buy1Get1FreeDeal")) {
            proType = "buyget";
        }

        Gson gson = new Gson();
        String productJson = gson.toJson(tmpProductList.get(0));
        Intent resultIntent = new Intent();
        resultIntent.putExtra("type", proType);
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

    private void setProduct(int id) {
        productList.clear();
        for (int i = 0; i < srcProductList.size(); i++) {
            if (srcProductList.get(i).getSupplier_id() == id) {
                productList.add(srcProductList.get(i));
            }
        }
        productSelectAdapter.setData(productList);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessProductList(ProductPromoteListRes res) {
        if (res.isStatus()) {
            if (res.getProductList().size() == 0) {
                srcProductList.clear();
                productList.clear();
                productSelectAdapter.setData(productList);
                lytEmpty.setVisibility(View.VISIBLE);
                return;
            } else {
                lytEmpty.setVisibility(View.GONE);
                srcProductList.clear();
                productList.clear();
                srcProductList.addAll(getProductsFromPromote(res));
                productList.addAll(srcProductList);
                supplierList.addAll(getSuppliersFromPromote(res));
                supplierAdapter.notifyDataSetChanged();
                productSelectAdapter.setData(productList);
                setProduct(supplierList.get(0).getId());
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
