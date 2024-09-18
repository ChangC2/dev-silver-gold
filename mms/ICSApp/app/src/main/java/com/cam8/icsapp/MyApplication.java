package com.cam8.icsapp;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MyApplication extends Application {

	public static final boolean TEST_MODE = false;

	private static MyApplication INSTANCE;
	public static MyApplication getInstance() {
		return INSTANCE;
	}

	private AppSettings appSettings;

	@Override
	public void onCreate() {
		super.onCreate();

		INSTANCE = this;
		appSettings = new AppSettings(getApplicationContext());
	}

	public String getAndroidId() {
		TelephonyManager tm =
				(TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		String androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
		Log.d("ID", "Android ID: " + androidId);
		return androidId;
	}
}
