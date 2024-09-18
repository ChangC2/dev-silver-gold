package com.cam8.mmsapp.model;

import java.util.ArrayList;

public class FaxonStage7 extends FaxonStageBase {

    public static final int STAGE_ID = 7;

    public FaxonStage7() {
        title = "Stage 7: seal gardolene D 6800/6";
        prefix = "s7";
        apiName = "postStage7";

        itemsList = new ArrayList<>();
        // Don't change order
        itemsList.add(new FaxonStageRowInfo("conductivity", "<1000µS/cm", "conductivity"));
        itemsList.add(new FaxonStageRowInfo("pH", "4.5-5.2", "ph"));
        itemsList.add(new FaxonStageRowInfo("absorbance", "0.250-0.333", "absorbance"));
        itemsList.add(new FaxonStageRowInfo("Time", "2-4 min", "time"));
    }
}
