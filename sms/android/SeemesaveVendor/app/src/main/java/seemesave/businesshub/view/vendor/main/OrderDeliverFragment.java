package seemesave.businesshub.view.vendor.main;

import static seemesave.businesshub.utils.TimeUtils.getDateFromUTCTimestamp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import seemesave.businesshub.R;
import seemesave.businesshub.adapter.DeliverOrderAdapter;
import seemesave.businesshub.api.order.OrderApi;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseFragment;
import seemesave.businesshub.model.common.DeliverOrderModel;
import seemesave.businesshub.model.res.DeliverOrderListRes;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.view_model.vendor.main.StoreFragViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDeliverFragment extends BaseFragment {
    private StoreFragViewModel mViewModel;
    private View mFragView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.lytEmpty)
    LinearLayout lytEmpty;

    private DeliverOrderAdapter deliverOrderAdapter;
    private ArrayList<DeliverOrderModel> orderList = new ArrayList<>();
    private ArrayList<DeliverOrderModel> pendingList = new ArrayList<>();
    private ArrayList<DeliverOrderModel> pickingList = new ArrayList<>();
    private ArrayList<DeliverOrderModel> collectionList = new ArrayList<>();
    private ArrayList<DeliverOrderModel> deliverList = new ArrayList<>();
    private ArrayList<DeliverOrderModel> failedList = new ArrayList<>();
    private ArrayList<DeliverOrderModel> successList = new ArrayList<>();

    private int limit = 20;
    private int offset = 0;
    private boolean isLoading = false;
    private boolean isLast = false;

    private boolean firstSuccess = false;

    @BindView(R.id.edtSearch)
    EditText edtSearch;
    @BindView(R.id.imgSearch)
    ImageView imgSearch;
    @BindView(R.id.imgClear)
    ImageView imgClear;

    @BindView(R.id.txtOrder)
    TextView txtOrder;
    @BindView(R.id.txtPending)
    TextView txtPending;
    @BindView(R.id.txtPickingOrder)
    TextView txtPickingOrder;
    @BindView(R.id.txtReadyCollect)
    TextView txtReadyCollect;
    @BindView(R.id.txtDeliveringOrder)
    TextView txtDeliveringOrder;
    @BindView(R.id.txtFailedOrder)
    TextView txtFailedOrder;
    @BindView(R.id.txtCompleted)
    TextView txtCompleted;

    private int curTab = 0;
    private String keyword = "";
    private String pickup_time = "";

    BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "refresh_deliver_data":
                    curTab = 1;
                    reload(false);
                    break;
            }
        }
    };
    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("refresh_deliver_data");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateReceiver,
                filter);
    }

    public OrderDeliverFragment() {
    }

    public static OrderDeliverFragment newInstance() {
        OrderDeliverFragment fragment = new OrderDeliverFragment();
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
        mViewModel = new ViewModelProvider(this).get(StoreFragViewModel.class);
        mFragView = inflater.inflate(R.layout.fragment_order_deliver, container, false);
        ButterKnife.bind(this, mFragView);
        initView();
        registerBroadcast();
        return mFragView;
    }


    private void initView() {
        orderList.clear();
        pendingList.clear();
        pickingList.clear();
        collectionList.clear();
        deliverList.clear();
        failedList.clear();
        successList.clear();
        nestedScrollView.setNestedScrollingEnabled(false);
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
        edtSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    keyword = edtSearch.getText().toString().toLowerCase();
                    reload(true);
                    return true;
                }
                return false;
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                reload(true);
            }
        });
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (curTab == 5) {
                    View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
                    int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));
                    if (diff == 0 && !isLoading && !isLast) {
                        isLoading = true;
                        offset = offset + limit;
                        showLoadingDialog();
                        OrderApi.getDeliverSuccessList(keyword, offset, limit);
                    }
                }
            }
        });
        setOrderRecycler();
        showLoadingDialog();
        OrderApi.getDeliverList(keyword);
    }
    private void reload(boolean loadDlg) {
        initParam();
        if (loadDlg)
            showLoadingDialog();
        if (curTab == 6) {
            OrderApi.getDeliverSuccessList(keyword, offset, limit);
        } else {
            OrderApi.getDeliverList(keyword);
        }
    }
    private void initParam() {
        if (curTab == 6) {
            offset = 0;
            isLoading = false;
            isLast = false;
            successList.clear();
        } else {
            orderList.clear();
            pendingList.clear();
            pickingList.clear();
            collectionList.clear();
            deliverList.clear();
            failedList.clear();
        }
    }
    private void onShowReadyDlg(int pos, DeliverOrderModel model) {
        Long tsLong = System.currentTimeMillis()/1000 + 90 * 60;
        pickup_time = getDateFromUTCTimestamp(tsLong, "yyyy-MM-dd hh:mm");
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dlg_ready_time, null);

        final android.app.AlertDialog dlg = new android.app.AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dlg.setCanceledOnTouchOutside(true);
        TextInputEditText edtNumber = dialogView.findViewById(R.id.edtNumber);
        edtNumber.setText(pickup_time);
        TextView btnYes = dialogView.findViewById(R.id.btnYes);
        edtNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentDate = Calendar.getInstance();
