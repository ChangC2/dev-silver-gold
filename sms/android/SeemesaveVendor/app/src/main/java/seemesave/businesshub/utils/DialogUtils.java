package seemesave.businesshub.utils;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import seemesave.businesshub.R;


public class DialogUtils {
    public static void showOkDialog(Context context, String title, String content) {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
                        adb
                       .setTitle(title)
                        .setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                       .setMessage(content)
                       .setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.dismiss())
                        .create()
                        .show();
    }

    public static void showOkDialogWithListener(Context context, String title, String content, DialogInterface.OnClickListener okListener)
    {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(content)
                .setCancelable(false)
                .setPositiveButton("Ok", okListener)
                .create()
                .show();
    }

    public static void showConfirmDialogWithListener(Context context, String title, String content, String okTitle, String cancelTitle, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(content)
                .setCancelable(false)
                .setPositiveButton(okTitle, okListener)
                .setNegativeButton(cancelTitle, cancelListener)
                .create()
                .show();
    }
}
