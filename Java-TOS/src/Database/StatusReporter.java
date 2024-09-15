package Database;

import Controller.Api;
import Utils.PreferenceManager;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StatusReporter {
    private static StatusReporter mInstance;

    public static StatusReporter getInstance() {
        if (mInstance == null) {
            mInstance = new StatusReporter();
        }
        return mInstance;
    }

    private StatusReporter() {}

    public void reportStatus(String status) {
        // Device configure was not done, then return
        if (TextUtils.isEmpty(PreferenceManager.getFactoryID()) || TextUtils.isEmpty(PreferenceManager.getMachineID())) {
            return;
        }
        execute(status);
    }

    public void execute(String status) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpPost post = new HttpPost(Api.SERVE_URL + Api.api_setMachineStatus);
                // add request parameters or form parameters
                List<NameValuePair> urlParameters = new ArrayList<>();
                urlParameters.add(new BasicNameValuePair("customerId", PreferenceManager.getFactoryID()));
                urlParameters.add(new BasicNameValuePair("machineId", PreferenceManager.getMachineID()));
                urlParameters.add(new BasicNameValuePair("status", status));

                try {
                    post.setEntity(new UrlEncodedFormEntity(urlParameters));
                    HttpClient httpClient = HttpClientBuilder.create().build();
                    HttpResponse result = httpClient.execute(post);
                    String json = EntityUtils.toString(result.getEntity(), "UTF-8");

                    JSONObject response = new JSONObject(json);
                    try {
                        if (response.has("status") && response.getBoolean("status")) {
                        } else {
                            String message = "";
                            if (response.has("message") && !response.isNull("message")) {
                                message = response.getString("message");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
