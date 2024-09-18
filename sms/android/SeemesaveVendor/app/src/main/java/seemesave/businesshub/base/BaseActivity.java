package seemesave.businesshub.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import seemesave.businesshub.R;
import seemesave.businesshub.utils.LoadingDialogFragment;


public abstract class BaseActivity extends AppCompatActivity {
    private LoadingDialogFragment dlg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }


    public static void setFullScreen(Activity activity){
        Window w = activity.getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
    public void setTransparentStatusBar(Activity activity){
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
    public void hideKeyBoard(){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    public void showMessages(int resID){
        Toast.makeText(this, getResources().getText(resID), Toast.LENGTH_LONG).show();
    }
    public void showMessages(String str){
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    public void showLoadingDialog() {
        try {
            if(dlg == null) {
                dlg = LoadingDialogFragment.newInstance();
                dlg.setCancelable(false);
                dlg.show(getSupportFragmentManager(), "loading");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void hideLoadingDialog() {
        try {
            if(dlg != null) {
                dlg.dismiss();
                dlg = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
