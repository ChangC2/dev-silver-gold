package com.mobile.seemesave.view.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.FriendAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.listener.ClickListener;
import com.mobile.seemesave.model.common.FriendModel;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.profile.FriendViewModel;
import com.rocky.contactfetcher.ContactFetcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendActivity extends BaseActivity {

    FriendActivity activity;
    private FriendViewModel mViewModel;

    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.lytFriend)
    LinearLayout lytFriend;
    @BindView(R.id.lytInvite)
    RelativeLayout lytInvite;
    @BindView(R.id.lytRequest)
    LinearLayout lytRequest;

    @BindView(R.id.recyclerFriends)
    RecyclerView recyclerFriends;

    @BindView(R.id.recyclerInvites)
    RecyclerView recyclerInvites;

    @BindView(R.id.recyclerRequests)
    RecyclerView recyclerRequests;

    @BindView(R.id.btnFriend)
    LinearLayout btnFriend;
    @BindView(R.id.btnInvite)
    LinearLayout btnInvite;
    @BindView(R.id.btnRequest)
    LinearLayout btnRequest;

    @BindView(R.id.txtFriend)
    TextView txtFriend;
    @BindView(R.id.txtInvite)
    TextView txtInvite;
    @BindView(R.id.txtRequest)
    TextView txtRequest;

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
    EditText editFName;

    @BindView(R.id.editEmail)
    EditText editEmail;

    @BindView(R.id.editMobile)
    EditText editMobile;

    @BindView(R.id.txtPhone)
    EditText txtPhone;


    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.txtNoFriend)
    TextView txtNoFriend;
    @BindView(R.id.txtSent)
    TextView txtSent;
    @BindView(R.id.txtReceived)
    TextView txtReceived;
    @BindView(R.id.btnUploadContacts)
    LinearLayout btnUploadContacts;


    @BindView(R.id.country_picker)
    CountryCodePicker countryCodePicker;

    private FriendAdapter friendAdapter;
    private ArrayList<FriendModel> friendList = new ArrayList<>();
    private FriendAdapter inviteAdapter;
    private ArrayList<FriendModel> inviteList = new ArrayList<>();
    private FriendAdapter requestAdapter;
    private ArrayList<FriendModel> requestList = new ArrayList<>();
    private int offset = 0;
    private int limit = 20;
    private boolean isLoading = false;
    private boolean isLast = false;
    static final int PICK_CONTACT = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mViewModel = new ViewModelProvider(this).get(FriendViewModel.class);
        setContentView(R.layout.activity_friend);
        ButterKnife.bind(this);
        activity = this;
        initView();
    }
    @Override
    public void onStart() {
        super.onStart();
        mViewModel.getIsBusy().observe(this, isBusy -> {
            if (isBusy){
                showLoadingDialog();
            }else {
                hideLoadingDialog();
            }
        });
        mViewModel.getFriendList().observe(this, list -> {
            if (offset == 0 && list.size() == 0) {
                txtNoFriend.setVisibility(View.VISIBLE);
                friendList.clear();
                friendAdapter.setData(friendList);
                return;
            } else {
                txtNoFriend.setVisibility(View.GONE);
                friendList.clear();
//                if (offset == 0) {
//                    friendList.clear();
//                }
//                if (list.size() < limit) {
//                    isLast = true;
//                }
                friendList.addAll(list);
                friendAdapter.setData(friendList);
                isLoading = false;
            }
        });
        mViewModel.getInviteList().observe(this, list -> {
            if (offset == 0 && list.size() == 0) {
                txtSent.setVisibility(View.GONE);
                return;
            } else {
                txtSent.setVisibility(View.VISIBLE);
                inviteList.clear();
                inviteList.addAll(list);
                inviteAdapter.setData(inviteList);
            }
        });
        mViewModel.getRequestList().observe(this, list -> {
            if (offset == 0 && list.size() == 0) {
                txtReceived.setVisibility(View.GONE);
                return;
            } else {
                txtReceived.setVisibility(View.VISIBLE);
                requestList.clear();
                requestList.addAll(list);
                requestAdapter.setData(requestList);
            }
        });

    }
    @Override
    public void onStop() {
        super.onStop();
    }
    private void initView() {
        setTab(0);
        setCheckView(0);
        friendList.clear();
        setRecyclerFriend();
        setRecyclerInvite();
        setRecyclerRequest();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                friendList.clear();
                offset = 0;
                mViewModel.setOffset(offset);
                mViewModel.loadData();
            }
        });
