package com.cam8.mmsapp.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Maintenance {
    String id = "";
    String machineId = "";
    String taskName = "";
    String taskCategory = "";
    String picture = "";
    String taskInstruction = "";
    int frequency = 0;
    int interlock = 0;
    long start = 0;

    // Custom fields for the operation
    // Task Running time
    long totalIncycleTime = 0;
    boolean taskCompleted = false;

    // Used for View in the menu
    String userNotes = "";

    // Used for Viewing in Maintenance Screen
    String files = "";

    public Maintenance(JSONObject jsonObject) {
        id = jsonObject.optString("id", "0");
        machineId = jsonObject.optString("machine_id", "0");
        taskName = jsonObject.optString("task_name", "0");
        taskCategory = jsonObject.optString("task_category", "0");
        picture = jsonObject.optString("picture", "0");
        taskInstruction = jsonObject.optString("task_instruction", "0");
        frequency = jsonObject.optInt("frequency", 0);
        interlock = jsonObject.optInt("interlock", 0);
        start = jsonObject.optLong("start", 0);

        // Custom Data
        totalIncycleTime = jsonObject.optLong("total_incycle_time", 0);
        taskCompleted = jsonObject.optBoolean("is_finished");

        userNotes = jsonObject.optString("note");

        // Attached Files
        files = jsonObject.optString("files");
    }

    public Maintenance() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMachineId() { return machineId; }
    public void setMachineId(String machineId) { this.machineId = machineId; }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }

    public String getTaskCategory() { return taskCategory; }
    public void setTaskCategory(String taskCategory) { this.taskCategory = taskCategory; }

    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }

    public String getTaskInstruction() { return taskInstruction; }
    public void setTaskInstruction(String taskInstruction) { this.taskInstruction = taskInstruction; }

    public int getFrequency() { return frequency; }
    public void setFrequency(int frequency) { this.frequency = frequency; }

    public int getInterlock() { return interlock; }
    public void setInterlock(int interlock) { this.interlock = interlock; }

    public void setStart(long start) { this.start = start; }
    public long getStart() { return start; }

    public void resetInCycleTime() {
        totalIncycleTime = 0;
    }

    public long getTotalIncycleTime() { return totalIncycleTime; }

    // Task Completed Status
    public boolean isTaskCompleted() { return taskCompleted; }
    public void setTaskCompleted(boolean taskCompleted) { this.taskCompleted = taskCompleted; }

    public String getUserNotes() { return userNotes; }
    public void setUserNotes(String userNotes) { this.userNotes = userNotes; }

    public String getFiles() { return files; }
    public void setFiles(String files) { this.files = files; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Maintenance that = (Maintenance) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public JSONObject toJsonData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("machine_id", machineId);
            jsonObject.put("task_name", taskName);
            jsonObject.put("task_category", taskCategory);
            jsonObject.put("picture", picture);
            jsonObject.put("task_instruction", taskInstruction);
            jsonObject.put("frequency", frequency);
            jsonObject.put("interlock", interlock);
            jsonObject.put("start", start);

            // Custom Data
            jsonObject.put("total_incycle_time", totalIncycleTime);
            jsonObject.put("is_finished", taskCompleted);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public void updateInfo(Maintenance newInfo) {
        this.id = newInfo.id;
        machineId = newInfo.machineId;
        taskName = newInfo.taskName;
        taskCategory = newInfo.taskCategory;
        picture = newInfo.picture;
        taskInstruction = newInfo.taskInstruction;
        frequency = newInfo.frequency;
        interlock = newInfo.interlock;
        start = newInfo.start;

        // Don't overwrite these values, need to use local data
        // boolean taskCompleted = false
        // long totalIncycleTime = 0;
    }

    public void addInCycleTime(long gaps) {
        if (taskCompleted || start == 0) {
            return;
        }

        totalIncycleTime += gaps;
    }

    public boolean timeIsForMaintenance() {
        if (totalIncycleTime > frequency * 1000 * 3600) {
            return true;
        } else {
            return false;
        }
    }
}
