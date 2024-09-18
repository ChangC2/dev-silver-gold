package seemesave.businesshub.view.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.BindView;
import seemesave.businesshub.R;
import seemesave.businesshub.application.App;
import seemesave.businesshub.utils.DialogUtils;
import seemesave.businesshub.view.auth.LoginActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import seemesave.businesshub.view.supplier.menu.SupplierProfileActivity;
import seemesave.businesshub.view.vendor.menu.BankDetailActivity;
import seemesave.businesshub.view.vendor.menu.UserProfileActivity;
import seemesave.businesshub.view.vendor.menu.VendorProfileActivity;

public class MenuOptionFragment extends BottomSheetDialogFragment {

    private View mFragView;
    private Activity mContext;
    private BottomFragListener mListener;

    @BindView(R.id.txtProfile)
    TextView txtProfile;

    public interface BottomFragListener {
        void onDismiss();
    }

    public MenuOptionFragment(Activity context, BottomFragListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new BottomSheetDialog(getContext(), R.style.BottomSheetStyle);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragView = inflater.inflate(R.layout.bottom_option_menu, container, false);
        ButterKnife.bind(this, mFragView);
        initView();
        return mFragView;
    }
    private void initView() {
//        try {
//            String data = DatabaseQueryClass.getInstance().getData(App.getUserID(), "PortalProfileInfo", "");
//            if (!TextUtils.isEmpty(data)) {
//                PortalProfileInfoRes localRes = GsonUtils.getInstance().fromJson(data, PortalProfileInfoRes.class);
//                txtBudget.setText(String.format("%1$s (%2$s)", localRes.getData().getBalance().get(0).getStr(), localRes.getData().getBalance().get(1).getStr()));
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        if (App.getPortalType()) {
            txtProfile.setText(mContext.getString(R.string.vendor_profile));
        } else {
            txtProfile.setText(mContext.getString(R.string.supplier_profile));
        }
    }

    private void onLogout() {
        DialogUtils.showConfirmDialogWithListener(mContext, getString(R.string.txt_warning), getString(R.string.logout_confirm), getString(R.string.txt_yes), getString(R.string.txt_no),
                (dialog, which) -> logout(),
                (dialog, which) -> dialog.dismiss());
    }
    private void logout() {
        App.logout();
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @OnClick({R.id.lytLogout, R.id.lytUserProfile, R.id.lytVendorProfile, R.id.lytBankDetail})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.lytLogout:
                onLogout();
                break;
            case R.id.lytUserProfile:
                startActivity(new Intent(mContext, UserProfileActivity.class));
                dismiss();
                break;
            case R.id.lytVendorProfile:
                if (App.getPortalType()) {
                    startActivity(new Intent(mContext, VendorProfileActivity.class));
                } else {
                    startActivity(new Intent(mContext, SupplierProfileActivity.class));
                }
                dismiss();
                break;
            case R.id.lytBankDetail:
                startActivity(new Intent(mContext, BankDetailActivity.class));
                dismiss();
                break;

        }
    }

}
