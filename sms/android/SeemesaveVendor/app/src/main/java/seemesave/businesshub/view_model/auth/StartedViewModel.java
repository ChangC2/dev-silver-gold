package seemesave.businesshub.view_model.auth;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.ViewModel;

import seemesave.businesshub.view.auth.LoginActivity;
import seemesave.businesshub.view.auth.SignupActivity;

public class StartedViewModel extends ViewModel {

    public StartedViewModel(){

    }

    public void gotoJoin(Context context) {
        Intent intent = new Intent(context, SignupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
    public void gotoSign(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
