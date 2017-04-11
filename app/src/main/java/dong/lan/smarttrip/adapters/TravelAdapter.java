package dong.lan.smarttrip.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import dong.lan.smarttrip.R;
import dong.lan.model.bean.travel.Travel;

/**
 * Created by 梁桂栋 on 16-10-12 ： 下午2:56.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.Holder> {

    private List<Travel> travels;
    public TravelAdapter(List<Travel> travels) {
        this.travels = travels;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_travels,null);
        final Holder holder = new Holder(view);
        if(itemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(travels.get(holder.getLayoutPosition()));
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Log.d("ADAPTER", "onBindViewHolder: "+position);
    }

    @Override
    public int getItemCount() {
        return travels==null?0:travels.size();
    }

    public void refresh(List<Travel> travels){
        this.travels.clear();
        this.travels.addAll(travels);
        notifyDataSetChanged();
    }

    private ItemClickListener<Travel> itemClickListener;
    public void setItemClickListener(ItemClickListener listener){
        this.itemClickListener = listener;
    }

    static class Header extends RecyclerView.ViewHolder{

        public Header(View itemView) {
            super(itemView);
        }
    }
    static class Holder extends RecyclerView.ViewHolder{

        TextView tittle;
        TextView day_tv;
        TextView time;
        TextView info;
        public Holder(View itemView) {
            super(itemView);
//            thumbImg = (DraweeView) itemView.findViewById(R.id.item_travel_img);
//            tittle = (TextView) itemView.findViewById(R.id.item_travel_tittle);
//            day_tv = (TextView) itemView.findViewById(R.id.item_travel_days);
//            time = (TextView) itemView.findViewById(R.id.item_travel_time);
//            info = (TextView) itemView.findViewById(R.id.item_travel_info);
        }
    }
}
