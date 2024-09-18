package seemesave.businesshub.utils;

import static seemesave.businesshub.api.ApiConstants.BASE_URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import seemesave.businesshub.R;
import seemesave.businesshub.listener.ClickListener;
import seemesave.businesshub.listener.CommentClickListener;
import seemesave.businesshub.model.common.CurrencyModel;
import seemesave.businesshub.model.common.UserModel;
import seemesave.businesshub.model.common.UserPortalModel;
import seemesave.businesshub.widget.iOSDialog.iOSDialog;
import seemesave.businesshub.widget.iOSDialog.iOSDialogBuilder;
import seemesave.businesshub.widget.iOSDialog.iOSDialogClickListener;

public class G {
    public static String vendor_privacy_url = "https://business.seemesave.com/pdf/VENDOR-ONLINE-TERMS-AND-CONDITIONS.pdf";
    public static String supplier_privacy_url = "https://business.seemesave.com/pdf/SUPPLIER-ONLINE-TERMS-AND-CONDITIONS.pdf";
    public static String UserAppPlayStoreUrl = "https://play.google.com/store/apps/details?id=com.mobile.seemesave";

    public static String GetUnReadNotificationCount = BASE_URL + "common/notification/unread-count/";
    public static String ClearNotification = BASE_URL + "common/notification/clear/";
    public static String DeleteNotification = BASE_URL + "common/notification/delete/";
    public static String SetNotificationReadUrl = BASE_URL + "common/notification/set-is-read/";
    public static String AcceptNotification = BASE_URL + "common/notification/accept/";
    public static String RejectNotification = BASE_URL + "common/notification/reject/";
    public static String GetVariantsUrl = BASE_URL + "common/product/get-variants/?barcode=%s";
    public static String GetProductUrl = BASE_URL + "common/product/search/?supplier_id=%s";
    public static String GetProductVariantUrl = BASE_URL + "common/product/get-variants/?barcode=%s";
    public static String CreatePromotion = BASE_URL + "common/promotion/create/";
    public static String UpdatePromotion = BASE_URL + "common/promotion/update/";
    public static String UpdateStock = BASE_URL + "common/promotion/update-stock/";
    public static String ActivateAds = BASE_URL + "common/ads/update-item/";
    public static String DeleteAds = BASE_URL + "common/ads/delete/";
    public static String CreatePost = BASE_URL + "common/post/create/";
    public static String UpdatePost = BASE_URL + "common/post/update/";
    public static String CreateFeaturedStore = BASE_URL + "common/featured-store/create/";
    public static String UpdateFeaturedStore = BASE_URL + "common/featured-store/update/";
    public static String CreateFeaturedBrand = BASE_URL + "common/featured-brand/create/";
    public static String UpdateFeaturedBrand = BASE_URL + "common/featured-brand/create/";
    public static String CreateHomeAdvert = BASE_URL + "common/home-advert/create/";
    public static String UpdateHomeAdvert = BASE_URL + "common/home-advert/create/";
    public static String CreateStory = BASE_URL + "common/story/create/";
    public static String UpdateStory = BASE_URL + "common/story/update/";
    public static String ResendStoreInvitation = BASE_URL + "vendor/follower/store/invite/send/";
    public static String DeleteStoreInvitation = BASE_URL + "vendor/follower/store/invite/delete/";
    public static String SendStoreInvitation = BASE_URL + "vendor/follower/store/invite/";
    public static String ResendAgentInvitation = BASE_URL + "common/agent-mgr/resend/";
    public static String DeleteAgentInvitation = BASE_URL + "common/agent-mgr/delete/";
    public static String SendAgentInvitation = BASE_URL + "common/agent-mgr/invite/";
    public static String UpdateAgentInfo = BASE_URL + "common/agent-mgr/update/";
    public static String AcceptSupplierInvitation = BASE_URL + "vendor/suppliers/accept/";
    public static String RejectSupplierInvitation = BASE_URL + "vendor/suppliers/reject/";

