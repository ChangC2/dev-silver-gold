
package seemesave.businesshub.view.supplier.promote;

import static seemesave.businesshub.utils.ParseUtil.getBuyGetProductsFromPromotion;
import static seemesave.businesshub.utils.ParseUtil.getComboProductsFromPromotion;
import static seemesave.businesshub.utils.ParseUtil.getSingleProductsFromPromotion;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.ProductPromotionAdapter;
import seemesave.businesshub.adapter.PromoteStoreAdapter;
import seemesave.businesshub.api.promote.PromoteApi;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.model.common.ProductOneModel;
import seemesave.businesshub.model.common.PromoteReceiverModel;
import seemesave.businesshub.model.res.PromoteDetailInfoRes;

public class PromoteDetailActivity extends BaseActivity {

    private PromoteDetailActivity activity;

    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtType)
    TextView txtType;
    @BindView(R.id.txtDescription)
    TextView txtDescription;
    @BindView(R.id.txtPeriod)
    TextView txtPeriod;
    @BindView(R.id.imgProduct)
    RoundedImageView imgProduct;

    @BindView(R.id.lytStore)
    LinearLayout lytStore;
    @BindView(R.id.lytSingle)
    LinearLayout lytSingle;
    @BindView(R.id.lytCombo)
    LinearLayout lytCombo;
    @BindView(R.id.lytBuy)
    LinearLayout lytBuy;

    @BindView(R.id.storeView)
    RecyclerView storeView;
    @BindView(R.id.storeIndicator)
    IndefinitePagerIndicator storeIndicator;
    private PromoteStoreAdapter storeAdapter;
    private ArrayList<PromoteReceiverModel> storeList = new ArrayList<>();

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

    private int promote_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_detail);
        ButterKnife.bind(this);
        activity = this;
        promote_id = getIntent().getIntExtra("id", -1);
        initView();
        showLoadingDialog();
        PromoteApi.getPromoteDetail(promote_id);
    }

    private void initView() {
        storeList.clear();
        singleList.clear();
        comboList.clear();
        buyList.clear();
        setStoreAdapter();
        setSingleAdapter();
        setComboAdapter();
        setBuyAdapter();
    }

    private void setStoreAdapter() {
        storeAdapter = new PromoteStoreAdapter(activity, storeList, false, new PromoteStoreAdapter.PromoteStoreRecyclerListener() {

            @Override
            public void onItemClicked(int pos, PromoteReceiverModel model) {

            }
        });
        storeView.setAdapter(storeAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        storeView.setLayoutManager(linearLayoutManager);
        storeIndicator.attachToRecyclerView(storeView);
    }

    private void setSingleAdapter() {
        singleAdapter = new ProductPromotionAdapter(activity, singleList, true, new ProductPromotionAdapter.ProductPromotionRecyclerListener() {
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
        comboAdapter = new ProductPromotionAdapter(activity, comboList, true, new ProductPromotionAdapter.ProductPromotionRecyclerListener() {
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
        buyAdapter = new ProductPromotionAdapter(activity, buyList, true, new ProductPromotionAdapter.ProductPromotionRecyclerListener() {
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

    private void initData(PromoteDetailInfoRes model) {
        Glide.with(activity)
                .load(model.getMedia())
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .into(imgProduct);
        txtTitle.setText(model.getTitle());
        txtDescription.setText(model.getDescription());
        String type = "";
        switch (model.getPromotion_type()) {
            case 1:
                type = "Promotion";
                break;
            case 2:
                type = "Can't Miss Deal";
                break;
            case 3:
                type = "Best Deal";
                break;
            case 4:
                type = "Exclusive Deal";
                break;
        }
        txtType.setText(type);
        txtPeriod.setText(String.format(java.util.Locale.US, "%s ~ %s", model.getStart_date(), model.getEnd_date()));
        if (model.getReceivers().size() == 0) {
            lytStore.setVisibility(View.GONE);
        } else {
            storeList.addAll(model.getReceivers());
            storeAdapter.setData(storeList);
            lytStore.setVisibility(View.VISIBLE);
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessAdsDetail(PromoteDetailInfoRes res) {
        hideLoadingDialog();
        if (res.isStatus()) {
            initData(res);
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