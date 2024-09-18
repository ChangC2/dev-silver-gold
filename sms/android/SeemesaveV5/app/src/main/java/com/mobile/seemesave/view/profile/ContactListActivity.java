package com.mobile.seemesave.view.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.ContactListAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.rocky.contactfetcher.Contact;
import com.rocky.contactfetcher.ContactFetcher;
import com.rocky.contactfetcher.ContactListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactListActivity extends BaseActivity {
    @BindView(R.id.txtTitle)
    TextView txtTitle;

    @BindView(R.id.lytSearch)
    LinearLayout lytSearch;

    @BindView(R.id.imgSearch)
    ImageView imgSearch;

    @BindView(R.id.editSearch)
    EditText editSearch;

    @BindView(R.id.recycler_view_list)
    RecyclerView recycler;

    private static final String TAG = ContactListActivity.class.getCanonicalName();
    private ContactListAdapter adapter;
    String searchKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        ButterKnife.bind(this);


        lytSearch.setVisibility(View.VISIBLE);
        imgSearch.setOnClickListener(view->onSearchClicked());

        editSearch.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                searchKey = editSearch.getText().toString();
                if (adapter != null) {
                    adapter.searchItems(searchKey);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        setRecycler();
        getContacts();
    }


    void setRecycler(){
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.removeAllViews();
        adapter = new ContactListAdapter(this, new ArrayList<>());
//        adapter.setKey(searchKey);
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener((position, v) -> {
            Intent intent = new Intent();
            intent.putExtra("name", adapter.getItem(position).displayName);
            String mobile_number = "";
            if (adapter.getItem(position).phoneNumbers.size() > 0)
                mobile_number = adapter.getItem(position).phoneNumbers.get(0);
            intent.putExtra("mobile_number", mobile_number);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }

    private void onSearchClicked(){
        if (lytSearch.getVisibility() == View.VISIBLE){
            lytSearch.setVisibility(View.GONE);
            txtTitle.setVisibility(View.VISIBLE);
//            imgBack.setVisibility(View.VISIBLE);
            editSearch.setText("");
            imgSearch.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_search));
        }else{
            lytSearch.setVisibility(View.VISIBLE);
            txtTitle.setVisibility(View.GONE);
//            imgBack.setVisibility(View.GONE);
            imgSearch.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_close));
        }
    }


    private void getContacts() {
        ContactFetcher.getContacts(this, new ContactListener<Contact>() {
            @Override
            public void onNext(Contact contact) {
                adapter.addItem(contact);
            }

            @Override
            public void onError(Throwable error) {
                Log.e(TAG, "onError: ", error);
            }

            @Override
            public void onComplete() {

            }
        });
    }
    @OnClick({R.id.btBack})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.loadSaveForLaterContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ContactFetcher.resolvePermissionResult(this, requestCode, permissions, grantResults);
    }
}
