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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.cam8.mmsapp.R;
import com.cam8.mmsapp.model.RegimeData;
import com.cam8.mmsapp.views.PercentageGauge;

public class GaugeFragment extends BaseFragment {

    public static GaugeFragment newInstance(String text) {
        GaugeFragment mFragment = new GaugeFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }


    PercentageGauge gaugeAvailable;
    PercentageGauge gaugePerformance;
    PercentageGauge gaugeQuality;
    PercentageGauge gaugeOEE;

    TextView tvValAvailable;
    TextView tvValPerformance;
    TextView tvValQuality;
    TextView tvValOEE;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infogauge_new, container, false);

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
        gaugeAvailable = parentView.findViewById(R.id.gauge_available);
        gaugePerformance = parentView.findViewById(R.id.gauge_performance);
        gaugeQuality = parentView.findViewById(R.id.gauge_quality);
        gaugeOEE = parentView.findViewById(R.id.gauge_oee);

        tvValAvailable = parentView.findViewById(R.id.tvPercentAvailable);
        tvValPerformance = parentView.findViewById(R.id.tvPercentPerformance);
        tvValQuality = parentView.findViewById(R.id.tvPercentQuality);
        tvValOEE = parentView.findViewById(R.id.tvPercentOEE);
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

        // Make Fake Data
        /*regimeData.Availablity = 95;
        regimeData.performance = 100;
        regimeData.quality = 75;
        regimeData.oee = 69;*/

        if (gaugeAvailable != null) {
            //gaugeAvailable.setPercentage(regimeData.Availablity, 500, 0);
            gaugeAvailable.setPercentage(regimeData.Availablity);
            tvValAvailable.setText(String.format("%d%s", (int) regimeData.Availablity, "%"));
        }

        if (gaugePerformance != null) {
            //gaugePerformance.setPercentage(regimeData.performance, 500, 0);
            gaugePerformance.setPercentage(regimeData.performance);
            tvValPerformance.setText(String.format("%d%s", (int) regimeData.performance, "%"));
        }

        if (gaugeQuality != null) {
            //gaugeQuality.setPercentage(regimeData.quality, 500, 0);
            gaugeQuality.setPercentage(regimeData.quality);
            tvValQuality.setText(String.format("%d%s", (int) regimeData.quality, "%"));
        }

        if (gaugeOEE != null) {
            //gaugeOEE.setPercentage(regimeData.oee, 500, 0);
            gaugeOEE.setPercentage(regimeData.oee);
            tvValOEE.setText(String.format("%d%s", (int) regimeData.oee, "%"));
        }
    }
}
