package com.cam8.mmsapp.network;

import android.content.Context;
import android.util.Log;

import com.cam8.mmsapp.AppSettings;

public class APIServerUtil {
    public enum APIType {api, updatePackage, gantt, resource}

    private static final String SERVER_BASE_URL = "https://api.slymms.com/api/";
    private static final String PACKAGE_BASE_URL = "https://api.slymms.com/Terminals/";
    private static final String GANTT_BASE_URL = "https://api.slymms.com/";

    public static String getUrl(Context cnxt, APIType apiType, String relativePath) {
        AppSettings appSettings = new AppSettings(cnxt);
        String baseURL = "";
        if (appSettings.isUsingLocalServer()) {
            String localServerIP = appSettings.getLocalServerIP();
            if (apiType == APIType.api) {
                baseURL = "https://" + localServerIP + "/mobile_api/api/";
            } else if (apiType == APIType.updatePackage) {
                //baseURL = "http://" + localServerIP + "/api.slymms.com/Terminals/";
                baseURL = PACKAGE_BASE_URL;
            } else if (apiType == APIType.gantt) {
                baseURL = "https://" + localServerIP + "/mobile_api/";
            } else if (apiType == APIType.resource) {
                baseURL = "https://" + localServerIP + "/mobile_api/";
            }
        } else {
            if (apiType == APIType.api) {
                baseURL = SERVER_BASE_URL;
            } else if (apiType == APIType.updatePackage) {
                baseURL = PACKAGE_BASE_URL;
            } else if (apiType == APIType.gantt) {
                baseURL = GANTT_BASE_URL;
            }else if (apiType == APIType.resource) {
                baseURL = GANTT_BASE_URL;
            }
        }

        String rURL = baseURL + relativePath;
        //Log.e("URL", rURL);

        return rURL;
    }
}
