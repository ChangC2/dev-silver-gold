package com.cam8.mmsapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cam8.mmsapp.adapters.MaintenancesAdapter;
import com.cam8.mmsapp.model.Maintenance;
import com.cam8.mmsapp.network.GoogleCertProvider;
import com.cam8.mmsapp.network.ITSRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MaintenancesActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    MaintenancesAdapter maintenancesAdapter;
    ArrayList<Maintenance> maintenances = new ArrayList<>();

    TextView tvTaskMachine;
    TextView tvTaskName;
    TextView tvTaskCategory;
    ImageView ivTaskLogo;
    TextView tvTaskInstruction;
    TextView tvTaskFrequency;
    TextView tvTaskCycleStartInterlock;
    TextView tvTimeToNext;
    EditText tvTaskNotes;
    Button btnCompleteMaintenanceTask;

    View viewAttaches;

    ImageLoader imageLoader;
    DisplayImageOptions imageOptions;

    Maintenance currMaintenanceInfo = new Maintenance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maintenances);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(R.string.title_maintenances);

        // Check Factory and Account Settings.
        if (TextUtils.isEmpty(appSettings.getCustomerID()) || TextUtils.isEmpty(appSettings.getMachineName())) {
            showAlert("Please set up factory and machine settings to set installation configuration.", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            return;
        }

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        imageOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .showImageOnLoading(R.drawable.ic_st_logo)
                .showImageOnFail(R.drawable.ic_st_logo)
                .showImageForEmptyUri(R.drawable.ic_st_logo)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        // PDF Files list
        recyclerView = findViewById(R.id.rcvMaintenances);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        maintenancesAdapter = new MaintenancesAdapter(mContext, maintenances, new MaintenancesAdapter.MaintenanceItemListener() {
            @Override
            public void onClick(View view, int position) {
                showItemInfo(position);
            }
        });
        recyclerView.setAdapter(maintenancesAdapter);

        tvTaskMachine = findViewById(R.id.tvTaskMachine);
        tvTaskName = findViewById(R.id.tvTaskName);
        tvTaskCategory = findViewById(R.id.tvTaskCategory);
        ivTaskLogo = findViewById(R.id.ivTaskLogo);
        tvTaskInstruction = findViewById(R.id.tvTaskInstruction);
        tvTaskFrequency = findViewById(R.id.tvTaskFrequency);
        tvTaskCycleStartInterlock = findViewById(R.id.tvTaskCycleStartInterlock);
        tvTimeToNext = findViewById(R.id.tvTimeToNext);

        tvTaskNotes = findViewById(R.id.tvTaskNotes);
        tvTaskNotes.setKeyListener(null);

        btnCompleteMaintenanceTask = findViewById(R.id.btnCompleteMaintenanceTask);
        btnCompleteMaintenanceTask.setOnClickListener(this);
        btnCompleteMaintenanceTask.setVisibility(View.GONE);

        viewAttaches = findViewById(R.id.viewAttaches);
        viewAttaches.setOnClickListener(this);
        TextView tvAttachFiles = findViewById(R.id.tvAttachFiles);
        SpannableString content = new SpannableString(tvAttachFiles.getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tvAttachFiles.setText(content);

        getMaintenances();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getMaintenances() {
        Log.e("Alarm", "Check Maintenance Task");
        MaintenanceTaskManager maintenanceTaskManager = MaintenanceTaskManager.getInstance(getApplicationContext());

        RequestParams requestParams = new RequestParams();
        requestParams.put("customerId", appSettings.getCustomerID());
        requestParams.put("machineId", appSettings.getMachineName());
        Log.e("getAllMaintenanceInfo", requestParams.toString());

        showProgressDialog();
        GoogleCertProvider.install(getApplicationContext());
        ITSRestClient.post(mContext, "getAllMaintenanceInfo", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                hideProgressDialog();

                // If the response is JSONObject instead of expected JSONArray
                Log.e("getAllMaintenanceInfo", response.toString());

                try {
                    if (response.has("status") && response.getBoolean("status")) {

                        // User Information is exist
                        JSONArray jsonArray = response.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonItem = jsonArray.getJSONObject(i);
                                Maintenance maintenance = new Maintenance(jsonItem);
                                maintenances.add(maintenance);
                            }

                            maintenancesAdapter.notifyDataSetChanged();
                            showItemInfo(0);
                        } else {
                            showAlert("No maintenance info for the maichine.", new View.OnClickListener(){
                                @Override
                                public void onClick(View view) {
                                    finish();
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                hideProgressDialog();

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                }

                Log.e("getAllMaintenanceInfo", errorMsg);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                hideProgressDialog();

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                }

                Log.e("getAllMaintenanceInfo", errorMsg);
            }
        });
    }

    private void showItemInfo(int position) {
        currMaintenanceInfo = maintenances.get(position);

        tvTaskMachine.setText(currMaintenanceInfo.getMachineId());
        tvTaskName.setText(currMaintenanceInfo.getTaskName());
        tvTaskCategory.setText(currMaintenanceInfo.getTaskCategory());
        tvTaskInstruction.setText(currMaintenanceInfo.getTaskInstruction());
        tvTaskFrequency.setText(String.valueOf(currMaintenanceInfo.getFrequency()));
        tvTaskCycleStartInterlock.setText(currMaintenanceInfo.getInterlock() > 0 ? "YES" : "NO");
        tvTaskNotes.setText(currMaintenanceInfo.getUserNotes());

        // Show Maintenance Task Logo
        imageLoader.displayImage(currMaintenanceInfo.getPicture(), ivTaskLogo, imageOptions);

        Maintenance localMaintenanceInfo = MaintenanceTaskManager.getInstance(mContext).getMaintenanceInfoWithID(currMaintenanceInfo.getId());
        if (localMaintenanceInfo != null) {
            long timeFrequencyMils = currMaintenanceInfo.getFrequency() * 3600 * 1000;
            long timeToNextInMils = timeFrequencyMils - localMaintenanceInfo.getTotalIncycleTime();

            boolean isDue = false;
            if (timeToNextInMils < 0) {
                isDue = true;
                timeToNextInMils = -timeToNextInMils;
            }

            float timeToNext = timeToNextInMils / (3600 * 1000.0f);
            if (isDue) {
                // Overdue by: {hourText} Hrs
                tvTimeToNext.setText(String.format("Overdue by: %.2f Hrs", timeToNext));
                tvTimeToNext.setTextColor(0xffff0000);
            } else {
                tvTimeToNext.setText(String.format("Due In: %.2f Hrs", timeToNext));
                if (currMaintenanceInfo.getFrequency() * 0.2 >= timeToNext) {
                    tvTimeToNext.setTextColor(0xffffff00);
                } else {
                    tvTimeToNext.setTextColor(0xff76CB66);
                }
            }
        } else {
            tvTimeToNext.setText("");
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btnCompleteMaintenanceTask) {

        } else if(viewId == R.id.viewAttaches) {
            // View Attaches
            if (currMaintenanceInfo == null || TextUtils.isEmpty(currMaintenanceInfo.getFiles())) {
                showToastMessage("No Attachs info!");
            }

            // Go to Viewing Files
            Intent intent = new Intent(this, ViewAttachesActivity.class);
            intent.putExtra("files", currMaintenanceInfo.getFiles());
            intent.putExtra("title", "Attaches for maintenance");
            startActivity(intent);
        }
    }
}
