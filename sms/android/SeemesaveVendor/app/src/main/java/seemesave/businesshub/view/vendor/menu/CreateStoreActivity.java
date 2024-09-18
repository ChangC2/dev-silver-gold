package seemesave.businesshub.view.vendor.menu;

import static seemesave.businesshub.utils.G.ADDRESS_PICKER_REQUEST;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.OneSelectionAdapter;
import seemesave.businesshub.adapter.ProductSelectAdapter;
import seemesave.businesshub.adapter.StoreCategorySelectAdapter;
import seemesave.businesshub.adapter.TimeListAdapter;
import seemesave.businesshub.api.store.StoreApi;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.listener.RecyclerClickListener;
import seemesave.businesshub.model.common.CurrencyModel;
import seemesave.businesshub.model.common.ProductDetailModel;
import seemesave.businesshub.model.common.StoreCategoryModel;
import seemesave.businesshub.model.common.StoreModel;
import seemesave.businesshub.model.common.TimeListModel;
import seemesave.businesshub.model.common.TimeZoneModel;
import seemesave.businesshub.model.res.BaseInfoRes;
import seemesave.businesshub.model.res.StoreRes;
import seemesave.businesshub.sqlite.DatabaseQueryClass;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.utils.GsonUtils;

public class CreateStoreActivity extends BaseActivity {

