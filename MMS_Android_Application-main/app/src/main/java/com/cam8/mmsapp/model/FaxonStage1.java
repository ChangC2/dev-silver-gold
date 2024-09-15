package com.cam8.mmsapp.model;

import java.util.ArrayList;

public class FaxonStage1 extends FaxonStageBase {

    public static final int STAGE_ID = 1;

    public FaxonStage1() {
        title = "Stage 1: Pre Clean Gardocien 1207";
        prefix = "s1";
        apiName = "postStage1";

        itemsList = new ArrayList<>();
        // Don't change order
        itemsList.add(new FaxonStageRowInfo("FA mls", "1.9-5.7", "fa_mls"));
        itemsList.add(new FaxonStageRowInfo("conc", "5-15%", "conc"));
        itemsList.add(new FaxonStageRowInfo("TA mls", "record", "ta_mls"));
        itemsList.add(new FaxonStageRowInfo("TA/FA", "<3.0", "ta_fa"));
        itemsList.add(new FaxonStageRowInfo("conductivity", "record", "conductivity"));
        itemsList.add(new FaxonStageRowInfo("Temp", "90-120°F", "temp"));
        itemsList.add(new FaxonStageRowInfo("Time", "3-5 min", "time"));
    }
}
