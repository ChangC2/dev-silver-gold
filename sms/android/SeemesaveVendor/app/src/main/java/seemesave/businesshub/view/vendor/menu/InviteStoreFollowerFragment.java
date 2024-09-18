package seemesave.businesshub.view.vendor.menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

import seemesave.businesshub.R;
import seemesave.businesshub.model.req.InvitePostReq;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InviteStoreFollowerFragment extends BottomSheetDialogFragment {

    private View mFragView;
    private Activity mContext;
    private BottomFragListener mListener;
    private int store_id = -1;

    @BindView(R.id.rdEmail)
    RadioButton rdEmail;
    @BindView(R.id.rdMobile)
    RadioButton rdMobile;
    @BindView(R.id.lytEmail)
    LinearLayout lytEmail;
    @BindView(R.id.edtEmail)
    TextInputEditText edtEmail;
    @BindView(R.id.edtFName)
    TextInputEditText edtFName;
    @BindView(R.id.lytMobile)
    LinearLayout lytMobile;
    @BindView(R.id.edtMobile)
    TextInputEditText edtMobile;
    @BindView(R.id.country_picker)
    CountryCodePicker countryCodePicker;
    @BindView(R.id.btnInvite)
    LinearLayout btnInvite;

    public interface BottomFragListener {
        void onDismiss();
    }

    public InviteStoreFollowerFragment(Activity context, int store_id, BottomFragListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.store_id = store_id;
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
        mFragView = inflater.inflate(R.layout.dlg_bottom_invite_store_follower, container, false);
        ButterKnife.bind(this, mFragView);
        initView();
        return mFragView;
    }

    private void initView() {
        rdEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lytEmail.setVisibility(View.VISIBLE);
                    lytMobile.setVisibility(View.GONE);
                } else {
                    lytEmail.setVisibility(View.GONE);
                    lytMobile.setVisibility(View.VISIBLE);
                }
            }
        });
        rdMobile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lytEmail.setVisibility(View.GONE);
                    lytMobile.setVisibility(View.VISIBLE);
                } else {
                    lytEmail.setVisibility(View.VISIBLE);
                    lytMobile.setVisibility(View.GONE);
                }
            }
        });
        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInvitation();
            }
        });
    }

    private void sendInvitation() {
        if (rdEmail.isChecked()) {
            if (TextUtils.isEmpty(edtEmail.getText().toString()) || TextUtils.isEmpty(edtFName.getText().toString())) {
                Toast.makeText(mContext, mContext.getText(R.string.txt_msg_finish_info), Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            if (TextUtils.isEmpty(edtMobile.getText().toString()) || TextUtils.isEmpty(edtFName.getText().toString())) {
                Toast.makeText(mContext, mContext.getText(R.string.txt_msg_finish_info), Toast.LENGTH_LONG).show();
                return;
            }
        }
        InvitePostReq req = new InvitePostReq();
        req.setMail(rdEmail.isChecked());
        req.setEmail(edtEmail.getText().toString());
        req.setCountryCode(countryCodePicker.getSelectedCountryCode());
        req.setPhoneNumber(edtMobile.getText().toString());
        req.setFirstName(edtFName.getText().toString());
        EventBus.getDefault().post(req);
        dismiss();
    }

}
