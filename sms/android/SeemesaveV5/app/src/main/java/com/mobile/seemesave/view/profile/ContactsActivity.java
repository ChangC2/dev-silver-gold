package com.mobile.seemesave.view.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.utils.G;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsActivity extends BaseActivity {

    @BindView(R.id.switchUpload)
    Switch switchUpload;

    @BindView(R.id.imgBack)
    ImageView imgBack;

    String names = "";
    String phoneNumbers = "";

    public static void start(Context context) {
        Intent intent = new Intent(context, ContactsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);
        switchUpload.setChecked(G.pref.getBoolean("sync_contacts", false));
        switchUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalBroadcastManager.getInstance(ContactsActivity.this).sendBroadcast(new Intent("upload_contacts"));
                if (switchUpload.isChecked()){
                    if (ContextCompat.checkSelfPermission(ContactsActivity.this,
                            Manifest.permission.READ_CONTACTS)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ContactsActivity.this,
                                new String[]{Manifest.permission.READ_CONTACTS},
                                101);
                    }else{
                        G.editor.putBoolean("sync_contacts", true);
                        G.editor.apply();
                        getContacts();
                        apiCallForUploadContacts();
                    }
                }else{
                    G.editor.putBoolean("sync_contacts", false);
                    G.editor.apply();
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void apiCallForUploadContacts(){
        Ion.with(this)
                .load(G.UploadContacts)
                .addHeader("Authorization", "Bearer " + G.pref.getString("token" , ""))
                .setBodyParameter("names", names)
                .setBodyParameter("phone_numbers", phoneNumbers)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        Toast.makeText(ContactsActivity.this, "Uploaded Successfully.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @SuppressLint("Range")
    private void getContacts(){
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                @SuppressLint("Range") String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                @SuppressLint("Range") String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (names.isEmpty()){
                            names = names + name;
                        }else{
                            names = names + "$#$" + name;
                        }

                        if (phoneNumbers.isEmpty()){
                            phoneNumbers = phoneNumbers + phoneNo.replace("+", "");
                        }else{
                            phoneNumbers = phoneNumbers + "$#$" + phoneNo.replace("+", "");
                        }
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts();
                    apiCallForUploadContacts();
                } else {
                    switchUpload.setChecked(false);
                    Toast.makeText(ContactsActivity.this, "Permission denied to read your Contacts", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}