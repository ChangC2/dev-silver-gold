package seemesave.businesshub.view_model.vendor.main;

import android.content.Intent;

import androidx.lifecycle.ViewModel;

import seemesave.businesshub.view.vendor.main.MainActivity;
import seemesave.businesshub.view.vendor.menu.PostGroupActivity;

public class MainViewModel extends ViewModel {
    public MainViewModel(){

    }
    public void goMenu(MainActivity context) {
        Intent intent = new Intent(context, PostGroupActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
