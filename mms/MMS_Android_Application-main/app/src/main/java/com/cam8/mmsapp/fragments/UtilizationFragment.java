/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.cam8.mmsapp.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import com.cam8.mmsapp.R;
import com.cam8.mmsapp.model.RegimeData;
import com.cam8.mmsapp.utils.ChartColorUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.goodiebag.protractorview.SpeedometerView;

import java.util.ArrayList;

public class UtilizationFragment extends BaseFragment {

    public static UtilizationFragment newInstance(String text) {
        UtilizationFragment mFragment = new UtilizationFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    PieChart chartPie;
    Typeface mTf;

    //*********************** Pie Chat *****************************/
    int[] pieChatColors = ChartColorUtils.getCharColors();
    ArrayList<Integer> colorList = new ArrayList<>();

    Handler mChartUpdateHandler = new Handler();
    Runnable mChartUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if (chartPie != null) {
                chartPie.setCenterText("BMI:");
                chartPie.highlightValue(2, 0, false);
                chartPie.invalidate();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_utilization, container, false);

        TAG = "utilization";

        init(savedInstanceState);
        initLayout(view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initLayout(View parentView) {
        chartPie = parentView.findViewById(R.id.chartPie);

        /*pieChatColors = new int[] {
                ContextCompat.getColor(mContext, R.color.red),
                ContextCompat.getColor(mContext, R.color.green),
                ContextCompat.getColor(mContext, R.color.colorChart01),
                ContextCompat.getColor(mContext, R.color.colorChart02),
                ContextCompat.getColor(mContext, R.color.colorChart03),
                ContextCompat.getColor(mContext, R.color.colorChart04),
                ContextCompat.getColor(mContext, R.color.colorChart05),
                ContextCompat.getColor(mContext, R.color.colorChart06),
                ContextCompat.getColor(mContext, R.color.colorChart07),
                ContextCompat.getColor(mContext, R.color.colorChart08),
                ContextCompat.getColor(mContext, R.color.colorChart09)
        };*/

        mTf = Typeface.createFromAsset(mContext.getAssets(), "fonts/lato.ttf");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void updateRegimeData(RegimeData regimeData) {
        super.updateRegimeData(regimeData);

        if (chartPie == null || !isVisible())
            return;

        // Reset the chart
        chartPie.clear();

        long totalTime = 0;
        totalTime += regimeData.inCycleTime;
        totalTime += regimeData.unCatTime;
        totalTime += regimeData.idle1Time;
        totalTime += regimeData.idle2Time;
        totalTime += regimeData.idle3Time;
        totalTime += regimeData.idle4Time;
        totalTime += regimeData.idle5Time;
        totalTime += regimeData.idle6Time;
        totalTime += regimeData.idle7Time;
        totalTime += regimeData.idle8Time;
        totalTime += regimeData.offlineT;

        if (totalTime == 0)
            totalTime = 100;

        // Calculate total time
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry((float) regimeData.inCycleTime * 100f / totalTime, "Cycle"));
        entries.add(new PieEntry((float) regimeData.unCatTime * 100f / totalTime, "Uncategorized"));

        colorList.clear();
        colorList.add(pieChatColors[1]); // InCycle Color
        colorList.add(pieChatColors[0]); // UnCat Color

        if (!TextUtils.isEmpty(appSettings.getDownTimeReason1())) {
            entries.add(new PieEntry((float) regimeData.idle1Time * 100f / totalTime, appSettings.getDownTimeReason1()));
            colorList.add(pieChatColors[2]);
        }
        if (!TextUtils.isEmpty(appSettings.getDownTimeReason2())) {
            entries.add(new PieEntry((float) regimeData.idle2Time * 100f / totalTime, appSettings.getDownTimeReason2()));
            colorList.add(pieChatColors[3]);
        }
        if (!TextUtils.isEmpty(appSettings.getDownTimeReason3())) {
            entries.add(new PieEntry((float) regimeData.idle3Time * 100f / totalTime, appSettings.getDownTimeReason3()));
            colorList.add(pieChatColors[4]);
        }
        if (!TextUtils.isEmpty(appSettings.getDownTimeReason4())) {
            entries.add(new PieEntry((float) regimeData.idle4Time * 100f / totalTime, appSettings.getDownTimeReason4()));
            colorList.add(pieChatColors[5]);
        }
        if (!TextUtils.isEmpty(appSettings.getDownTimeReason5())) {
            entries.add(new PieEntry((float) regimeData.idle5Time * 100f / totalTime, appSettings.getDownTimeReason5()));
            colorList.add(pieChatColors[6]);
        }
        if (!TextUtils.isEmpty(appSettings.getDownTimeReason6())) {
            entries.add(new PieEntry((float) regimeData.idle6Time * 100f / totalTime, appSettings.getDownTimeReason6()));
            colorList.add(pieChatColors[7]);
        }
        if (!TextUtils.isEmpty(appSettings.getDownTimeReason7())) {
            entries.add(new PieEntry((float) regimeData.idle7Time * 100f / totalTime, appSettings.getDownTimeReason7()));
            colorList.add(pieChatColors[8]);
        }
        if (!TextUtils.isEmpty(appSettings.getDownTimeReason8())) {
            entries.add(new PieEntry((float) regimeData.idle8Time * 100f / totalTime, appSettings.getDownTimeReason8()));
            colorList.add(pieChatColors[9]);
        }

        entries.add(new PieEntry((float) regimeData.offlineT * 100f / totalTime, "Offline"));
        colorList.add(pieChatColors[10]);

        PieDataSet dataSet = new PieDataSet(entries, ""/*"Percentage"*/);

        // space between slices
        dataSet.setSliceSpace(2f);
        dataSet.setColors(colorList);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);           // Show text for the piechart contents
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTypeface(mTf);
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.WHITE);

        // set data
        chartPie.setData((PieData) pieData);


        chartPie.setExtraOffsets(10, 10, 10, 10);
        chartPie.setDragDecelerationFrictionCoef(0.95f);

        // apply styling
        chartPie.getDescription().setEnabled(false);
        chartPie.getDescription().setTextColor(0xffffff);
        chartPie.setHoleRadius(5f);
        chartPie.setTransparentCircleRadius(57f);
        //chartPie.setCenterText(generateCenterText());
        //chartPie.setCenterTextTypeface(mTf);
        //chartPie.setCenterTextSize(9f);

        chartPie.setUsePercentValues(true);     // Use percentage value
        chartPie.setDrawCenterText(false);      // Disable Center Text
        chartPie.setDrawHoleEnabled(true);      // Draw Hole
        chartPie.setDrawSlicesUnderHole(false); // Draw Slice
        chartPie.setDrawEntryLabels(false);     // Hide Entry Label ***

        // Set Legend
        Legend legendPie = chartPie.getLegend();
        legendPie.setTextSize(11f);
        legendPie.setTextColor(0xffffffff);
        legendPie.setTypeface(mTf);
        legendPie.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legendPie.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legendPie.setOrientation(Legend.LegendOrientation.VERTICAL);
        legendPie.setDrawInside(false);
        legendPie.setForm(Legend.LegendForm.CIRCLE);
        legendPie.setYEntrySpace(0f);
        legendPie.setYOffset(0f);
        // do not forget to refresh the chart
        // chartPie.invalidate();
        // chartPie.animateY(0);

        chartPie.invalidate();

        //mChartUpdateHandler.postDelayed(mChartUpdateRunnable, 0);
    }
}
