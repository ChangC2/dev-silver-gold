package com.cam8.mmsapp.model;

import java.util.ArrayList;

public class FaxonStage4 extends FaxonStageBase {

    public static final int STAGE_ID = 4;

    public FaxonStage4() {
        title = "Stage 4: Activate Gardolene v6513/Gardobond Additive H 7212";
        prefix = "s4";
        apiName = "postStage4";

        itemsList = new ArrayList<>();
        // Don't change order
        itemsList.add(new FaxonStageRowInfo("conductivity", "<2000µS/cm", "conductivity"));
        itemsList.add(new FaxonStageRowInfo("pH", "8.5-10.0", "ph"));
        itemsList.add(new FaxonStageRowInfo("temp", "70-90°F", "temp"));
        itemsList.add(new FaxonStageRowInfo("Time", "1-3 min", "time"));
    }
}
