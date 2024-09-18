package seemesave.businesshub.view.supplier.menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import seemesave.businesshub.R;
import seemesave.businesshub.application.App;
import seemesave.businesshub.model.common.AgentModel;
import seemesave.businesshub.utils.G;

public class InviteVendorFragment extends BottomSheetDialogFragment {

    private View mFragView;
    private Activity mContext;
    private BottomFragListener mListener;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtSend)
    TextView txtSend;
    @BindView(R.id.edtEmail)
    TextInputEditText edtEmail;
    @BindView(R.id.edtDescription)
    TextInputEditText edtDescription;

    @BindView(R.id.btnInvite)
    LinearLayout btnInvite;
    public interface BottomFragListener {
        void onDismiss();
    }

    public InviteVendorFragment(Activity context, BottomFragListener listener) {
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
        mFragView = inflater.inflate(R.layout.dlg_bottom_invite_vendor, container, false);
        ButterKnife.bind(this, mFragView);
        initView();
        return mFragView;
    }

    private void initView() {
        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInvitation();
            }
        });
    }

    private void sendInvitation() {
        if (TextUtils.isEmpty(edtEmail.getText().toString()) || TextUtils.isEmpty(edtDescription.getText().toString())) {
            Toast.makeText(mContext, mContext.getText(R.string.txt_msg_finish_info), Toast.LENGTH_LONG).show();
            return;
        }
        G.showLoading(mContext);
        Ion.with(mContext)
                .load(G.SendVendorInvitation)
                .addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken())
                .setBodyParameter("email", edtEmail.getText().toString())
                .setBodyParameter("description", edtDescription.getText().toString())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        G.hideLoading();
                        if (e == null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("status")) {
                                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("refresh_data"));
                                    dismiss();
                                    Toast.makeText(mContext, getString(R.string.txt_msg_send_invitation), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException jsonException) {
                                Toast.makeText(mContext, R.string.msg_something_wrong, Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(mContext, R.string.msg_something_wrong, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
