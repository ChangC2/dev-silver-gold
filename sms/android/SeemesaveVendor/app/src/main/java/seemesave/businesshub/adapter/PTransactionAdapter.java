package seemesave.businesshub.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import seemesave.businesshub.R;
import seemesave.businesshub.listener.RecyclerClickListener;
import seemesave.businesshub.model.common.PTransactionModel;

public class PTransactionAdapter extends RecyclerView.Adapter<PTransactionAdapter.ViewHolder> {

    Context mContext;
    private ArrayList<PTransactionModel> mList = new ArrayList<>();
    private RecyclerClickListener listener;

    public PTransactionAdapter(Context context, ArrayList<PTransactionModel> list, RecyclerClickListener pListener) {
        this.mList.clear();
        this.mList.addAll(list);
        listener = pListener;
        mContext = context;
    }

    public void setData(ArrayList<PTransactionModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    // ******************************class ViewHoler redefinition ***************************//
    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ckStatus)
        CheckBox ckStatus;

        @BindView(R.id.swSelect)
        SwitchCompat swSelect;

        @BindView(R.id.txtBudget)
        TextView txtBudget;

        @BindView(R.id.txtSpent)
        TextView txtSpent;

        @BindView(R.id.txtCnt)
        TextView txtCnt;

        @BindView(R.id.txtTitle)
        TextView txtTitle;

        @BindView(R.id.txtPeriod)
        TextView txtPeriod;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("ClickableViewAccessibility")
        public void setData(int position) {
            PTransactionModel model = mList.get(position);

        }
    }
    // ******************************class ViewHoler redefinition ***************************//
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder Vholder, final int position) {
        Vholder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}