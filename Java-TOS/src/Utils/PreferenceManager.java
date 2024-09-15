package Utils;

import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.prefs.Preferences;

public class PreferenceManager {

    public static final String KEY_HST_PATH = "key_hst_folder";

    public static final String KEY_LOGIN_STATUS = "key_login_status";

    public static final String KEY_USER_ID = "key_userid";
    public static final String KEY_USER_NAME = "key_user_name";
    public static final String KEY_USER_PIC = "key_user_pic";
    public static final String KEY_USER_LEVEL = "key_user_level";

    public static final String KEY_LICENSE_APPROVED = "key_license_approved";
    public static final String KEY_LICENSE_SITECODE = "key_license_sitecode";

    public static final String CONTROL_TYPE = "key_control_type";
    public static final String MONITOR_SIGNAL_FROM = "key_monitor_signal_from";

    public static final String KEY_FANUC_IP = "key_fanuc_ip";
    public static final String KEY_FANUC_PORT = "key_fanuc_port";

    public static final String KEY_FANUC_VERSION_CODE = "key_fanuc_version_code";

    public static final String KEY_HAAS_IP = "key_haas_ip";
    public static final String KEY_HAAS_PORT = "key_haas_port";
    public static final String KEY_HAAS_ADDR_MONITOR = "key_fanuc_addr_monitor";
    public static final String KEY_HAAS_ADDR_TOOL = "key_fanuc_addr_tool";
    public static final String KEY_HAAS_ADDR_SECTION = "key_fanuc_addr_section";
    public static final String KEY_HAAS_ADDR_CHANNEL = "key_fanuc_addr_channel";
    public static final String KEY_HAAS_ADDR_PROGRAMNUM = "key_fanuc_addr_programnum";

    public static final String KEY_PLC_IP = "key_plc_ip";
    public static final String KEY_PLC_PORT = "key_plc_port";

    public static final String KEY_PLC_IP1 = "key_plc_ip1";
    public static final String KEY_PLC_PORT1 = "key_plc_port1";

    public static final String KEY_SCALEHP_IP = "key_scalehp_ip";

    public static final String KEY_DATA_CYCLE = "key_data_cycle";

    public static final String KEY_SENSOR_ADDRESS1 = "key_sensor_address1";
    public static final String KEY_SENSOR_ADDRESS2 = "key_sensor_address2";
    public static final String KEY_SENSOR_ADDRESS3 = "key_sensor_address3";


    // TEACH MODE SETTINGS
    public static final String KEY_HIGH_LIMIT_PERCENT = "high_limit_percent";
    public static final String KEY_WEAR_LIMIT_PERCENT = "wear_limit_percent";
    public static final String KEY_LOW_LIMIT_ERROR_PERCENT = "low_limit_error_percent";
    public static final String KEY_TARGET_LOAD_PERCENT = "target_load_percent";
    public static final String KEY_DEFAULT_HIGH_LIMIT_DELAY = "default_high_limit_delay";
    public static final String KEY_DEFAULT_WEAR_LIMIT_DELAY = "default_wear_limit_delay";
    public static final String KEY_DEFAULT_START_DELAY = "default_start_delay";
    public static final String KEY_DEFAULT_FILTER = "default_filter";
    public static final String KEY_SCALE_CHART_ABOVE_HIGH_LIMIT = "scale_chart_above_high_limit";

    public static final String KEY_DEFAULT_LEADIN_TRIGGER = "default_leadin_trigger";
    public static final String KEY_DEFAULT_LEADIN_FEEDRATE = "default_leadin_feedrate";

    public static final String KEY_MACRO_INTERRUPT_ENABLED = "macro_interrupt_enabled";
    public static final String KEY_LEAD_IN_FR_ENABLED = "lead_in_fr_enabled";
    public static final String KEY_ADAPTIVE_ENABLED = "adaptive_enabled";
    public static final String KEY_ADAPTIVE_MIN = "adaptive_min";
    public static final String KEY_ADAPTIVE_MAX = "adaptive_max";
    public static final String KEY_ADAPTIVE_HIGH_LIMIT = "adaptive_high_limit";
    public static final String KEY_ADAPTIVE_WEAR_LIMIT = "adaptive_wear_limit";

    // ADAPTIVE SETTINGS
    public static final String KEY_PID_GAIN = "pid_gain";
    public static final String KEY_PID_RESET = "pid_reset";
    public static final String KEY_PID_RATE = "pid_rate";

    // CHART SETTINGS
    public static final String KEY_LINE_WIDTH = "line_width";

    // EDIT PROFILE
    public static final String KEY_PHONE = "phone";
    public static final String KEY_COMPANY_ID = "company_id";


    // GENERAL SETTING PAGE OPTIONS
    public static final String KEY_FACTORY_ID = "key_factory_id";
    public static final String KEY_FACTORY_NAME = "key_factory_name";
    public static final String KEY_FACTORY_LOGO = "key_factory_logo";

    public static final String KEY_MACHINE_ID = "key_machine_id";
    public static final String KEY_TOOL_DATA_FILEPATH = "key_tool_data_filepath";
    public static final String KEY_APP_VERSION = "key_app_version";
    public static final String KEY_EMAIL = "key_email";
    public static final String KEY_SITE_KEY = "key_site_key";

