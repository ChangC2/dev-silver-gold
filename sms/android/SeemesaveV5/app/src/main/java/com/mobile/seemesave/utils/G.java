package com.mobile.seemesave.utils;

import static com.mobile.seemesave.api.ApiConstants.BASE_URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.mobile.seemesave.R;
import com.mobile.seemesave.listener.BroadCastReceiverListener;
import com.mobile.seemesave.listener.ClickListener;
import com.mobile.seemesave.listener.CommentClickListener;
import com.mobile.seemesave.model.common.CurrencyModel;
import com.mobile.seemesave.model.common.DiscoverModel;
import com.mobile.seemesave.model.common.PromotionOneModel;
import com.mobile.seemesave.model.common.StoreCategoryModel;
import com.mobile.seemesave.model.common.UserModel;
import com.mobile.seemesave.widget.iOSDialog.iOSDialog;
import com.mobile.seemesave.widget.iOSDialog.iOSDialogBuilder;
import com.mobile.seemesave.widget.iOSDialog.iOSDialogClickListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

public class G {

    public static String GetVersionUrl = BASE_URL + "version-info/";
    public static String OrderRating = BASE_URL + "shopping-cart/set-rate/";
    public static String DeliverRating = BASE_URL + "deliver-cart/set-rate/";
    public static String DeleteShoppingCart = BASE_URL + "shopping-cart/remove/";
    public static String DeleteDeliverCart = BASE_URL + "deliver-cart/remove/";
    public static String AcceptDeliverCartChange = BASE_URL + "deliver-cart/accept-change/";
    public static String RejectDeliverCartChange = BASE_URL + "deliver-cart/cancel-order/";
    public static String AcceptShoppingCartChange = BASE_URL + "shopping-cart/accept-change/";
    public static String RejectShoppingCartChange = BASE_URL + "shopping-cart/cancel-order/";
    public static String UploadPostUrl = BASE_URL + "post/create/";
    public static String DeletePostUrl = BASE_URL + "post/delete/";
    public static String SearchTagStringUrl = BASE_URL + "tag/search/?keyword=%s";
    public static String GetTagSuggestUrl = BASE_URL + "/tag/suggest/?keyword=%s&type_list=%s";
    public static String ClearNotification = BASE_URL + "notification/clear/";
    public static String DeleteNotification = BASE_URL + "notification/delete/";
    public static String SetNotificationReadUrl = BASE_URL + "notification/set-is-read/";
    public static String AcceptNotification = BASE_URL + "notification/accept/";
    public static String RejectNotification = BASE_URL + "notification/reject/";
    public static String GetUserDetailUrl = BASE_URL + "user/user-mgr/";
    public static String DeleteUser = BASE_URL + "user/user-mgr/";
    public static String UploadContacts = BASE_URL + "friend/friend-sync/";
    public static String InviteFriendUrl = BASE_URL + "friend/invite/";
    public static String GetFriendListUrl = BASE_URL + "friend/all/?offset=%d&page_size=%d";
    public static String AcceptInviteUrl = BASE_URL + "friend/accept/";
    public static String ForgotPassUrl = BASE_URL + "user/forgot-password/";
    public static String REGISTER = BASE_URL + "user/register/";
    public static String SetFeedLikeUrl = BASE_URL + "newsfeed/set-like/";
    public static String SetFeedFollowUrl = BASE_URL + "newsfeed/set-follow/";
    public static String FeedPPLUrl = BASE_URL + "newsfeed/ppl/?ppc_type=%s&feed_list=%s";
    public static String GetFeedCommentsUrl = BASE_URL + "newsfeed/review/all/?newsfeed_id=%s&offset=%d&page_size=%d";
    public static String SetFeedCommentsUrl = BASE_URL + "newsfeed/review/all/";
    public static String ReplyCommentsUrl = BASE_URL + "newsfeed/review/reply/";
    public static String GetLikedFriendUrl = BASE_URL + "newsfeed/detail/friends/?id=%s";
    public static String StoreFollowUrl = BASE_URL + "store/set-follow/";
    public static String SetLikeProductsUrl = BASE_URL + "product/set-like/";
    public static String FollowBrandUrl = BASE_URL + "brands/follow-brand/";
    public static String ShareShoppingListUrl = BASE_URL + "shopping-list/share-shopping-list/";
    public static String ShareFeedUrl = BASE_URL + "newsfeed/share/get/";
    public static String InviteFriendAndShareShoppingList = BASE_URL + "shopping-list/share-invite/";
    public static String InviteFriendAndShareFeed = BASE_URL + "newsfeed/share/invite/";
    public static String REGISTER_TOKEN = BASE_URL + "user/set-device-id/";
    public static String GetBookmarkCategory = BASE_URL + "bookmark/category/get/";
    public static String CreateBookmarkCategory = BASE_URL + "bookmark/category/add/";
    public static String DeleteBookmarkCategory = BASE_URL + "bookmark/category/delete/";
    public static String GetBookFeedsUrl = BASE_URL + "bookmark/feed/?category_id=%s&offset=%d&page_size=%d";
    public static String SetFeedBookmarkUrl = BASE_URL + "bookmark/feed/add/";
    public static String RemoveFeedBookmarkUrl = BASE_URL + "bookmark/feed/delete/";
    public static String GetVariantsUrl = BASE_URL + "product/get-variants/?one_product_id=%s";

    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;
    public static AlertDialog mProgressDialog;
    public static int tabIndex = -1;
    public static String mFavPage = "product";
    public static String[] tabName = {"home", "store", "favourite", "account"};
    public static ArrayList<DiscoverModel> stories = new ArrayList<>();
    public static int ADDRESS_PICKER_REQUEST = 101;
    public final static int permission_write_data = 788;
    public final static int permission_Read_data = 789;
    public static ClickListener clickListener = null;
    public static CommentClickListener commentClickListener = null;




