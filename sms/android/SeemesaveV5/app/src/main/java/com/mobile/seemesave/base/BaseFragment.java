package com.mobile.seemesave.base;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.mobile.seemesave.R;
import com.mobile.seemesave.utils.LoadingDialogFragment;


public class BaseFragment extends Fragment {
    private BaseActivity mActivity;
    LoadingDialogFragment dlg;

    public String parentName = "";
    public String fragmentName = "";

    public BaseFragment() {
    }
    public static BaseFragment newInstance(String param1, String param2) {
        BaseFragment fragment = new BaseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout root = new LinearLayout(getActivity());
        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    public FragmentActivity getContext() {
        if (getActivity() != null) {
            return getActivity();
        }

        return mActivity;
    }
    public void showLoadingDialog() {
        if(dlg == null) {
            dlg = LoadingDialogFragment.newInstance();
            dlg.setCancelable(true);
            dlg.show(getChildFragmentManager(), "bottomNav");
        }
    }

    public void hideLoadingDialog() {
        if(dlg != null) {
            dlg.dismiss();
            dlg = null;
        }
    }
    public void showMessages(int resID){
        Toast.makeText(getContext(), getResources().getText(resID), Toast.LENGTH_SHORT).show();
    }
    public void showMessages(String str){
        try {
            Toast toast = new Toast(getContext());
            View toast_view = LayoutInflater.from(getContext()).inflate(R.layout.layout_toast, null);
            TextView tvMessage = toast_view.findViewById(R.id.tvMessage);
            tvMessage.setText(str);
            toast.setView(toast_view);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}