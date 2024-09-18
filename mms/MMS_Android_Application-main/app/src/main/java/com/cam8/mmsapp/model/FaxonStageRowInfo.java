package com.cam8.mmsapp.model;

public class FaxonStageRowInfo {
    String name = "";
    String range = "";
    String apiParam = "";
    String result = "";

    /*public FaxonStageRowInfo(String name, String range) {
        this.name = name;
        this.range = range;
    }*/

    public FaxonStageRowInfo(String name, String range, String apiParam) {
        this.name = name;
        this.range = range;
        this.apiParam = apiParam;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRange() { return range; }
    public void setRange(String range) { this.range = range; }

    public String getApiParam() { return apiParam; }
    public void setApiParam(String apiParam) { this.apiParam = apiParam; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

}
