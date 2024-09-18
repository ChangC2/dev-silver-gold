package com.mobile.seemesave.view.profile;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.SelectFriendsAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.listener.RecyclerClickListener;
import com.mobile.seemesave.model.common.MCommon;
import com.mobile.seemesave.utils.G;
import com.rocky.contactfetcher.ContactFetcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendSelectActivity extends BaseActivity {

    @BindView(R.id.imgBack)
    ImageView imgBack;

    @BindView(R.id.recyclerList)
    RecyclerView recyclerList;

    @BindView(R.id.lytShare)
    LinearLayout lytShare;

    @BindView(R.id.btnInviteRequest)
    LinearLayout btnInviteRequest;

    @BindView(R.id.lytEmail)
    LinearLayout lytEmail;

    @BindView(R.id.lytMobile)
    LinearLayout lytMobile;

    @BindView(R.id.lytContact)
    LinearLayout lytContact;

    @BindView(R.id.chkEmail)
    CheckBox chkEmail;

    @BindView(R.id.chkMobile)
    CheckBox chkMobile;

    @BindView(R.id.chkContact)
    CheckBox chkContact;

    @BindView(R.id.editFName)
    TextInputEditText editFName;

    @BindView(R.id.editEmail)
    TextInputEditText editEmail;

    @BindView(R.id.editMobile)
    TextInputEditText editMobile;

    @BindView(R.id.txtPhone)
    TextInputEditText txtPhone;

    @BindView(R.id.txtChoose)
    TextView txtChoose;

    @BindView(R.id.country_picker)
    CountryCodePicker countryCodePicker;

    @BindView(R.id.lytInvite)
    RelativeLayout lytInvite;

    @BindView(R.id.btnSelect)
    LinearLayout btnSelect;
    @BindView(R.id.btnInvite)
    LinearLayout btnInvite;

    @BindView(R.id.txtSelect)
    TextView txtSelect;
    @BindView(R.id.txtInvite)
    TextView txtInvite;

    @BindView(R.id.txtNoFriend)
    TextView txtNoFriend;


    FriendSelectActivity activity;

    ArrayList<MCommon> friendLists = new ArrayList<>();
    ArrayList<MCommon> selectedFriends = new ArrayList<>();

    private SelectFriendsAdapter listAdapter;
    String id = "";
    int type = 0;
    static final int PICK_CONTACT=101;
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_friend_select);
        ButterKnife.bind(this);
        activity = this;
        type = getIntent().getIntExtra("type", 0);
        id = getIntent().getStringExtra("id");
        imgBack.setOnClickListener(view->onBack());
        lytShare.setOnClickListener(view->onShare());
        lytShare.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_grey_light_rect_10));
        chkEmail.setOnClickListener(view->onChkEmail());
        chkMobile.setOnClickListener(view->onChkMobile());
        btnInviteRequest.setOnClickListener(view->onSend());
        chkContact.setOnClickListener(view->onGetContacts());
        txtChoose.setOnClickListener(view->onGetContacts());

        btnSelect.setOnClickListener(view->onFriends());
        btnInvite.setOnClickListener(view->onInvites());

        onChkEmail();
        onFriends();
    }

    private void onFriends(){

        btnSelect.setBackground(getDrawable(R.drawable.bk_blue_rect_5));
        btnInvite.setBackground(getDrawable(R.drawable.ic_transparent));
        txtSelect.setTextColor(ContextCompat.getColor(activity, R.color.white_color));
        txtInvite.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
        btnInviteRequest.setVisibility(View.GONE);
        lytShare.setVisibility(View.VISIBLE);
        lytInvite.setVisibility(View.GONE);
        selectedFriends.clear();
        apiCallForGetList();
    }

    private void onInvites(){
        btnInvite.setBackground(getDrawable(R.drawable.bk_blue_rect_5));
        btnSelect.setBackground(getDrawable(R.drawable.ic_transparent));
        txtInvite.setTextColor(ContextCompat.getColor(activity, R.color.white_color));
        txtSelect.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));

        recyclerList.setVisibility(View.GONE);
        lytShare.setVisibility(View.GONE);
        lytInvite.setVisibility(View.VISIBLE);
        btnInviteRequest.setVisibility(View.VISIBLE);
        txtNoFriend.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void onBack(){
        finish();
    }

    private void onShare(){
        if (type == 0)
            apiCallForShareShoppingList();
        else
            apiCallForShareFeedList();
    }


    private void setRecyclerList() {
        if (friendLists.size() == 0){
            recyclerList.setVisibility(View.GONE);
            txtNoFriend.setVisibility(View.VISIBLE);
        }else{
            recyclerList.setVisibility(View.VISIBLE);
            txtNoFriend.setVisibility(View.GONE);
        }
        lytShare.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerList.setLayoutManager(linearLayoutManager);
        recyclerList.setItemAnimator(new DefaultItemAnimator());
        RecyclerClickListener listener = new RecyclerClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (friendLists.get(position).isHasLiked()) {
                    selectedFriends.add(friendLists.get(position));
                }else{
                    for (int i=0; i<selectedFriends.size();i++){
                        if (selectedFriends.get(i).getId().equalsIgnoreCase(friendLists.get(position).getId())){
                            selectedFriends.remove(i);
                            break;
                        }
                    }
                }
                if (selectedFriends.size()>0){
                    lytShare.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_blue_rect_10));
                }else{
                    lytShare.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_grey_light_rect_10));
                }
            }

            @Override
            public void onClick(View v, int position, int type) {
            }
        };
        listAdapter = new SelectFriendsAdapter(activity, friendLists, listener);
        recyclerList.setAdapter(listAdapter);
    }

    //***************************************//
    //             API Call Method           //
    //***************************************//

    void apiCallForGetList() {
        showLoadingDialog();
        String token = G.pref.getString("token" , "");
        String url = String.format(java.util.Locale.US,G.GetFriendListUrl, 0, 100);
        Ion.with(activity)
                .load(url)
                .addHeader("Authorization", "Bearer " + token)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        if (e == null){
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("status")){
                                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("friendInfoList");
                                    friendLists.clear();
                                    for (int i=0;i<jsonArray.length();i++){
                                        JSONObject friendInfo = jsonArray.getJSONObject(i);
                                        MCommon friend = new MCommon();
                                        friend.setFreindInfoId(friendInfo.getString("id"));
                                        friend.setId(friendInfo.getJSONObject("Friend").getString("id"));
                                        friend.setNew(friendInfo.getBoolean("is_pending"));
                                        friend.setName(friendInfo.getJSONObject("Friend").optString("first_name") + " " + friendInfo.getJSONObject("Friend").optString("last_name"));
                                        friend.setImageUrl(friendInfo.getJSONObject("Friend").optString("image_url"));
                                        if (!friend.isNew()){
                                            friendLists.add(friend);
                                        }
                                    }
                                }
                            } catch (JSONException jsonException) {
                            }
                        }
                        setRecyclerList();
                    }
                });
    }

    private void apiCallForShareShoppingList(){
        if (selectedFriends.size() == 0){
            return;
        }

        JsonObject json = new JsonObject();
        json.addProperty("shopping_list_id", id);

        String idList = selectedFriends.get(0).getId();
        for (int i=1;i<selectedFriends.size();i++){
            idList = idList + "," + selectedFriends.get(i).getId();
        }
        json.addProperty("friend_id_list", idList);

        String token = G.pref.getString("token" , "");
        showLoadingDialog();
        Ion.with(activity)
                .load(G.ShareShoppingListUrl)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        if (e == null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("status")){
                                    Toast.makeText(activity, R.string.shared_shopping_list, Toast.LENGTH_LONG).show();
                                    finish();
                                }else{
                                    Toast.makeText(activity, R.string.connection_fail, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException jsonException) {
                                Toast.makeText(activity, R.string.connection_fail, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void apiCallForShareFeedList(){
        if (selectedFriends.size() == 0){
            return;
        }

        JsonObject json = new JsonObject();
        json.addProperty("newsfeed_id", id);

        String idList = selectedFriends.get(0).getId();
        for (int i=1;i<selectedFriends.size();i++){
            idList = idList + "," + selectedFriends.get(i).getId();
        }
        json.addProperty("shared_users", idList);

        String token = G.pref.getString("token" , "");
        showLoadingDialog();
        Ion.with(activity)
                .load(G.ShareFeedUrl)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        if (e == null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("status")){
                                    Toast.makeText(activity, R.string.shared_feed, Toast.LENGTH_LONG).show();
                                    finish();
                                }else{
                                    Toast.makeText(activity, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException jsonException) {
                                Toast.makeText(activity, R.string.connection_fail, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }


    private void onSend(){
        if (chkEmail.isChecked())
            apiCallForSendEmail();
        else
            apiCallForSendMobile();
    }

    private void onChkEmail(){
        chkEmail.setChecked(true);
        chkMobile.setChecked(false);
        chkContact.setChecked(false);
        lytEmail.setVisibility(View.VISIBLE);
        lytMobile.setVisibility(View.GONE);
        lytContact.setVisibility(View.GONE);
        editFName.setText("");
    }

    private void onChkMobile(){
        chkEmail.setChecked(false);
        chkMobile.setChecked(true);
        chkContact.setChecked(false);
        lytEmail.setVisibility(View.GONE);
        lytMobile.setVisibility(View.VISIBLE);
        lytContact.setVisibility(View.GONE);
        editFName.setText("");
    }

    private void onGetContacts(){
        startContactPickActivity();
    }

    private void startContactPickActivity(){
        chkEmail.setChecked(false);
        chkMobile.setChecked(false);
        chkContact.setChecked(true);
        lytEmail.setVisibility(View.GONE);
        lytMobile.setVisibility(View.GONE);
        lytContact.setVisibility(View.VISIBLE);
        editFName.setText("");
        txtPhone.setText("");
        Intent intent = new Intent(activity, ContactListActivity.class);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, PICK_CONTACT);
    }


    private void apiCallForSendEmail(){
        if (TextUtils.isEmpty(editEmail.getText().toString()) || TextUtils.isEmpty(editFName.getText().toString())){
            Toast.makeText(activity, R.string.missing_param, Toast.LENGTH_LONG).show();
            return;
        }
        JsonObject json = new JsonObject();
        if (type == 0)
            json.addProperty("shopping_list_id", id);
        else
            json.addProperty("newsfeed_id", id);
        json.addProperty("first_name", editFName.getText().toString());
        json.addProperty("email", editEmail.getText().toString());


        String token = G.pref.getString("token" , "");
        showLoadingDialog();
        Ion.with(activity)
                .load("PUT", type == 0 ? G.InviteFriendAndShareShoppingList : G.InviteFriendAndShareFeed)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getBoolean("status")) {
                                Toast.makeText(activity, R.string.invite_sent, Toast.LENGTH_LONG).show();
                                finish();
                            }else{
                                Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException jsonException) {
                            Toast.makeText(activity, R.string.connection_fail, Toast.LENGTH_LONG).show();
                        }
                    }
                });

        G.hideSoftKeyboard(activity);
    }

    private void apiCallForSendMobile(){
        if ((chkContact.isChecked() && TextUtils.isEmpty(txtPhone.getText().toString())) || (chkMobile.isChecked() && TextUtils.isEmpty(editMobile.getText().toString())) || TextUtils.isEmpty(editFName.getText().toString())){
            Toast.makeText(activity, R.string.missing_param, Toast.LENGTH_LONG).show();
            return;
        }

        if ((chkContact.isChecked() && !G.isValidMobile(txtPhone.getText().toString())) ||
                (chkMobile.isChecked() && !G.isValidMobile(countryCodePicker.getSelectedCountryCode() + editMobile.getText().toString()))){

            Toast.makeText(activity, R.string.invalid_phone, Toast.LENGTH_LONG).show();
            return;
        }

        JsonObject json = new JsonObject();
        if (type == 0)
            json.addProperty("shopping_list_id", id);
        else
            json.addProperty("newsfeed_id", id);
        json.addProperty("first_name", editFName.getText().toString());
        if (chkContact.isChecked()){
            json.addProperty("phoneNumber", txtPhone.getText().toString()
                    .replace("+", "")
                    .replace(" ", "")
                    .replace("-", "")
                    .replace("(", "")
                    .replace(")", ""));
            json.addProperty("countryCode", "");
        }else{
            json.addProperty("phoneNumber", editMobile.getText().toString());
            json.addProperty("countryCode", countryCodePicker.getSelectedCountryCode());
        }

        String token = G.pref.getString("token" , "");
        showLoadingDialog();
        Ion.with(activity)
                .load("PUT", type == 0 ? G.InviteFriendAndShareShoppingList : G.InviteFriendAndShareFeed)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getBoolean("status")) {
                                Toast.makeText(activity, R.string.invite_sent, Toast.LENGTH_LONG).show();
                                finish();
                            }else{
                                Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException jsonException) {
                            Toast.makeText(activity, R.string.connection_fail, Toast.LENGTH_LONG).show();
                        }
                    }
                });

        G.hideSoftKeyboard(activity);
    }

    public void requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Read Contacts permission");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please enable access to contacts.");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {android.Manifest.permission.READ_CONTACTS}
                                    , PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            } else {
                startContactPickActivity();
            }
        } else {
            startContactPickActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ContactFetcher.resolvePermissionResult(activity, requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {
                    editFName.setText(data.getStringExtra("name"));
                    txtPhone.setText(data.getStringExtra("mobile_number"));
                }
                break;
        }
    }
}