    public static void openUrlBrowser(Context context, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    public static void setStarted(boolean flag) {
        editor.putBoolean("started", flag);
        editor.apply();
    }

    public static boolean getStarted() {
        return pref.getBoolean("started", false);
    }

    public static Location location = null;
    public static UserModel user = new UserModel();
    public static ArrayList<StoreCategoryModel> storeCategoryList = new ArrayList<>();
    public static PromotionOneModel promotionInfo = new PromotionOneModel();

    public static void showDlg(Context context, String msg, ClickListener listener) {
        iOSDialogBuilder builder = new iOSDialogBuilder(context)
                .setTitle(context.getString(R.string.txt_app_name))
                .setSubtitle(msg)
                .setBoldPositiveLabel(true)
                .setCancelable(false)
                .setPositiveListener(context.getString(R.string.yes), new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        if (listener != null)
                            listener.onClick(true);
                        dialog.dismiss();
                    }
                });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setFont(context.getResources().getFont(R.font.normal));
        }
        builder.build().show();
    }

    public static void showDlg(Context context, String msg, ClickListener listener, boolean cancelable) {

        iOSDialogBuilder builder = new iOSDialogBuilder(context)
                .setTitle("SeeMeSave")
                .setSubtitle(msg)
                .setBoldPositiveLabel(true)
                .setCancelable(false)
                .setPositiveListener(context.getString(R.string.yes), new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        if (listener != null)
                            listener.onClick(true);
                        dialog.dismiss();
                    }
                });
        if (cancelable)
            builder.setNegativeListener(context.getString(R.string.cancel), new iOSDialogClickListener() {
                @Override
                public void onClick(iOSDialog dialog) {
                    dialog.dismiss();
                }
            });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setFont(context.getResources().getFont(R.font.normal));
        }
        builder.build().show();
    }

    public static void showLoading(Context context) {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                return;
            }
            mProgressDialog = new AlertDialog.Builder(context, R.style.DialogTheme).create();
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(true);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.dlg_loading);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideLoading() {
        try {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getRegisterType() {
        return pref.getBoolean("is_phone", false);
    }

    public static void initUserInfo(UserModel model, boolean isPhone, String password, String token, boolean is_init) {
        if (is_init) {
            editor.putBoolean("is_login", true);
            editor.putBoolean("is_phone", isPhone);
            editor.putString("token", token);
            editor.putString("password", password);
        }
        if (!TextUtils.isEmpty(password)) {
            editor.putString("password", password);
        }
        editor.putInt("deliverCount", 0);
        editor.putInt("cartCount", 0);
        editor.putInt("listCount", 0);
        editor.putInt("notificationCount", 0);
        editor.putString("email", model.getEmail());
        editor.putString("phoneNumber", model.getPhoneNumber());
        editor.putString("user_id", String.valueOf(model.getId()));
        editor.putString("first_name", model.getFirst_name());
        editor.putString("last_name", model.getLast_name());
        editor.putString("email", model.getEmail());
        editor.putString("countryCode", model.getCountryCode());
        editor.putString("phoneNumber", model.getPhoneNumber());
        editor.putString("register_with", model.getRegister_with());
        editor.putFloat("latitude", (float) model.getLatitude());
        editor.putFloat("longitude", (float) model.getLongitude());
        editor.putString("address", model.getAddress());
        editor.putString("full_address", model.getAddress());
        editor.putString("image_url", model.getImage_url());
        editor.putString("bg_image_url", model.getBg_image_url());
        editor.putString("gender", model.getGender());
        editor.putInt("age", model.getAge());
        editor.putString("bio", model.getBio());
        editor.putString("time_zone", model.getTime_zone());
        editor.putInt("time_offset", model.getTime_offset());
        if (model.getCurrency() == null) {
            editor.putString("currency_id", "");
            editor.putString("currency_simple", "");
            editor.putString("currency_iso", "");
            editor.putString("currency_full", "");
        } else {
            editor.putString("currency_id", String.valueOf(model.getCurrency().getId()));
            editor.putString("currency_simple", model.getCurrency().getSimple());
            editor.putString("currency_iso", model.getCurrency().getIso());
            editor.putString("currency_full", model.getCurrency().getFull());
        }
        editor.putInt("friendCount", model.getFriendCount());
        editor.putInt("followedStoreCount", model.getFollowedStoreCount());
        editor.putInt("likedProductCount", model.getLikedProductCount());
        editor.apply();

    }

    public static void setBadgeCount(int listCount, int cartCount, int notificationCount, int deliverCount) {
        editor.putInt("listCount", listCount);
        editor.putInt("cartCount", cartCount);
        editor.putInt("deliverCount", deliverCount);
        editor.putInt("notificationCount", notificationCount);
        editor.apply();
    }

    public static void getUserInfoFromLocal() {
        UserModel luser = new UserModel();
        luser.setId(Integer.parseInt(pref.getString("user_id", "0")));
        luser.setFirst_name(pref.getString("first_name", ""));
        luser.setLast_name(pref.getString("last_name", ""));
        luser.setEmail(pref.getString("email", ""));
        luser.setCountryCode(pref.getString("countryCode", "27"));
        luser.setPhoneNumber(pref.getString("phoneNumber", ""));
        luser.setRegister_with(pref.getString("register_with", "email"));
        luser.setLatitude((double) pref.getFloat("latitude", 0));
        luser.setLongitude((double) pref.getFloat("longitude", 0));
        luser.setAddress(pref.getString("address", ""));
        luser.setImage_url(pref.getString("image_url", ""));
        luser.setBg_image_url(pref.getString("bg_image_url", ""));
        luser.setGender(pref.getString("gender", ""));
        luser.setAge(pref.getInt("age", 0));
        luser.setBio(pref.getString("bio", ""));
        luser.setFriendCount(pref.getInt("friendCount", 0));
        luser.setFollowedStoreCount(pref.getInt("followedStoreCount", 0));
        luser.setLikedProductCount(pref.getInt("likedProductCount", 0));
        luser.setTime_zone(pref.getString("time_zone", ""));
        luser.setTime_offset(pref.getInt("time_offset", 0));
        CurrencyModel mCurrency = new CurrencyModel();
        mCurrency.setId(Integer.parseInt(pref.getString("currency_id", "0")));
        mCurrency.setSimple(pref.getString("currency_simple", ""));
        mCurrency.setIso(pref.getString("currency_iso", ""));
        mCurrency.setFull(pref.getString("currency_full", ""));
        luser.setCurrency(mCurrency);
        G.user = luser;
    }

    public static String getUserID() {
        return String.valueOf(pref.getString("user_id", "0"));
    }

    public static void saveUserAddress(double lat, double lng, String address) {
        editor.putFloat("latitude", (float) lat);
        editor.putFloat("longitude", (float) lng);
        editor.putString("full_address", address);
        editor.apply();
    }

    public static String getAddress() {
        return pref.getString("full_address", "");
    }
    public static void saveFullAddress(String address) {
        Log.e("address_update:", address);
        editor.putString("full_address", address);
    }

    public static void saveAudioStatus(boolean status) {
        editor.putString("audio_status", String.valueOf(status));
        editor.apply();
    }

    public static String getAudioStatus() {
        return pref.getString("audio_status", "false");
    }

    public static String getUserPassword() {
        return pref.getString("password", "");
    }

    public static boolean is_login() {
        return pref.getBoolean("is_login", false);
    }

    public static String getToken() {
        String token = "Bearer " + G.pref.getString("token", "");
        return token;
    }

    public static boolean isAvailableGoogleApi(Context context) {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS;
    }

    public static double meterDistanceBetweenPoints(double lat_a, double lng_a, double lat_b, double lng_b) {
        double pk = (float) (180.f / Math.PI);

        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);
        if (Double.isNaN(tt))
            return 0.0;
        else
            return 6366000 * tt;
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        } catch (Exception e) {
            return false;
        }
    }

    public static void showShareDlg(Context context, ClickListener listener) {
        iOSDialogBuilder builder = new iOSDialogBuilder(context)
                .setTitle("SeeMeSave")
                .setSubtitle("Do you want to share with your friends or share to other services?")
                .setBoldPositiveLabel(true)
                .setCancelable(true)
                .setNegativeListener(context.getString(R.string.share_to), new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        if (listener != null)
                            listener.onClick(false);
                        dialog.dismiss();
                    }
                })
                .setPositiveListener(context.getString(R.string.share_with), new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        if (listener != null)
                            listener.onClick(true);
                        dialog.dismiss();
                    }
                });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setFont(context.getResources().getFont(R.font.normal));
        }
        builder.build().show();
    }

    public static void logout() {
        editor.putBoolean("is_login", false);
        editor.remove("user_id");
        editor.remove("first_name");
        editor.remove("last_name");
        editor.remove("email");
        editor.remove("countryCode");
        editor.remove("phoneNumber");
        editor.remove("address");
        editor.remove("register_with");
        editor.remove("latitude");
        editor.remove("longitude");
        editor.remove("address");
        editor.remove("image_url");
        editor.remove("bg_image_url");
        editor.remove("gender");
        editor.remove("age");
        editor.remove("bio");
        editor.remove("friendCount");
        editor.remove("followedStoreCount");
        editor.remove("likedProductCount");
        editor.remove("time_zone");
        editor.remove("time_offset");
        editor.remove("currency_id");
        editor.remove("currency_simple");
        editor.remove("currency_iso");
        editor.remove("currency_full");

        editor.remove("listCount");
        editor.remove("cartCount");
        editor.remove("deliverCount");
        editor.remove("notificationCount");

        editor.apply();
    }

    public static String getVersion(Context context) {
        try {
            if (context == null) {
                return "1.0";
            } else {
                PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                return pInfo.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return "1.0";
        }
    }

    public static void black_status_bar(Activity activity) {
        View view = activity.getWindow().getDecorView();

        int flags = view.getSystemUiVisibility();
        flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        view.setSystemUiVisibility(flags);
        activity.getWindow().setStatusBarColor(Color.BLACK);
    }

    public static void white_status_bar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        int flags = view.getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        view.setSystemUiVisibility(flags);
        activity.getWindow().setStatusBarColor(Color.WHITE);
    }

    public static void setLightFullScreen(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isAcceptingText()) {
                inputMethodManager.hideSoftInputFromWindow(
                        activity.getCurrentFocus().getWindowToken(),
                        0
                );
            }
        } catch (Exception e) {

        }
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;
    }


    public static void onShare(Context context) {
        String name = G.user.getFirst_name() + " " + G.user.getLast_name();
        String text = "Share From Seemesave\n\n" + name;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.app_name)));

//        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//        String text = "Share From Seemesave\n\n" + name;
//        emailIntent.setData(Uri.parse(String.format(Locale.US, "mailto:%s", contentString)));
//        intent.putExtra(Intent.EXTRA_TEXT, text);
//        context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    public static void registerBroadCast(Context context, String[] actions, BroadCastReceiverListener listener) {
        IntentFilter filter = new IntentFilter();
        for (String action : actions) {
            filter.addAction(action);
        }
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                listener.onReceive(context, intent);
            }
        };
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver,
                filter);
    }
    public static void sendBroadCast(Context context, String action){
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(action));
    }


}
