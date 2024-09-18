package seemesave.businesshub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import seemesave.businesshub.R;
import seemesave.businesshub.application.App;
import seemesave.businesshub.model.common.SupplierModel;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.ViewHolder>{
    private Context mContext;
    private SupplierRecyclerListener mListener;
    private ArrayList<SupplierModel> mList = new ArrayList<>();

    public void setData(ArrayList<SupplierModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public interface SupplierRecyclerListener{
        void onItemClicked(int pos, SupplierModel model);
    }

    public SupplierAdapter(Context context, ArrayList<SupplierModel> list, SupplierRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_supplier, parent, false);
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
        @BindView(R.id.imgLogo)
        ImageView imgLogo;
        @BindView(R.id.imgOption)
        ImageView imgOption;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txtDescription)
        TextView txtDescription;
        @BindView(R.id.btnStatusActive)
        LinearLayout btnStatusActive;
        @BindView(R.id.btnStatusPending)
        LinearLayout btnStatusPending;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void setData(int position) {
            SupplierModel model = mList.get(position);
            if (App.getPortalType()) {
                Glide.with(mContext)
                        .load(model.getSupplier().getLogo())
                        .centerCrop()
                        .placeholder(R.drawable.ic_me)
                        .into(imgLogo);
                txtTitle.setText(model.getSupplier().getName());
            } else {
                Glide.with(mContext)
                        .load(model.getVendor().getLogo())
                        .centerCrop()
                        .placeholder(R.drawable.ic_me)
                        .into(imgLogo);
                txtTitle.setText(model.getVendor().getName());
            }
            txtDescription.setText(model.getDescription());
            if (model.isPending()) {
                btnStatusPending.setVisibility(View.VISIBLE);
                btnStatusActive.setVisibility(View.GONE);
            } else {
                btnStatusPending.setVisibility(View.GONE);
                btnStatusActive.setVisibility(View.VISIBLE);
            }
            imgOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClicked(position, model);
                }
            });
        }
    }
}
