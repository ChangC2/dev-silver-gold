package seemesave.businesshub.view.vendor.ads;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import seemesave.businesshub.R;
import seemesave.businesshub.adapter.AdsCampaignAdapter;
import seemesave.businesshub.base.BaseFragment;
import seemesave.businesshub.model.common.AdsCampaignModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdsCampaignFragment extends BaseFragment {
    private View mFragView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private AdsCampaignAdapter campaignAdapter;
    public AdsCampaignFragment() {
    }

    public static AdsCampaignFragment newInstance() {
        AdsCampaignFragment fragment = new AdsCampaignFragment();
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
        mFragView = inflater.inflate(R.layout.fragment_ads_campaign, container, false);
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
        setRecycler();
    }
    private void setRecycler() {
        ArrayList<AdsCampaignModel> list = new ArrayList<>();
        list.add(new AdsCampaignModel(1));
        list.add(new AdsCampaignModel(2));
        list.add(new AdsCampaignModel(3));
        campaignAdapter = new AdsCampaignAdapter(getActivity(), list, new AdsCampaignAdapter.AdsCampaignRecyclerListener() {

            @Override
            public void onItemClicked(int pos, AdsCampaignModel model) {

            }
        });
        recyclerView.setAdapter(campaignAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

}