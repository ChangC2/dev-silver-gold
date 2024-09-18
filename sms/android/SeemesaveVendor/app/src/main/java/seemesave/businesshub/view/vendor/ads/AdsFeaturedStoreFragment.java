package seemesave.businesshub.view.vendor.ads;

import static seemesave.businesshub.utils.TimeUtils.getDateDiff;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.AdsPostAdapter;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseFragment;
import seemesave.businesshub.listener.RecyclerClickListener;
import seemesave.businesshub.model.common.PostModel;
import seemesave.businesshub.sqlite.DatabaseQueryClass;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.view_model.vendor.ads.AdsFeaturedStoreFragViewModel;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdsFeaturedStoreFragment extends BaseFragment {
    private View mFragView;
    private AdsFeaturedStoreFragViewModel mViewModel;

    @BindView(R.id.lytEdit)
    LinearLayout lytEdit;
    @BindView(R.id.icEdit)
    ImageView icEdit;
    @BindView(R.id.txtEdit)
    TextView txtEdit;
    @BindView(R.id.lytDelete)
    LinearLayout lytDelete;
    @BindView(R.id.icDelete)
    ImageView icDelete;
    @BindView(R.id.txtDelete)
    TextView txtDelete;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.lytEmpty)
    LinearLayout lytEmpty;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.imgExpand)
    ImageView imgExpand;
    @BindView(R.id.lytSearch)
    LinearLayout lytSearch;
    @BindView(R.id.txtStartDate)
    TextView txtStartDate;
    @BindView(R.id.txtEndDate)
    TextView txtEndDate;
    @BindView(R.id.edtSearch)
    EditText edtSearch;
    @BindView(R.id.imgClear)
    ImageView imgClear;

    private AdsPostAdapter recyclerAdapter;
    private ArrayList<PostModel> dataList = new ArrayList<>();
    private ArrayList<Integer> selIDList = new ArrayList<>();


    private int offset = 0;
    private int limit = 20;
    private boolean isLoading = false;
    private boolean isLast = false;

    private String promotionType = "FeaturedStore";
    private String keyword = "", start_date = "", end_date = "";
    private int itemCheckedCount = 0;
    private String app_name = "", model_name = "";
    private boolean isExpand = false;
    private Calendar myCalendar = Calendar.getInstance();
    private boolean firstLoading = false;
    public AdsFeaturedStoreFragment() {
    }

    public static AdsFeaturedStoreFragment newInstance() {
        AdsFeaturedStoreFragment fragment = new AdsFeaturedStoreFragment();
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
        mViewModel = new ViewModelProvider(this).get(AdsFeaturedStoreFragViewModel.class);
        mFragView = inflater.inflate(R.layout.fragment_ads_promotion, container, false);
        ButterKnife.bind(this, mFragView);
        initView();
        return mFragView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.getIsBusy().observe(this, isBusy -> {
            if (isBusy) {
                showLoadingDialog();
            } else {
                hideLoadingDialog();
            }
        });
        mViewModel.getDataList().observe(this, list -> {
            if (offset == 0 && list.size() == 0) {
                lytEmpty.setVisibility(View.VISIBLE);
                return;
            } else {
                lytEmpty.setVisibility(View.GONE);
                if (offset == 0) {
                    dataList.clear();
                }
                if (list.size() < limit) {
                    isLast = true;
                }
                dataList.addAll(list);
                recyclerAdapter.setData(dataList);
                isLoading = false;
            }
        });
        mViewModel.getApp_name().observe(this, name -> {
            if (!TextUtils.isEmpty(name)) {
                app_name = name;
            }
        });
        mViewModel.getModel_name().observe(this, name -> {
            if (!TextUtils.isEmpty(name)) {
                model_name = name;
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initView() {
        nestedScrollView.setNestedScrollingEnabled(false);
        selIDList.clear();
        dataList.clear();
        setRecycler();


        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                initParam();
                mViewModel.setIsBusy(true);
                mViewModel.loadData(keyword, start_date, end_date);
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
                    mViewModel.setOffset(offset);
                    mViewModel.setIsBusy(true);
                    mViewModel.loadData(keyword, start_date, end_date);
                }

            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() == 0) {
                    imgClear.setVisibility(View.GONE);
                } else {
                    imgClear.setVisibility(View.VISIBLE);
                }

            }
        });
    }
    public void loadData() {
        if (!firstLoading) {
            try {
                String local_data = DatabaseQueryClass.getInstance().getData(App.getUserID(), "AdsFeaturedStore", "");
                if (TextUtils.isEmpty(local_data)) {
                    mViewModel.setIsBusy(true);
                } else {
                    mViewModel.loadLocalData();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            mViewModel.loadData(keyword, start_date, end_date);
            firstLoading = true;
        }
    }
    public void showExpand(){
        ObjectAnimator rotate = ObjectAnimator.ofFloat(imgExpand, "rotation", 180f, 0f);
        rotate.setDuration(500);
        rotate.start();
        lytSearch.setVisibility(View.VISIBLE);
    }

    public void hideExpand(){
        ObjectAnimator rotate = ObjectAnimator.ofFloat(imgExpand, "rotation", 0f, 180f);
        rotate.setDuration(500);
        rotate.start();
        lytSearch.setVisibility(View.GONE);
        edtSearch.setText("");
        keyword = "";
        start_date = "";
        end_date = "";
        imgClear.setVisibility(View.GONE);
        txtStartDate.setText(getString(R.string.txt_start_date));
        txtEndDate.setText(getString(R.string.txt_end_date));
    }
    private void initParam() {
        offset = 0;
        isLast = false;
        isLoading = false;
        mViewModel.setOffset(offset);
        selIDList.clear();
    }

    private void setRecycler() {
        RecyclerClickListener listener = new RecyclerClickListener() {
            @Override
            public void onClick(View v, int position) {
                //ckStatus
                onCheckStatus(true);
            }

            @Override
            public void onClick(View v, int position, int type) {
                //swSelect
                Ion.with(getActivity())
                        .load("PUT", G.ActivateAds)
                        .addHeader("Authorization", App.getToken())
                        .addHeader("Content-Language", App.getPortalToken())
                        .setBodyParameter("app_name", app_name)
                        .setBodyParameter("model_name", model_name)
                        .setBodyParameter("id", String.valueOf(dataList.get(position).getId()))
                        .setBodyParameter("field", "active")
                        .setBodyParameter("value", String.valueOf(dataList.get(position).isActive()))
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {

                            }
                        });
            }
        };
        recyclerAdapter = new AdsPostAdapter(getActivity(), dataList, "FeaturedStore", listener);
        recyclerView.setAdapter(recyclerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void onCheckStatus(boolean status) {
        if (status) {
            int checkCount = 0;
            selIDList.clear();
            for (int i = 0; i < dataList.size(); i++) {
                if (dataList.get(i).isCheck()) {
                    selIDList.add(dataList.get(i).getId());
                    checkCount++;
                }
            }
            itemCheckedCount = checkCount;
            if (checkCount > 0) {
                if (checkCount == 1) {
                    lytEdit.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bk_blue_rect_5));
                    icEdit.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_edit_white));
                    txtEdit.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_color));
                } else {
                    lytEdit.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bk_grey_solid));
                    icEdit.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_edit_dark));
                    txtEdit.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_grey_800));
                }
                lytDelete.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bk_red_solid));
                icDelete.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_delete_white));
                txtDelete.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_color));
            } else {
                lytEdit.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bk_grey_solid));
                icEdit.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_edit_dark));
                txtEdit.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_grey_800));

                lytDelete.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bk_grey_solid));
                icDelete.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_delete_dark));
                txtDelete.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_grey_800));
            }
        } else {
            lytEdit.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bk_grey_solid));
            icEdit.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_edit_dark));
            txtEdit.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_grey_800));

            lytDelete.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bk_grey_solid));
            icDelete.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_delete_dark));
            txtDelete.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_grey_800));
        }

    }

    private void onCreatePromotion() {
        Intent intent = new Intent(getActivity(), CreateFeaturedStoreActivity.class);
        startActivity(intent);
    }

    private void onEditPromotion() {
        if (itemCheckedCount == 1) {
            Intent intent = new Intent(getActivity(), CreateFeaturedStoreActivity.class);
            intent.putExtra("id", selIDList.get(0));
            startActivity(intent);
        }
    }

    private void onDeletePromotion() {
        if (itemCheckedCount > 0) {
            G.showLoading(getActivity());
            String oneId = String.valueOf(selIDList.get(0));
            for (int i = 1; i < selIDList.size(); i ++) {
                oneId += "," + selIDList.get(i);
            }
            Ion.with(getActivity())
                    .load("DELETE", G.DeleteAds)
                    .addHeader("Authorization", App.getToken())
                    .addHeader("Content-Language", App.getPortalToken())
                    .setBodyParameter("app_name", app_name)
                    .setBodyParameter("model_name", model_name)
                    .setBodyParameter("id_list", oneId)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            ArrayList<PostModel> tmpList = new ArrayList<>();
                            tmpList.clear();
                            for (int i = 0; i < dataList.size(); i ++) {
                                if (!dataList.get(i).isCheck()) {
                                    tmpList.add(dataList.get(i));
                                }
                            }
                            recyclerAdapter.setData(tmpList);
                            itemCheckedCount= 0;
                            selIDList.clear();
                            onCheckStatus(false);
                            G.hideLoading();
                            Toast.makeText(getActivity(), R.string.msg_deleted_successfully, Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
    private void onSelectDate(String type) {
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
                    txtStartDate.setText(selDate);
                } else {
                    txtEndDate.setText(selDate);
                }
            }

        };
        new DatePickerDialog(getActivity(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void onSearch() {
        keyword = edtSearch.getText().toString();
        start_date = txtStartDate.getText().toString();
        end_date = txtEndDate.getText().toString();
        if (!TextUtils.isEmpty(start_date) && TextUtils.isEmpty(end_date)) {
            Toast.makeText(getActivity(), R.string.msg_select_end_date, Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(start_date) && !TextUtils.isEmpty(end_date)) {
            Toast.makeText(getActivity(), R.string.msg_select_start_date, Toast.LENGTH_LONG).show();
            return;
        }
        if (!TextUtils.isEmpty(start_date) && !TextUtils.isEmpty(end_date)) {
            @SuppressLint("SimpleDateFormat") int dateDifference = (int) getDateDiff(new SimpleDateFormat("yyyy-MM-dd"), start_date, end_date);
            if (dateDifference < 0) {
                Toast.makeText(getActivity(), R.string.msg_select_end_date, Toast.LENGTH_LONG).show();
                return;
            }
        }

        mViewModel.loadData(keyword, start_date, end_date);
    }
    @OnClick({R.id.btnCreate, R.id.lytEdit, R.id.lytDelete, R.id.imgExpand, R.id.txtStartDate, R.id.txtEndDate, R.id.btnSearch, R.id.imgClear})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.imgClear:
                edtSearch.setText("");
                keyword = "";
                break;
            case R.id.btnCreate:
                onCreatePromotion();
                break;
            case R.id.btnSearch:
                onSearch();
                break;
            case R.id.lytEdit:
                onEditPromotion();
                break;
            case R.id.lytDelete:
                onDeletePromotion();
                break;
            case R.id.txtStartDate:
                onSelectDate("start");
                break;
            case R.id.txtEndDate:
                onSelectDate("end");
                break;
            case R.id.imgExpand:
                if (isExpand) {
                    hideExpand();
                } else {
                    showExpand();
                }
                isExpand = !isExpand;
                break;
        }
    }
}