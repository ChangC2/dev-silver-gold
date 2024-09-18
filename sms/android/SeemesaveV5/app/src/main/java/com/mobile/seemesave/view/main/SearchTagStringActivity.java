package com.mobile.seemesave.view.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.TagSelectAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.listener.RecyclerClickListener;
import com.mobile.seemesave.utils.G;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchTagStringActivity extends BaseActivity {

    @BindView(R.id.btnClose)
    LinearLayout btnClose;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.editSearch)
    EditText editSearch;

    ArrayList<String> lists = new ArrayList<>();
    TagSelectAdapter adapter;

    String query = "";
    String []typeList = {"", "friend", "store", "brand", "product"};
    int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        G.setLightFullScreen(this);
        setContentView(R.layout.activity_search_tag_string);
        ButterKnife.bind(this);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    query = editSearch.getText().toString();
                    getTagList();
                }
                return false;
            }
        });
        query = getIntent().getStringExtra("tag_string");
        type = getIntent().getIntExtra("type", 0);
        apiCallForGetTagList();
    }

    private void setRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        RecyclerClickListener listener = new RecyclerClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent();
                intent.putExtra("tag_string", lists.get(position));
                intent.putExtra("type", type);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

            @Override
            public void onClick(View v, int position, int type) {
            }
        };
        adapter = new TagSelectAdapter(this, lists, listener);
        recycler.setAdapter(adapter);
    }

    private void getTagList(){
        switch (type){
            case 0:
                apiCallForGetTagList();
                break;
            default:
                apiCallForGetList();
                break;
        }
    }

    //***************************************//
    //             API Call Method           //
    //***************************************//

    void apiCallForGetTagList() {
        showLoadingDialog();
        String token = G.pref.getString("token" , "");
        String encodedQuery = "";
        try {
            encodedQuery = URLEncoder.encode(query, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = String.format(java.util.Locale.US,G.SearchTagStringUrl, encodedQuery);

        Ion.with(this)
                .load(url)
                .addHeader("Authorization", "Bearer " + token)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        lists.clear();
                        if (e == null){
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("status")){
                                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("data");
                                    for (int i=0;i<jsonArray.length();i++){
                                        lists.add(jsonArray.getJSONObject(i).getString("tag"));
                                    }
                                }
                            } catch (JSONException jsonException) {
                            }
                        }
                        setRecycler();
                    }
                });
    }

    void apiCallForGetList() {
        showLoadingDialog();
        String token = G.pref.getString("token" , "");
        String encodedQuery = "";
        try {
            encodedQuery = URLEncoder.encode(query, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = String.format(java.util.Locale.US,G.GetTagSuggestUrl, encodedQuery, typeList[type]);

        Ion.with(this)
                .load(url)
                .addHeader("Authorization", "Bearer " + token)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        lists.clear();
                        if (e == null){
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("status")){
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i=0;i<jsonArray.length();i++){
                                        lists.add(jsonArray.getString(i));
                                    }
                                }
                            } catch (JSONException jsonException) {
                            }
                        }
                        setRecycler();
                    }
                });
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
    }
}