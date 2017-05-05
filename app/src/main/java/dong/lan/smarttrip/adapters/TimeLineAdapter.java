package dong.lan.smarttrip.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dong.lan.model.bean.travel.Node;
import dong.lan.model.bean.travel.Transportation;
import dong.lan.model.utils.TimeUtil;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.ui.customview.LabelTextView;
import io.realm.RealmResults;

/**
 * Created by 梁桂栋 on 17-5-5 ： 下午7:44.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder> {

    private RealmResults<Node> nodes;

    public TimeLineAdapter(RealmResults<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_time_line, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Node node = nodes.get(position);

        String monthStr = TimeUtil.getTime(node.getArrivedTime(), "MM_dd");
        String preMonthStr = position == 0 ? "" : TimeUtil.getTime(nodes.get(position - 1).getArrivedTime(), "MM_dd");
        if (!monthStr.equals(preMonthStr)) {
            holder.dayTv.setText(monthStr.substring(0, 1));
            holder.monthTv.setText(monthStr.substring(2, 3));
        } else {
            holder.dayTv.setText("  ");
            holder.monthTv.setText("  ");
        }
        Transportation transportation = node.getTransportations().get(0);
        String s = TimeUtil.getTime(transportation.getStartTime(), "HH:mm");
        String e = TimeUtil.getTime(transportation.getEndTime(), "HH:mm");
        holder.timeInfo.setText(s + " -> " + e);
        int type = transportation.getType();
        switch (type) {
            case Transportation.TYPE_WALK:
                holder.trans.setImageResource(R.drawable.plane);
                break;
            case Transportation.TYPE_PLANE:
                holder.trans.setImageResource(R.drawable.plane);
                break;
            case Transportation.TYPE_TRAIN:
                holder.trans.setImageResource(R.drawable.train);
                break;
        }
        holder.info.setText(node.getAddress() + "\n" + node.getDescription());
        long curTime = System.currentTimeMillis();
        if (curTime < node.getArrivedTime()) {
            holder.dot.setBgColor(0xff4B4A5A);
        } else {
            holder.dot.setBgColor(0xff22C7A9);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return nodes == null ? 0 : nodes.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_time_line)
        View line;
        @BindView(R.id.item_time_line_day)
        TextView dayTv;
        @BindView(R.id.item_time_line_month)
        TextView monthTv;
        @BindView(R.id.item_time_line_dot)
        LabelTextView dot;
        @BindView(R.id.item_time_line_tran)
        ImageView trans;
        @BindView(R.id.item_time_line_time_info)
        TextView timeInfo;
        @BindView(R.id.item_time_line_info)
        TextView info;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
