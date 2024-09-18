package seemesave.businesshub.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import seemesave.businesshub.R;

import java.io.File;
import java.util.ArrayList;

public class ImageSelectAdapter extends RecyclerView.Adapter<ImageSelectAdapter.ViewHolder> {
    private Context mContext;
    private ImageSelectRecyclerListener mListener;
    private ArrayList<String> mList = new ArrayList<>();
    private boolean editable = true;

    public interface ImageSelectRecyclerListener {
        void onItemClicked(int pos, String model);

        void onItemAdd(int pos, String model);
    }

    public void setData(ArrayList<String> list, boolean editable) {
        this.mList.clear();
        this.mList.addAll(list);
        this.editable = editable;
        notifyDataSetChanged();
    }

    public ImageSelectAdapter(Context context, ArrayList<String> list, ImageSelectRecyclerListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_image_select, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String image_url = mList.get(holder.getLayoutPosition());
        if (TextUtils.isEmpty(image_url)) {
            holder.imgClose.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(mContext.getDrawable(R.drawable.ic_add_post))
                    .placeholder(R.drawable.ic_me)
                    .into(holder.imgItem);
        } else {
            if (editable) {
                holder.imgClose.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(new File(image_url))
                        .placeholder(R.drawable.ic_me)
                        .into(holder.imgItem);
            } else {
                holder.imgClose.setVisibility(View.GONE);
                Glide.with(mContext)
                        .load(image_url)
                        .placeholder(R.drawable.ic_me)
                        .into(holder.imgItem);
            }

        }
        holder.li_item.setOnClickListener(v -> {
            if (TextUtils.isEmpty(image_url)) {
                mListener.onItemAdd(holder.getLayoutPosition(), image_url);
            } else {
                mListener.onItemClicked(holder.getLayoutPosition(), image_url);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout li_item;
        private RoundedImageView imgItem;
        private ImageView imgClose;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            li_item = itemView.findViewById(R.id.li_item);
            imgItem = itemView.findViewById(R.id.imgItem);
            imgClose = itemView.findViewById(R.id.imgClose);
        }
    }
}
