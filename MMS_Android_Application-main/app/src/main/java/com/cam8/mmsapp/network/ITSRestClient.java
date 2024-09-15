package com.cam8.mmsapp.network;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

public class ITSRestClient {

    // New Server
    private static final String BASE_URL = "https://api.slymms.com/api/";

    private static AsyncHttpClient asyncClient;
    private static SyncHttpClient syncClient;

    public static AsyncHttpClient getInstance() {
        if (asyncClient == null) {
            HttpsTrustManager.allowAllSSL();
            asyncClient = new AsyncHttpClient();
            asyncClient.setMaxRetriesAndTimeout(2, 30);

            //client.addHeader("Accept", "application/json");
            //client.addHeader("Content-Type", "application/json");
            //client.addHeader("Content-Type", "multipart/form-data; boundary=--------------------------179797280813534994574905");
        }

        if (syncClient == null) {
            HttpsTrustManager.allowAllSSL();
            syncClient = new SyncHttpClient();
            syncClient.setMaxRetriesAndTimeout(2, 20);

            //client.addHeader("Accept", "application/json");
            //client.addHeader("Content-Type", "application/json");
            //client.addHeader("Content-Type", "multipart/form-data; boundary=--------------------------179797280813534994574905");
        }

        return asyncClient;
    }

    public static void get(Context cnxt, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getInstance();

        // String urlString = getAbsoluteUrl(url);
        String urlString = APIServerUtil.getUrl(cnxt, APIServerUtil.APIType.api, url);

        asyncClient.get(urlString, params, responseHandler);
    }

    public static void getSync(Context cnxt, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getInstance();

        // String urlString = getAbsoluteUrl(url);
        String urlString = APIServerUtil.getUrl(cnxt, APIServerUtil.APIType.api, url);

        syncClient.get(urlString, params, responseHandler);
    }

    public static void post(Context cnxt, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getInstance();

        // String urlString = getAbsoluteUrl(url);
        String urlString = APIServerUtil.getUrl(cnxt, APIServerUtil.APIType.api, url);

        asyncClient.post(urlString, params, responseHandler);
    }

    public static void postSync(Context cnxt, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getInstance();

        // String urlString = getAbsoluteUrl(url);
        String urlString = APIServerUtil.getUrl(cnxt, APIServerUtil.APIType.api, url);

        syncClient.post(urlString, params, responseHandler);
    }

    public static void getFullUrl(String fullUrl, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getInstance();
        asyncClient.get(fullUrl, params, responseHandler);
    }

    public static void postFullUrl(String fullUrl, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getInstance();
        asyncClient.post(fullUrl, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
