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
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.Nullable;

import com.cam8.mmsapp.R;
import com.cam8.mmsapp.model.RegimeData;
import com.goodiebag.protractorview.SpeedometerView;

public class OEEFragment extends BaseFragment {

    public static OEEFragment newInstance(String text) {
        OEEFragment mFragment = new OEEFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    SpeedometerView gaugeAvailable;
    SpeedometerView gaugePerformance;
    SpeedometerView gaugeQuality;
    SpeedometerView gaugeOEE;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infogauge, container, false);

        TAG = "oee";

        init(savedInstanceState);
        initLayout(view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initLayout(View parentView) {
        {
            gaugeAvailable = parentView.findViewById(R.id.gauge_available);
            gaugeAvailable.setSpeed(30);
            gaugeAvailable.setLabelConverter(new SpeedometerView.LabelConverter() {
                @Override
                public String getLabelFor(double progress, double maxProgress) {
                    return String.valueOf((int) Math.round(progress));
                }
            });

            // configure value range and ticks
            gaugeAvailable.setMaxSpeed(100);
            gaugeAvailable.setMajorTickStep(20);
            gaugeAvailable.setMinorTicks(0);

            gaugeAvailable.setLabelTextSize(18);


            // Configure value range colors
            gaugeAvailable.addColoredRange(10, 30, Color.BLUE);
            gaugeAvailable.addColoredRange(40, 60, Color.YELLOW);
            gaugeAvailable.addColoredRange(60, 90, Color.RED);
        }

        {
            gaugePerformance = parentView.findViewById(R.id.gauge_performance);
            gaugePerformance.setSpeed(70);
            gaugePerformance.setLabelConverter(new SpeedometerView.LabelConverter() {
                @Override
                public String getLabelFor(double progress, double maxProgress) {
                    return String.valueOf((int) Math.round(progress));
                }
            });

            // configure value range and ticks
            gaugePerformance.setMaxSpeed(100);
            gaugePerformance.setMajorTickStep(20);
            gaugePerformance.setMinorTicks(0);

            gaugePerformance.setLabelTextSize(18);


            // Configure value range colors
            gaugePerformance.addColoredRange(10, 30, Color.BLUE);
            gaugePerformance.addColoredRange(40, 60, Color.YELLOW);
            gaugePerformance.addColoredRange(60, 90, Color.RED);
        }

        {
            gaugeQuality = parentView.findViewById(R.id.gauge_quality);
            gaugeQuality.setSpeed(50);
            gaugeQuality.setLabelConverter(new SpeedometerView.LabelConverter() {
                @Override
                public String getLabelFor(double progress, double maxProgress) {
                    return String.valueOf((int) Math.round(progress));
                }
            });

            // configure value range and ticks
            gaugeQuality.setMaxSpeed(100);
            gaugeQuality.setMajorTickStep(20);
            gaugeQuality.setMinorTicks(0);

            gaugeQuality.setLabelTextSize(18);


            // Configure value range colors
            gaugeQuality.addColoredRange(10, 30, Color.BLUE);
            gaugeQuality.addColoredRange(40, 60, Color.YELLOW);
            gaugeQuality.addColoredRange(60, 90, Color.RED);
        }

        {
            gaugeOEE = parentView.findViewById(R.id.gauge_oee);
            gaugeOEE.setSpeed(40);
            gaugeOEE.setLabelConverter(new SpeedometerView.LabelConverter() {
                @Override
                public String getLabelFor(double progress, double maxProgress) {
                    return String.valueOf((int) Math.round(progress));
                }
            });

            // configure value range and ticks
            gaugeOEE.setMaxSpeed(100);
            gaugeOEE.setMajorTickStep(20);
            gaugeOEE.setMinorTicks(0);

            gaugeOEE.setLabelTextSize(18);


            // Configure value range colors
            gaugeOEE.addColoredRange(10, 30, Color.BLUE);
            gaugeOEE.addColoredRange(40, 60, Color.YELLOW);
            gaugeOEE.addColoredRange(60, 90, Color.RED);
        }
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

        if (gaugeAvailable != null) {
            gaugeAvailable.setSpeed(regimeData.Availablity, 200, 0);
        }

        if (gaugePerformance != null) {
            gaugePerformance.setSpeed(regimeData.performance, 200, 0);
        }

        if (gaugeQuality != null) {
            gaugeQuality.setSpeed(regimeData.quality, 200, 0);
        }

        if (gaugeOEE != null) {
            gaugeOEE.setSpeed(regimeData.oee, 200, 0);
        }
    }
}
