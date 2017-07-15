package dong.lan.weather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dong.lan.weather.R;
import dong.lan.weather.bean.WeatherHourly;

/**
 * Created by 梁桂栋 on 17-7-9 ： 下午6:23.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder> {


    List<WeatherHourly> hourlies;

    public HourlyWeatherAdapter(List<WeatherHourly> hourlies) {
        this.hourlies = hourlies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hourly, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WeatherHourly hourly = hourlies.get(position);
        holder.time.setText(hourly.time());
        holder.windInfo.setText(hourly.windInfo());
        holder.windDir.setText(hourly.windDir());
        holder.cond.setText(hourly.cond());
        holder.tmp.setText(hourly.tmp());
    }

    @Override
    public int getItemCount() {
        return hourlies == null ? 0 : hourlies.size();
    }

    public void reset(List<WeatherHourly> weatherHourlies) {
        if(hourlies == null)
            hourlies = new ArrayList<>();
        hourlies.clear();
        hourlies.addAll(weatherHourlies);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView cond;
        TextView tmp;
        TextView windDir;
        TextView windInfo;
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            cond = (TextView) itemView.findViewById(R.id.item_hourly_cond);
            tmp = (TextView) itemView.findViewById(R.id.item_hourly_tmp);
            windDir = (TextView) itemView.findViewById(R.id.item_hourly_wind_dir);
            windInfo = (TextView) itemView.findViewById(R.id.item_hourly_wind_info);
            time = (TextView) itemView.findViewById(R.id.item_hourly_time);
        }
    }
}