    // PROFILE SETTINGS
    public static final String KEY_NAME = "key_name";
    public static final String KEY_PROFILE_PICTURE = "key_profile_picture";

    // ALERT REPORT SETTINGS
    public static final String KEY_EMAIL_LIST = "key_email_list";
    public static final String KEY_ALERT_ON_HL = "key_alert_on_hl";
    public static final String KEY_ALERT_ON_WL = "key_alert_on_wl";
    public static final String KEY_ALERT_ON_LL = "key_alert_on_ll";

    public static final String KEY_ALERT_EMAIL1 = "key_alert_email1";
    public static final String KEY_ALERT_EMAIL2 = "key_alert_email2";
    public static final String KEY_ALERT_EMAIL3 = "key_alert_email3";

    // Simulator
    public static final String KEY_PLC_SIMULATOR = "key_plc_simulator";

    // JOB Information
    public static final String KEY_JOB_ID = "key_job_id";
    public static final String KEY_JOB_DETAILS = "key_job_details";
    public static final String KEY_JOB_REWORK = "key_job_rework";
    public static final String KEY_JOB_SETUP = "key_job_setup";

    // MMS Settings
    public static final String KEY_DOWNTIME_REASON1 = "key_downtime_reason1";
    public static final String KEY_DOWNTIME_REASON2 = "key_downtime_reason2";
    public static final String KEY_DOWNTIME_REASON3 = "key_downtime_reason3";
    public static final String KEY_DOWNTIME_REASON4 = "key_downtime_reason4";
    public static final String KEY_DOWNTIME_REASON5 = "key_downtime_reason5";
    public static final String KEY_DOWNTIME_REASON6 = "key_downtime_reason6";
    public static final String KEY_DOWNTIME_REASON7 = "key_downtime_reason7";
    public static final String KEY_DOWNTIME_REASON8 = "key_downtime_reason8";

    // Stop Time Limit Settings
    public static final String KEY_STOP_TIME_LIMIT = "key_stop_time_limit";
    // Production Time Settings
    public static final String KEY_PLANNED_PRODUCTION_TIME = "key_planned_production_time";
    // Target Cycle Time
    public static final String KEY_TARGET_CYCLE_TIME = "key_target_cycle_time";

    public static final String KEY_AUTOMATIC_PARTS_COUNTER = "key_automatic_parts_counter";
    public static final String KEY_MIN_ELAPSED_TIME = "key_min_elapsed_time";
    public static final String KEY_PARTS_PER_CYCLE = "key_parts_per_cycle";


    // Timer Status
    private static final String KEY_TIME_INCYCLE = "key_time_incycle";

    private static final String KEY_TIME_IDLE1 = "key_time_idle1";
    private static final String KEY_TIME_IDLE2 = "key_time_idle2";
    private static final String KEY_TIME_IDLE3 = "key_time_idle3";
    private static final String KEY_TIME_IDLE4 = "key_time_idle4";
    private static final String KEY_TIME_IDLE5 = "key_time_idle5";
    private static final String KEY_TIME_IDLE6 = "key_time_idle6";
    private static final String KEY_TIME_IDLE7 = "key_time_idle7";
    private static final String KEY_TIME_IDLE8 = "key_time_idle8";

    private static final String KEY_TIME_UNCAT = "key_time_uncate";

    private static final String KEY_PARTS_GOOD = "key_parts_good";
    private static final String KEY_PARTS_BAD = "key_parts_bad";

    private static final String KEY_SHIFT_PARTS_GOOD = "key_shift_parts_good";
    private static final String KEY_SHIFT_PARTS_BAD = "key_shift_parts_bad";

    private static final String KEY_QTY_COMPLETED = "key_qty_completed";
    private static final String KEY_LAST_TIME = "key_last_recorded_time";

    private static final String KEY_SERVER_CONN_STATUS = "key_server_conn_status";
    private static final String KEY_FANUC_CONN_STATUS = "key_fanuc_conn_status";

    private static final String KEY_CYCLE_STOP_ALERT = "key_cycle_stop_alert";

    // Auto Monitor
    private static final String KEY_AUTO_MONITOR = "key_auto_monitor";
    private static final String KEY_SPINDLE_TIME = "key_spindle_time";
    private static final String KEY_SPINDLE_THRESHOLD = "key_spindle_threshold";

    // HP2 Settings
    public static final String KEY_HP2_ENABLE = "key_hp2_enable";
    public static final String KEY_HP2_IP = "key_hp2_ip";
    public static final String KEY_HP2_PORT = "key_hp2_port";
    public static final String KEY_HP2_REGADDRESS = "key_hp2_reg_address";
    public static final String KEY_HP2_X = "key_hp2_x_csv_record";

    private static Preferences prefs;

    static {
        prefs = Preferences.userNodeForPackage(PreferenceManager.class);
    }

