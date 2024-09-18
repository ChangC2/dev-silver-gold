package seemesave.businesshub.view.vendor.ads;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import seemesave.businesshub.R;
import seemesave.businesshub.base.BaseFragment;

import butterknife.ButterKnife;

public class AdsStoreFragment extends BaseFragment {
    private View mFragView;

    public AdsStoreFragment() {
    }

    public static AdsStoreFragment newInstance() {
        AdsStoreFragment fragment = new AdsStoreFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragView = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.bind(this, mFragView);
        initView();
        return mFragView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initView() {

    }

}