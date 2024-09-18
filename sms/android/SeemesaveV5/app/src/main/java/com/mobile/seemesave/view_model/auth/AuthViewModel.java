package com.mobile.seemesave.view_model.auth;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.ViewModel;

import com.mobile.seemesave.view.auth.LoginActivity;
import com.mobile.seemesave.view.auth.SignupActivity;

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
