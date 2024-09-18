package seemesave.businesshub.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import seemesave.businesshub.R;
import seemesave.businesshub.model.common.StoreReviewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class StoreReviewAdapter extends RecyclerView.Adapter<StoreReviewAdapter.ViewHolder>{
    private Context mContext;
    private StoreReviewRecyclerListener mListener;
    private ArrayList<StoreReviewModel> mList = new ArrayList<>();

    public void setData(ArrayList<StoreReviewModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public interface StoreReviewRecyclerListener{
        void onItemClicked(int pos, StoreReviewModel model);
        void onItemAdd(int pos, StoreReviewModel model);
    }

    public StoreReviewAdapter(Context context, ArrayList<StoreReviewModel> list, StoreReviewRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_store_review, parent, false);
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
        @BindView(R.id.imgUser)
        CircleImageView imgUser;
        @BindView(R.id.txtName)
        TextView tvName;
        @BindView(R.id.txtTime)
        TextView tvTime;
        @BindView(R.id.txtDesc)
        TextView tvDesc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void setData(int position) {
            StoreReviewModel model = mList.get(position);
            Glide.with(mContext)
                    .load(model.getUser().getImage_url())
                    .centerCrop()
                    .placeholder(R.drawable.ic_me)
                    .into(imgUser);
            tvName.setText(model.getUser().getFirst_name() + " " + model.getUser().getLast_name());
            tvDesc.setText(model.getComment());
            tvTime.setText(model.getCreated().split(" ")[0]);

        }
    }
}
