package seemesave.businesshub.view.vendor.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import seemesave.businesshub.R;
import seemesave.businesshub.base.BaseFragment;
import seemesave.businesshub.view.vendor.menu.BankDetailActivity;
import seemesave.businesshub.view.vendor.menu.FollowerActivity;
import seemesave.businesshub.view.vendor.menu.PaymentActivity;
import seemesave.businesshub.view.vendor.menu.PostGroupActivity;
import seemesave.businesshub.view.vendor.menu.PostSupplierActivity;
import seemesave.businesshub.view.vendor.menu.ProductActivity;
import seemesave.businesshub.view.vendor.menu.StoreActivity;
import seemesave.businesshub.view.vendor.menu.SupplierActivity;
import seemesave.businesshub.view.vendor.menu.UserManagementActivity;
import seemesave.businesshub.view.vendor.menu.UserProfileActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import seemesave.businesshub.view.vendor.menu.VendorProfileActivity;

public class MenuFragment extends BaseFragment {
    private View mFragView;

    public MenuFragment() {
    }

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
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
    @OnClick({R.id.lytStore, R.id.lytPostGroup, R.id.lytPostSupplier, R.id.lytFollower, R.id.lytUserManagement, R.id.lytStoreProfile, R.id.lytUserProfile, R.id.lytSupplier, R.id.lytPayment, R.id.lytBankDetail, R.id.lytProduct})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.lytStore:
                startActivity(new Intent(getActivity(), StoreActivity.class));
                break;
            case R.id.lytProduct:
                startActivity(new Intent(getActivity(), ProductActivity.class));
                break;
            case R.id.lytBankDetail:
                startActivity(new Intent(getActivity(), BankDetailActivity.class));
                break;
            case R.id.lytPostGroup:
                startActivity(new Intent(getActivity(), PostGroupActivity.class));
                break;
            case R.id.lytPostSupplier:
                startActivity(new Intent(getActivity(), PostSupplierActivity.class));
                break;
            case R.id.lytFollower:
                startActivity(new Intent(getActivity(), FollowerActivity.class));
                break;
            case R.id.lytSupplier:
                startActivity(new Intent(getActivity(), SupplierActivity.class));
                break;
            case R.id.lytUserManagement:
                startActivity(new Intent(getActivity(), UserManagementActivity.class));
                break;
            case R.id.lytUserProfile:
                startActivity(new Intent(getActivity(), UserProfileActivity.class));
                break;
            case R.id.lytStoreProfile:
                startActivity(new Intent(getActivity(), VendorProfileActivity.class));
                break;
            case R.id.lytPayment:
                startActivity(new Intent(getActivity(), PaymentActivity.class));
                break;
        }
    }
}