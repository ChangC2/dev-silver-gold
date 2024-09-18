package com.cam8.mmsapp.model;

import java.util.ArrayList;

public class FaxonStage5 extends FaxonStageBase {

    public static final int STAGE_ID = 5;

    public FaxonStage5() {
        title = "Stage 5: Zinc Phosphate - Gardobond R 2219/Gardobond Additive H 7212/Gardobond Additive H 7014";
        prefix = "s5";
        apiName = "postStage5";

        itemsList = new ArrayList<>();
        // Don't change order
        itemsList.add(new FaxonStageRowInfo("total acid", "25-35 mls", "total_acid"));
        itemsList.add(new FaxonStageRowInfo("free acid", "2.5-3.5 mls", "free_acid"));
        itemsList.add(new FaxonStageRowInfo("gas points", "1.5-3.0", "gas_points"));
        itemsList.add(new FaxonStageRowInfo("zinc", "3-5g/L", "zinc"));
        itemsList.add(new FaxonStageRowInfo("conductivity", "record", "conductivity"));
        itemsList.add(new FaxonStageRowInfo("Temp", "130-150°F", "temp"));
        itemsList.add(new FaxonStageRowInfo("Time", "8-20 min", "time"));
    }
}
