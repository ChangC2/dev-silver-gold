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
import seemesave.businesshub.model.common.PostGroupModel;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostGroupDetailAdapter extends RecyclerView.Adapter<PostGroupDetailAdapter.ViewHolder>{
    private Context mContext;
    private PostGroupRecyclerListener mListener;
    private ArrayList<PostGroupModel> mList = new ArrayList<>();

    public interface PostGroupRecyclerListener{
        void onItemClicked(int pos, PostGroupModel model);
    }

    public PostGroupDetailAdapter(Context context, ArrayList<PostGroupModel> list, PostGroupRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_post_group_detail, parent, false);
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
        @BindView(R.id.imgProduct)
        RoundedImageView imgProduct;

        @BindView(R.id.imgDownload)
        ImageView imgDownload;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void setData(int position) {
            PostGroupModel model = mList.get(position);
            Glide.with(mContext)
                    .load(model.getImage_url())
                    .centerCrop()
                    .placeholder(R.drawable.ic_me)
                    .into(imgProduct);

        }
    }
}
