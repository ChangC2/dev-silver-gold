package com.cam8.mmsapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate;
import com.cam8.mmsapp.alarm.AlarmSettings;
import com.cam8.mmsapp.model.Maintenance;
import com.cam8.mmsapp.model.ShiftTime;
import com.cam8.mmsapp.model.ShiftTimeManager;
import com.cam8.mmsapp.model.TankTemperatureData;
import com.cam8.mmsapp.network.GoogleCertProvider;
import com.cam8.mmsapp.network.ITSRestClient;
import com.cam8.mmsapp.report.CheckAppUpdateHelper;
import com.cam8.mmsapp.utils.DateUtil;
import com.cam8.mmsapp.utils.NetUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class MyApplication extends MultiDexApplication {

	public static final boolean TEST_VERSION = false;

	private static MyApplication INSTANCE;
	public static MyApplication getInstance() {
		return INSTANCE;
	}

	private AppSettings appSettings;

	private boolean appRunningStatus;
	private boolean appSettingsStatus;

	private TankTemperatureData tankTemperatureData;

	public Locale getDefaultLanguage() {
		return Locale.ENGLISH;
	}
	private LocalizationApplicationDelegate localizationDelegate = new LocalizationApplicationDelegate();

	@Override
	public void onCreate() {
		super.onCreate();

		INSTANCE = this;
		appSettings = new AppSettings(getApplicationContext());
		appRunningStatus = false;
		appSettingsStatus = false;

		tankTemperatureData = new TankTemperatureData();

		// Load Device Setting if not loaded.
		getDeviceSettings();

		// Load Shift Data
		getShiftsData();
	}

	@Override
	protected void attachBaseContext(Context base) {
		//super.attachBaseContext(base);

		localizationDelegate.setDefaultLanguage(base, Locale.ENGLISH);
		super.attachBaseContext(localizationDelegate.attachBaseContext(base));

		MultiDex.install(this);
	}

	@Override
	public void onConfigurationChanged(@NonNull Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		localizationDelegate.onConfigurationChanged(this);

		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// Log.e("ResList", "Landscape");
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			// Log.e("ResList", "Portrait");
		}

		appSettings.setAppOrientation(newConfig.orientation);
	}

	@Override
	public Context getApplicationContext() {
		return localizationDelegate.getApplicationContext(super.getApplicationContext());
	}

	@Override
	public Resources getResources() {
		return localizationDelegate.getResources(getBaseContext(), super.getResources());
	}

	public String getAndroidId() {
		TelephonyManager tm =
				(TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		String androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
		Log.d("ID", "Android ID: " + androidId);
		return androidId;
	}

	public void setAppRunningStatus(boolean status) {
		appRunningStatus = status;
	}
	public boolean isAppRunningStatus() {
		return appRunningStatus;
	}

	public void setAppSettingsStatus(boolean status) {
		appSettingsStatus = status;
	}
	public boolean isAppSettingsStatus() {
		return appSettingsStatus;
	}

	public TankTemperatureData getTankTemperatureData() { return tankTemperatureData; }
	public void resetTempData() {
		if (tankTemperatureData != null) {
			tankTemperatureData.reset();
		}
	}

	public void checkAppValidStatus() {
		// Check Customer information
		final AppSettings appSettings = new AppSettings(this);
		if (TextUtils.isEmpty(appSettings.getUserId())) {
			return;
		}

		if (!NetUtil.isInternetAvailable(this)) {
			return;
		}

		Log.e("Alarm", "Check Valid Status");

		// API name checkvalidstatus.php
		// Params : username, deviceid
		// Response : {status : true, data : {valid : "false"}}

		RequestParams requestParams = new RequestParams();
		requestParams.put("userId", appSettings.getUserId());
		requestParams.put("deviceId", getAndroidId());

		ITSRestClient.post(INSTANCE, "checkValidStatus", requestParams, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);

				// If the response is JSONObject instead of expected JSONArray
				//Log.e("Alarm", "checkValidStatus:" + response.toString());

				if (response.has("status")) {
					try {
						boolean status = response.getBoolean("status");
						if (status) {
							appSettings.setValidStatus(true);
						} else {
							String message = response.optString("message");
							if ("This device isn't allowed".equalsIgnoreCase(message)) {
								appSettings.setValidStatus(false);
							} else {
								// message = No data.
								// There is no such case in normal
							}
						}

						// Notify user validate status
						Intent intent = new Intent(AlarmSettings.ACTION_VALID_STATUS_UPDATED);
						intent.putExtra("STATUS", AlarmSettings.STATUS_VALIDATE_USAGE);
						LocalBroadcastManager.getInstance(MyApplication.this).sendBroadcast(intent);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);

				Log.e("Alarm", "CheckVDError:" + String.valueOf(statusCode));
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);

				Log.e("Alarm", "CheckVDError:" + String.valueOf(statusCode));
			}
		});
	}

	public void checkAppVersion() {
		if (!NetUtil.isInternetAvailable(this)) {
			return;
		}

		Log.e("Alarm", "Check Version Status");
		Context context = MyApplication.this;
		new CheckAppUpdateHelper(this, new CheckAppUpdateHelper.onVersionCheckCallback() {
			@Override
			public void onFailed(String message) {
				Log.e("Alarm", "Ver Check Failed: " + message);
			}

			@Override
			public void onSuccess(boolean haveUpdates, String newVersion) {
				//Log.e("Alarm", "Ver New: " + newVersion);

				if (haveUpdates) {
					// New version is available
					Intent intent = new Intent(AlarmSettings.ACTION_VALID_STATUS_UPDATED);
					intent.putExtra("STATUS", AlarmSettings.STATUS_NEW_VESION);
					intent.putExtra("VERSION", newVersion);
					LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
				}
			}
		}).execute();
	}

	public void getMaintenanceTask() {
		if (!NetUtil.isInternetAvailable(this)) {
			return;
		}

		Log.e("Alarm", "Check Maintenance Task");
		MaintenanceTaskManager maintenanceTaskManager = MaintenanceTaskManager.getInstance(getApplicationContext());

		RequestParams requestParams = new RequestParams();
		requestParams.put("customerId", appSettings.getCustomerID());
		requestParams.put("machineId", appSettings.getMachineName());
		Log.e("getAllMaintenanceInfo", requestParams.toString());

		GoogleCertProvider.install(getApplicationContext());
		ITSRestClient.post(getInstance(), "getAllMaintenanceInfo", requestParams, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);

				// If the response is JSONObject instead of expected JSONArray
				//Log.e("getAllMaintenanceInfo", response.toString());

				try {
					if (response.has("status") && response.getBoolean("status")) {

						// User Information is exist
						ArrayList<Maintenance> maintenanceArrayList = new ArrayList<>();
						JSONArray jsonArray = response.getJSONArray("data");
						if (jsonArray.length() > 0) {
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonItem = jsonArray.getJSONObject(i);
								Maintenance maintenance = new Maintenance(jsonItem);

								// If Task is still not completed, then add it in the list
								if (!maintenance.isTaskCompleted()) {
									maintenanceArrayList.add(maintenance);
								}
							}
						}

						// Update Local info with new info
						maintenanceTaskManager.refreshDataList(maintenanceArrayList);

						// Check start time
						ArrayList<Maintenance> newMaintenanceList = maintenanceTaskManager.listMaintenances;
						final long startTimeInSec = System.currentTimeMillis() / 1000;
						for (int i = 0; i < newMaintenanceList.size(); i++) {
							Maintenance maintenanceInfo = newMaintenanceList.get(i);

							// If new maintenance, then set the start time as now
							if (maintenanceInfo.getStart() == 0) {
								RequestParams requestParams = new RequestParams();
								requestParams.put("customerId", appSettings.getCustomerID());
								requestParams.put("machineId", appSettings.getMachineName());
								requestParams.put("taskId", maintenanceInfo.getId());
								requestParams.put("start", startTimeInSec);

								Log.e("enterInMaintenance", requestParams.toString());

								GoogleCertProvider.install(getApplicationContext());
								ITSRestClient.post(getInstance(), "enterInMaintenance", requestParams, new JsonHttpResponseHandler() {
									@Override
									public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
										super.onSuccess(statusCode, headers, response);

										// If the response is JSONObject instead of expected JSONArray
										Log.e("enterInMaintenance", response.toString());

										try {
											if (response.has("status") && response.getBoolean("status")) {
												maintenanceInfo.setStart(startTimeInSec);
												maintenanceTaskManager.updateLocalData();
												Log.e("enterInMaintenance", "Updated start time!");
											}
										} catch (JSONException e) {
											e.printStackTrace();
										}
									}

									@Override
									public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
										super.onFailure(statusCode, headers, responseString, throwable);

										String errorMsg = throwable.getMessage();
										if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
											errorMsg = getString(R.string.error_connection_timeout);
										}

										Log.e("enterInMaintenance", errorMsg);
									}

									@Override
									public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
										super.onFailure(statusCode, headers, throwable, errorResponse);

										String errorMsg = throwable.getMessage();
										if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
											errorMsg = getString(R.string.error_connection_timeout);
										}

										Log.e("enterInMaintenance", errorMsg);
									}
								});
							}
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);

				String errorMsg = throwable.getMessage();
				if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
					errorMsg = getString(R.string.error_connection_timeout);
				}

				Log.e("getAllMaintenanceInfo", errorMsg);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);

				String errorMsg = throwable.getMessage();
				if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
					errorMsg = getString(R.string.error_connection_timeout);
				}

				Log.e("getAllMaintenanceInfo", errorMsg);
			}
		});
	}

	public void getDeviceSettings() {
		if (!NetUtil.isInternetAvailable(this)) {
			return;
		}

		Log.e("Alarm", "Get Device Settings");

		if (isAppSettingsStatus()) {
			Log.e("getAppSetting", "Now In Manual Settings");
			return;
		}

		final AppSettings appSettings = new AppSettings(this);
		if (TextUtils.isEmpty(appSettings.getMachineName()) || TextUtils.isEmpty(appSettings.getCustomerID())) {
			Log.e("getAppSetting", "Machine Info is missing and cancel api calls.");
			return;
		}

		RequestParams requestParams = new RequestParams();
		requestParams.put("factory_id", appSettings.getCustomerID());
		requestParams.put("machine_id", appSettings.getMachineName());
		//Log.e("getAppSetting", requestParams.toString());

		GoogleCertProvider.install(getApplicationContext());
		ITSRestClient.post(getInstance(), "getAppSetting", requestParams, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);

				// If the response is JSONObject instead of expected JSONArray
				// Log.e("getAppSetting", response.toString());

				if (response.optBoolean("status")) {
					try {
						JSONObject settingObj = response.getJSONObject("data");

						// Downtime Reason
						appSettings.setDowntimeReason1(settingObj.getString("downtime_reason1"));
						appSettings.setDowntimeReason2(settingObj.getString("downtime_reason2"));
						appSettings.setDowntimeReason3(settingObj.getString("downtime_reason3"));
						appSettings.setDowntimeReason4(settingObj.getString("downtime_reason4"));
						appSettings.setDowntimeReason5(settingObj.getString("downtime_reason5"));
						appSettings.setDowntimeReason6(settingObj.getString("downtime_reason6"));
						appSettings.setDowntimeReason7(settingObj.getString("downtime_reason7"));
						appSettings.setDowntimeReason8(settingObj.getString("downtime_reason8"));

						// CSLock Settings
						appSettings.setUseCSLock(settingObj.getInt("cslock_cycle") > 0);
						appSettings.setReverseCSLock(settingObj.getInt("cslock_reverse") > 0);
						appSettings.setGuestLock(settingObj.getInt("cslock_guest") > 0);
						appSettings.setCSLockSoundEnabled(settingObj.getInt("cslock_alert") > 0);

						// Time Settings
						appSettings.setStopTimeLimit(settingObj.getLong("time_stop") * 1000);
						appSettings.setPlannedProductionTime(settingObj.getLong("time_production") * 1000);

						// Alert Emails Setting
						appSettings.setCycleStopAlert(settingObj.getInt("cycle_send_alert") > 0);
						appSettings.setAlertEmail1(settingObj.getString("cycle_email1"));
						appSettings.setAlertEmail2(settingObj.getString("cycle_email2"));
						appSettings.setAlertEmail3(settingObj.getString("cycle_email3"));
						appSettings.setAlertEmail1Time((float) settingObj.getDouble("cycle_email1_time"));
						appSettings.setAlertEmail2Time((float) settingObj.getDouble("cycle_email2_time"));
						appSettings.setAlertEmail3Time((float) settingObj.getDouble("cycle_email3_time"));

						// Automatic Parts Counter
						appSettings.setAutomaticPartsCounter(settingObj.getInt("automatic_part") > 0);
						appSettings.setMinElapsedStopTime(settingObj.getInt("automatic_min_time") * 1000);
						appSettings.setPartsPerCycle(settingObj.getInt("automatic_part_per_cycle"));

						// Gantt Display Option
						appSettings.setChartOption(settingObj.getInt("gantt_chart_display"));

						// Daily Goal Target Options
						appSettings.setDGTTitle(settingObj.optString("calc_chart_title"));
						appSettings.setDGTFomular(settingObj.optInt("calc_chart_formula"));
						appSettings.setDGTOption(settingObj.optInt("calc_chart_option"));
						appSettings.setDGTUnit(settingObj.optString("calc_chart_unit"));
						appSettings.setDGTDispMode(settingObj.optInt("calc_chart_disp_mode"));
						appSettings.setDGTMaxValue((float) settingObj.optDouble("calc_chart_max_value"));

						Intent intent = new Intent(AlarmSettings.ACTION_VALID_STATUS_UPDATED);
						intent.putExtra("STATUS", AlarmSettings.STATUS_NEW_SETTINGS);
						LocalBroadcastManager.getInstance(MyApplication.this).sendBroadcast(intent);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);

				String errorMsg = throwable.getMessage();
				if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
					errorMsg = getString(R.string.error_connection_timeout);
				}

				Log.e("getAppSetting", errorMsg);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);

				String errorMsg = throwable.getMessage();
				if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
					errorMsg = getString(R.string.error_connection_timeout);
				}

				Log.e("getAppSetting", errorMsg);
			}
		});
	}

	public void getShiftsData() {
		if (!NetUtil.isInternetAvailable(this)) {
			return;
		}

		final AppSettings appSettings = new AppSettings(this);
		if (TextUtils.isEmpty(appSettings.getCustomerID())) {
			Log.e("getShiftSetting", "Customer Info is missing and cancel api calls.");
			return;
		}

		RequestParams requestParams = new RequestParams();
		requestParams.put("customer_id", appSettings.getCustomerID());
		requestParams.put("machineId", appSettings.getMachineName());
		requestParams.put("date", DateUtil.toStringFormat_22(new Date()));

		GoogleCertProvider.install(getApplicationContext());
		//HttpsTrustManager.allowAllSSL();
		ITSRestClient.post(getInstance(), "get_shift_detail", requestParams, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);

				// If the response is JSONObject instead of expected JSONArray
				Log.e("ShiftData", response.toString());
				try {
					if (response.has("status") && response.getBoolean("status")) {
						JSONObject jsonShiftData = response.getJSONObject("shift_info");

						String shift1Start = jsonShiftData.getString("shift1_start");
						String shift1End = jsonShiftData.getString("shift1_end");

						String shift2Start = jsonShiftData.getString("shift2_start");
						String shift2End = jsonShiftData.getString("shift2_end");

						String shift3Start = jsonShiftData.getString("shift3_start");
						String shift3End = jsonShiftData.getString("shift3_end");

						ArrayList<ShiftTime> newShiftArray = new ArrayList<>();
						if (!TextUtils.isEmpty(shift1Start) && !TextUtils.isEmpty(shift1End)) {
							newShiftArray.add(new ShiftTime(shift1Start, shift1End));
						}

						if (!TextUtils.isEmpty(shift2Start) && !TextUtils.isEmpty(shift2End)) {
							newShiftArray.add(new ShiftTime(shift2Start, shift2End));
						}

						if (!TextUtils.isEmpty(shift3Start) && !TextUtils.isEmpty(shift3End)) {
							newShiftArray.add(new ShiftTime(shift3Start, shift3End));
						}

						ShiftTimeManager.getInstance().setShiftTime(newShiftArray);
					} else {
						String message = "";
						if (response.has("message") && !response.isNull("message")) {
							message = response.getString("message");
						}

						if (TextUtils.isEmpty(message)) {
							message = getString(R.string.error_server_busy);
						}

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);

				String errorMsg = throwable.getMessage();
				if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
					errorMsg = getString(R.string.error_connection_timeout);
				}

				Log.e("ShiftData", errorMsg);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);

				String errorMsg = throwable.getMessage();
				if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
					errorMsg = getString(R.string.error_connection_timeout);
				}

				Log.e("ShiftData", errorMsg);
			}
		});
	}
}
