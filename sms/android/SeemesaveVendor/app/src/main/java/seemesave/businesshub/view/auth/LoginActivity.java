package seemesave.businesshub.view.auth;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import seemesave.businesshub.R;
import seemesave.businesshub.adapter.AdsTabAdapter;
import seemesave.businesshub.adapter.UserTypeAdapter;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.model.common.AdsTabModel;
import seemesave.businesshub.model.common.UserPortalModel;
import seemesave.businesshub.model.res.LoginRes;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.view.supplier.main.MainSupplierActivity;
import seemesave.businesshub.view.vendor.main.MainActivity;
import seemesave.businesshub.view_model.auth.LoginViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends BaseActivity {
    private LoginViewModel mViewModel;
    @BindView(R.id.edtNumber)
    TextInputEditText edtNumber;
    @BindView(R.id.edtEmail)
    TextInputEditText edtEmail;
    @BindView(R.id.edtPassword)
    TextInputEditText edtPassword;
    @BindView(R.id.btnForgotPassword)
    Button btnForgotPassword;
    @BindView(R.id.country_picker)
    CountryCodePicker countryCodePicker;
    @BindView(R.id.toggoleBtn)
    ToggleButton toggoleBtn;

    @BindView(R.id.ckMobile)
    CheckBox ckMobile;
    @BindView(R.id.ckEmail)
    CheckBox ckEmail;
    @BindView(R.id.lytMobile)
    LinearLayout lytMobile;
    @BindView(R.id.lytEmail)
    LinearLayout lytEmail;

    private boolean isMobile = true;
    private String countryCodeStr = "";
    private LoginActivity activity;
    private UserTypeAdapter vendorAdapter;
    private UserTypeAdapter supplierAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        activity = this;
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mViewModel.getIsBusy().observe(this, isBusy -> {
            if (isBusy) {
                G.showLoading(activity);
            } else {
                G.hideLoading();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        edtEmail.setText("jin.lu@seemesave.com");
        edtPassword.setText("123456");
        btnForgotPassword.setPaintFlags(btnForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        ckMobile.setOnClickListener(view -> onCheckMobile(true));
        ckEmail.setOnClickListener(view -> onCheckMobile(false));
        toggoleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    edtPassword.setInputType(129);
                }
            }
        });

        countryCodePicker.setOnCountryChangeListener(() -> {
            countryCodeStr = countryCodePicker.getSelectedCountryCode();
        });
    }

    private void onCheckMobile(boolean is_mobile) {
        isMobile = is_mobile;
        if (isMobile) {
            lytMobile.setVisibility(View.VISIBLE);
            lytEmail.setVisibility(View.GONE);
            ckMobile.setChecked(true);
            ckEmail.setChecked(false);
        } else {
            lytMobile.setVisibility(View.GONE);
            lytEmail.setVisibility(View.VISIBLE);
            ckMobile.setChecked(false);
            ckEmail.setChecked(true);
        }
    }

    @OnClick({R.id.btBack, R.id.btLogin, R.id.btnSignup, R.id.btnForgotPassword})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.btLogin:
                mViewModel.onLogin(activity, isMobile, edtEmail.getText().toString(), countryCodePicker.getSelectedCountryCode(), edtNumber.getText().toString(), edtPassword.getText().toString());
                break;
            case R.id.btnSignup:
                mViewModel.gotoSignup(this);
                break;
            case R.id.btnForgotPassword:
                mViewModel.gotoForgotPassword(this);
                break;
        }
    }

    boolean isVendor = false;

    private void showPortalDlg(LoginRes res) {
        UserPortalModel selectedPortal = new UserPortalModel();
        ArrayList<UserPortalModel> mVendorList = new ArrayList<>();
        ArrayList<UserPortalModel> mSupplierList = new ArrayList<>();
        ArrayList<UserPortalModel> aVendorList = new ArrayList<>();
        ArrayList<UserPortalModel> aSupplierList = new ArrayList<>();

        mVendorList.clear();
        mSupplierList.clear();
        aVendorList.clear();
        aSupplierList.clear();

        View dialogView = activity.getLayoutInflater().inflate(R.layout.dlg_choose_portal, null);

        final android.app.AlertDialog dlg = new android.app.AlertDialog.Builder(activity)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dlg.setCanceledOnTouchOutside(true);
        LinearLayout ok = dialogView.findViewById(R.id.btnOk);
        LinearLayout lytVendor = dialogView.findViewById(R.id.lytVendor);
        RecyclerView rvVendor = dialogView.findViewById(R.id.rvVendor);
        LinearLayout lytSupplier = dialogView.findViewById(R.id.lytSupplier);
        RecyclerView rvSupplier = dialogView.findViewById(R.id.rvSupplier);
        SegmentedButtonGroup buttonGroup_draggable = dialogView.findViewById(R.id.buttonGroup_draggable);
        LinearLayout lytSelectedPortal = dialogView.findViewById(R.id.lytSelectedPortal);
        CircleImageView imgPortalLogo = dialogView.findViewById(R.id.imgPortalLogo);
        TextView txtPortalName = dialogView.findViewById(R.id.txtPortalName);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);


        vendorAdapter = new UserTypeAdapter(activity, mVendorList, new UserTypeAdapter.UserTypeRecyclerListener() {
            @Override
            public void onItemClicked(int pos, UserPortalModel model) {
                isVendor = true;
                selectedPortal.setId(model.getId());
                selectedPortal.setPortal_token(model.getPortal_token());
                selectedPortal.setLogo(model.getLogo());
                selectedPortal.setName(model.getName());
                selectedPortal.setTime_offset(model.getTime_offset());
                selectedPortal.setTime_zone(model.getTime_zone());

                Glide.with(activity)
                        .load(selectedPortal.getLogo())
                        .into(imgPortalLogo);
                txtPortalName.setText(selectedPortal.getName());
                lytSelectedPortal.setVisibility(View.VISIBLE);

                if (buttonGroup_draggable.getPosition() == 0) {
                    for (int i = 0; i < mVendorList.size(); i++) {
                        if (i == pos) {
                            mVendorList.get(pos).setCheck(true);
                        } else {
                            mVendorList.get(pos).setCheck(false);
                        }
                    }
                    for (int i = 0; i < mSupplierList.size(); i++) {
                        mSupplierList.get(pos).setCheck(false);
                    }
                    for (int i = 0; i < aVendorList.size(); i++) {
                        aVendorList.get(pos).setCheck(false);
                    }
                    for (int i = 0; i < aSupplierList.size(); i++) {
                        aSupplierList.get(pos).setCheck(false);
                    }
                    supplierAdapter.setData(mSupplierList);
                } else {
                    for (int i = 0; i < aVendorList.size(); i++) {
                        if (i == pos) {
                            aVendorList.get(pos).setCheck(true);
                        } else {
                            aVendorList.get(pos).setCheck(false);
                        }
                    }
                    for (int i = 0; i < aSupplierList.size(); i++) {
                        aSupplierList.get(pos).setCheck(false);
                    }
                    for (int i = 0; i < mVendorList.size(); i++) {
                        mVendorList.get(pos).setCheck(false);
                    }
                    for (int i = 0; i < mSupplierList.size(); i++) {
                        mSupplierList.get(pos).setCheck(false);
                    }
                    supplierAdapter.setData(aSupplierList);
                }
                vendorAdapter.notifyItemChanged(pos);
            }
        });
        rvVendor.setAdapter(vendorAdapter);
        rvVendor.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);

        supplierAdapter = new UserTypeAdapter(activity, mSupplierList, new UserTypeAdapter.UserTypeRecyclerListener() {
            @Override
            public void onItemClicked(int pos, UserPortalModel model) {
                isVendor = false;
                selectedPortal.setId(model.getId());
                selectedPortal.setPortal_token(model.getPortal_token());
                selectedPortal.setLogo(model.getLogo());
                selectedPortal.setName(model.getName());
                selectedPortal.setTime_offset(model.getTime_offset());
                selectedPortal.setTime_zone(model.getTime_zone());

                Glide.with(activity)
                        .load(selectedPortal.getLogo())
                        .into(imgPortalLogo);
                txtPortalName.setText(selectedPortal.getName());
                lytSelectedPortal.setVisibility(View.VISIBLE);

                if (buttonGroup_draggable.getPosition() == 0) {
                    for (int i = 0; i < mSupplierList.size(); i++) {
                        if (i == pos) {
                            mSupplierList.get(pos).setCheck(true);
                        } else {
                            mSupplierList.get(pos).setCheck(false);
                        }
                    }
                    for (int i = 0; i < mVendorList.size(); i++) {
                        mVendorList.get(pos).setCheck(false);
                    }
                    for (int i = 0; i < aSupplierList.size(); i++) {
                        aSupplierList.get(pos).setCheck(false);
                    }
                    for (int i = 0; i < aVendorList.size(); i++) {
                        aVendorList.get(pos).setCheck(false);
                    }
                    vendorAdapter.setData(mVendorList);
                } else {
                    for (int i = 0; i < aSupplierList.size(); i++) {
                        if (i == pos) {
                            aSupplierList.get(pos).setCheck(true);
                        } else {
                            aSupplierList.get(pos).setCheck(false);
                        }
                    }
                    for (int i = 0; i < aVendorList.size(); i++) {
                        aVendorList.get(pos).setCheck(false);
                    }
                    for (int i = 0; i < mVendorList.size(); i++) {
                        mVendorList.get(pos).setCheck(false);
                    }
                    for (int i = 0; i < mSupplierList.size(); i++) {
                        mSupplierList.get(pos).setCheck(false);
                    }
                    vendorAdapter.setData(aVendorList);
                }
                supplierAdapter.notifyItemChanged(pos);
            }
        });
        rvSupplier.setAdapter(supplierAdapter);
        rvSupplier.setLayoutManager(linearLayoutManager1);

        if (res.getOwns().getVendors().size() > 0) {
            lytVendor.setVisibility(View.VISIBLE);
            for (int i = 0; i < res.getOwns().getVendors().size(); i++) {
                UserPortalModel portalModel = new UserPortalModel();
                portalModel.setId(res.getOwns().getVendors().get(i).getId());
                portalModel.setPortal_token(res.getOwns().getVendors().get(i).getPortal_token());
                portalModel.setLogo(res.getOwns().getVendors().get(i).getLogo());
                portalModel.setName(res.getOwns().getVendors().get(i).getName());
                portalModel.setTime_offset(res.getOwns().getVendors().get(i).getTime_offset());
                portalModel.setTime_zone(res.getOwns().getVendors().get(i).getTime_zone());
                mVendorList.add(portalModel);
            }
            vendorAdapter.setData(mVendorList);
        } else {
            lytVendor.setVisibility(View.GONE);
        }
        if (res.getOwns().getSuppliers().size() > 0) {
            lytSupplier.setVisibility(View.VISIBLE);
            for (int i = 0; i < res.getOwns().getSuppliers().size(); i++) {
                UserPortalModel portalModel = new UserPortalModel();
                portalModel.setId(res.getOwns().getSuppliers().get(i).getId());
                portalModel.setPortal_token(res.getOwns().getSuppliers().get(i).getPortal_token());
                portalModel.setLogo(res.getOwns().getSuppliers().get(i).getLogo());
                portalModel.setName(res.getOwns().getSuppliers().get(i).getName());
                portalModel.setTime_offset(res.getOwns().getSuppliers().get(i).getTime_offset());
                portalModel.setTime_zone(res.getOwns().getSuppliers().get(i).getTime_zone());
                mSupplierList.add(portalModel);
            }
            supplierAdapter.setData(mSupplierList);
        } else {
            lytSupplier.setVisibility(View.GONE);
        }

        for (int i = 0; i < res.getAgents().getVendors().size(); i++) {
            UserPortalModel portalModel = new UserPortalModel();
            portalModel.setId(res.getAgents().getVendors().get(i).getId());
            portalModel.setPortal_token(res.getAgents().getVendors().get(i).getPortal_token());
            portalModel.setLogo(res.getAgents().getVendors().get(i).getVendor().getLogo());
            portalModel.setName(res.getAgents().getVendors().get(i).getVendor().getName());
            portalModel.setTime_offset(res.getAgents().getVendors().get(i).getVendor().getTime_offset());
            portalModel.setTime_zone(res.getAgents().getVendors().get(i).getVendor().getTime_zone());
            aVendorList.add(portalModel);
        }
        for (int i = 0; i < res.getAgents().getSuppliers().size(); i++) {
            UserPortalModel portalModel = new UserPortalModel();
            portalModel.setId(res.getAgents().getSuppliers().get(i).getId());
            portalModel.setPortal_token(res.getAgents().getSuppliers().get(i).getPortal_token());
            portalModel.setLogo(res.getAgents().getSuppliers().get(i).getSupplier().getLogo());
            portalModel.setName(res.getAgents().getSuppliers().get(i).getSupplier().getName());
            portalModel.setTime_offset(res.getAgents().getSuppliers().get(i).getSupplier().getTime_offset());
            portalModel.setTime_zone(res.getAgents().getSuppliers().get(i).getSupplier().getTime_zone());
            aSupplierList.add(portalModel);
        }

        if (res.getOwns().getVendors().size() == 0 && res.getOwns().getSuppliers().size() == 0) {
            buttonGroup_draggable.setPosition(1, true);
        }

        buttonGroup_draggable.setOnPositionChangedListener(new SegmentedButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(int position) {
                if (position == 0) {
                    if (mVendorList.size() > 0) {
                        lytVendor.setVisibility(View.VISIBLE);
                        vendorAdapter.setData(mVendorList);
                    } else {
                        lytVendor.setVisibility(View.GONE);
                    }
                    if (mSupplierList.size() > 0) {
                        lytSupplier.setVisibility(View.VISIBLE);
                        supplierAdapter.setData(mSupplierList);
                    } else {
                        lytSupplier.setVisibility(View.GONE);
                    }
                } else {
                    if (aVendorList.size() > 0) {
                        lytVendor.setVisibility(View.VISIBLE);
                        vendorAdapter.setData(aVendorList);
                    } else {
                        lytVendor.setVisibility(View.GONE);
                    }
                    if (aSupplierList.size() > 0) {
                        lytSupplier.setVisibility(View.VISIBLE);
                        supplierAdapter.setData(aSupplierList);
                    } else {
                        lytSupplier.setVisibility(View.GONE);
                    }
                }
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(selectedPortal.getName())) {
                    App.initPortalInfo(res.getToken(), res.getUser(), selectedPortal, edtPassword.getText().toString(), isVendor);
                    dlg.dismiss();
                    Intent intent;
                    if (isVendor) {
                        intent = new Intent(activity, MainActivity.class);
                    } else {
                        intent = new Intent(activity, MainSupplierActivity.class);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    showMessages(R.string.txt_msg_select_portal);
                }
            }
        });
        dlg.setCanceledOnTouchOutside(false);
        dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dlg.show();
    }

    private void goVendor(LoginRes res) {
        UserPortalModel selectedPortal = new UserPortalModel();
        selectedPortal.setId(res.getOwns().getVendors().get(0).getId());
        selectedPortal.setPortal_token(res.getOwns().getVendors().get(0).getPortal_token());
        selectedPortal.setLogo(res.getOwns().getVendors().get(0).getLogo());
        selectedPortal.setName(res.getOwns().getVendors().get(0).getName());
        selectedPortal.setTime_offset(res.getOwns().getVendors().get(0).getTime_offset());
        selectedPortal.setTime_zone(res.getOwns().getVendors().get(0).getTime_zone());
        isVendor = true;

        App.initPortalInfo(res.getToken(), res.getUser(), selectedPortal, edtPassword.getText().toString(), isVendor);
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCompleteLogin(LoginRes res) {
        if (res.isStatus()) {
            if (res.getOwns().getSuppliers().size() == 0 && res.getOwns().getVendors().size() == 0 &&
                    res.getAgents().getSuppliers().size() == 0 && res.getAgents().getVendors().size() == 0) {
                showMessages(R.string.txt_msg_no_portal);
            } else {
                showPortalDlg(res);
            }

        } else {
            if (G.isNetworkAvailable(activity)) {
                showMessages(res.getMessage());
            } else {
                showMessages(R.string.msg_connection_fail);
            }
        }
    }
}