    private CreateStoreActivity activity;

    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.ckCollect)
    CheckBox ckCollect;
    @BindView(R.id.ckDeliver)
    CheckBox ckDeliver;
    @BindView(R.id.edtName)
    TextInputEditText edtName;
    @BindView(R.id.edtCategory)
    TextInputEditText edtCategory;
    @BindView(R.id.edtDescription)
    TextInputEditText edtDescription;
    @BindView(R.id.edtNumber)
    TextInputEditText edtNumber;
    @BindView(R.id.edtBuildingNumber)
    TextInputEditText edtBuildingNumber;
    @BindView(R.id.edtVatNumber)
    TextInputEditText edtVatNumber;
    @BindView(R.id.edtStreet1)
    TextInputEditText edtStreet1;
    @BindView(R.id.edtStreet2)
    TextInputEditText edtStreet2;
    @BindView(R.id.edtSuburb)
    TextInputEditText edtSuburb;
    @BindView(R.id.edtCity)
    TextInputEditText edtCity;
    @BindView(R.id.edtState)
    TextInputEditText edtState;
    @BindView(R.id.edtCountry)
    TextInputEditText edtCountry;
    @BindView(R.id.edtPostalCode)
    TextInputEditText edtPostalCode;

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

    @BindView(R.id.ckSun)
    CheckBox ckSun;
    @BindView(R.id.btnSun)
    LinearLayout btnSun;
    @BindView(R.id.txtSunday)
    TextView txtSunday;
    @BindView(R.id.txtSunStart1)
    TextView txtSunStart1;
    @BindView(R.id.txtSunEnd1)
    TextView txtSunEnd1;
    @BindView(R.id.txtSunStart2)
    TextView txtSunStart2;
    @BindView(R.id.txtSunEnd2)
    TextView txtSunEnd2;
    @BindView(R.id.lytSunStart1)
    LinearLayout lytSunStart1;
    @BindView(R.id.lytSunEnd1)
    LinearLayout lytSunEnd1;
    @BindView(R.id.lytSunStart2)
    LinearLayout lytSunStart2;
    @BindView(R.id.lytSunEnd2)
    LinearLayout lytSunEnd2;

    @BindView(R.id.ckMon)
    CheckBox ckMon;
    @BindView(R.id.btnMon)
    LinearLayout btnMon;
    @BindView(R.id.txtMonday)
    TextView txtMonday;
    @BindView(R.id.txtMonStart1)
    TextView txtMonStart1;
    @BindView(R.id.txtMonEnd1)
    TextView txtMonEnd1;
    @BindView(R.id.txtMonStart2)
    TextView txtMonStart2;
    @BindView(R.id.txtMonEnd2)
    TextView txtMonEnd2;
    @BindView(R.id.lytMonStart1)
    LinearLayout lytMonStart1;
    @BindView(R.id.lytMonEnd1)
    LinearLayout lytMonEnd1;
    @BindView(R.id.lytMonStart2)
    LinearLayout lytMonStart2;
    @BindView(R.id.lytMonEnd2)
    LinearLayout lytMonEnd2;

    @BindView(R.id.ckTue)
    CheckBox ckTue;
    @BindView(R.id.btnTue)
    LinearLayout btnTue;
    @BindView(R.id.txtTuesday)
    TextView txtTuesday;
    @BindView(R.id.txtTueStart1)
    TextView txtTueStart1;
    @BindView(R.id.txtTueEnd1)
    TextView txtTueEnd1;
    @BindView(R.id.txtTueStart2)
    TextView txtTueStart2;
    @BindView(R.id.txtTueEnd2)
    TextView txtTueEnd2;
    @BindView(R.id.lytTueStart1)
    LinearLayout lytTueStart1;
    @BindView(R.id.lytTueEnd1)
    LinearLayout lytTueEnd1;
    @BindView(R.id.lytTueStart2)
    LinearLayout lytTueStart2;
    @BindView(R.id.lytTueEnd2)
    LinearLayout lytTueEnd2;

    @BindView(R.id.ckWed)
    CheckBox ckWed;
    @BindView(R.id.btnWed)
    LinearLayout btnWed;
    @BindView(R.id.txtWednesday)
    TextView txtWednesday;
    @BindView(R.id.txtWedStart1)
    TextView txtWedStart1;
    @BindView(R.id.txtWedEnd1)
    TextView txtWedEnd1;
    @BindView(R.id.txtWedStart2)
    TextView txtWedStart2;
    @BindView(R.id.txtWedEnd2)
    TextView txtWedEnd2;
    @BindView(R.id.lytWedStart1)
    LinearLayout lytWedStart1;
    @BindView(R.id.lytWedEnd1)
    LinearLayout lytWedEnd1;
    @BindView(R.id.lytWedStart2)
    LinearLayout lytWedStart2;
    @BindView(R.id.lytWedEnd2)
    LinearLayout lytWedEnd2;

    @BindView(R.id.ckThu)
    CheckBox ckThu;
    @BindView(R.id.btnThu)
    LinearLayout btnThu;
    @BindView(R.id.txtThursday)
    TextView txtThursday;
    @BindView(R.id.txtThuStart1)
    TextView txtThuStart1;
    @BindView(R.id.txtThuEnd1)
    TextView txtThuEnd1;
    @BindView(R.id.txtThuStart2)
    TextView txtThuStart2;
    @BindView(R.id.txtThuEnd2)
    TextView txtThuEnd2;
    @BindView(R.id.lytThuStart1)
    LinearLayout lytThuStart1;
    @BindView(R.id.lytThuEnd1)
    LinearLayout lytThuEnd1;
    @BindView(R.id.lytThuStart2)
    LinearLayout lytThuStart2;
    @BindView(R.id.lytThuEnd2)
    LinearLayout lytThuEnd2;

    @BindView(R.id.ckFri)
    CheckBox ckFri;
    @BindView(R.id.btnFri)
    LinearLayout btnFri;
    @BindView(R.id.txtFriday)
    TextView txtFriday;
    @BindView(R.id.txtFriStart1)
    TextView txtFriStart1;
    @BindView(R.id.txtFriEnd1)
    TextView txtFriEnd1;
    @BindView(R.id.txtFriStart2)
    TextView txtFriStart2;
    @BindView(R.id.txtFriEnd2)
    TextView txtFriEnd2;
    @BindView(R.id.lytFriStart1)
    LinearLayout lytFriStart1;
    @BindView(R.id.lytFriEnd1)
    LinearLayout lytFriEnd1;
    @BindView(R.id.lytFriStart2)
    LinearLayout lytFriStart2;
    @BindView(R.id.lytFriEnd2)
    LinearLayout lytFriEnd2;

    @BindView(R.id.ckSat)
    CheckBox ckSat;
    @BindView(R.id.btnSat)
    LinearLayout btnSat;
    @BindView(R.id.txtSaturday)
    TextView txtSaturday;
    @BindView(R.id.txtSatStart1)
    TextView txtSatStart1;
    @BindView(R.id.txtSatEnd1)
    TextView txtSatEnd1;
    @BindView(R.id.txtSatStart2)
    TextView txtSatStart2;
    @BindView(R.id.txtSatEnd2)
    TextView txtSatEnd2;
    @BindView(R.id.lytSatStart1)
    LinearLayout lytSatStart1;
    @BindView(R.id.lytSatEnd1)
    LinearLayout lytSatEnd1;
    @BindView(R.id.lytSatStart2)
    LinearLayout lytSatStart2;
    @BindView(R.id.lytSatEnd2)
    LinearLayout lytSatEnd2;

    @BindView(R.id.spinnerCurrency)
    Spinner spinnerCurrency;
    private ArrayAdapter<CurrencyModel> currencyAdapter;
    private ArrayList<CurrencyModel> currencyList = new ArrayList<>();
    private String selCurrency = "ZAR";

    private ArrayList<StoreCategoryModel> storeCategoryList = new ArrayList<>();
    StoreCategorySelectAdapter storeCategorySelectAdapter;

    @BindView(R.id.spinnerTimezone)
    Spinner spinnerTimezone;
    ArrayAdapter<TimeZoneModel> spinnerTimezoneAdapter;
    ArrayList<TimeZoneModel> arrayTimezone = new ArrayList<>();
    private String selTimezone = "";
    private int selTimeOffset = 0;

    @BindView(R.id.edtTag)
    TextInputEditText edtTag;
    @BindView(R.id.tagRecyclerView)
    RecyclerView tagRecyclerView;
    private OneSelectionAdapter tagAdapter;
    private ArrayList<String> tagList = new ArrayList<>();

    @BindView(R.id.edtAddress)
    TextInputEditText edtAddress;

    private int curStep = 0;
    private boolean isSun = false, isMon = false, isTue = false, isWed = false, isThu = false, isFri = false, isSat = false;
    private String selAddress = "";
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;
    private ArrayList<TimeListModel> timeList = new ArrayList<>();

    private int id = -1;
    private StoreModel storeModel = new StoreModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_store);
        ButterKnife.bind(this);
        activity = this;
        storeCategoryList.clear();
        getLocalList();
        initView();
        id = getIntent().getIntExtra("id", -1);
        if (id != -1) {
            showLoadingDialog();
            StoreApi.getStoreDetail(id);
        }
    }


    private void initView() {
        tagList.clear();
        timeList.clear();
        for (int i = 0 ; i < 24; i++) {
            String times = "";
            if (i < 10) {
                times = "0" + i;
            } else {
                times = String.valueOf(i);
            }
            times += ":00";
            TimeListModel timeListModel = new TimeListModel();
            timeListModel.setCheck(false);
            timeListModel.setName(times);
            timeList.add(timeListModel);
        }
        setView();
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
        ckSun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSun = isChecked;
                onCheckDay(0);
            }
        });
        ckMon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isMon = isChecked;
                onCheckDay(1);
            }
        });
        ckTue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isTue = isChecked;
                onCheckDay(2);
            }
        });
        ckWed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isWed = isChecked;
                onCheckDay(3);
            }
        });
        ckThu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isThu = isChecked;
                onCheckDay(4);
            }
        });
        ckFri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isFri = isChecked;
                onCheckDay(5);
            }
        });
        ckSat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSat = isChecked;
                onCheckDay(6);
            }
        });
        setTagAdapter();
    }
    private void getLocalList() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(App.getUserID(), "BaseInfo", "");
            if (!TextUtils.isEmpty(data)) {
                BaseInfoRes localRes = GsonUtils.getInstance().fromJson(data, BaseInfoRes.class);
                arrayTimezone.addAll(localRes.getTimezoneList());
                currencyList.addAll(localRes.getCurrencyList());
                storeCategoryList.addAll(localRes.getStoreCategoryList());
                setTimezoneSpinner();
                setCurrencyAdapter();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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



    private void onShowTimeDlg(String dateType, int datePos, String selDate) {
        View dialogView = activity.getLayoutInflater().inflate(R.layout.dlg_choose_time, null);

        final android.app.AlertDialog dlg = new android.app.AlertDialog.Builder(activity)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dlg.setCanceledOnTouchOutside(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);

        RecyclerClickListener mListner = new RecyclerClickListener() {
            @Override
            public void onClick(View v, int vPosition) {
                setDateTxt(dateType, datePos, timeList.get(vPosition).getName());
                dlg.dismiss();
            }

            @Override
            public void onClick(View v, int vPosition, int type) {

            }
        };
        TimeListAdapter variantsAdapter = new TimeListAdapter(activity, selDate, timeList, mListner);
        recyclerView.setAdapter(variantsAdapter);

        dlg.setCanceledOnTouchOutside(false);
        dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dlg.show();

    }
    private void setDateTxt(String dateType, int datePos, String date) {
        switch (dateType) {
            case "sun":
                if (datePos == 1) {
                    txtSunStart1.setText(date);
                } else if (datePos == 2) {
                    txtSunEnd1.setText(date);
                } else if (datePos == 3) {
                    txtSunStart2.setText(date);
                } else if (datePos == 4) {
                    txtSunEnd2.setText(date);
                }
                break;
            case "mon":
                if (datePos == 1) {
                    txtMonStart1.setText(date);
                } else if (datePos == 2) {
                    txtMonEnd1.setText(date);
                } else if (datePos == 3) {
                    txtMonStart2.setText(date);
                } else if (datePos == 4) {
                    txtMonEnd2.setText(date);
                }
                break;
            case "tue":
                if (datePos == 1) {
                    txtTueStart1.setText(date);
                } else if (datePos == 2) {
                    txtTueEnd1.setText(date);
                } else if (datePos == 3) {
                    txtTueStart2.setText(date);
                } else if (datePos == 4) {
                    txtTueEnd2.setText(date);
                }
                break;
            case "wed":
                if (datePos == 1) {
                    txtWedStart1.setText(date);
                } else if (datePos == 2) {
                    txtWedEnd1.setText(date);
                } else if (datePos == 3) {
                    txtWedStart2.setText(date);
                } else if (datePos == 4) {
                    txtWedEnd2.setText(date);
                }
                break;
            case "thu":
                if (datePos == 1) {
                    txtThuStart1.setText(date);
                } else if (datePos == 2) {
                    txtThuEnd1.setText(date);
                } else if (datePos == 3) {
                    txtThuStart2.setText(date);
                } else if (datePos == 4) {
                    txtThuEnd2.setText(date);
                }
                break;
            case "fri":
                if (datePos == 1) {
                    txtFriStart1.setText(date);
                } else if (datePos == 2) {
                    txtFriEnd1.setText(date);
                } else if (datePos == 3) {
                    txtFriStart2.setText(date);
                } else if (datePos == 4) {
                    txtFriEnd2.setText(date);
                }
                break;
            case "sat":
                if (datePos == 1) {
                    txtSatStart1.setText(date);
                } else if (datePos == 2) {
                    txtSatEnd1.setText(date);
                } else if (datePos == 3) {
                    txtSatStart2.setText(date);
                } else if (datePos == 4) {
                    txtSatEnd2.setText(date);
                }
                break;
        }
    }
    private void setCurrencyAdapter() {
        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selCurrency = currencyList.get(position).getIso();
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

    private void setTimezoneSpinner() {
        spinnerTimezone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selTimezone = arrayTimezone.get(position).getTime_zone();
                selTimeOffset = arrayTimezone.get(position).getTime_offset();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerTimezoneAdapter = new ArrayAdapter<TimeZoneModel>(this, R.layout.custom_spinner, arrayTimezone);
        spinnerTimezoneAdapter.setDropDownViewResource(R.layout.custom_spinner_combo);
        spinnerTimezone.setAdapter(spinnerTimezoneAdapter);
        spinnerTimezone.setSelection(getTimezoneIndex());
    }

    private int getTimezoneIndex() {
        for (int i = 0; i < arrayTimezone.size(); i++) {
            if (arrayTimezone.get(i).getTime_zone().equalsIgnoreCase(selTimezone)) {
                return i;
            }
        }
        return -1;
    }

    private void onNext() {
        if (validation()) {
            if (curStep == 3) {
                onCreateStore();
            } else {
                curStep++;
                setView();
            }
        } else {
            Toast.makeText(activity, R.string.txt_msg_finish_info, Toast.LENGTH_LONG).show();
        }

    }

    private boolean validation() {
        boolean isValid = true;
        switch (curStep) {
            case 0:
                if ((!ckCollect.isChecked() && !ckDeliver.isChecked()) || TextUtils.isEmpty(edtName.getText().toString()) || TextUtils.isEmpty(edtAddress.getText().toString())) {
                    isValid = false;
                } else {
                    isValid = false;
                    for (int i = 0; i < storeCategoryList.size(); i ++) {
                        if (storeCategoryList.get(i).isCheck()) {
                            isValid = true;
                        }
                    }
                }

                break;
            case 1:
                if (TextUtils.isEmpty(edtBuildingNumber.getText().toString()) || TextUtils.isEmpty(edtVatNumber.getText().toString())) {
                    isValid = false;
                }
                break;
            case 2:
                if (!ckSun.isChecked() && !ckMon.isChecked() && !ckTue.isChecked() && !ckWed.isChecked() && !ckThu.isChecked() && !ckFri.isChecked() && !ckSat.isChecked()) {
                    isValid = false;
                }
                break;
            case 3:
                if (TextUtils.isEmpty(edtStreet1.getText().toString()) || TextUtils.isEmpty(edtSuburb.getText().toString()) || TextUtils.isEmpty(edtCity.getText().toString()) || TextUtils.isEmpty(edtState.getText().toString()) || TextUtils.isEmpty(edtCountry.getText().toString()) || TextUtils.isEmpty(edtPostalCode.getText().toString())) {
                    isValid = false;
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
                txtStep.setText(getString(R.string.txt_store_slide1));
                imgStep.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_add_store_step1));
                btnBack.setVisibility(View.GONE);
                btnNext.setVisibility(View.VISIBLE);
                lytSlide1.setVisibility(View.VISIBLE);
                lytSlide2.setVisibility(View.GONE);
                lytSlide3.setVisibility(View.GONE);
                lytSlide4.setVisibility(View.GONE);
                break;
            case 1:
                txtStep.setText(getString(R.string.txt_store_slide2));
                imgStep.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_add_store_step2));
                btnBack.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.VISIBLE);
                lytSlide1.setVisibility(View.GONE);
                lytSlide2.setVisibility(View.VISIBLE);
                lytSlide3.setVisibility(View.GONE);
                lytSlide4.setVisibility(View.GONE);
                break;
            case 2:
                txtStep.setText(getString(R.string.txt_store_slide3));
                imgStep.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_add_store_step3));
                btnBack.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.VISIBLE);
                lytSlide1.setVisibility(View.GONE);
                lytSlide2.setVisibility(View.GONE);
                lytSlide3.setVisibility(View.VISIBLE);
                lytSlide4.setVisibility(View.GONE);
                break;
            case 3:
                txtStep.setText(getString(R.string.txt_store_slide4));
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

    private void onCheckDay(int day) {
        switch (day) {
            case 0:
                if (isSun) {
                    btnSun.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_blue_rect_5));
                    txtSunday.setTextColor(ContextCompat.getColor(activity, R.color.white_color));
                    lytSunStart1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtSunStart1.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                    lytSunEnd1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtSunEnd1.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                    lytSunStart2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtSunStart2.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                    lytSunEnd2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtSunEnd2.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                } else {
                    btnSun.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_grey_solid));
                    txtSunday.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytSunStart1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtSunStart1.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytSunEnd1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtSunEnd1.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytSunStart2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtSunStart2.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytSunEnd2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtSunEnd2.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                }
                break;
            case 1:
                if (isMon) {
                    btnMon.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_blue_rect_5));
                    txtMonday.setTextColor(ContextCompat.getColor(activity, R.color.white_color));
                    lytMonStart1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtMonStart1.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                    lytMonEnd1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtMonEnd1.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                    lytMonStart2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtMonStart2.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                    lytMonEnd2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtMonEnd2.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                } else {
                    btnMon.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_grey_solid));
                    txtMonday.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytMonStart1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtMonStart1.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytMonEnd1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtMonEnd1.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytMonStart2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtMonStart2.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytMonEnd2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtMonEnd2.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                }
                break;
            case 2:
                if (isTue) {
                    btnTue.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_blue_rect_5));
                    txtTuesday.setTextColor(ContextCompat.getColor(activity, R.color.white_color));
                    lytTueStart1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtTueStart1.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                    lytTueEnd1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtTueEnd1.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                    lytTueStart2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtTueStart2.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                    lytTueEnd2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtTueEnd2.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                } else {
                    btnTue.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_grey_solid));
                    txtTuesday.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytTueStart1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtTueStart1.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytTueEnd1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtTueEnd1.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytTueStart2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtTueStart2.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytTueEnd2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtTueEnd2.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                }
                break;
            case 3:
                if (isWed) {
                    btnWed.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_blue_rect_5));
                    txtWednesday.setTextColor(ContextCompat.getColor(activity, R.color.white_color));
                    lytWedStart1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtWedStart1.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                    lytWedEnd1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtWedEnd1.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                    lytWedStart2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtWedStart2.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                    lytWedEnd2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtWedEnd2.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                } else {
                    btnWed.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_grey_solid));
                    txtWednesday.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytWedStart1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtWedStart1.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytWedEnd1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtWedEnd1.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytWedStart2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtWedStart2.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytWedEnd2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtWedEnd2.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                }
                break;
            case 4:
                if (isThu) {
                    btnThu.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_blue_rect_5));
                    txtThursday.setTextColor(ContextCompat.getColor(activity, R.color.white_color));
                    lytThuStart1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtThuStart1.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                    lytThuEnd1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtThuEnd1.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                    lytThuStart2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtThuStart2.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                    lytThuEnd2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtThuEnd2.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                } else {
                    btnThu.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_grey_solid));
                    txtThursday.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytThuStart1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtThuStart1.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytThuEnd1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtThuEnd1.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytThuStart2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtThuStart2.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytThuEnd2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtThuEnd2.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                }
                break;
            case 5:
                if (isFri) {
                    btnFri.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_blue_rect_5));
                    txtFriday.setTextColor(ContextCompat.getColor(activity, R.color.white_color));
                    lytFriStart1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtFriStart1.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                    lytFriEnd1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtFriEnd1.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                    lytFriStart2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtFriStart2.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                    lytFriEnd2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtFriEnd2.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                } else {
                    btnFri.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_grey_solid));
                    txtFriday.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytFriStart1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtFriStart1.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytFriEnd1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtFriEnd1.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytFriStart2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtFriStart2.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytFriEnd2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtFriEnd2.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                }
                break;
            case 6:
                if (isSat) {
                    btnSat.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_blue_rect_5));
                    txtSaturday.setTextColor(ContextCompat.getColor(activity, R.color.white_color));
                    lytSatStart1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtSatStart1.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                    lytSatEnd1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtSatEnd1.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                    lytSatStart2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtSatStart2.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                    lytSatEnd2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_white));
                    txtSatEnd2.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                } else {
                    btnSat.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_grey_solid));
                    txtSaturday.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytSatStart1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtSatStart1.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytSatEnd1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtSatEnd1.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytSatStart2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtSatStart2.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                    lytSatEnd2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_ads_item_grey));
                    txtSatEnd2.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_800));
                }
                break;

        }
    }

    private String getCurrencyId() {
        String currencyId = "";
        for (int i = 0; i < currencyList.size(); i ++) {
            if (currencyList.get(i).getIso().equalsIgnoreCase(selCurrency)) {
                currencyId = String.valueOf(currencyList.get(i).getId());
            }
        }
        return  currencyId;
    }
    private void onCreateStore() {
        boolean isCollect = ckCollect.isChecked();
        boolean isDeliver = ckDeliver.isChecked();
        String name = edtName.getText().toString();
        String description = edtDescription.getText().toString();
        String contactNumber = edtNumber.getText().toString();
        String buildingNumber = edtBuildingNumber.getText().toString();
        String vatNumber = edtVatNumber.getText().toString();
        String tag_list = tagList.size() == 0 ? "" : tagList.get(0);
        if (tagList.size() > 1) {
            for (int i = 1; i < tagList.size(); i++) {
                tag_list += "," + tagList.get(i);
            }
        }
        String category_ids = "";
        for (int i = 0; i < storeCategoryList.size(); i++) {
            if (storeCategoryList.get(i).isCheck()) {
                if (TextUtils.isEmpty(category_ids)) {
                    category_ids = String.valueOf(storeCategoryList.get(i).getId());
                } else {
                    category_ids += "," + String.valueOf(storeCategoryList.get(i).getId());
                }
            }

        }
        String trading_time = "";
        String sun_str = "";
        String mon_str = "";
        String tue_str = "";
        String wed_str = "";
        String thu_str = "";
        String fri_str = "";
        String sat_str = "";
        if (isSun) {
            sun_str = "0:" + txtSunStart1.getText().toString().replace(":", ".") + "-" + txtSunEnd1.getText().toString().replace(":", ".");
            trading_time = sun_str;
        }
        if (isMon) {
            mon_str = "1:" + txtMonStart1.getText().toString().replace(":", ".") + "-" + txtMonEnd1.getText().toString().replace(":", ".");
            if (TextUtils.isEmpty(trading_time)) {
                trading_time = mon_str;
            } else {
                trading_time = trading_time + "/" + mon_str;
            }
        }
        if (isTue) {
            tue_str = "2:" + txtTueStart1.getText().toString().replace(":", ".") + "-" + txtTueEnd1.getText().toString().replace(":", ".");
            if (TextUtils.isEmpty(trading_time)) {
                trading_time = tue_str;
            } else {
                trading_time = trading_time + "/" + tue_str;
            }
        }
        if (isWed) {
            wed_str = "3:" + txtWedStart1.getText().toString().replace(":", ".") + "-" + txtWedEnd1.getText().toString().replace(":", ".");
            if (TextUtils.isEmpty(trading_time)) {
                trading_time = wed_str;
            } else {
                trading_time = trading_time + "/" + wed_str;
            }
        }
        if (isThu) {
            thu_str = "4:" + txtThuStart1.getText().toString().replace(":", ".") + "-" + txtThuEnd1.getText().toString().replace(":", ".");
            if (TextUtils.isEmpty(trading_time)) {
                trading_time = thu_str;
            } else {
                trading_time = trading_time + "/" + thu_str;
            }
        }
        if (isFri) {
            fri_str = "5:" + txtFriStart1.getText().toString().replace(":", ".") + "-" + txtFriEnd1.getText().toString().replace(":", ".");
            if (TextUtils.isEmpty(trading_time)) {
                trading_time = fri_str;
            } else {
                trading_time = trading_time + "/" + fri_str;
            }
        }
        if (isSat) {
            sat_str = "6:" + txtSatStart1.getText().toString().replace(":", ".") + "-" + txtSatEnd1.getText().toString().replace(":", ".");
            if (TextUtils.isEmpty(trading_time)) {
                trading_time = sat_str;
            } else {
                trading_time = trading_time + "/" + sat_str;
            }
        }
        String street1 = edtStreet1.getText().toString();
        String street2 = edtStreet2.getText().toString();
        String suburb = edtSuburb.getText().toString();
        String city = edtCity.getText().toString();
        String state = edtState.getText().toString();
        String country = edtCountry.getText().toString();
        String postalCode = edtPostalCode.getText().toString();
        String currencyID = getCurrencyId();

        showLoadingDialog();
        Builders.Any.B builder;

        if (id == -1) {
            builder = Ion.with(activity).load("POST", G.CreateStore);
        } else {
            builder = Ion.with(activity).load("PUT", G.UpdateStore);
        }

        builder.addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken());
        if (id != -1) {
            builder.setBodyParameter("storeId", String.valueOf(id));
        }
        builder.setBodyParameter("account_name", name)
                .setBodyParameter("is_click_collect", String.valueOf(isCollect))
                .setBodyParameter("is_click_deliver", String.valueOf(isDeliver))
                .setBodyParameter("time_offset", String.valueOf(selTimeOffset))
                .setBodyParameter("time_zone", selTimezone)
                .setBodyParameter("currency", currencyID)
                .setBodyParameter("name", name)
                .setBodyParameter("street", selAddress)
                .setBodyParameter("categories", category_ids)
                .setBodyParameter("contact_number", contactNumber)
                .setBodyParameter("description", description)
                .setBodyParameter("building_number", buildingNumber)
                .setBodyParameter("vat_number", vatNumber)
                .setBodyParameter("address_street1", street1)
                .setBodyParameter("address_street2", street2)
                .setBodyParameter("address_suburb", suburb)
                .setBodyParameter("address_city", city)
                .setBodyParameter("address_state", state)
                .setBodyParameter("address_country", country)
                .setBodyParameter("address_postal_code", postalCode)
                .setBodyParameter("latitude", String.valueOf(currentLatitude))
                .setBodyParameter("longitude", String.valueOf(currentLongitude))
                .setBodyParameter("trading_time", trading_time)
                .setBodyParameter("tag_list", tag_list);
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
                                    if (id == -1) {
                                        Toast.makeText(activity, R.string.msg_success_created, Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(activity, R.string.msg_updated_successfully, Toast.LENGTH_LONG).show();
                                    }
                                    LocalBroadcastManager.getInstance(activity).sendBroadcast(new Intent("refresh_store"));
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

    @SuppressLint("MissingInflatedId")
    private void onCategoryDlg() {
        View dialogView = activity.getLayoutInflater().inflate(R.layout.dlg_choose_category, null);

        final android.app.AlertDialog dlg = new android.app.AlertDialog.Builder(activity)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dlg.setCanceledOnTouchOutside(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        dialogView.findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = false;
                String category_names = "";
                for (int i = 0; i < storeCategoryList.size(); i ++) {
                    if (storeCategoryList.get(i).isCheck()) {
                        isValid = true;
                        if (TextUtils.isEmpty(category_names)) {
                            category_names = storeCategoryList.get(i).getName();
                        } else {
                            category_names += ", " + storeCategoryList.get(i).getName();
                        }

                    }
                }
                if (isValid) {
                    edtCategory.setText(category_names);
                } else {
                    Toast.makeText(activity, R.string.msng_select_store_cateogry, Toast.LENGTH_LONG).show();
                }
                dlg.dismiss();
            }
        });
        storeCategorySelectAdapter = new StoreCategorySelectAdapter(activity, storeCategoryList, new StoreCategorySelectAdapter.StoreCategorySelectRecyclerListener() {
            @Override
            public void onItemClicked(int pos, StoreCategoryModel model) {
                ArrayList<StoreCategoryModel> tmpList = new ArrayList<>();
                tmpList.clear();
                for (int i = 0; i < storeCategoryList.size(); i++) {
                    StoreCategoryModel productDetailModel = new StoreCategoryModel();
                    productDetailModel = storeCategoryList.get(i);
                    if (i == pos) {
                        productDetailModel.setCheck(!productDetailModel.isCheck());
                    }
                    tmpList.add(productDetailModel);
                }
                storeCategorySelectAdapter.setData(tmpList);
            }

        });
        recyclerView.setAdapter(storeCategorySelectAdapter);

        dlg.setCanceledOnTouchOutside(false);
        dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dlg.show();
    }
    @OnClick({R.id.btBack, R.id.btnNext, R.id.btnBack, R.id.edtCategory, R.id.edtAddress,
    R.id.lytSunStart1, R.id.lytSunStart2, R.id.lytSunEnd1, R.id.lytSunEnd2,
            R.id.lytMonStart1, R.id.lytMonStart2, R.id.lytMonEnd1, R.id.lytMonEnd2,
            R.id.lytTueStart1, R.id.lytTueStart2, R.id.lytTueEnd1, R.id.lytTueEnd2,
            R.id.lytWedStart1, R.id.lytWedStart2, R.id.lytWedEnd1, R.id.lytWedEnd2,
            R.id.lytThuStart1, R.id.lytThuStart2, R.id.lytThuEnd1, R.id.lytThuEnd2,
            R.id.lytFriStart1, R.id.lytFriStart2, R.id.lytFriEnd1, R.id.lytFriEnd2,
            R.id.lytSatStart1, R.id.lytSatStart2, R.id.lytSatEnd1, R.id.lytSatEnd2})
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
            case R.id.edtCategory:
                onCategoryDlg();
                break;
            case R.id.edtAddress:
                Intent i = new Intent(activity, LocationGoogleActivity.class);
                startActivityForResult(i, ADDRESS_PICKER_REQUEST);
                break;
            case R.id.lytSunStart1:
                onShowTimeDlg("sun", 1, txtSunStart1.getText().toString());
                break;
            case R.id.lytSunEnd1:
                onShowTimeDlg("sun", 2, txtSunEnd1.getText().toString());
                break;
            case R.id.lytSunStart2:
                onShowTimeDlg("sun", 3, txtSunStart2.getText().toString());
                break;
            case R.id.lytSunEnd2:
                onShowTimeDlg("sun", 4, txtSunEnd2.getText().toString());
                break;
            case R.id.lytMonStart1:
                onShowTimeDlg("mon", 1, txtMonStart1.getText().toString());
                break;
            case R.id.lytMonEnd1:
                onShowTimeDlg("mon", 2, txtMonEnd1.getText().toString());
                break;
            case R.id.lytMonStart2:
                onShowTimeDlg("mon", 3, txtMonStart2.getText().toString());
                break;
            case R.id.lytMonEnd2:
                onShowTimeDlg("mon", 4, txtMonEnd2.getText().toString());
                break;
            case R.id.lytTueStart1:
                onShowTimeDlg("tue", 1, txtTueStart1.getText().toString());
                break;
            case R.id.lytTueEnd1:
                onShowTimeDlg("tue", 2, txtTueEnd1.getText().toString());
                break;
            case R.id.lytTueStart2:
                onShowTimeDlg("tue", 3, txtTueStart2.getText().toString());
                break;
            case R.id.lytTueEnd2:
                onShowTimeDlg("tue", 4, txtTueEnd2.getText().toString());
                break;
            case R.id.lytWedStart1:
                onShowTimeDlg("wed", 1, txtWedStart1.getText().toString());
                break;
            case R.id.lytWedEnd1:
                onShowTimeDlg("wed", 2, txtWedEnd1.getText().toString());
                break;
            case R.id.lytWedStart2:
                onShowTimeDlg("wed", 3, txtWedStart2.getText().toString());
                break;
            case R.id.lytWedEnd2:
                onShowTimeDlg("wed", 4, txtWedEnd2.getText().toString());
                break;
            case R.id.lytThuStart1:
                onShowTimeDlg("thu", 1, txtThuStart1.getText().toString());
                break;
            case R.id.lytThuEnd1:
                onShowTimeDlg("thu", 2, txtThuEnd1.getText().toString());
                break;
            case R.id.lytThuStart2:
                onShowTimeDlg("thu", 3, txtThuStart2.getText().toString());
                break;
            case R.id.lytThuEnd2:
                onShowTimeDlg("thu", 4, txtThuEnd2.getText().toString());
                break;
            case R.id.lytFriStart1:
                onShowTimeDlg("fri", 1, txtFriStart1.getText().toString());
                break;
            case R.id.lytFriEnd1:
                onShowTimeDlg("fri", 2, txtFriEnd1.getText().toString());
                break;
            case R.id.lytFriStart2:
                onShowTimeDlg("fri", 3, txtFriStart2.getText().toString());
                break;
            case R.id.lytFriEnd2:
                onShowTimeDlg("fri", 4, txtFriEnd2.getText().toString());
                break;
            case R.id.lytSatStart1:
                onShowTimeDlg("sat", 1, txtSatStart1.getText().toString());
                break;
            case R.id.lytSatEnd1:
                onShowTimeDlg("sat", 2, txtSatEnd1.getText().toString());
                break;
            case R.id.lytSatStart2:
                onShowTimeDlg("sat", 3, txtSatStart2.getText().toString());
                break;
            case R.id.lytSatEnd2:
                onShowTimeDlg("sat", 4, txtSatEnd2.getText().toString());
                break;
        }
    }
    private void initData() {
        txtTitle.setText(R.string.txt_update_store);
        edtName.setText(storeModel.getName());
        edtDescription.setText(storeModel.getDescription());
        edtNumber.setText(storeModel.getContact_number());
        edtAddress.setText(storeModel.getStreet());
        selAddress = storeModel.getStreet();
        currentLatitude = storeModel.getCoordinates().get(0);
        currentLongitude = storeModel.getCoordinates().get(1);
        ckCollect.setChecked(storeModel.isIs_click_collect());
        ckDeliver.setChecked(storeModel.isIs_click_deliver());
        edtBuildingNumber.setText(storeModel.getBuilding_number());
        edtVatNumber.setText(storeModel.getVat_number());
        if (!TextUtils.isEmpty(storeModel.getTag_list())) {
            if (storeModel.getTag_list().contains(",")) {
                ArrayList<String> tagItems = new ArrayList<String>(Arrays.asList(storeModel.getTag_list().split(",")));
                tagList.addAll(tagItems);
            } else {
                tagList.add(storeModel.getTag_list());
            }
            tagAdapter.setData(tagList);
        }
        String category_name = "";
        if (storeModel.getCategories() != null && storeModel.getCategories().size() > 0) {
            for (int i = 0; i < storeCategoryList.size(); i ++) {
                for (int j = 0; j < storeModel.getCategories().size(); j++) {
                    if (storeCategoryList.get(i).getId() == storeModel.getCategories().get(j).getId()) {
                        storeCategoryList.get(i).setCheck(true);
                        if (TextUtils.isEmpty(category_name)) {
                            category_name = storeCategoryList.get(i).getName();
                        } else {
                            category_name += ", " + storeCategoryList.get(i).getName();
                        }
                    }
                }
            }
        }
        edtCategory.setText(category_name);
        selCurrency = storeModel.getCurrency().getIso();
        spinnerCurrency.setSelection(getCurrencyIndex());
        selTimezone = storeModel.getTime_zone();
        selTimeOffset = storeModel.getTime_offset();
        spinnerTimezone.setSelection(getTimezoneIndex());
        String trading_time = storeModel.getTrading_time();
        if (!TextUtils.isEmpty(trading_time)) {
            trading_time = trading_time.replace("00:00", "0");
            trading_time = trading_time.replace(":00", "");
            trading_time = trading_time.replace(".00", "");
            if (trading_time.contains("/")) {
                ArrayList<String> dayList = new ArrayList<String>(Arrays.asList(trading_time.split("/")));
                for (int i = 0; i < dayList.size(); i++) {
                    parseOneTradingTime(dayList.get(i));
                }
            } else {
                parseOneTradingTime(trading_time);
            }
        }
        edtStreet1.setText(storeModel.getAddress_street1());
        edtStreet2.setText(storeModel.getAddress_street2());
        edtSuburb.setText(storeModel.getAddress_suburb());
        edtCity.setText(storeModel.getAddress_city());
        edtState.setText(storeModel.getAddress_state());
        edtCountry.setText(storeModel.getAddress_country());
        edtPostalCode.setText(storeModel.getAddress_postal_code());
    }
    private void parseOneTradingTime(String dayTime) {
        ArrayList<String> timeList = new ArrayList<String>(Arrays.asList(dayTime.split(":")));
        String dayStr = timeList.get(0);
        String timeStr = timeList.get(1);
        switch (dayStr) {
            case "0":
                isSun = true;
                ckSun.setChecked(true);
                if (timeStr.contains(",")) {
                    ArrayList<String> oneList = new ArrayList<String>(Arrays.asList(timeStr.split(",")));
                    String oneStr = oneList.get(0);
                    String twoStr = oneList.get(1);
                    ArrayList<String> onePartList = new ArrayList<String>(Arrays.asList(oneStr.split("-")));
                    txtSunStart1.setText(onePartList.get(0) + ":00");
                    txtSunEnd1.setText(onePartList.get(1) + ":00");
                    ArrayList<String> twoPartList = new ArrayList<String>(Arrays.asList(twoStr.split("-")));
                    txtSunStart2.setText(twoPartList.get(0) + ":00");
                    txtSunEnd2.setText(twoPartList.get(1) + ":00");
                } else {
                    ArrayList<String> oneList = new ArrayList<String>(Arrays.asList(timeStr.split("-")));
                    txtSunStart1.setText(oneList.get(0) + ":00");
                    txtSunEnd1.setText(oneList.get(1) + ":00");
                }
                break;
            case "1":
                isMon = true;
                ckMon.setChecked(true);
                if (timeStr.contains(",")) {
                    ArrayList<String> oneList = new ArrayList<String>(Arrays.asList(timeStr.split(",")));
                    String oneStr = oneList.get(0);
                    String twoStr = oneList.get(1);
                    ArrayList<String> onePartList = new ArrayList<String>(Arrays.asList(oneStr.split("-")));
                    txtMonStart1.setText(onePartList.get(0) + ":00");
                    txtMonEnd1.setText(onePartList.get(1) + ":00");
                    ArrayList<String> twoPartList = new ArrayList<String>(Arrays.asList(twoStr.split("-")));
                    txtMonStart2.setText(twoPartList.get(0) + ":00");
                    txtMonEnd2.setText(twoPartList.get(1) + ":00");
                } else {
                    ArrayList<String> oneList = new ArrayList<String>(Arrays.asList(timeStr.split("-")));
                    txtMonStart1.setText(oneList.get(0) + ":00");
                    txtMonEnd1.setText(oneList.get(1) + ":00");
                }
                break;
            case "2":
                isTue = true;
                ckTue.setChecked(true);
                if (timeStr.contains(",")) {
                    ArrayList<String> oneList = new ArrayList<String>(Arrays.asList(timeStr.split(",")));
                    String oneStr = oneList.get(0);
                    String twoStr = oneList.get(1);
                    ArrayList<String> onePartList = new ArrayList<String>(Arrays.asList(oneStr.split("-")));
                    txtTueStart1.setText(onePartList.get(0) + ":00");
                    txtTueEnd1.setText(onePartList.get(1) + ":00");
                    ArrayList<String> twoPartList = new ArrayList<String>(Arrays.asList(twoStr.split("-")));
                    txtTueStart2.setText(twoPartList.get(0) + ":00");
                    txtTueEnd2.setText(twoPartList.get(1) + ":00");
                } else {
                    ArrayList<String> oneList = new ArrayList<String>(Arrays.asList(timeStr.split("-")));
                    txtTueStart1.setText(oneList.get(0) + ":00");
                    txtTueEnd1.setText(oneList.get(1) + ":00");
                }
                break;
            case "3":
                isWed = true;
                ckWed.setChecked(true);
                if (timeStr.contains(",")) {
                    ArrayList<String> oneList = new ArrayList<String>(Arrays.asList(timeStr.split(",")));
                    String oneStr = oneList.get(0);
                    String twoStr = oneList.get(1);
                    ArrayList<String> onePartList = new ArrayList<String>(Arrays.asList(oneStr.split("-")));
                    txtWedStart1.setText(onePartList.get(0) + ":00");
                    txtWedEnd1.setText(onePartList.get(1) + ":00");
                    ArrayList<String> twoPartList = new ArrayList<String>(Arrays.asList(twoStr.split("-")));
                    txtWedStart2.setText(twoPartList.get(0) + ":00");
                    txtWedEnd2.setText(twoPartList.get(1) + ":00");
                } else {
                    ArrayList<String> oneList = new ArrayList<String>(Arrays.asList(timeStr.split("-")));
                    txtWedStart1.setText(oneList.get(0) + ":00");
                    txtWedEnd1.setText(oneList.get(1) + ":00");
                }
                break;
            case "4":
                isThu = true;
                ckThu.setChecked(true);
                if (timeStr.contains(",")) {
                    ArrayList<String> oneList = new ArrayList<String>(Arrays.asList(timeStr.split(",")));
                    String oneStr = oneList.get(0);
                    String twoStr = oneList.get(1);
                    ArrayList<String> onePartList = new ArrayList<String>(Arrays.asList(oneStr.split("-")));
                    txtThuStart1.setText(onePartList.get(0) + ":00");
                    txtThuEnd1.setText(onePartList.get(1) + ":00");
                    ArrayList<String> twoPartList = new ArrayList<String>(Arrays.asList(twoStr.split("-")));
                    txtThuStart2.setText(twoPartList.get(0) + ":00");
                    txtThuEnd2.setText(twoPartList.get(1) + ":00");
                } else {
                    ArrayList<String> oneList = new ArrayList<String>(Arrays.asList(timeStr.split("-")));
                    txtThuStart1.setText(oneList.get(0) + ":00");
                    txtThuEnd1.setText(oneList.get(1) + ":00");
                }
                break;
            case "5":
                isFri = true;
                ckFri.setChecked(true);
                if (timeStr.contains(",")) {
                    ArrayList<String> oneList = new ArrayList<String>(Arrays.asList(timeStr.split(",")));
                    String oneStr = oneList.get(0);
                    String twoStr = oneList.get(1);
                    ArrayList<String> onePartList = new ArrayList<String>(Arrays.asList(oneStr.split("-")));
                    txtFriStart1.setText(onePartList.get(0) + ":00");
                    txtFriEnd1.setText(onePartList.get(1) + ":00");
                    ArrayList<String> twoPartList = new ArrayList<String>(Arrays.asList(twoStr.split("-")));
                    txtFriStart2.setText(twoPartList.get(0) + ":00");
                    txtFriEnd2.setText(twoPartList.get(1) + ":00");
                } else {
                    ArrayList<String> oneList = new ArrayList<String>(Arrays.asList(timeStr.split("-")));
                    txtFriStart1.setText(oneList.get(0) + ":00");
                    txtFriEnd1.setText(oneList.get(1) + ":00");
                }
                break;
            case "6":
                isSat = true;
                ckSat.setChecked(true);
                if (timeStr.contains(",")) {
                    ArrayList<String> oneList = new ArrayList<String>(Arrays.asList(timeStr.split(",")));
                    String oneStr = oneList.get(0);
                    String twoStr = oneList.get(1);
                    ArrayList<String> onePartList = new ArrayList<String>(Arrays.asList(oneStr.split("-")));
                    txtSatStart1.setText(onePartList.get(0) + ":00");
                    txtSatEnd1.setText(onePartList.get(1) + ":00");
                    ArrayList<String> twoPartList = new ArrayList<String>(Arrays.asList(twoStr.split("-")));
                    txtSatStart2.setText(twoPartList.get(0) + ":00");
                    txtSatEnd2.setText(twoPartList.get(1) + ":00");
                } else {
                    ArrayList<String> oneList = new ArrayList<String>(Arrays.asList(timeStr.split("-")));
                    txtSatStart1.setText(oneList.get(0) + ":00");
                    txtSatEnd1.setText(oneList.get(1) + ":00");
                }
                break;

        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessStoreDetail(StoreRes res) {
        hideLoadingDialog();
        if (res.isStatus() && res.getData() != null && !TextUtils.isEmpty(res.getData().getName())) {
            storeModel = res.getData();
            initData();
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
        if (resultCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra("fullAddress") != null) {
                    // String address = data.getStringExtra(MapUtility.ADDRESS);
                    currentLatitude = data.getDoubleExtra("lat", 0.0);
                    currentLongitude = data.getDoubleExtra("lon", 0.0);
                    selAddress = data.getStringExtra("fullAddress");
                    edtAddress.setText(selAddress);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}