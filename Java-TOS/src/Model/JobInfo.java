package Model;

import org.json.JSONException;
import org.json.JSONObject;

public class JobInfo {
    public String Id = "";
    public String jobID  = "";
    public String customer  = "";
    public String partNumber  = "";
    public String programNumber  = "";
    public String description  = "";
    public String partsPerCycle  = "";
    public long targetCycleTime  = 0;
    public String qtyRequired  = "";
    public String qtyCompleted  = "";
    public String orderDate  = "";
    public String dueDate  = "";
    public String qtyGoodCompleted  = "";
    public String qtyBadCompleted  = "";

    public String aux1data  = "0";
    public String aux2data  = "0";
    public String aux3data  = "0";

    public String jobGuides = "";

    public JobInfo(String jobDetails) {
        try {
            JSONObject jsonObject = new JSONObject(jobDetails);
            customer = jsonObject.optString("customer").replace("null", "");
            partNumber = jsonObject.optString("partNumber").replace("null", "");
            programNumber = jsonObject.optString("programNumber").replace("null", "");
            description = jsonObject.optString("description").replace("null", "");
            partsPerCycle = jsonObject.optString("partsPerCycle").replace("null", "0");
            targetCycleTime = jsonObject.optLong("targetCycleTime");
            qtyRequired = jsonObject.optString("qtyRequired").replace("null", "");
            qtyCompleted = jsonObject.optString("qtyCompleted").replace("null", "");

            aux1data = jsonObject.optString("aux1data").replace("null", "");
            aux2data = jsonObject.optString("aux2data").replace("null", "");
            aux3data = jsonObject.optString("aux3data").replace("null", "");

            jobGuides = jsonObject.optString("files");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
