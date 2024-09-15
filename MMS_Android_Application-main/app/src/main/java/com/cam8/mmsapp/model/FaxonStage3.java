package com.cam8.mmsapp.model;

import java.util.ArrayList;

public class FaxonStage3 extends FaxonStageBase {

    public static final int STAGE_ID = 3;

    public FaxonStage3() {
        title = "Stage 3 : Rinse City Water";
        prefix = "s3";
        apiName = "postStage3";

        itemsList = new ArrayList<>();
        // Don't change order
        itemsList.add(new FaxonStageRowInfo("conductivity", "<1250µS/cm", "conductivity"));
        itemsList.add(new FaxonStageRowInfo("Total alk", "<0.50ml", "total_alk"));
        itemsList.add(new FaxonStageRowInfo("pH", "record", "ph"));
        itemsList.add(new FaxonStageRowInfo("Time", "1-10 min", "time"));
    }
}