    public static void saveValue(String key, String val) {
        try {
            prefs.put(key, val);
            prefs.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveValue(String key, int val) {
        try {
            prefs.putInt(key, val);
            prefs.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveValue(String key, boolean val) {
        try {
            prefs.putBoolean(key, val);
            prefs.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveValue(String key, float val) {
        try {
            prefs.putFloat(key, val);
            prefs.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveValue(String key, long val) {
        try {
            prefs.putLong(key, val);
            prefs.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getStringValue(String key) {
        return prefs.get(key, "");
    }
    public static String getStringValue(String key, String def) {
        return prefs.get(key, def);
    }

    public static int getIntValue(String key) {
        return prefs.getInt(key, 0);
    }
    public static int getIntValue(String key, int def) {
        return prefs.getInt(key, def);
    }

    public static long getLongValue(String key) {return  prefs.getLong(key, 0); }
    public static long getLongValue(String key, long def) {return  prefs.getLong(key, def); }

    public static float getFloatValue(String key) {
        return prefs.getFloat(key, 0);
    }
    public static float getFloatValue(String key, float def) {
        return prefs.getFloat(key, def);
    }

    public static void remove(String key) {
        try {
            prefs.remove(key);
            prefs.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isLoginned() {
        return prefs.getBoolean(KEY_LOGIN_STATUS, false);
    }
    public static void setLogin() {
        saveValue(KEY_LOGIN_STATUS, true);
    }

    public static void signOut() {
        saveValue(KEY_LOGIN_STATUS, false);
        saveValue(KEY_USER_LEVEL, -1);
    }


    public static void setUserID(String userName) { saveValue(KEY_USER_ID, userName); }
    public static String getUserID() { return getStringValue(KEY_USER_ID); }

    public static void setUserName(String userName) { saveValue(KEY_USER_NAME, userName); }
    public static String getUserName() { return getStringValue(KEY_USER_NAME, "Unattended"); }

    public static void setUserAvatar(String userAvatar) {
        saveValue(KEY_USER_PIC, userAvatar);
    }

    public static String getUserAvatar() {
        return getStringValue(KEY_USER_PIC);
    }

    public static void setUserLevel(int userLevel) {
        saveValue(KEY_USER_LEVEL, userLevel);
    }
    public static int getUserLevel() {
        return getIntValue(KEY_USER_LEVEL, -1);
    }


    // License Status
    public static boolean isLicenseApproved() {
        return prefs.getBoolean(KEY_LICENSE_APPROVED, false);
    }

    public static void setLicenseApproved(boolean value) {
        saveValue(KEY_LICENSE_APPROVED, value);
    }

    public static void setLicenseSiteCode(String siteCode) {
        saveValue(KEY_LICENSE_SITECODE, siteCode);
    }

    public static String getLicenseSiteCode() {
        return getStringValue(KEY_LICENSE_SITECODE);
    }


    // Control Types : 0-Fanuc 18i, 1-Fanuc 0i, 2-Fanuc 31i, 3-Fanuc i, 4-Okuma P300, 5-HAAS
    public static final int CONTROL_TYPE_FANUC_18I = 0;
    public static final int CONTROL_TYPE_FANUC_0I = 1;
    public static final int CONTROL_TYPE_FANUC_31I = 2;
    public static final int CONTROL_TYPE_FANUC_I = 3;
    public static final int CONTROL_TYPE_FANUC_OKUMA = 4;
    public static final int CONTROL_TYPE_FANUC_HAAS = 5;
    public static void setControlType(int value) { saveValue(CONTROL_TYPE, value); }
    public static int getControlType() { return getIntValue(CONTROL_TYPE, 0); }

    // Monitor Signal From : 0-Fanuc, 1-Manual, 2-PLC
    public static final int MONITOR_SIGNAL_FROM_FANUC = 0;
    public static final int MONITOR_SIGNAL_FROM_MANUAL = 1;
    public static final int MONITOR_SIGNAL_FROM_PLC = 2;

    public static final int MONITOR_HASSSIGNAL_FROM_MACHINE = 0;

    public static void setMonitorSignalFrom(int value) { saveValue(MONITOR_SIGNAL_FROM, value); }
    public static int getMonitorSignalFrom() { return getIntValue(MONITOR_SIGNAL_FROM, 0); }

    // Fanuc IP and Port
    public static void setFanucIP(String value) {
        saveValue(KEY_FANUC_IP, value);
    }
    public static String getFanucIP() {
        return getStringValue(KEY_FANUC_IP, "127.0.0.1");
    }

    public static void setFanucPort(int value) {
        saveValue(KEY_FANUC_PORT, value);
    }
    public static int getFanucPort() {
        return getIntValue(KEY_FANUC_PORT, 602);
    }

    // Fanuc Version Code
    public static void setFanucVersionCode(int value) {
        saveValue(KEY_FANUC_VERSION_CODE, value);
    }
    public static int getFanucVersionCode() {
        return getIntValue(KEY_FANUC_VERSION_CODE, 0);
    }

    // HAAS Device Settings
    public static void setHaasIP(String value) {
        saveValue(KEY_HAAS_IP, value);
    }
    public static String getHaasIP() {
        return getStringValue(KEY_HAAS_IP);
    }

    public static void setHaasPort(int value) {
        saveValue(KEY_HAAS_PORT, value);
    }
    public static int getHaasPort() {
        return getIntValue(KEY_HAAS_PORT, 0);
    }

    public static void setHaasAddrMonitor(int value) {
        saveValue(KEY_HAAS_ADDR_MONITOR, value);
    }
    public static int getHaasAddrMonitor() {
        return getIntValue(KEY_HAAS_ADDR_MONITOR, 0);
    }

    public static void setHaasAddrTool(int value) {
        saveValue(KEY_HAAS_ADDR_TOOL, value);
    }
    public static int getHaasAddrTool() {
        return getIntValue(KEY_HAAS_ADDR_TOOL, 0);
    }

    public static void setHaasAddrSection(int value) {
        saveValue(KEY_HAAS_ADDR_SECTION, value);
    }
    public static int getHaasAddrSection() {
        return getIntValue(KEY_HAAS_ADDR_SECTION, 0);
    }

    public static void setHaasAddrChannel(int value) {
        saveValue(KEY_HAAS_ADDR_CHANNEL, value);
    }
    public static int getHaasAddrChannel() {
        return getIntValue(KEY_HAAS_ADDR_CHANNEL, 0);
    }

    public static void setHaasAddrProgN(int value) {
        saveValue(KEY_HAAS_ADDR_PROGRAMNUM, value);
    }
    public static int getHaasAddrProgN() {
        return getIntValue(KEY_HAAS_ADDR_PROGRAMNUM, 0);
    }

    // PLC IP and Port
    public static void setPLCIP(String value) {
        saveValue(KEY_PLC_IP, value);
    }
    public static String getPLCIP() {
        return getStringValue(KEY_PLC_IP, "192.168.1.25");
    }
    public static void setPLCPort(int value) {
        saveValue(KEY_PLC_PORT, value);
    }
    public static int getPLCPort() { return getIntValue(KEY_PLC_PORT, 502); }

    // PLC IP and Port in Process Monitor
    public static void setPLCIP1(String value) {
        saveValue(KEY_PLC_IP1, value);
    }
    public static String getPLCIP1() {
        return getStringValue(KEY_PLC_IP1, "192.168.1.25");
    }

    public static void setPLCPort1(int value) {
        saveValue(KEY_PLC_PORT1, value);
    }
    public static int getPLCPort1() { return getIntValue(KEY_PLC_PORT1, 502); }

    // Scale HP IP
    public static void setScaleHPIP(String value) { saveValue(KEY_SCALEHP_IP, value); }
    public static String getScaleHPIP() { return getStringValue(KEY_SCALEHP_IP); }

    // Data Cycle
    public static void setDataCycle(int value) {
        saveValue(KEY_DATA_CYCLE, value);
    }

    public static int getDataCycle() {
        return getIntValue(KEY_DATA_CYCLE, 50);
    }

    public static void setSensorAddress1(int value) {saveValue(KEY_SENSOR_ADDRESS1, value);}
    public static int getSensorAddress1() {
        return getIntValue(KEY_SENSOR_ADDRESS1, 1);
    }
    public static void setSensorAddress2(int value) {saveValue(KEY_SENSOR_ADDRESS2, value);}
    public static int getSensorAddress2() {
        return getIntValue(KEY_SENSOR_ADDRESS2, 0);
    }
    public static void setSensorAddress3(int value) {saveValue(KEY_SENSOR_ADDRESS3, value);}
    public static int getSensorAddress3() {
        return getIntValue(KEY_SENSOR_ADDRESS3, 0);
    }

    // Adaptive Settings Page
    public static boolean isLeadInFREnabled() {
        return prefs.getBoolean(KEY_LEAD_IN_FR_ENABLED, false);
    }

    public static void setLeadInFREnabled(boolean value) {
        saveValue(KEY_LEAD_IN_FR_ENABLED, value);
    }

    public static boolean isMacroInterruptEnabled() {
        return prefs.getBoolean(KEY_MACRO_INTERRUPT_ENABLED, false);
    }

    public static void setMacroInterruptEnabled(boolean value) {
        saveValue(KEY_MACRO_INTERRUPT_ENABLED, value);
    }


    public static boolean isAdaptiveEnabled() {
        return prefs.getBoolean(KEY_ADAPTIVE_ENABLED, false);
    }

    public static void setAdaptiveEnabled(boolean value) {
        saveValue(KEY_ADAPTIVE_ENABLED, value);
    }

    public static void setAdaptiveHighLimit(int adaptiveHighLimit) {
        saveValue(KEY_ADAPTIVE_HIGH_LIMIT, adaptiveHighLimit);
    }

    public static int getAdaptiveHighLimit() {
        return getIntValue(KEY_ADAPTIVE_HIGH_LIMIT, 40);
    }

    public static void setDefaultFilter(float defaultFilter) {
        saveValue(KEY_DEFAULT_FILTER, defaultFilter);
    }

    public static float getDefaultFilter() {
        return getFloatValue(KEY_DEFAULT_FILTER, 0.0f);
    }

    public static void setAdaptiveMin(int adaptiveMin) {
        saveValue(KEY_ADAPTIVE_MIN, adaptiveMin);
    }

    public static int getAdaptiveMin() {
        return getIntValue(KEY_ADAPTIVE_MIN, 70);
    }

    public static void setAdaptiveMax(int adaptiveMax) {
        saveValue(KEY_ADAPTIVE_MAX, adaptiveMax);
    }

    public static int getAdaptiveMax() {
        return getIntValue(KEY_ADAPTIVE_MAX, 130);
    }

    public static void setTargetLoadPercent(float targetLoadPercent) {
        saveValue(KEY_TARGET_LOAD_PERCENT, targetLoadPercent);
    }

    public static float getTargetLoadPercent() {
        return getFloatValue(KEY_TARGET_LOAD_PERCENT, 1.5f);
    }

    public static void setAdaptiveWearLimit(int adaptiveWearLimit) {
        saveValue(KEY_ADAPTIVE_WEAR_LIMIT, adaptiveWearLimit);
    }

    public static int getAdaptiveWearLimit() {
        return getIntValue(KEY_ADAPTIVE_WEAR_LIMIT, 50);
    }

    public static void setDefaultHighLimitDelay(float defaultHighLimitDelay) {
        saveValue(KEY_DEFAULT_HIGH_LIMIT_DELAY, defaultHighLimitDelay);
    }

    public static float getDefaultHighLimitDelay() {
        return getFloatValue(KEY_DEFAULT_HIGH_LIMIT_DELAY, 0.5f);
    }

    public static void setDefaultWearLimitDelay(float defaultWearLimitDelay) {
        saveValue(KEY_DEFAULT_WEAR_LIMIT_DELAY, defaultWearLimitDelay);
    }

    public static float getDefaultWearLimitDelay() {
        return getFloatValue(KEY_DEFAULT_WEAR_LIMIT_DELAY, 0.5f);
    }

    public static void setDefaultStartDelay(float value) {
        saveValue(KEY_DEFAULT_START_DELAY, value);
    }

    public static float getDefaultStartDelay() {
        return getFloatValue(KEY_DEFAULT_START_DELAY, 1.5f);
    }

    public static void setHighLimitPercent(float highLimitPercent) {
        saveValue(KEY_HIGH_LIMIT_PERCENT, highLimitPercent);
    }

    public static float getHighLimitPercent() {
        return getFloatValue(KEY_HIGH_LIMIT_PERCENT, 40.0f);
    }

    public static void setWearLimitPercent(float wearLimitPercent) {
        saveValue(KEY_WEAR_LIMIT_PERCENT, wearLimitPercent);
    }

    public static float getWearLimitPercent() {
        return getFloatValue(KEY_WEAR_LIMIT_PERCENT, 25.0f);
    }

    public static void setLowLimitErrorPercent(float lowLimitErrorPercent) {
        saveValue(KEY_LOW_LIMIT_ERROR_PERCENT, lowLimitErrorPercent);
    }

    public static float getLowLimitErrorPercent() {
        return getFloatValue(KEY_LOW_LIMIT_ERROR_PERCENT, 15.0f);
    }

    public static void setScaleChartAboveHighLimit(float scaleChartAboveHighLimit) {
        saveValue(KEY_SCALE_CHART_ABOVE_HIGH_LIMIT, scaleChartAboveHighLimit);
    }

    public static float getScaleChartAboveHighLimit() {
        return getFloatValue(KEY_SCALE_CHART_ABOVE_HIGH_LIMIT, 15.0f);
    }

    // Default Lead-In Trigger
    public static void setDefaultLeadInTrigger(float value) {
        saveValue(KEY_DEFAULT_LEADIN_TRIGGER, value);
    }

    public static float getDefaultLeadInTrigger() {
        return getFloatValue(KEY_DEFAULT_LEADIN_TRIGGER, 0.0f);
    }

    // Default Lead-In Feedrate
    public static void setDefaultLeadInFeedrate(float value) {
        saveValue(KEY_DEFAULT_LEADIN_FEEDRATE, value);
    }

    public static float getDefaultLeadInFeedrate() {
        return getFloatValue(KEY_DEFAULT_LEADIN_FEEDRATE, 100.0f);
    }


    // ADAPTIVE PID SETTINGS
    public static void setPIDGain(float PIDGain) {
        saveValue(KEY_PID_GAIN, PIDGain);
    }

    public static float getPIDGain() {
        return getFloatValue(KEY_PID_GAIN, 18.0f);
    }

    public static void setPIDReset(float PIDReset) {
        saveValue(KEY_PID_RESET, PIDReset);
    }

    public static float getPIDReset() {
        return getFloatValue(KEY_PID_RESET, 0.01f);
    }

    public static void setPIDRate(float PIDRate) {
        saveValue(KEY_PID_RATE, PIDRate);
    }

    public static float getPIDRate() {
        return getFloatValue(KEY_PID_RATE, 0.0f);
    }

    // CHART SETTINGGS
    public static void setLineWidth(float lineWidth) {
        saveValue(KEY_LINE_WIDTH, lineWidth);
    }

    public static float getLineWidth() {
        float dLineWidth = getFloatValue(KEY_LINE_WIDTH);

        if (dLineWidth <= 0)
            dLineWidth = 3;

        return dLineWidth;
    }

    // General Settings Page
    public static void setFactoryID(String factoryID) { saveValue(KEY_FACTORY_ID, factoryID); }
    public static String getFactoryID() { return getStringValue(KEY_FACTORY_ID); }

    public static void setFactoryName(String factoryName) { saveValue(KEY_FACTORY_NAME, factoryName); }
    public static String getFactoryName() { return getStringValue(KEY_FACTORY_NAME); }

    public static void setFactoryLogo(String factoryLogo) { saveValue(KEY_FACTORY_LOGO, factoryLogo); }
    public static String getFactoryLogo() { return getStringValue(KEY_FACTORY_LOGO); }

    public static void setMachineID(String machineID) {
        saveValue(KEY_MACHINE_ID, machineID);
    }
    public static String getMachineID() { return getStringValue(KEY_MACHINE_ID); }

    public static void setToolDataFilepath(String toolDataFilepath) { saveValue(KEY_TOOL_DATA_FILEPATH, toolDataFilepath); }
    public static String getToolDataFilepath() {
        String toolDataFolder = getStringValue(KEY_TOOL_DATA_FILEPATH);
        if (toolDataFolder == null || toolDataFolder.isEmpty()) {
            toolDataFolder = FileSystems.getDefault()
                    .getPath("tooldatafiles")
                    .normalize()
                    .toAbsolutePath()
                    .toString();
            new File(toolDataFolder).mkdirs();
        }
        return toolDataFolder;
    }

    public static void setAppVersion(String appVersion) {
        saveValue(KEY_APP_VERSION, appVersion);
    }
    public static String getAppVersion() {
        return getStringValue(KEY_APP_VERSION);
    }

    public static void setEmail(String email) {
        saveValue(KEY_EMAIL, email);
    }
    public static String getEmail() {
        return getStringValue(KEY_EMAIL);
    }

    public static void setSiteKey(String siteKey) {
        saveValue(KEY_SITE_KEY, siteKey);
    }
    public static String getSiteKey() {
        return getStringValue(KEY_SITE_KEY);
    }

    // Alert Report Settings
    // User Picuture and Name

    public static void setName(String name) {
        saveValue(KEY_NAME, name);
    }
    public static String getName() {
        return getStringValue(KEY_NAME);
    }

    public static void setProfilePicture(String profilePicture) {
        saveValue(KEY_PROFILE_PICTURE, profilePicture);
    }
    public static String getProfilePicture() {
        return getStringValue(KEY_PROFILE_PICTURE);
    }

    // EDIT PROFILE SETTINGS
    public static void setCompanyID(String companyID) {
        saveValue(KEY_COMPANY_ID, companyID);
    }
    public static String getCompanyID() {
        return getStringValue(KEY_COMPANY_ID);
    }

    public static void setPhone(String phone) {
        saveValue(KEY_PHONE, phone);
    }
    public static String getPhone() {
        return getStringValue(KEY_PHONE);
    }

    public static boolean isAlertOnHlEnabled() {
        return prefs.getBoolean(KEY_ALERT_ON_HL, false);
    }
    public static void setAlertOnHl(boolean value) {
        saveValue(KEY_ALERT_ON_HL, value);
    }

    public static boolean isAlertOnLlEnabled() {
        return prefs.getBoolean(KEY_ALERT_ON_LL, false);
    }
    public static void setAlertOnLl(boolean value) {
        saveValue(KEY_ALERT_ON_LL, value);
    }

    public static boolean isAlertOnWlEnabled() {
        return prefs.getBoolean(KEY_ALERT_ON_HL, false);
    }
    public static void setAlertOnWl(boolean value) {
        saveValue(KEY_ALERT_ON_WL, value);
    }

    //Retrieve the values
//    Gson gson = new Gson();
//    String jsonText = Prefs.getString("key", null);
//    String[] text = gson.fromJson(jsonText, String[].class);  //EDIT: gso to gson

    public static void setEmailList(String emailList) {
        saveValue(KEY_EMAIL_LIST, emailList);
    }
    public static String getEmailList() {
        return getStringValue(KEY_EMAIL_LIST);
    }


    public static void setAlertEmail1(String value) {
        saveValue(KEY_ALERT_EMAIL1, value);
    }
    public static String getAlertEmail1() {
        return getStringValue(KEY_ALERT_EMAIL1);
    }

    public static void setAlertEmail2(String value) {
        saveValue(KEY_ALERT_EMAIL2, value);
    }
    public static String getAlertEmail2() {
        return getStringValue(KEY_ALERT_EMAIL2);
    }

    public static void setAlertEmail3(String value) {
        saveValue(KEY_ALERT_EMAIL3, value);
    }

    public static String getAlertEmail3() {
        return getStringValue(KEY_ALERT_EMAIL3);
    }

    public static boolean isPLCSimulatorEnabled() {
        return prefs.getBoolean(KEY_PLC_SIMULATOR, false);
    }
    public static void setPLCSimulatorEnabled(boolean value) {
        saveValue(KEY_PLC_SIMULATOR, value);
    }

    // Job Information
    public static void setJobID(String value) {
        saveValue(KEY_JOB_ID, value);
    }
    public static String getJobID() {
        return getStringValue(KEY_JOB_ID);
    }

    public static void setJobDetails(String value) {
        saveValue(KEY_JOB_DETAILS, value);
    }
    public static String getJobDetails() {
        return getStringValue(KEY_JOB_DETAILS);
    }

    public static boolean isJobReworkStatus() { return prefs.getBoolean(KEY_JOB_REWORK, false); }
    public static void setJobReworkStatus(boolean value) { saveValue(KEY_JOB_REWORK, value); }

    public static boolean isJobSetupStatus() {
        return prefs.getBoolean(KEY_JOB_SETUP, false);
    }
    public static void setJobSetupStatus(boolean value) { saveValue(KEY_JOB_SETUP, value); }

    // Downtime Status
    public static String getDownTimeReason1() {
        return getStringValue(KEY_DOWNTIME_REASON1, "Clear Chips"); // Clear Chips
    }
    public static void setDowntimeReason1(String reason) {
        saveValue(KEY_DOWNTIME_REASON1, reason);
    }

    public static String getDownTimeReason2() {
        return getStringValue(KEY_DOWNTIME_REASON2, "Wait Materials"); // Wait Materials
    }
    public static void setDowntimeReason2(String reason) {
        saveValue(KEY_DOWNTIME_REASON2, reason);
    }

    public static String getDownTimeReason3() {
        return getStringValue(KEY_DOWNTIME_REASON3, "Wait Tooling"); // Wait Tooling
    }
    public static void setDowntimeReason3(String reason) {
        saveValue(KEY_DOWNTIME_REASON3, reason);
    }

    public static String getDownTimeReason4() {
        return getStringValue(KEY_DOWNTIME_REASON4, "Break"); // Break
    }
    public static void setDowntimeReason4(String reason) {
        saveValue(KEY_DOWNTIME_REASON4, reason);
    }

    public static String getDownTimeReason5() {
        return getStringValue(KEY_DOWNTIME_REASON5, "No Operator"); // No Operator
    }
    public static void setDowntimeReason5(String reason) {
        saveValue(KEY_DOWNTIME_REASON5, reason);
    }

    public static String getDownTimeReason6() {
        return getStringValue(KEY_DOWNTIME_REASON6, "P.M."); // P.M.
    }
    public static void setDowntimeReason6(String reason) {
        saveValue(KEY_DOWNTIME_REASON6, reason);
    }

    public static String getDownTimeReason7() {
        return getStringValue(KEY_DOWNTIME_REASON7, "Unplanned Repair"); // Unplanned Repair
    }
    public static void setDowntimeReason7(String reason) {
        saveValue(KEY_DOWNTIME_REASON7, reason);
    }

    public static String getDownTimeReason8() {
        return getStringValue(KEY_DOWNTIME_REASON8, "Other"); // Other
    }
    public static void setDowntimeReason8(String reason) {
        saveValue(KEY_DOWNTIME_REASON8, reason);
    }

    public static long getStopTimeLimit() {
        long stopTime = getLongValue(KEY_STOP_TIME_LIMIT);
        if (stopTime == 0) {
            stopTime = 90 * 1000; // Default Time : 90s
            setStopTimeLimit(stopTime);
        }
        return stopTime;
    }
    public static void setStopTimeLimit(long time) { saveValue(KEY_STOP_TIME_LIMIT, time); }

    // Planned Production Time
    public static long getPlannedProductionTime() {
        long plannedTime = getLongValue(KEY_PLANNED_PRODUCTION_TIME);
        if (plannedTime == 0) {
            plannedTime = 8 * 3600 * 1000; // Default Time : 8 hour
            setPlannedProductionTime(plannedTime);
        }
        return plannedTime;
    }
    public static void setPlannedProductionTime(long time) { saveValue(KEY_PLANNED_PRODUCTION_TIME, time); }

    // Target Cycle Time In Seconds
    public static long getTargetCycleTimeSeconds() {
        long targetCycleTime = getLongValue(KEY_TARGET_CYCLE_TIME);
        if (targetCycleTime == 0) {
            String jobDetails = getJobDetails();
            try {
                JSONObject jsonObject = new JSONObject(jobDetails);
                String strValue = jsonObject.optString("targetCycleTime");

                if (!TextUtils.isEmpty(strValue)) {
                    targetCycleTime = Long.parseLong(strValue);

                    setTargetCycleTimeSeconds(targetCycleTime);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return targetCycleTime;
    }
    public static void setTargetCycleTimeSeconds(long time) { saveValue(KEY_TARGET_CYCLE_TIME, time); }


    public static boolean isAutomaticPartsCounter() {
        return prefs.getBoolean(KEY_AUTOMATIC_PARTS_COUNTER, false);
    }
    public static void setAutomaticPartsCounter(boolean value) {
        saveValue(KEY_AUTOMATIC_PARTS_COUNTER, value);
    }

    public static long getMinElapsedStopTime() {
        long stopTime = getLongValue(KEY_MIN_ELAPSED_TIME);
        if (stopTime == 0) {
            stopTime = 10 * 1000; // Default Time : 10s
            setMinElapsedStopTime(stopTime);
        }
        return stopTime;
    }
    public static void setMinElapsedStopTime(long time) { saveValue(KEY_MIN_ELAPSED_TIME, time); }

    public static int getPartsPerCycle() {
        int partsPerCycle = getIntValue(KEY_PARTS_PER_CYCLE);
        if (partsPerCycle == 0) {
            partsPerCycle = 1;
            setPartsPerCycle(partsPerCycle);
        }
        return partsPerCycle;
    }
    public static void setPartsPerCycle(int value) { saveValue(KEY_PARTS_PER_CYCLE, value); }

    public static long getTimeIncycle() { return getLongValue(KEY_TIME_INCYCLE); }
    public static void setTimeInCycle(long time) { saveValue(KEY_TIME_INCYCLE, time); }

    public static long getTimeUnCat() { return getLongValue(KEY_TIME_UNCAT); }
    public static void setTimeUnCat(long time) { saveValue(KEY_TIME_UNCAT, time); }

    public static long getTimeIdle1() { return getLongValue(KEY_TIME_IDLE1); }
    public static void setTimeIdle1(long time) { saveValue(KEY_TIME_IDLE1, time); }

    public static long getTimeIdle2() { return getLongValue(KEY_TIME_IDLE2); }
    public static void setTimeIdle2(long time) { saveValue(KEY_TIME_IDLE2, time); }

    public static long getTimeIdle3() { return getLongValue(KEY_TIME_IDLE3); }
    public static void setTimeIdle3(long time) { saveValue(KEY_TIME_IDLE3, time); }

    public static long getTimeIdle4() { return getLongValue(KEY_TIME_IDLE4); }
    public static void setTimeIdle4(long time) { saveValue(KEY_TIME_IDLE4, time); }

    public static long getTimeIdle5() { return getLongValue(KEY_TIME_IDLE5); }
    public static void setTimeIdle5(long time) { saveValue(KEY_TIME_IDLE5, time); }

    public static long getTimeIdle6() { return getLongValue(KEY_TIME_IDLE6); }
    public static void setTimeIdle6(long time) { saveValue(KEY_TIME_IDLE6, time); }

    public static long getTimeIdle7() { return getLongValue(KEY_TIME_IDLE7); }
    public static void setTimeIdle7(long time) { saveValue(KEY_TIME_IDLE7, time); }

    public static long getTimeIdle8() { return getLongValue(KEY_TIME_IDLE8); }
    public static void setTimeIdle8(long time) { saveValue(KEY_TIME_IDLE8, time); }

    // Good / Bad Parts
    public static int getGoodParts() { return getIntValue(KEY_PARTS_GOOD); }
    public static void setGoodParts(int value) { saveValue(KEY_PARTS_GOOD, value);}

    public static int getBadParts() { return getIntValue(KEY_PARTS_BAD); }
    public static void setBadParts(int value) { saveValue(KEY_PARTS_BAD, value);}

    // Shift Good / Bad Parts
    public static int getShiftGoodParts() { return getIntValue(KEY_SHIFT_PARTS_GOOD); }
    public static void setShiftGoodParts(int value) { saveValue(KEY_SHIFT_PARTS_GOOD, value);}

    public static int getShiftBadParts() { return getIntValue(KEY_SHIFT_PARTS_BAD); }
    public static void setShiftBadParts(int value) { saveValue(KEY_SHIFT_PARTS_BAD, value);}

    // Fanuc Connection Status
    public static boolean getFanucConnectionStatus() { return prefs.getBoolean(KEY_FANUC_CONN_STATUS, false); }
    public static void setFanucConnectionStatus(boolean value) { saveValue(KEY_FANUC_CONN_STATUS, value);}

    // Server Connection Status
    public static boolean getServerConnectionStatus() { return prefs.getBoolean(KEY_SERVER_CONN_STATUS, false); }
    public static void setServerConnectionStatus(boolean value) { saveValue(KEY_SERVER_CONN_STATUS, value);}

    public static String getQtyCompleted() { return getStringValue(KEY_QTY_COMPLETED); }
    public static void setQtyCompleted(String value) { saveValue(KEY_QTY_COMPLETED, value);}

    public static String getLastTime() { return getStringValue(KEY_LAST_TIME, ""); }
    public static void setLastTime(String time) { saveValue(KEY_LAST_TIME, time); }

    public static boolean isCycleStopAlert() { return prefs.getBoolean(KEY_CYCLE_STOP_ALERT, false); }
    public static void setCycleStopAlert(boolean value) { saveValue(KEY_CYCLE_STOP_ALERT, value); }


    // Auto Monitor Settings
    public static boolean isAutoMonitorStatus() {
        return prefs.getBoolean(KEY_AUTO_MONITOR, false);
    }
    public static void setAutoMonitorStatus(boolean value) {
        saveValue(KEY_AUTO_MONITOR, value);
    }

    public static long getSpindleTime() { return getLongValue(KEY_SPINDLE_TIME); }
    public static void setSpindleTime(long time) { saveValue(KEY_SPINDLE_TIME, time); }

    public static float getSpindleThreshold() { return getFloatValue(KEY_SPINDLE_THRESHOLD); }
    public static void setSpindleThreshold(float value) { saveValue(KEY_SPINDLE_THRESHOLD, value); }

    // HP2 Settings.
    public static boolean isHP2Enabled() {
        return prefs.getBoolean(KEY_HP2_ENABLE, false);
    }
    public static void setHP2Enabled(boolean value) {
        saveValue(KEY_HP2_ENABLE, value);
    }

    public static void setHP2IP(String value) {
        saveValue(KEY_HP2_IP, value);
    }
    public static String getHP2IP() {
        return getStringValue(KEY_HP2_IP, "192.168.0.100");
    }

    public static void setHP2Port(int value) {
        saveValue(KEY_HP2_PORT, value);
    }
    public static int getHP2Port() {
        return getIntValue(KEY_HP2_PORT, 5150);
    }

    public static void setHP2RegAddr(int value) {
        saveValue(KEY_HP2_REGADDRESS, value);
    }
    public static int getHP2RegAddr() {
        return getIntValue(KEY_HP2_REGADDRESS, 0);
    }

    public static float getHP2X() { return getFloatValue(KEY_HP2_X); }
    public static void setHP2X(float value) { saveValue(KEY_HP2_X, value); }

}

