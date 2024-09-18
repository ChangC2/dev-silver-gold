package seemesave.businesshub.view_model.auth;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.ViewModel;

import seemesave.businesshub.view.auth.LoginActivity;
import seemesave.businesshub.view.auth.SignupActivity;

public class AuthViewModel extends ViewModel {

    public AuthViewModel(){

    }

    public void gotoLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void gotoSignup(Context context) {
        Intent intent = new Intent(context, SignupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
