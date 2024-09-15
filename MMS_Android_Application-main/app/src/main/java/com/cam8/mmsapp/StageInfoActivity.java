package com.cam8.mmsapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cam8.mmsapp.adapters.StageInfoAdapter;
import com.cam8.mmsapp.model.FaxonStage1;
import com.cam8.mmsapp.model.FaxonStage2;
import com.cam8.mmsapp.model.FaxonStage3;
import com.cam8.mmsapp.model.FaxonStage4;
import com.cam8.mmsapp.model.FaxonStage5;
import com.cam8.mmsapp.model.FaxonStage6;
import com.cam8.mmsapp.model.FaxonStage7;
import com.cam8.mmsapp.model.FaxonStage8;
import com.cam8.mmsapp.model.FaxonStage9;
import com.cam8.mmsapp.model.FaxonStageBase;

public class StageInfoActivity extends BaseActivity implements View.OnClickListener {

    FaxonStageBase faxonStageInfo;
    RecyclerView rvStageResult;
    StageInfoAdapter stageInfoAdapter;
    EditText edtNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide Status Bar
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

        setContentView(R.layout.activity_stageinfo);

        Intent intent = getIntent();
        int stageID = intent.getIntExtra("STAGE_ID", 0);
        if (stageID == FaxonStage1.STAGE_ID) {
            faxonStageInfo = new FaxonStage1();
        } else if (stageID == FaxonStage2.STAGE_ID) {
            faxonStageInfo = new FaxonStage2();
        } else if (stageID == FaxonStage3.STAGE_ID) {
            faxonStageInfo = new FaxonStage3();
        } else if (stageID == FaxonStage4.STAGE_ID) {
            faxonStageInfo = new FaxonStage4();
        } else if (stageID == FaxonStage5.STAGE_ID) {
            faxonStageInfo = new FaxonStage5();
        } else if (stageID == FaxonStage6.STAGE_ID) {
            faxonStageInfo = new FaxonStage6();
        } else if (stageID == FaxonStage7.STAGE_ID) {
            faxonStageInfo = new FaxonStage7();
        } else if (stageID == FaxonStage8.STAGE_ID) {
            faxonStageInfo = new FaxonStage8();
        } else if (stageID == FaxonStage9.STAGE_ID) {
            faxonStageInfo = new FaxonStage9();
        } else {
            // Wrong Calls
            finish();
            return;
        }

        // Set Title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(faxonStageInfo.getTitle());

        // Stage Result
        rvStageResult = findViewById(R.id.rvStageResult);
        rvStageResult.setLayoutManager(new LinearLayoutManager(mContext));
        stageInfoAdapter = new StageInfoAdapter(mContext, faxonStageInfo, new StageInfoAdapter.StageInfoItemListener() {
            @Override
            public void onClick(View view, int position) {

            }
        });
        rvStageResult.setAdapter(stageInfoAdapter);

        edtNotes = findViewById(R.id.edtNotes);

        findViewById(R.id.btnSaveResult).setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnSaveResult) {

        }
    }

    private void saveSettings() {


    }

}
