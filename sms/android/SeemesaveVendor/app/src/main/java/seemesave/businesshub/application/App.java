package seemesave.businesshub.application;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;

import seemesave.businesshub.model.common.UserModel;
import seemesave.businesshub.model.common.UserPortalModel;
import seemesave.businesshub.utils.G;

public class App extends MultiDexApplication {
    public static App get;
    public static Context context;
    int PRIVATE_MODE = 0;

    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;

    public static synchronized App getInstance() {
        App vendorApp;
        synchronized (App.class) {
            if (get == null) {
                get = new App();
            }
            vendorApp = get;
        }
        return vendorApp;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        FirebaseApp.initializeApp(this);
        get = this;
        pref = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        editor = pref.edit();
        context = getApplicationContext();
    }

    public void setStarted(boolean flag) {
        editor.putBoolean("started", flag);
        editor.apply();
    }

    public boolean getStarted() {
        return pref.getBoolean("started", false);
    }

    public boolean is_login() {
        return pref.getBoolean("is_login", false);
    }

    public static void initPortalInfo(String user_token, UserModel tUser, UserPortalModel portal, String password, boolean type) {
        editor.putBoolean("is_login", true);
        if (!TextUtils.isEmpty(user_token)) {
            editor.putString("token", user_token);
        }
        editor.putInt("user_id", tUser.getId());
        editor.putString("password", password);
        editor.putString("user_first_name", tUser.getFirst_name());
        editor.putString("user_last_name", tUser.getLast_name());
        editor.putString("user_image_url", tUser.getImage_url());
        if (portal != null) {
            editor.putString("portal_token", portal.getPortal_token());
            editor.putInt("portal_id", portal.getId());
            editor.putString("portal_name", portal.getName());
            editor.putString("portal_logo", portal.getLogo());
            editor.putBoolean("portal_type", type);
        }
        editor.apply();
    }
    public static void initNotificationCount(int unread_count, int collect_count, int deliver_count) {
        editor.putInt("unread_count", unread_count);
        editor.putInt("collect_count", collect_count);
        editor.putInt("deliver_count", deliver_count);
        editor.apply();
    }
    public static boolean getPortalType() {
        return pref.getBoolean("portal_type", false);
    }
    public static int getUnreadNotificationCount() {
        return pref.getInt("unread_count", 0);
    }
    public static int getUnreadCollectCount() {
        return pref.getInt("collect_count", 0);
    }
    public static int getUnreadDeliverCount() {
        return pref.getInt("deliver_count", 0);
    }
    public static void logout() {
        editor.remove("is_login");
        editor.remove("token");
        editor.remove("user_id");
        editor.remove("user_first_name");
        editor.remove("user_last_name");
        editor.remove("user_image_url");
        editor.remove("portal_token");
        editor.remove("portal_id");
        editor.remove("portal_name");
        editor.remove("portal_logo");
        editor.remove("portal_type");
        editor.remove("unread_count");
        editor.remove("collect_count");
        editor.remove("deliver_count");
        editor.apply();
    }
    public static String getUserImage() {
        return pref.getString("user_image_url", "");
    }
    public static String getUserFirstName() {
        return pref.getString("user_first_name", "");
    }
    public static String getUserLastName() {
        return pref.getString("user_last_name", "");
    }
    public static String getToken() {
        String token = "Bearer " + pref.getString("token", "");
        return token;
    }

    public static String getPortalToken() {
        String token = pref.getString("portal_token", "");
        return token;
    }

    public static String getUserID() {
        String user_id = String.valueOf(pref.getInt("user_id", -1));
        return user_id;
    }
}
