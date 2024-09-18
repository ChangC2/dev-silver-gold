package seemesave.businesshub.view.supplier.promote;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.PromoteStoreAdapter;
import seemesave.businesshub.api.promote.PromoteApi;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.model.common.PromoteReceiverModel;
import seemesave.businesshub.model.res.PromoteBaseInfoRes;
import seemesave.businesshub.model.res.PromoteDetailInfoRes;
import seemesave.businesshub.utils.G;

public class PromoteStoreActivity extends BaseActivity {

    private PromoteStoreActivity activity;

    @BindView(R.id.lytEmpty)
    LinearLayout lytEmpty;
    @BindView(R.id.storeView)
    RecyclerView storeView;
    private PromoteStoreAdapter storeAdapter;
    private ArrayList<PromoteReceiverModel> storeList = new ArrayList<>();


    private int promote_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_store);
        ButterKnife.bind(this);
        activity = this;
        promote_id = getIntent().getIntExtra("id", -1);
        initView();
        showLoadingDialog();
        PromoteApi.getPromoteBaseInfo();
    }

    private void initView() {
        storeList.clear();
        setStoreAdapter();
    }

    private void setStoreAdapter() {
        storeAdapter = new PromoteStoreAdapter(activity, storeList, true, new PromoteStoreAdapter.PromoteStoreRecyclerListener() {

            @Override
            public void onItemClicked(int pos, PromoteReceiverModel model) {

            }
        });
        storeView.setAdapter(storeAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        storeView.setLayoutManager(linearLayoutManager);
    }


    private void initData(PromoteDetailInfoRes model) {
        if (model.getReceivers().size() == 0) {
            lytEmpty.setVisibility(View.VISIBLE);
        } else {
            for (int i = 0; i < model.getReceivers().size(); i++) {
                updateStoreList(model.getReceivers().get(i));
            }
            Collections.sort(storeList, new Comparator<PromoteReceiverModel>() {
                @Override
                public int compare(PromoteReceiverModel lhs, PromoteReceiverModel rhs) {
                    return String.valueOf(rhs.isCheck()).compareTo(String.valueOf(lhs.isCheck()));
                }
            });
            storeAdapter.setData(storeList);
            lytEmpty.setVisibility(View.GONE);
        }
    }

    private void updateStoreList(PromoteReceiverModel model) {
        int storeId = model.getStore().getId();
        for (int i = 0; i < storeList.size(); i++) {
            if (storeId == storeList.get(i).getStore().getId()) {
                storeList.get(i).setId(model.getId());
                storeList.get(i).setStore(model.getStore());
                storeList.get(i).setPay_to_promote(model.getPay_to_promote());
                storeList.get(i).setIs_accepted(model.isIs_accepted());
                storeList.get(i).setIs_rejected(model.isIs_rejected());
                storeList.get(i).setReject_reason(model.getReject_reason());
                storeList.get(i).setCheck(true);
            }
        }
    }

    private void onUpdateStore() {
        String selStores = "";
        for (int i = 0; i < storeList.size(); i ++) {
            if (storeList.get(i).getId() == -1 && storeList.get(i).isCheck()) {
                selStores += storeList.get(i).getStore().getId() + ",";
            }
        }
        if (TextUtils.isEmpty(selStores)) {
            Toast.makeText(activity, R.string.msg_select_store, Toast.LENGTH_LONG).show();
            return;
        } else {
            selStores = selStores.substring(0, selStores.length() - 1);
        }
        showLoadingDialog();
        Builders.Any.B builder;
        builder = Ion.with(activity).load("POST", G.UpdatePaytoPromoteStore);

        builder.addHeader("Authorization", App.getToken())
                .addHeader("Content-Language", App.getPortalToken());
        builder.setBodyParameter("paytopromote_id", String.valueOf(promote_id))
                .setBodyParameter("store_id_list", selStores);
        builder.asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        if (e != null) {
                            Toast.makeText(activity, R.string.msg_something_wrong, Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("status")) {
                                    Toast.makeText(activity, R.string.msg_updated_successfully, Toast.LENGTH_LONG).show();
                                    LocalBroadcastManager.getInstance(activity).sendBroadcast(new Intent("refresh_paytopromote"));
                                    finish();
                                } else {
                                    Toast.makeText(activity, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException jsonException) {
                                Toast.makeText(activity, R.string.msg_something_wrong, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessPromoteDetail(PromoteDetailInfoRes res) {
        hideLoadingDialog();
        if (res.isStatus()) {
            initData(res);
        } else {
            if (!TextUtils.isEmpty(res.getMessage())) {
                Toast.makeText(activity, res.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessBase(PromoteBaseInfoRes res) {
        hideLoadingDialog();
        if (res.isStatus() && res.getStoreList().size() > 0) {
            storeList.clear();
            for (int i = 0; i < res.getStoreList().size(); i++) {
                PromoteReceiverModel model = new PromoteReceiverModel();
                model.setId(-1);
                model.setStore(res.getStoreList().get(i));
                storeList.add(model);
            }

            showLoadingDialog();
            PromoteApi.getPromoteDetail(promote_id);
        } else {
            if (!TextUtils.isEmpty(res.getMessage())) {
                Toast.makeText(activity, res.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    @OnClick({R.id.btBack, R.id.btnUpdate})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.btnUpdate:
                onUpdateStore();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