    public static String UpdatePortalProfile = BASE_URL + "common/profile/update-vendor/";
    public static String UpdateUserProfile = BASE_URL + "auth/update-user-info/";
    public static String UpdateBankDetail = BASE_URL + "common/payment/bank/update/";
    public static String CreateStore = BASE_URL + "vendor/stores/create/";
    public static String UpdateStore = BASE_URL + "vendor/stores/update/";
    public static String DeleteStore = BASE_URL + "vendor/stores/create/";
    public static String GetStoreReviews = BASE_URL + "vendor/stores/get-all-reviews/?id=%s&offset=%d&page_size=%d";
    public static String SetStoreReviewUrl = BASE_URL + "vendor/stores/reply-to-review/";

    public static String DeleteProduct = BASE_URL + "common/product/portal/delete/";
    public static String CreateProduct = BASE_URL + "common/product/portal/create/";
    public static String UpdateProduct = BASE_URL + "common/product/portal/update/";
    public static String CreateVariant = BASE_URL + "common/product/set-variants/";

    public static String SetBeingPicked = BASE_URL + "vendor/click-collect/set-being-picked/";
    public static String GetOrderDetail = BASE_URL + "vendor/click-collect/detail/?id=%s";
    public static String SetOrderReady = BASE_URL + "vendor/click-collect/set-ready/";
    public static String SetOrderFinished = BASE_URL + "vendor/click-collect/set-finish/";
    public static String SetUpdateStock = BASE_URL + "vendor/click-collect/set-order-changed/";

    public static String GetDeliverOrderDetail = BASE_URL + "vendor/click-deliver/detail/?id=%s";
    public static String SetDeliverOrderPicking = BASE_URL + "vendor/click-deliver/set-picking/";
    public static String SetDeliverOrderReady = BASE_URL + "vendor/click-deliver/set-ready/";
    public static String GetDeliverOrderStatus = BASE_URL + "vendor/click-deliver/get-order-status/?id=";
    public static String SetDeliverUpdateStock = BASE_URL + "vendor/click-deliver/set-order-changed/";

    public static String CreatePaytoPromote = BASE_URL + "supplier/pay-to-promote/create/";
    public static String UpdatePaytoPromoteStore = BASE_URL + "supplier/pay-to-promote/store/add/";
    public static String GetPromoteReviews = BASE_URL + "supplier/pay-to-promote/reviews/all/?id=%s&offset=%d&page_size=%d";
    public static String SetPromoteReviewUrl = BASE_URL + "supplier/pay-to-promote/reviews/create/";
    public static String SetPromoteReplyUrl = BASE_URL + "supplier/pay-to-promote/reviews/reply/";

    public static String DeleteBrand = BASE_URL + "supplier/brand/delete/";
    public static String CreateBrand = BASE_URL + "supplier/brand/create/";
    public static String UpdateBrand = BASE_URL + "supplier/brand/update/";

    public static String ResendBrandInvitation = BASE_URL + "supplier/follower/brand/invite/send/";
    public static String DeleteBrandInvitation = BASE_URL + "supplier/follower/brand/invite/delete/";
    public static String SendBrandInvitation = BASE_URL + "supplier/follower/brand/invite/";

    public static String SendVendorInvitation = BASE_URL + "supplier/supplier_follow/create/";
    public static String ResendVendorInvitation = BASE_URL + "supplier/supplier_follow/resend-invitation/";
    public static String DeleteVendorInvitation = BASE_URL + "supplier/supplier_follow/delete/";

    public static String UpdateSupplierPortalProfile = BASE_URL + "common/profile/update-supplier/";

    public static int ADDRESS_PICKER_REQUEST = 101;

    public static AlertDialog mProgressDialog;
    public static int tabIndex = -1;
    public static String[] tabName = {"home", "order", "analytics", "ads", "menu"};
    public static Location location = null;
    public static UserModel user = new UserModel();
    public static CommentClickListener commentClickListener = null;

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
        try{
            if (mProgressDialog != null && mProgressDialog.isShowing()){
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void hideLoading() {
        try {
            if (mProgressDialog != null){
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public static void setLightFullScreen(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    public static boolean isAvailableGoogleApi(Context context){
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


    public static void openUrlBrowser(Context context, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
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
}
