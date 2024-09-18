package com.cam8.mmsapp.model;

import java.util.ArrayList;

public class FaxonStage6 extends FaxonStageBase {

    public static final int STAGE_ID = 6;

    public FaxonStage6() {
        title = "Stage 6: Rinse- City Water";
        prefix = "s6";
        apiName = "postStage6";

        itemsList = new ArrayList<>();
        // Don't change order
        itemsList.add(new FaxonStageRowInfo("conductivity", "<1250µS/cm", "conductivity"));
        itemsList.add(new FaxonStageRowInfo("pH", "record", "ph"));
        itemsList.add(new FaxonStageRowInfo("total acid", "<0.05 ml", "total_acid"));
        itemsList.add(new FaxonStageRowInfo("Time", "1-10 min", "time"));
    }
}
