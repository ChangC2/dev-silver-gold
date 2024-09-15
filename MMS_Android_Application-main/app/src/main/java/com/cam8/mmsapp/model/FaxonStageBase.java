package com.cam8.mmsapp.model;

import android.text.TextUtils;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FaxonStageBase {

    String title = "";
    String prefix = "";
    String apiName = "";
    ArrayList<FaxonStageRowInfo> itemsList;
    String notes = "";  // Notes is optional field.

    // Stage Title
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    // Stage API Prefix
    public String getPrefix() { return prefix; }
    public void setPrefix(String prefix) { this.prefix = prefix; }

    // Stage Save API
    public String getApiName() { return apiName; }
    public void setApiName(String apiName) { this.apiName = apiName; }

    // Items Info List
    public ArrayList<FaxonStageRowInfo> getItemsList() {
        return itemsList;
    }

    // Stage Notes
    public void setNotes(String notes) { this.notes = notes; }
    public String getNotes() { return notes; }

    public boolean isValidInput() {
        for (FaxonStageRowInfo item: itemsList) {
            if (TextUtils.isEmpty(item.result)) {
                return false;
            }
        }

        // Notes is optional to input.
        /*if (TextUtils.isEmpty(notes)) {
            return false;
        }*/

        return true;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            for (FaxonStageRowInfo item: itemsList) {
                jsonObject.put(item.apiParam, item.result);
            }
            jsonObject.put("notes", notes);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public void loadDataFrom(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            for (FaxonStageRowInfo item: itemsList) {
                item.result = jsonObject.optString(item.apiParam);
            }
            notes = jsonObject.optString("notes");
        } catch (JSONException e) {
            e.printStackTrace();

            // Reset Data if Exception
            reset();
        }
    }

    public void reset() {
        for (FaxonStageRowInfo item: itemsList) {
            item.result = "";
        }
        notes = "";
    }

    public void fillParams(RequestParams requestParams) {
        for (FaxonStageRowInfo item: itemsList) {
            requestParams.put(prefix + "_" + item.getApiParam(), item.getResult());
        }
        requestParams.put(prefix + "_" + "notes", notes);
    }
}
