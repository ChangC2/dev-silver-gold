package com.cam8.mmsapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cam8.mmsapp.AppSettings;
import com.cam8.mmsapp.BaseActivity;
import com.cam8.mmsapp.R;
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
import com.cam8.mmsapp.report.StageInfoSaveHelper;

import java.util.Objects;


public class StageInfoFragment extends DialogFragment implements View.OnClickListener, StageInfoSaveHelper.onStageInfoCallback {

    public interface StageInputsInterface {
        public void onInputFinished();
    }

    BaseActivity baseActivity;
    Context context;
    int stageID;
    StageInputsInterface inputsInterface;

    AppSettings appSettings;


    FaxonStageBase faxonStageInfo;
    TextView tvStageTitle;
    RecyclerView rvStageResult;
    StageInfoAdapter stageInfoAdapter;
    EditText edtNotes;

    public StageInfoFragment(BaseActivity activity, int stageID, StageInputsInterface inputsInterface) {

        this.baseActivity = activity;
        this.stageID = stageID;
        this.inputsInterface = inputsInterface;

        this.appSettings = new AppSettings(baseActivity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stageinfo, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve Stage Info
        if (stageID == FaxonStage1.STAGE_ID) {
            faxonStageInfo = new FaxonStage1();
            faxonStageInfo.loadDataFrom(appSettings.getStage1Info());
        } else if (stageID == FaxonStage2.STAGE_ID) {
            faxonStageInfo = new FaxonStage2();
            faxonStageInfo.loadDataFrom(appSettings.getStage2Info());
        } else if (stageID == FaxonStage3.STAGE_ID) {
            faxonStageInfo = new FaxonStage3();
            faxonStageInfo.loadDataFrom(appSettings.getStage3Info());
        } else if (stageID == FaxonStage4.STAGE_ID) {
            faxonStageInfo = new FaxonStage4();
            faxonStageInfo.loadDataFrom(appSettings.getStage4Info());
        } else if (stageID == FaxonStage5.STAGE_ID) {
            faxonStageInfo = new FaxonStage5();
            faxonStageInfo.loadDataFrom(appSettings.getStage5Info());
        } else if (stageID == FaxonStage6.STAGE_ID) {
            faxonStageInfo = new FaxonStage6();
            faxonStageInfo.loadDataFrom(appSettings.getStage6Info());
        } else if (stageID == FaxonStage7.STAGE_ID) {
            faxonStageInfo = new FaxonStage7();
            faxonStageInfo.loadDataFrom(appSettings.getStage7Info());
        } else if (stageID == FaxonStage8.STAGE_ID) {
            faxonStageInfo = new FaxonStage8();
            faxonStageInfo.loadDataFrom(appSettings.getStage8Info());
        } else if (stageID == FaxonStage9.STAGE_ID) {
            faxonStageInfo = new FaxonStage9();
            faxonStageInfo.loadDataFrom(appSettings.getStage9Info());
        }

        // Stage Title
        tvStageTitle = view.findViewById(R.id.tvStageTitle);
        tvStageTitle.setText(faxonStageInfo.getTitle());

        // Data Inputs
        rvStageResult = view.findViewById(R.id.rvStageResult);
        rvStageResult.setLayoutManager(new LinearLayoutManager(context));
        stageInfoAdapter = new StageInfoAdapter(context, faxonStageInfo, new StageInfoAdapter.StageInfoItemListener() {
            @Override
            public void onClick(View view, int position) {

            }
        });
        rvStageResult.setAdapter(stageInfoAdapter);

        // Notes Section
        edtNotes = view.findViewById(R.id.edtNotes);
        edtNotes.setText(faxonStageInfo.getNotes());

        // Add Close Action
        view.findViewById(R.id.btnClose).setOnClickListener(this);
        view.findViewById(R.id.btnSaveResult).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        //int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        //int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        //getDialog().getWindow().setLayout(width, height);

        //ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        //params.width = LayoutParams.MATCH_PARENT;
        //params.height = LayoutParams.MATCH_PARENT;
        //getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = (int) (displayMetrics.heightPixels * 0.9);
        int width = (int) (displayMetrics.widthPixels * 0.7);
        getDialog().getWindow().setLayout(width, height);

        setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnClose) {
            hideKeyboard();

            inputsInterface.onInputFinished();
            dismiss();
        } else if (viewId == R.id.btnSaveResult) {
            // Hide Keyboard
            hideKeyboard();

            String notes = edtNotes.getText().toString().trim();
            faxonStageInfo.setNotes(notes);

            /*if (!faxonStageInfo.isValidInput()) {
                baseActivity.showToastMessage("Please input values.");
                return;
            }*/

            // Save current Stage Data to Server, but We use another way to save all data at the same time.
            //new StageInfoSaveHelper(context, this, faxonStageInfo).execute();


            if (stageID == FaxonStage1.STAGE_ID) {
                appSettings.setStage1Info(faxonStageInfo.toString());
            } else if (stageID == FaxonStage2.STAGE_ID) {
                appSettings.setStage2Info(faxonStageInfo.toString());
            } else if (stageID == FaxonStage3.STAGE_ID) {
                appSettings.setStage3Info(faxonStageInfo.toString());
            } else if (stageID == FaxonStage4.STAGE_ID) {
                appSettings.setStage4Info(faxonStageInfo.toString());
            } else if (stageID == FaxonStage5.STAGE_ID) {
                appSettings.setStage5Info(faxonStageInfo.toString());
            } else if (stageID == FaxonStage6.STAGE_ID) {
                appSettings.setStage6Info(faxonStageInfo.toString());
            } else if (stageID == FaxonStage7.STAGE_ID) {
                appSettings.setStage7Info(faxonStageInfo.toString());
            } else if (stageID == FaxonStage8.STAGE_ID) {
                appSettings.setStage8Info(faxonStageInfo.toString());
            } else if (stageID == FaxonStage9.STAGE_ID) {
                appSettings.setStage9Info(faxonStageInfo.toString());
            }

            inputsInterface.onInputFinished();
            dismiss();
        }
    }

    private void hideKeyboard() {
        try {
            // Hide Keyboard
            //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            if (getDialog().getCurrentFocus() != null) {
                InputMethodManager inputManager = (InputMethodManager) Objects.requireNonNull(requireActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getDialog().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailed(String message) {
        baseActivity.showToastMessage(message);
    }

    @Override
    public void onSuccess(int id) {
        baseActivity.showToastMessage("Success to save!");
        dismiss();
    }
}
