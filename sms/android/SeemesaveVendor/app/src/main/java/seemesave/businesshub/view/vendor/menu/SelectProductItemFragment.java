package seemesave.businesshub.view.vendor.menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.BaseInfoAdapter;
import seemesave.businesshub.listener.RecyclerClickListener;
import seemesave.businesshub.model.common.BaseInfoModel;

public class SelectProductItemFragment extends BottomSheetDialogFragment {

    private View mFragView;
    private Activity mContext;
    private BottomFragListener mListener;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.edtSearch)
    EditText edtSearch;
    @BindView(R.id.imgClear)
    ImageView imgClear;
    @BindView(R.id.btnConfirm)
    LinearLayout btnConfirm;

    private BaseInfoAdapter baseInfoAdapter;

    private ArrayList<BaseInfoModel> dataList = new ArrayList<>();
    private ArrayList<BaseInfoModel> srcList = new ArrayList<>();
    String selType = "";

    private BaseInfoModel selModel = new BaseInfoModel();
    public interface BottomFragListener {
        void onDismiss();
    }

    public SelectProductItemFragment(Activity context, String type, ArrayList<BaseInfoModel> list, BottomFragListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.selType = type;
        this.srcList.clear();
        this.dataList.clear();
        this.dataList.addAll(list);
        this.srcList.addAll(list);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new BottomSheetDialog(getContext(), R.style.BottomSheetStyle);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragView = inflater.inflate(R.layout.dlg_bottom_product_item, container, false);
        ButterKnife.bind(this, mFragView);
        initView();
        return mFragView;
    }

    private void initView() {
        txtTitle.setText(selType);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        RecyclerClickListener mListner = new RecyclerClickListener() {
            @Override
            public void onClick(View v, int vPosition) {
                selModel = dataList.get(vPosition);
                dataList.clear();
                for (int i = 0; i < srcList.size(); i++) {
                    BaseInfoModel mModel = new BaseInfoModel();
                    mModel.setId(srcList.get(i).getId());
                    mModel.setName(srcList.get(i).getName());
                    mModel.setTitle(srcList.get(i).getTitle());
                    mModel.setType(srcList.get(i).getType());
                    mModel.setParent_id(srcList.get(i).getParent_id());
                    boolean selCheck = false;
                    if (i == vPosition) {
                        selCheck = true;
                    }
                    mModel.setCheck(selCheck);
                    dataList.add(mModel);
                }
                baseInfoAdapter.setData(dataList);
            }
            @Override
            public void onClick(View v, int vPosition, int type) {
            }
        };
        baseInfoAdapter = new BaseInfoAdapter(mContext, selType, dataList, mListner);
        recyclerView.setAdapter(baseInfoAdapter);


        edtSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = edtSearch.getText().toString().toLowerCase();
                    dataList.clear();
                    for (int i = 0; i < srcList.size(); i++) {
                        if (selType.equalsIgnoreCase(mContext.getString(R.string.txt_category))) {
                            if (srcList.get(i).getTitle().toLowerCase().contains(keyword)) {
                                dataList.add(srcList.get(i));
                            }
                        } else {
                            if (srcList.get(i).getName().toLowerCase().contains(keyword)) {
                                dataList.add(srcList.get(i));
                            }
                        }
                    }
                    baseInfoAdapter.setData(dataList);
                    return true;
                }
                return false;
            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() == 0) {
                    imgClear.setVisibility(View.GONE);
                } else {
                    imgClear.setVisibility(View.VISIBLE);
                }
            }
        });
        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataList.clear();
                dataList.addAll(srcList);
                baseInfoAdapter.setData(dataList);
                edtSearch.setText("");
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selModel.setType(selType);
                EventBus.getDefault().post(selModel);
                dismiss();
            }
        });
    }

}
