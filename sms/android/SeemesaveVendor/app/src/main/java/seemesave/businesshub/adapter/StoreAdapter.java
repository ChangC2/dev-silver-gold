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

import java.util.ArrayList;
import java.util.Locale;

import seemesave.businesshub.R;
import seemesave.businesshub.model.common.StoreModel;
import seemesave.businesshub.utils.G;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class StoreAdapter  extends RecyclerView.Adapter<StoreAdapter.ViewHolder>{
    private Context mContext;
    private StoreRecyclerListener mListener;
    private ArrayList<StoreModel> mList = new ArrayList<>();

    public interface StoreRecyclerListener{
        void onItemClicked(int pos, StoreModel model);
    }

    public StoreAdapter(Context context, ArrayList<StoreModel> list, StoreRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }
    public void setData(ArrayList<StoreModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_store, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StoreModel model = mList.get(holder.getLayoutPosition());
        holder.txtTitle.setText(model.getName());
        holder.txtAddress.setText(model.getAddress());
        holder.txtReviewCnt.setText(String.format(Locale.US, mContext.getString(R.string.review_cnt), model.getReviewCount()));
        holder.txtFollowerCnt.setText(String.format(Locale.US, mContext.getString(R.string.follower_cnt), model.getFollowerCount()));
        holder.imgOption.setOnClickListener(v -> {
            mListener.onItemClicked(holder.getLayoutPosition(), model);
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTitle;
        private TextView txtAddress;
        private TextView txtReviewCnt, txtFollowerCnt;
        private ImageView imgOption;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtReviewCnt = itemView.findViewById(R.id.txtReviewCnt);
            txtFollowerCnt = itemView.findViewById(R.id.txtFollowerCnt);
            imgOption = itemView.findViewById(R.id.imgOption);
        }
    }
}
