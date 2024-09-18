package com.mobile.seemesave.application;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraXConfig;
import androidx.core.app.ActivityCompat;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;

import java.util.Timer;
import java.util.TimerTask;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.luck.picture.lib.app.IApp;
import com.luck.picture.lib.engine.PictureSelectorEngine;
import com.mobile.seemesave.api.user.UserApi;
import com.mobile.seemesave.service.GpsTracker;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.widget.imagePicker.PictureSelectorEngineImp;
public class App extends MultiDexApplication implements IApp, CameraXConfig.Provider{
    public static App get;
    public static Context context;
    int PRIVATE_MODE = 0;

    private final Handler mHandler = new Handler();
    private Timer mTimer = null;

    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;

    public static synchronized App getInstance() {
        App mApp;
        synchronized (App.class) {
            if (get == null) {
                get = new App();
            }
            mApp = get;
        }
        return mApp;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        FirebaseApp.initializeApp(this);

        get = this;
        pref = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        editor = pref.edit();
        try {
            G.pref = pref;
            G.editor = pref.edit();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        UXCam.startWithKey("qp2tosex54nysqc");

        context = getApplicationContext();
        mTimer = new Timer();

//        FirebaseMessaging.getInstance().subscribeToTopic("all").addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//            }
//        });
        mTimer.schedule(new TimerTaskToGetLocation(), 3, 60 * 1000 * 10);
    }
    public void setDelverRel(String relId, String content) {
        editor.putString("relId", relId);
        editor.putString("relBody", content);
        editor.apply();
    }


    @Override
    public Context getAppContext() {
        return null;
    }

    @Override
    public PictureSelectorEngine getPictureSelectorEngine() {
        return new PictureSelectorEngineImp();
    }
    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    }

    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        getGPS();
                    }
                }
            });
        }
    }

    private void getGPS(){
        try {
            GpsTracker gpsTracker = new GpsTracker(getApplicationContext());
            if (TextUtils.isEmpty(G.getAddress())){
                if (gpsTracker.canGetLocation() && gpsTracker.getLocation() != null) {
                    G.location = gpsTracker.getLocation();
                }
            }
            if (gpsTracker.canGetLocation() && gpsTracker.getLocation() != null) {
                Location location  = gpsTracker.getLocation();
                Log.e("location_update:", "1");
                JsonObject json = new JsonObject();
                json.addProperty("latitude", location.getLatitude());
                json.addProperty("longitude", location.getLongitude());
                UserApi.updateLocation(json);
                Log.e("Uploading Location", String.format("##################%f:%f", G.location.getLatitude(), G.location.getLongitude()));
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
    }
}
