package dong.lan.weather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dong.lan.weather.R;
import dong.lan.weather.bean.WeatherSuggest;

/**
 * Created by 梁桂栋 on 17-7-9 ： 下午6:23.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class WeatherSuggestAdapter extends RecyclerView.Adapter<WeatherSuggestAdapter.ViewHolder> {


    private List<WeatherSuggest> suggests;

    public WeatherSuggestAdapter(List<WeatherSuggest> suggests) {
        this.suggests = suggests;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather_suggest, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WeatherSuggest suggest = suggests.get(position);
        holder.desc.setText(suggest.describer());
        holder.level.setText(suggest.level());
        holder.type.setText(suggest.type());
    }

    @Override
    public int getItemCount() {
        return suggests == null ? 0 : suggests.size();
    }

    public void reset(List<WeatherSuggest> weatherSuggests) {
        if(suggests == null)
            suggests = new ArrayList<>();
        suggests.clear();
        suggests.addAll(weatherSuggests);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView type;
        TextView level;
        TextView desc;

        public ViewHolder(View itemView) {
            super(itemView);
            type = (TextView) itemView.findViewById(R.id.item_suggest_type);
            level = (TextView) itemView.findViewById(R.id.item_suggest_level);
            desc = (TextView) itemView.findViewById(R.id.item_suggest_desc);
        }
    }
}
