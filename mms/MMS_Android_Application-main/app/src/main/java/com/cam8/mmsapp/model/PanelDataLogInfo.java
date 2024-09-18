package com.cam8.mmsapp.model;

import android.text.TextUtils;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

public class PanelDataLogInfo {

    String date = "";
    String time = "";
    String break_pass_fail = "";
    String grade = "";

    String phose1 = "";
    String phose2 = "";
    String phose3 = "";

    String striped1 = "";
    String striped2 = "";
    String striped3 = "";

    String mg1 = "";
    String mg2 = "";
    String mg3 = "";

    String average = "";
    String notes = "";

    public PanelDataLogInfo() {}

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getBreak_pass_fail() { return break_pass_fail; }
    public void setBreak_pass_fail(String break_pass_fail) { this.break_pass_fail = break_pass_fail; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getPhose1() { return phose1; }
    public void setPhose1(String phose1) { this.phose1 = phose1; }

    public String getPhose2() { return phose2; }
    public void setPhose2(String phose2) { this.phose2 = phose2; }

    public String getPhose3() { return phose3; }
    public void setPhose3(String phose3) { this.phose3 = phose3; }


    public String getStriped1() { return striped1; }
    public void setStriped1(String striped1) { this.striped1 = striped1; }

    public String getStriped2() { return striped2; }
    public void setStriped2(String striped2) { this.striped2 = striped2; }

    public String getStriped3() { return striped3; }
    public void setStriped3(String striped3) { this.striped3 = striped3; }

    public String getMg1() { return mg1; }
    public void setMg1(String mg1) { this.mg1 = mg1; }

    public String getMg2() { return mg2; }
    public void setMg2(String mg2) { this.mg2 = mg2; }

    public String getMg3() { return mg3; }
    public void setMg3(String mg3) { this.mg3 = mg3; }

    public String getAverage() { return average; }
    public void setAverage(String average) { this.average = average; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public boolean isValidInput() {
        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(time) || TextUtils.isEmpty(grade)) {
            return false;
        }

        if (TextUtils.isEmpty(phose1) || TextUtils.isEmpty(striped1) || TextUtils.isEmpty(mg1)) {
            return false;
        }

        if (TextUtils.isEmpty(phose2) || TextUtils.isEmpty(striped2) || TextUtils.isEmpty(mg2)) {
            return false;
        }

        if (TextUtils.isEmpty(phose3) || TextUtils.isEmpty(striped3) || TextUtils.isEmpty(mg3)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("date", date);
            jsonObject.put("time", time);
            jsonObject.put("break_pass_fail", break_pass_fail);
            jsonObject.put("grade", grade);

            jsonObject.put("phose1", phose1);
            jsonObject.put("phose2", phose2);
            jsonObject.put("phose3", phose3);

            jsonObject.put("striped1", striped1);
            jsonObject.put("striped2", striped2);
            jsonObject.put("striped3", striped3);

            jsonObject.put("mg1", mg1);
            jsonObject.put("mg2", mg2);
            jsonObject.put("mg3", mg3);

            jsonObject.put("average", average);
            jsonObject.put("notes", notes);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public void loadDataFrom(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);

            date = jsonObject.optString("date");
            time = jsonObject.optString("time");
            break_pass_fail = jsonObject.optString("break_pass_fail");
            grade = jsonObject.optString("grade");

            phose1 = jsonObject.optString("phose1");
            phose2 = jsonObject.optString("phose2");
            phose3 = jsonObject.optString("phose3");

            striped1 = jsonObject.optString("striped1");
            striped2 = jsonObject.optString("striped2");
            striped3 = jsonObject.optString("striped3");

            mg1 = jsonObject.optString("mg1");
            mg2 = jsonObject.optString("mg2");
            mg3 = jsonObject.optString("mg3");

            average = jsonObject.optString("average");
            notes = jsonObject.optString("notes");
        } catch (JSONException e) {
            e.printStackTrace();

            reset();
        }
    }

    public void reset() {
        date = "";
        time = "";
        break_pass_fail = "";
        grade = "";

        phose1 = "";
        phose2 = "";
        phose3 = "";

        striped1 = "";
        striped2 = "";
        striped3 = "";

        mg1 = "";
        mg2 = "";
        mg3 = "";

        average = "";
        notes = "";
    }

    public void fillParams(RequestParams requestParams) {
        requestParams.put("p_break_pass_fail", break_pass_fail);
        requestParams.put("p_grade", grade);
        requestParams.put("p_phose1", phose1);
        requestParams.put("p_phose2", phose2);
        requestParams.put("p_phose3", phose3);
        requestParams.put("p_striped1", striped1);
        requestParams.put("p_striped2", striped2);
        requestParams.put("p_striped3", striped3);
        requestParams.put("p_mg1", mg1);
        requestParams.put("p_mg2", mg2);
        requestParams.put("p_mg3", mg3);
        requestParams.put("p_average", average);
        requestParams.put("p_notes", notes);
        requestParams.put("p_date", date);
        requestParams.put("p_time", time);
    }
}
