package com.mobile.seemesave.view.menu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mobile.seemesave.R;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.utils.G;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactUsActivity extends BaseActivity {

    private ContactUsActivity activity;

    @BindView(R.id.txtEmail)
    TextView txtEmail;

    @BindView(R.id.editFName)
    TextInputEditText editFName;

    @BindView(R.id.editLName)
    TextInputEditText editLName;
    @BindView(R.id.editEmail)
    TextInputEditText editEmail;

    @BindView(R.id.editMessage)
    TextInputEditText editMessage;


    @BindView(R.id.btnSend)
    LinearLayout btnSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);
        activity = this;
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initView() {

    }
    private void onShare(){
        String msg = G.user.getFirst_name() + " " + G.user.getLast_name();
        String text = "Share From Seemesave\n\n" + msg;
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:info@seemesave.com")); // only email apps should handle this
        String[] addresses = {"info@seemesave.com"};
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else{
            Toast.makeText(activity, "There is no email application installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void onSend(){
        if (TextUtils.isEmpty(editFName.getText()) || TextUtils.isEmpty(editLName.getText()) || TextUtils.isEmpty(editEmail.getText()) || TextUtils.isEmpty(editMessage.getText())){
            Toast.makeText(this, R.string.missing_param, Toast.LENGTH_LONG).show();
            return;
        }
        String msg = "Name: " + editFName.getText().toString() +  " " +  editLName.getText().toString();
        msg += "\n" + "Email Address: " + editEmail.getText().toString();
        msg += "\n" + editMessage.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:info@seemesave.com")); // only email apps should handle this
        String[] addresses = {"info@seemesave.com"};
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else{
            Toast.makeText(activity, "There is no email application installed.", Toast.LENGTH_SHORT).show();
        }
    }
    @OnClick({R.id.imgBack, R.id.txtEmail, R.id.btnSend})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtEmail:
                onShare();
                break;
            case R.id.btnSend:
                onSend();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}