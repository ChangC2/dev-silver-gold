package seemesave.businesshub.view.vendor.product;

import android.content.Intent;
import android.graphics.Typeface;
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

public class ProductBuyGetSelectActivity extends BaseActivity {
    private ProductSelectViewModel mViewModel;
    private ProductBuyGetSelectActivity activity;

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
    @BindView(R.id.buyRecyclerView)
    RecyclerView buyRecyclerView;
    @BindView(R.id.getRecyclerView)
    RecyclerView getRecyclerView;
    @BindView(R.id.txtBuy)
    TextView txtBuy;
    @BindView(R.id.txtGet)
    TextView txtGet;

    private ProductSelectAdapter buySelectAdapter;
    private ArrayList<ProductDetailModel> buyList = new ArrayList<>();

    private ProductSelectAdapter getSelectAdapter;
    private ArrayList<ProductDetailModel> getList = new ArrayList<>();

    private String selType = "buyget";
    private boolean selProductType = true;
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
        setContentView(R.layout.activity_product_buyget);
        ButterKnife.bind(this);
        activity = this;
        selType = getIntent().getStringExtra("type");

        initView();
    }

    private void initView() {
        nestedScrollView.setNestedScrollingEnabled(false);
        onSelectBuy(true);
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
        buyList.clear();
        getList.clear();
        if (App.getPortalType()) {
            loadSupplierData();
        }
        setTypeRecycler();
        setBuyProductAdapter();
        setGetProductAdapter();
        txtTitle.setText(R.string.txt_buyget_product);
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
    private void setBuyProductAdapter() {
        buySelectAdapter = new ProductSelectAdapter(activity, buyList, selType, new ProductSelectAdapter.ProductSelectRecyclerListener() {
            @Override
            public void onItemClicked(int pos, ProductDetailModel model) {
                ArrayList<ProductDetailModel> tmpList = new ArrayList<>();
                tmpList.clear();
                for (int i = 0; i < buyList.size(); i++) {
                    ProductDetailModel productDetailModel = new ProductDetailModel();
                    productDetailModel = buyList.get(i);
                    if (i == pos) {
                        productDetailModel.setCheck(!productDetailModel.isCheck());
                    }
                    tmpList.add(productDetailModel);
                }
                buySelectAdapter.setData(tmpList);
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
        buyRecyclerView.setAdapter(buySelectAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        buyRecyclerView.setLayoutManager(linearLayoutManager);
    }
    private void setGetProductAdapter() {
        getSelectAdapter = new ProductSelectAdapter(activity, getList, selType, new ProductSelectAdapter.ProductSelectRecyclerListener() {
            @Override
            public void onItemClicked(int pos, ProductDetailModel model) {
                ArrayList<ProductDetailModel> tmpList = new ArrayList<>();
                tmpList.clear();
                for (int i = 0; i < getList.size(); i++) {
                    ProductDetailModel productDetailModel = new ProductDetailModel();
                    productDetailModel = getList.get(i);
                    if (i == pos) {
                        productDetailModel.setCheck(!productDetailModel.isCheck());
                    }
                    tmpList.add(productDetailModel);
                }
                getSelectAdapter.setData(tmpList);
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
        getRecyclerView.setAdapter(getSelectAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        getRecyclerView.setLayoutManager(linearLayoutManager);
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
    private int getSupplierIndex(){
        for(int i=0; i<supplierList.size(); i++){
            if(supplierList.get(i).getId() == selSupplier){
                return i;
            }
        }
        return -1;
    }
    private void onSelectBuy(boolean kind) {
        selProductType = kind;
        if (kind) {
            txtBuy.setTypeface(null, Typeface.BOLD);
            txtGet.setTypeface(null, Typeface.NORMAL);
            getRecyclerView.setVisibility(View.GONE);
            buyRecyclerView.setVisibility(View.VISIBLE);
        } else {
            txtBuy.setTypeface(null, Typeface.NORMAL);
            txtGet.setTypeface(null, Typeface.BOLD);
            buyRecyclerView.setVisibility(View.GONE);
            getRecyclerView.setVisibility(View.VISIBLE);
        }

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
        ArrayList<ProductDetailModel> tmpProductList1 = new ArrayList<>();
        tmpProductList1.clear();
        ArrayList<ProductDetailModel> tmpProductList2 = new ArrayList<>();
        tmpProductList2.clear();
        for (int i = 0; i < buyList.size(); i ++) {
            if (buyList.get(i).isCheck()) {
                tmpProductList1.add(buyList.get(i));
            }
        }
        for (int i = 0; i < getList.size(); i ++) {
            if (getList.get(i).isCheck()) {
                tmpProductList2.add(getList.get(i));
            }
        }
        if (tmpProductList1.size() == 0) {
            Toast.makeText(activity, R.string.msg_select_more_buy_product, Toast.LENGTH_LONG).show();
            return;
        }
//        if (tmpProductList1.size() == 1 && tmpProductList1.get(0).getProduct_count() < 2) {
//
//        }
        if (tmpProductList2.size() == 0) {
            Toast.makeText(activity, R.string.msg_select_more_buy_product, Toast.LENGTH_LONG).show();
            return;
        }
        ProductOneModel productOneModel = new ProductOneModel();
        productOneModel.setPrice(edtPrice.getText().toString());
        if (App.getPortalType()) {
            productOneModel.setStock(Integer.parseInt(edtQty.getText().toString()));
        }
        productOneModel.setBarcode(tmpProductList1.get(0).getBarcode());
        productOneModel.setImageUrl(tmpProductList1.get(0).getThumbnail_image());
        productOneModel.setTitle(tmpProductList1.get(0).getBrand());
        productOneModel.setDescription(tmpProductList1.get(0).getDescription());
        productOneModel.setUnit(tmpProductList1.get(0).getUnit());
        productOneModel.setPack_size(tmpProductList1.get(0).getPackSize());
        productOneModel.setCount(tmpProductList1.get(0).getProduct_count());
        productOneModel.setVariant_string(tmpProductList1.get(0).getVariant_string());
        productOneModel.setPay_to_promote(0);
        productOneModel.setCheck(true);

        ArrayList<ProductModel> tmpBuys = new ArrayList<>();
        tmpBuys.clear();
        for (int i = 0; i < tmpProductList1.size(); i ++) {
            ProductModel productModel = new ProductModel();
            productModel.setProductDetail(tmpProductList1.get(i));
            productModel.setQuantity(tmpProductList1.get(i).getProduct_count());
            tmpBuys.add(productModel);
        }
        productOneModel.setBuyList(tmpBuys);

        ArrayList<ProductModel> tmpGets = new ArrayList<>();
        tmpGets.clear();
        for (int i = 0; i < tmpProductList2.size(); i ++) {
            ProductModel productModel = new ProductModel();
            productModel.setProductDetail(tmpProductList2.get(i));
            productModel.setQuantity(tmpProductList2.get(i).getProduct_count());
            tmpGets.add(productModel);
        }
        productOneModel.setGetList(tmpGets);

        productOneModel.setProduct_type("Buy1Get1FreeDeal");


        Gson gson = new Gson();
        String productJson = gson.toJson(productOneModel);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("type", selType);
        resultIntent.putExtra("product", productJson);
        setResult(500, resultIntent);
        finish();

    }
    @OnClick({R.id.btBack, R.id.btnConfirm, R.id.txtBuy, R.id.txtGet})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                onConfirm();
                break;
            case R.id.txtBuy:
                onSelectBuy(true);
                break;
            case R.id.txtGet:
                onSelectBuy(false);
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
                buyList.clear();
                getList.clear();
                buySelectAdapter.setData(buyList);
                getSelectAdapter.setData(getList);
                lytEmpty.setVisibility(View.VISIBLE);
                return;
            } else {
                lytEmpty.setVisibility(View.GONE);
                if (offset == 0) {
                    buyList.clear();
                    getList.clear();
                }
                if (res.getProductList().size() < limit) {
                    isLast = true;
                }
                for (int i = 0; i < res.getProductList().size(); i++) {
                    ProductDetailModel model = new ProductDetailModel(res.getProductList().get(i));
                    buyList.add(model);
                }

                buySelectAdapter.setData(buyList);
                getList.addAll(res.getProductList());
                getSelectAdapter.setData(getList);
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
