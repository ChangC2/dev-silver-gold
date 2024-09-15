package com.cam8.mmsapp.model;

import java.util.ArrayList;

public class FaxonStage8 extends FaxonStageBase {

    public static final int STAGE_ID = 8;

    public FaxonStage8() {
        title = "Stage 8: Final rinse DI water";
        prefix = "s8";
        apiName = "postStage8";

        itemsList = new ArrayList<>();
        // Don't change order
        itemsList.add(new FaxonStageRowInfo("conductivity", "<50µS/cm", "conductivity"));
        itemsList.add(new FaxonStageRowInfo("pH", "record", "ph"));
        itemsList.add(new FaxonStageRowInfo("Temp", "90-110°F", "temp"));
        itemsList.add(new FaxonStageRowInfo("Time", "1-5 min", "time"));
    }
}
