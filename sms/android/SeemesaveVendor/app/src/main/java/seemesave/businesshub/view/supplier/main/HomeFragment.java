package seemesave.businesshub.view.supplier.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.PayProductAdapter;
import seemesave.businesshub.adapter.StoreReviewAdapter;
import seemesave.businesshub.api.dashboard.DashboardApi;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseFragment;
import seemesave.businesshub.model.common.PayPromoteModel;
import seemesave.businesshub.model.common.StoreReviewModel;
import seemesave.businesshub.model.res.DashboardInfoRes;
import seemesave.businesshub.sqlite.DatabaseQueryClass;
import seemesave.businesshub.utils.GsonUtils;
import seemesave.businesshub.view.vendor.main.MainActivity;
import seemesave.businesshub.view.vendor.menu.FollowerActivity;
import seemesave.businesshub.view.vendor.menu.StoreActivity;

public class HomeFragment extends BaseFragment {
    private View mFragView;
    @BindView(R.id.txtPromotion)
    TextView txtPromotion;

    @BindView(R.id.txtPost)
    TextView txtPost;

    @BindView(R.id.txtStore)
    TextView txtStore;


    @BindView(R.id.txtStoreReview)
    TextView txtStoreReview;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.lytEmpty)
    LinearLayout lytEmpty;
    private StoreReviewAdapter storeReviewAdapter;
    ArrayList<StoreReviewModel> storeReviewList = new ArrayList<>();

    @BindView(R.id.txtPayProduct)
    TextView txtPayProduct;
    @BindView(R.id.payRecyclerView)
    RecyclerView payRecyclerView;
    @BindView(R.id.paylytEmpty)
    LinearLayout paylytEmpty;
    private PayProductAdapter payProductAdapter;
    ArrayList<PayPromoteModel> payPromoteList = new ArrayList<>();

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragView = inflater.inflate(R.layout.fragment_home_supplier, container, false);
        ButterKnife.bind(this, mFragView);
        initView();
        return mFragView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
    }


    private void initView() {
        storeReviewList.clear();
        payPromoteList.clear();
        setStoreReviewRecycler();
        setPayRecycler();

        try {
            String local_data = DatabaseQueryClass.getInstance().getData(App.getUserID(), "HomeFragment", String.valueOf(App.getPortalType()));
            if (TextUtils.isEmpty(local_data)) {
                showLoadingDialog();
            } else {
                loadLocalData();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        DashboardApi.getSupplierDashboardInfo();
    }

    private void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(App.getUserID(), "HomeFragment", String.valueOf(App.getPortalType()));
            if (!TextUtils.isEmpty(data)) {
                DashboardInfoRes localRes = GsonUtils.getInstance().fromJson(data, DashboardInfoRes.class);
                initData(localRes);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setStoreReviewRecycler() {
        storeReviewAdapter = new StoreReviewAdapter(getActivity(), storeReviewList, new StoreReviewAdapter.StoreReviewRecyclerListener() {

            @Override
            public void onItemClicked(int pos, StoreReviewModel model) {

            }

            @Override
            public void onItemAdd(int pos, StoreReviewModel model) {

            }
        });
        recyclerView.setAdapter(storeReviewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setPayRecycler() {
        payProductAdapter = new PayProductAdapter(getActivity(), payPromoteList, new PayProductAdapter.PayProductRecyclerListener() {

            @Override
            public void onItemClicked(int pos, PayPromoteModel model) {

            }
        });
        payRecyclerView.setAdapter(payProductAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        payRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initData(DashboardInfoRes res) {
        storeReviewList.clear();
        storeReviewList.addAll(res.getCommentList());
        storeReviewAdapter.setData(storeReviewList);
        if (storeReviewList.size() == 0) {
            lytEmpty.setVisibility(View.VISIBLE);
        } else {
            lytEmpty.setVisibility(View.GONE);
        }
        payPromoteList.clear();
        payPromoteList.addAll(res.getSupplierPayToPromtes());
        payProductAdapter.setData(payPromoteList);
        if (payPromoteList.size() == 0) {
            paylytEmpty.setVisibility(View.VISIBLE);
        } else {
            paylytEmpty.setVisibility(View.GONE);
        }
        txtPromotion.setText(String.valueOf(res.getSearchCount()));
        txtPost.setText(String.valueOf(res.getPostCount()));
        txtStore.setText(String.valueOf(res.getProductCount()));
        txtStoreReview.setText(String.format(Locale.US, getString(R.string.store_review_count), storeReviewList.size()));
        txtPayProduct.setText(String.format(Locale.US, getString(R.string.pay_product_count), payPromoteList.size()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessAdsDetail(DashboardInfoRes res) {
        hideLoadingDialog();
        if (res.isStatus()) {
            initData(res);
            String data = new Gson().toJson(res, new TypeToken<DashboardInfoRes>() {
            }.getType());
            DatabaseQueryClass.getInstance().insertData(
                    App.getUserID(),
                    "HomeFragment",
                    data,
                    String.valueOf(App.getPortalType()),
                    ""
            );
        } else {
            if (!TextUtils.isEmpty(res.getMessage())) {
                Toast.makeText(getActivity(), res.getMessage(), Toast.LENGTH_LONG).show();    
            }
        }
    }

    @OnClick({R.id.btnPromotionView, R.id.btnPostView, R.id.btnStoreView, R.id.txtStoreReviewAll})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btnPromotionView:
//                Intent i = new Intent(getActivity(), MainActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                i.putExtra("page", 3);
//                i.putExtra("sub_page", "promotion");
//                startActivity(i);
                break;
            case R.id.btnPostView:
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.putExtra("page", 3);
//                intent.putExtra("sub_page", "post");
//                startActivity(intent);
                break;
            case R.id.btnStoreView:
                startActivity(new Intent(getActivity(), StoreActivity.class));
                break;
            case R.id.txtStoreReviewAll:

                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}