package com.cam8.icsapp.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ITSRestClient {
    private static final String BASE_URL = "https://slymms.com/api/";

    private static AsyncHttpClient client;
    public static AsyncHttpClient getInstance() {
        if (client == null) {
            HttpsTrustManager.allowAllSSL();
            client = new AsyncHttpClient();
            client.setMaxRetriesAndTimeout(2, 15);

            //client.addHeader("Accept", "application/json");
            //client.addHeader("Content-Type", "application/json");
            //client.addHeader("Content-Type", "multipart/form-data; boundary=--------------------------179797280813534994574905");
        }
        return client;
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getInstance();
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getInstance();
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void getFullUrl(String fullUrl, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getInstance();
        client.get(fullUrl, params, responseHandler);
    }

    public static void postFullUrl(String fullUrl, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getInstance();
        client.post(fullUrl, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
