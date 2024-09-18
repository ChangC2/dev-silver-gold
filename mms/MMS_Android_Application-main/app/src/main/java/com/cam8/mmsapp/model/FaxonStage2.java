package com.cam8.mmsapp.model;

import java.util.ArrayList;

public class FaxonStage2 extends FaxonStageBase {

    public static final int STAGE_ID = 2;

    public FaxonStage2() {
        title = "Stage2 : Clean Gardoclean 1207";
        prefix = "s2";
        apiName = "postStage2";

        itemsList = new ArrayList<>();
        // Don't change order
        itemsList.add(new FaxonStageRowInfo("FA mls", "1.1-1.9", "fa_mls"));
        itemsList.add(new FaxonStageRowInfo("conc", "3-5%", "conc"));
        itemsList.add(new FaxonStageRowInfo("TA mls", "record", "ta_mls"));
        itemsList.add(new FaxonStageRowInfo("TA/FA", "<3.0", "ta_fa"));
        itemsList.add(new FaxonStageRowInfo("conductivity", "record", "conductivity"));
        itemsList.add(new FaxonStageRowInfo("Temp", "90-120°F", "temp"));
        itemsList.add(new FaxonStageRowInfo("Time", "3-5 min", "time"));
    }
}
