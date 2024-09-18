package seemesave.businesshub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import seemesave.businesshub.R;
import seemesave.businesshub.model.common.AdsCampaignModel;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdsCampaignAdapter extends RecyclerView.Adapter<AdsCampaignAdapter.ViewHolder>{
    private Context mContext;
    private AdsCampaignRecyclerListener mListener;
    private ArrayList<AdsCampaignModel> mList = new ArrayList<>();

    public interface AdsCampaignRecyclerListener{
        void onItemClicked(int pos, AdsCampaignModel model);
    }

    public AdsCampaignAdapter(Context context, ArrayList<AdsCampaignModel> list, AdsCampaignRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_ads_campaign, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(holder.getLayoutPosition());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.swSelect)
        SwitchCompat swSelect;
        @BindView(R.id.txtBudget)
        TextView txtBudget;
        @BindView(R.id.txtSpent)
        TextView txtSpent;
        @BindView(R.id.txtCnt)
        TextView txtCnt;
        @BindView(R.id.txtType)
        TextView txtType;
        @BindView(R.id.txtPeriod)
        TextView txtPeriod;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void setData(int position) {
            AdsCampaignModel model = mList.get(position);

        }
    }
}
