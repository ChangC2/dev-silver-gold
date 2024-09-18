package seemesave.businesshub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import com.bumptech.glide.Glide;
import seemesave.businesshub.R;
import seemesave.businesshub.model.common.AdsTabModel;
import seemesave.businesshub.model.common.OrderModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdsTabAdapter extends RecyclerView.Adapter<AdsTabAdapter.ViewHolder>{
    private Context mContext;
    private AdsTabRecyclerListener mListener;
    private ArrayList<AdsTabModel> mList = new ArrayList<>();
    private int selected_index = 0;
    public interface AdsTabRecyclerListener{
        void onItemClicked(int pos, AdsTabModel model);
    }

    public void setSelected_index(int index){
        selected_index = index;
        notifyDataSetChanged();
    }

    public AdsTabAdapter(Context context, ArrayList<AdsTabModel> list, AdsTabRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_ads_tab, parent, false);
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
        @BindView(R.id.cardView)
        CardView cardView;
        @BindView(R.id.imgLogo)
        ImageView imgLogo;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void setData(int position) {
            AdsTabModel model = mList.get(position);
            int resourceId = R.drawable.ic_campaign;
            switch (model.getId()) {
                case 1:
                    resourceId = R.drawable.ic_campaign;
                    break;
                case 2:
                    resourceId = R.drawable.ic_promotion;
                    break;
                case 3:
                    resourceId = R.drawable.ic_pay_promote;
                    break;
                case 4:
                    resourceId = R.drawable.ic_best_deal;
                    break;
                case 5:
                    resourceId = R.drawable.ic_exclusive;
                    break;
                case 6:
                    resourceId = R.drawable.ic_menu_stores;
                    break;
                case 7:
                    resourceId = R.drawable.ic_feature_store;
                    break;
                case 8:
                    resourceId = R.drawable.ic_ads_post;
                    break;
                case 9:
                    resourceId = R.drawable.ic_story;
                    break;

            }
            Glide.with(mContext)
                    .load(ContextCompat.getDrawable(mContext, resourceId))
                    .into(imgLogo);
            txtTitle.setText(model.getLabel());

            if (position == selected_index) {
                txtTitle.setTextColor(ContextCompat.getColor(mContext, R.color.main_blue_color));
//                cardView.setCardElevation(1f);
                cardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.white_color));
                imgLogo.setColorFilter(ContextCompat.getColor(mContext, R.color.main_blue_color), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                txtTitle.setTextColor(ContextCompat.getColor(mContext, R.color.md_grey_800));
//                cardView.setCardElevation(0f);
                cardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.md_grey_300));
                imgLogo.setColorFilter(ContextCompat.getColor(mContext, R.color.md_grey_800), android.graphics.PorterDuff.Mode.SRC_IN);
            }
            itemView.setOnClickListener(v -> {
                selected_index = getLayoutPosition();
                notifyDataSetChanged();
                mListener.onItemClicked(position, model);
            });

        }
    }
}