//        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
//                int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));
//                if (diff == 0 && !isLoading && !isLast) {
//                    isLoading = true;
//                    offset = offset + limit;
//                    mViewModel.setOffset(offset);
//                    mViewModel.loadData();
//                }
//            }
//        });
        mViewModel.loadData();
    }
    private void setRecyclerFriend() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerFriends.setLayoutManager(linearLayoutManager);
        friendAdapter = new FriendAdapter(activity, friendList, new FriendAdapter.FriendRecyclerListener() {
            @Override
            public void onItemClicked(int pos, FriendModel model) {
                Intent intent = new Intent(activity, FriendDetailActivity.class);
                intent.putExtra("friend_id", model.getFriend().getId());
                intent.putExtra("image", model.getFriend().getImage_url());
                intent.putExtra("name", model.getFriend().getFirst_name() + " " + model.getFriend().getLast_name());
                startActivity(intent);
            }

            @Override
            public void onRemove(int pos, FriendModel model) {
                ClickListener listener = new ClickListener() {
                    @Override
                    public void onClick(boolean flag) {
                        apiCallForDeleteFriend(model.getId());
                    }
                };
                G.showDlg(activity, getString(R.string.confirm_delete_friend), listener, true);
            }
            @Override
            public void onAccept(int pos, FriendModel model) {

            }

            @Override
            public void onDecline(int pos, FriendModel model) {

            }
        });
        recyclerFriends.setAdapter(friendAdapter);
    }
    private void setRecyclerInvite() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerInvites.setLayoutManager(linearLayoutManager);
        inviteAdapter = new FriendAdapter(activity, inviteList, new FriendAdapter.FriendRecyclerListener() {
            @Override
            public void onItemClicked(int pos, FriendModel model) {

            }

            @Override
            public void onRemove(int pos, FriendModel model) {
                ClickListener listener = new ClickListener() {
                    @Override
                    public void onClick(boolean flag) {
                        apiCallForDeleteFriend(model.getId());
                    }
                };
                G.showDlg(activity, getString(R.string.confirm_delete_friend), listener, true);
            }
            @Override
            public void onAccept(int pos, FriendModel model) {

            }

            @Override
            public void onDecline(int pos, FriendModel model) {

            }
        });
        recyclerInvites.setAdapter(inviteAdapter);
    }
    private void setRecyclerRequest() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerRequests.setLayoutManager(linearLayoutManager);
        requestAdapter = new FriendAdapter(activity, requestList, new FriendAdapter.FriendRecyclerListener() {
            @Override
            public void onItemClicked(int pos, FriendModel model) {

            }

            @Override
            public void onRemove(int pos, FriendModel model) {

            }

            @Override
            public void onAccept(int pos, FriendModel model) {
                JsonObject json = new JsonObject();
                json.addProperty("infoId", model.getId());
                String token = G.pref.getString("token", "");
                showLoadingDialog();
                Ion.with(activity)
                        .load("PUT", G.AcceptInviteUrl)
                        .addHeader("Authorization", "Bearer " + token)
                        .addHeader("Content-Type", "application/json")
                        .setJsonObjectBody(json)
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                hideLoadingDialog();
                                Toast.makeText(activity, "Accepted to invite friend", Toast.LENGTH_LONG).show();
                                requestList.remove(pos);
                                requestAdapter.setData(requestList);
                            }
                        });
            }

            @Override
            public void onDecline(int pos, FriendModel model) {
                JsonObject json = new JsonObject();
                json.addProperty("infoId", model.getId());
                String token = G.pref.getString("token", "");
                showLoadingDialog();
                Ion.with(activity)
                        .load("DELETE", G.AcceptInviteUrl)
                        .addHeader("Authorization", "Bearer " + token)
                        .addHeader("Content-Type", "application/json")
                        .setJsonObjectBody(json)
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                hideLoadingDialog();
                                Toast.makeText(activity, "Declined to invite friend", Toast.LENGTH_LONG).show();
                                requestList.remove(pos);
                                requestAdapter.setData(requestList);
                            }
                        });
            }
        });
        recyclerRequests.setAdapter(requestAdapter);
    }
    @SuppressLint({"UseCompatLoadingForDrawables"})
    private void setTab(int pos) {
        switch (pos) {
            case 0:
                btnFriend.setBackground(getDrawable(R.drawable.bk_blue_rect_5));
                btnInvite.setBackground(getDrawable(R.drawable.ic_transparent));
                btnRequest.setBackground(getDrawable(R.drawable.ic_transparent));
                txtFriend.setTextColor(ContextCompat.getColor(activity, R.color.white_color));
                txtInvite.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                txtRequest.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));

                lytFriend.setVisibility(View.VISIBLE);
                lytInvite.setVisibility(View.GONE);
                lytRequest.setVisibility(View.GONE);
                break;
            case 1:
                btnFriend.setBackground(getDrawable(R.drawable.ic_transparent));
                btnInvite.setBackground(getDrawable(R.drawable.bk_blue_rect_5));
                btnRequest.setBackground(getDrawable(R.drawable.ic_transparent));
                txtFriend.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                txtInvite.setTextColor(ContextCompat.getColor(activity, R.color.white_color));
                txtRequest.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));

                lytFriend.setVisibility(View.GONE);
                lytInvite.setVisibility(View.VISIBLE);
                lytRequest.setVisibility(View.GONE);
                break;
            case 2:
                btnFriend.setBackground(getDrawable(R.drawable.ic_transparent));
                btnInvite.setBackground(getDrawable(R.drawable.ic_transparent));
                btnRequest.setBackground(getDrawable(R.drawable.bk_blue_rect_5));
                txtFriend.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                txtInvite.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                txtRequest.setTextColor(ContextCompat.getColor(activity, R.color.white_color));

                lytFriend.setVisibility(View.GONE);
                lytInvite.setVisibility(View.GONE);
                lytRequest.setVisibility(View.VISIBLE);
                break;
        }
    }
    private void setCheckView(int pos) {
        switch (pos) {
            case 0:
                chkEmail.setChecked(true);
                chkMobile.setChecked(false);
                chkContact.setChecked(false);
                lytEmail.setVisibility(View.VISIBLE);
                lytMobile.setVisibility(View.GONE);
                lytContact.setVisibility(View.GONE);
                break;
            case 1:
                chkEmail.setChecked(false);
                chkMobile.setChecked(true);
                chkContact.setChecked(false);
                lytEmail.setVisibility(View.GONE);
                lytMobile.setVisibility(View.VISIBLE);
                lytContact.setVisibility(View.GONE);
                break;
            case 2:
                chkEmail.setChecked(false);
                chkMobile.setChecked(false);
                chkContact.setChecked(true);
                lytEmail.setVisibility(View.GONE);
                lytMobile.setVisibility(View.GONE);
                lytContact.setVisibility(View.VISIBLE);
                break;
        }
    }
    private void onGetContacts() {
        startContactPickActivity();
    }
    private void startContactPickActivity() {
        editFName.setText("");
        txtPhone.setText("");
        Intent intent = new Intent(activity, ContactListActivity.class);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, PICK_CONTACT);
    }
    private void onSend() {
        if (chkEmail.isChecked())
            apiCallForSendEmail();
        else
            apiCallForSendMobile();
    }
    private void onUploadContacts() {
        Intent intent = new Intent(activity, ContactsActivity.class);
        startActivity(intent);
    }
    void apiCallForDeleteFriend(int id) {
        JsonObject json = new JsonObject();
        json.addProperty("infoId", id);
        String token = G.pref.getString("token", "");
        showLoadingDialog();
        Ion.with(activity)
                .load("DELETE", G.AcceptInviteUrl)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        Toast.makeText(activity, "Friend deleted", Toast.LENGTH_LONG).show();
                        mViewModel.loadData();
                    }
                });
    }
    private void apiCallForSendEmail() {
        if (TextUtils.isEmpty(editEmail.getText().toString()) || TextUtils.isEmpty(editFName.getText().toString())) {
            Toast.makeText(activity, R.string.missing_param, Toast.LENGTH_LONG).show();
            return;
        }
        JsonObject json = new JsonObject();
        json.addProperty("first_name", editFName.getText().toString());
        json.addProperty("email", editEmail.getText().toString());
        String token = G.pref.getString("token", "");
        showLoadingDialog();
        Ion.with(activity)
                .load("POST", G.InviteFriendUrl)
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
                                editEmail.setText("");
                                editFName.setText("");
                                mViewModel.loadData();
                            } else {
                                Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException jsonException) {
                            Toast.makeText(activity, R.string.connection_fail, Toast.LENGTH_LONG).show();
                        }
                    }
                });
        G.hideSoftKeyboard(activity);
    }

    private void apiCallForSendMobile() {
        if ((chkContact.isChecked() && TextUtils.isEmpty(txtPhone.getText().toString())) || (chkMobile.isChecked() && TextUtils.isEmpty(editMobile.getText().toString())) || TextUtils.isEmpty(editFName.getText().toString())) {
            Toast.makeText(activity, R.string.missing_param, Toast.LENGTH_LONG).show();
            return;
        }
        if ((chkContact.isChecked() && !G.isValidMobile(txtPhone.getText().toString())) ||
                (chkMobile.isChecked() && !G.isValidMobile(countryCodePicker.getSelectedCountryCode() + editMobile.getText().toString()))) {

            Toast.makeText(activity, R.string.invalid_phone, Toast.LENGTH_LONG).show();
            return;
        }
        JsonObject json = new JsonObject();
        json.addProperty("first_name", editFName.getText().toString());
        if (chkContact.isChecked()) {
            String phoneNumber = txtPhone.getText().toString().replace("+", "");
            json.addProperty("phoneNumber", phoneNumber);
            json.addProperty("countryCode", "");
        } else {
            json.addProperty("phoneNumber", editMobile.getText().toString());
            json.addProperty("countryCode", countryCodePicker.getSelectedCountryCode());
        }

        String token = G.pref.getString("token", "");
        showLoadingDialog();
        Ion.with(activity)
                .load("POST", G.InviteFriendUrl)
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
                                editFName.setText("");
                                if (chkContact.isChecked()) {
                                    txtPhone.setText("");
                                } else {
                                    editMobile.setText("");
                                }
                                mViewModel.loadData();
                            } else {
                                Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException jsonException) {
                            Toast.makeText(activity, R.string.connection_fail, Toast.LENGTH_LONG).show();
                        }
                    }
                });

        G.hideSoftKeyboard(activity);
    }
    @OnClick({R.id.btBack, R.id.btnFriend, R.id.btnInvite, R.id.btnRequest, R.id.chkEmail, R.id.chkMobile, R.id.chkContact, R.id.btnInviteRequest, R.id.btnUploadContacts, R.id.txtChoose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.btnFriend:
                setTab(0);
                break;
            case R.id.btnInvite:
                setTab(1);
                break;
            case R.id.btnRequest:
                setTab(2);
                break;
            case R.id.chkEmail:
                setCheckView(0);
                break;
            case R.id.chkMobile:
                setCheckView(1);
                break;
            case R.id.chkContact:
                setCheckView(2);
                onGetContacts();
                break;
            case R.id.btnInviteRequest:
                onSend();
                break;
            case R.id.btnUploadContacts:
                onUploadContacts();
                break;
            case R.id.txtChoose:
                onGetContacts();
                break;
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
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    editFName.setText(data.getStringExtra("name"));
                    txtPhone.setText(data.getStringExtra("mobile_number"));
                }
                break;
        }
    }
}
