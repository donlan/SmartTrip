package dong.lan.smarttrip.adapters.binder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dong.lan.model.utils.TimeUtil;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.base.AbstractBinder;
import dong.lan.smarttrip.adapters.base.BaseHolder;
import dong.lan.smarttrip.adapters.base.BinderClickListener;
import dong.lan.model.features.ITravel;
import dong.lan.smarttrip.ui.customview.LabelTextView;

/**
 * Created by 梁桂栋 on 17-2-17 ： 下午4:09.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public  class TravelBinder extends AbstractBinder<ITravel> {
    @Override
    public BaseHolder<ITravel> bindViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_travels, null);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                if(clickListener!=null)
                clickListener.onClick(valueAt(pos), pos, 0);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int pos = holder.getLayoutPosition();
                if(clickListener!=null)
                clickListener.onClick(valueAt(pos), pos, 1);
                return true;
            }
        });
        return holder;
    }


    @Override
    public void setBinderClickListener(BinderClickListener< ITravel> clickListener) {
        this.clickListener = clickListener;
    }
}


class ViewHolder extends BaseHolder<ITravel> {

    private static final String TAG = "TAG";
    @BindView(R.id.item_travels_image)
    ImageView image;
    @BindView(R.id.item_travels_tittle)
    LabelTextView tittle;
    @BindView(R.id.item_travels_time)
    LabelTextView content;
    @BindView(R.id.item_travels_msg_hint)
    LabelTextView msgHint;

    public ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindData(ITravel travel) {
        Glide.with(itemView.getContext())
                .load(travel.imageUrl())
                .placeholder(R.drawable.demo_image1)
                .error(R.drawable.error_404)
                .into(image);
        tittle.setText(travel.name());
        int day = TimeUtil.getTimeDays(travel.getStartTime(), travel.getEndTime());
        content.setText(TimeUtil.getTime(travel.getStartTime(), "yyyy.MM.dd   ") + day + " 天");
        if (travel.unread() <= 0) {
            msgHint.setVisibility(View.GONE);
        } else {
            msgHint.setVisibility(View.VISIBLE);
            msgHint.setText("" + travel.unread());
        }
    }
}