//                date = Calendar.getInstance();
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        String selMonth = String.valueOf(monthOfYear + 1);
                        if (monthOfYear < 9) {
                            selMonth = "0" + selMonth;
                        }
                        String selDay = String.valueOf(dayOfMonth);
                        if (dayOfMonth < 10) {
                            selDay = "0" + selDay;
                        }
                        String selDate = String.format(java.util.Locale.US, "%s-%s-%s", year, selMonth, selDay);

                        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String selHour = String.valueOf(hourOfDay);
                                if (hourOfDay < 10) {
                                    selHour = "0" + selHour;
                                }
                                String selMinute = String.valueOf(minute);
                                if (minute < 10) {
                                    selMinute = "0" + selMinute;
                                }

                                pickup_time = String.format(java.util.Locale.US, "%s %s:%s", selDate, selHour, selMinute);
                                edtNumber.setText(pickup_time);

                            }
                        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date = null;
                try {
                    date = format.parse(pickup_time + ":00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (System.currentTimeMillis() > date.getTime()) {
                    Toast.makeText(getActivity(), R.string.msg_confirm_order_time, Toast.LENGTH_LONG).show();
                    return;
                }

                dlg.dismiss();
                showLoadingDialog();
                Ion.with(getActivity())
                        .load("POST", G.SetDeliverOrderReady)
                        .addHeader("Authorization", App.getToken())
                        .addHeader("Content-Language", App.getPortalToken())
                        .setBodyParameter("id", String.valueOf(model.getId()))
                        .setBodyParameter("pickupTime", pickup_time + ":00")
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                hideLoadingDialog();
                                pickingList.remove(pos);
                                deliverOrderAdapter.setData(pickingList);
                                setPage(3);
                            }
                        });
            }
        });

        dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dlg.show();



    }
    private void setOrderRecycler() {
        deliverOrderAdapter = new DeliverOrderAdapter(getActivity(), orderList, new DeliverOrderAdapter.OrderRecyclerListener() {

            @Override
            public void onItemClicked(int pos, DeliverOrderModel model) {
                if (model.getCart_status() == 2) {
                    showLoadingDialog();
                    Ion.with(getActivity())
                            .load("POST", G.SetDeliverOrderPicking)
                            .addHeader("Authorization", App.getToken())
                            .addHeader("Content-Language", App.getPortalToken())
                            .setBodyParameter("id", String.valueOf(model.getId()))
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {
                                    hideLoadingDialog();
                                    orderList.remove(pos);
                                    deliverOrderAdapter.setData(orderList);
                                    setPage(2);
                                }
                            });
                } else if (model.getCart_status() == 12) {
                    onShowReadyDlg(pos, model);
                } else {
                    showLoadingDialog();
                    Ion.with(getActivity())
                            .load("GET", G.GetDeliverOrderStatus + model.getId())
                            .addHeader("Authorization", App.getToken())
                            .addHeader("Content-Language", App.getPortalToken())
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {
                                    hideLoadingDialog();
                                    if (e != null) {
                                        Toast.makeText(getActivity(), R.string.msg_something_wrong, Toast.LENGTH_LONG).show();
                                    } else {
                                        try {
                                            JSONObject jsonObject = new JSONObject(result);
                                            if (jsonObject.getBoolean("status")) {
                                                if (model.getCart_status() == 13) {
                                                    //collection
                                                    model.setCart_status(jsonObject.getJSONObject("data").getInt("cart_status"));
                                                    collectionList.set(pos, model);
                                                    deliverOrderAdapter.setData(collectionList);
                                                } else if (model.getCart_status() == 30 || model.getCart_status() == 31) {
                                                    //success
                                                    model.setCart_status(jsonObject.getJSONObject("data").getInt("cart_status"));
                                                    successList.set(pos, model);
                                                    deliverOrderAdapter.setData(successList);
                                                } else {
                                                    //deliver
                                                    model.setCart_status(jsonObject.getJSONObject("data").getInt("cart_status"));
                                                    successList.set(pos, model);
                                                    deliverOrderAdapter.setData(successList);
                                                }
                                            } else {
                                                Toast.makeText(getActivity(), jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException jsonException) {
                                        }
                                    }

                                }
                            });
                }
            }
        });
        recyclerView.setAdapter(deliverOrderAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @SuppressLint("ResourceAsColor")
    private void initPage() {
        txtOrder.setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_600));
        txtPending.setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_600));
        txtPickingOrder.setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_600));
        txtReadyCollect.setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_600));
        txtDeliveringOrder.setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_600));
        txtFailedOrder.setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_600));
        txtCompleted.setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_600));
        txtOrder.setTypeface(null, Typeface.NORMAL);
        txtPending.setTypeface(null, Typeface.NORMAL);
        txtPickingOrder.setTypeface(null, Typeface.NORMAL);
        txtReadyCollect.setTypeface(null, Typeface.NORMAL);
        txtDeliveringOrder.setTypeface(null, Typeface.NORMAL);
        txtFailedOrder.setTypeface(null, Typeface.NORMAL);
        txtCompleted.setTypeface(null, Typeface.NORMAL);
    }

    private void setPage(int pos) {
        if (isLoading) {
            return;
        }
        initPage();
        curTab = pos;
        lytEmpty.setVisibility(View.GONE);
        switch (pos) {
            case 0:
                txtOrder.setTextColor(ContextCompat.getColor(getContext(), R.color.main_blue_color));
                txtOrder.setTypeface(null, Typeface.BOLD);
                deliverOrderAdapter.setData(orderList);
                if (orderList.size() == 0) {
                    lytEmpty.setVisibility(View.VISIBLE);
                } else {
                    lytEmpty.setVisibility(View.GONE);
                }
                break;
            case 1:
                txtPending.setTextColor(ContextCompat.getColor(getContext(), R.color.main_blue_color));
                txtPending.setTypeface(null, Typeface.BOLD);
                deliverOrderAdapter.setData(pendingList);
                if (pendingList.size() == 0) {
                    lytEmpty.setVisibility(View.VISIBLE);
                } else {
                    lytEmpty.setVisibility(View.GONE);
                }
                break;
            case 2:
                txtPickingOrder.setTextColor(ContextCompat.getColor(getContext(), R.color.main_blue_color));
                txtPickingOrder.setTypeface(null, Typeface.BOLD);
                deliverOrderAdapter.setData(pickingList);
                if (pickingList.size() == 0) {
                    lytEmpty.setVisibility(View.VISIBLE);
                } else {
                    lytEmpty.setVisibility(View.GONE);
                }
                break;
            case 3:
                txtReadyCollect.setTextColor(ContextCompat.getColor(getContext(), R.color.main_blue_color));
                txtReadyCollect.setTypeface(null, Typeface.BOLD);
                deliverOrderAdapter.setData(collectionList);
                if (collectionList.size() == 0) {
                    lytEmpty.setVisibility(View.VISIBLE);
                } else {
                    lytEmpty.setVisibility(View.GONE);
                }
                break;
            case 4:
                txtDeliveringOrder.setTextColor(ContextCompat.getColor(getContext(), R.color.main_blue_color));
                txtDeliveringOrder.setTypeface(null, Typeface.BOLD);
                deliverOrderAdapter.setData(deliverList);
                if (deliverList.size() == 0) {
                    lytEmpty.setVisibility(View.VISIBLE);
                } else {
                    lytEmpty.setVisibility(View.GONE);
                }
                break;
            case 5:
                txtFailedOrder.setTextColor(ContextCompat.getColor(getContext(), R.color.main_blue_color));
                txtFailedOrder.setTypeface(null, Typeface.BOLD);
                deliverOrderAdapter.setData(failedList);
                if (failedList.size() == 0) {
                    lytEmpty.setVisibility(View.VISIBLE);
                } else {
                    lytEmpty.setVisibility(View.GONE);
                }
                break;
            case 6:
                txtCompleted.setTextColor(ContextCompat.getColor(getContext(), R.color.main_blue_color));
                txtCompleted.setTypeface(null, Typeface.BOLD);
                if (!firstSuccess) {
                    reload(true);
                } else {
                    deliverOrderAdapter.setData(successList);
                    if (successList.size() == 0) {
                        lytEmpty.setVisibility(View.VISIBLE);
                    } else {
                        lytEmpty.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    @OnClick({R.id.txtOrder, R.id.txtPending, R.id.txtPickingOrder, R.id.txtReadyCollect, R.id.txtDeliveringOrder, R.id.txtFailedOrder, R.id.txtCompleted, R.id.imgClear})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.txtOrder:
                setPage(0);
                break;
            case R.id.txtPending:
                setPage(1);
                break;
            case R.id.txtPickingOrder:
                setPage(2);
                break;
            case R.id.txtReadyCollect:
                setPage(3);
                break;
            case R.id.txtDeliveringOrder:
                setPage(4);
                break;
            case R.id.txtFailedOrder:
                setPage(5);
                break;
            case R.id.txtCompleted:
                setPage(6);
                break;
            case R.id.imgClear:
                edtSearch.setText("");
                keyword = "";
                reload(true);
                break;
        }
    }

    private void initData(ArrayList<DeliverOrderModel> list) {
        if (curTab == 6) {
            if (offset == 0 && list.size() == 0) {
                successList.clear();
                deliverOrderAdapter.setData(successList);
                lytEmpty.setVisibility(View.VISIBLE);
                return;
            } else {
                lytEmpty.setVisibility(View.GONE);
                if (offset == 0) {
                    successList.clear();
                }
                if (list.size() < limit) {
                    isLast = true;
                }
                successList.addAll(list);
                deliverOrderAdapter.setData(successList);
                isLoading = false;
            }
            firstSuccess = true;
        } else {
            for (int i = 0; i < list.size(); i ++) {
                if (list.get(i).getCart_status() == 2) {
                    orderList.add(list.get(i));
                }  else if (list.get(i).getCart_status() == 10) {
                    pendingList.add(list.get(i));
                } else if (list.get(i).getCart_status() == 12) {
                    pickingList.add(list.get(i));
                } else if (list.get(i).getCart_status() == 13) {
                    collectionList.add(list.get(i));
                } else if (list.get(i).getCart_status() == 50) {
                    failedList.add(list.get(i));
                } else {
                    deliverList.add(list.get(i));
                }
            }
            switch (curTab) {
                case 0:
                    deliverOrderAdapter.setData(orderList);
                    if (orderList.size() == 0) {
                        lytEmpty.setVisibility(View.VISIBLE);
                    } else {
                        lytEmpty.setVisibility(View.GONE);
                    }
                    break;
                case 1:
                    deliverOrderAdapter.setData(pendingList);
                    if (pendingList.size() == 0) {
                        lytEmpty.setVisibility(View.VISIBLE);
                    } else {
                        lytEmpty.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    deliverOrderAdapter.setData(pickingList);
                    if (pickingList.size() == 0) {
                        lytEmpty.setVisibility(View.VISIBLE);
                    } else {
                        lytEmpty.setVisibility(View.GONE);
                    }
                    break;
                case 3:
                    deliverOrderAdapter.setData(collectionList);
                    if (collectionList.size() == 0) {
                        lytEmpty.setVisibility(View.VISIBLE);
                    } else {
                        lytEmpty.setVisibility(View.GONE);
                    }
                    break;
                case 4:
                    deliverOrderAdapter.setData(deliverList);
                    if (deliverList.size() == 0) {
                        lytEmpty.setVisibility(View.VISIBLE);
                    } else {
                        lytEmpty.setVisibility(View.GONE);
                    }
                    break;
                case 5:
                    deliverOrderAdapter.setData(failedList);
                    if (failedList.size() == 0) {
                        lytEmpty.setVisibility(View.VISIBLE);
                    } else {
                        lytEmpty.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessList(DeliverOrderListRes res) {
        hideLoadingDialog();
        if (res.isStatus()) {
            initData(res.getDataList());
        } else {
            if (!TextUtils.isEmpty(res.getMessage())) {
                Toast.makeText(getActivity(), res.getMessage(), Toast.LENGTH_LONG).show();    
            }
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