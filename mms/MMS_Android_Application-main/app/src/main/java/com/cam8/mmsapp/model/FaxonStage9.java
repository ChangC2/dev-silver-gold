package com.cam8.mmsapp.model;

import java.util.ArrayList;

public class FaxonStage9 extends FaxonStageBase {

    public static final int STAGE_ID = 9;

    public FaxonStage9() {
        title = "Stage 9: Dry Manula air blow";
        prefix = "s9";
        apiName = "postStage9";

        itemsList = new ArrayList<>();
        // Don't change order
        itemsList.add(new FaxonStageRowInfo("Temp", "125-175°F", "temp"));
        itemsList.add(new FaxonStageRowInfo("Time", "5 min", "time"));
    }
}
