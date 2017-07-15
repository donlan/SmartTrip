package dong.lan.weather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dong.lan.weather.R;
import dong.lan.weather.bean.DayWeather;

/**
 * Created by 梁桂栋 on 17-7-9 ： 下午6:23.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class DayWeatherAdapter extends RecyclerView.Adapter<DayWeatherAdapter.ViewHolder> {


    List<DayWeather> dayWeathers;

    public DayWeatherAdapter(List<DayWeather> dayWeathers) {
        this.dayWeathers = dayWeathers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day_weather, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DayWeather dayWeather = dayWeathers.get(position);
        holder.date.setText(dayWeather.date());
        holder.wind.setText(dayWeather.wind());
        holder.cond.setText(dayWeather.cond());
        holder.tmp.setText(dayWeather.tmp());
    }

    @Override
    public int getItemCount() {
        return dayWeathers == null ? 0 : dayWeathers.size();
    }

    public void reset(List<DayWeather> dayWeathers) {
        if(this.dayWeathers == null)
            this.dayWeathers = new ArrayList<>();
        this.dayWeathers.clear();
        this.dayWeathers.addAll(dayWeathers);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView cond;
        TextView tmp;
        TextView wind;
        TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            cond = (TextView) itemView.findViewById(R.id.weather_day_cond);
            tmp = (TextView) itemView.findViewById(R.id.weather_day_tmp);
            wind = (TextView) itemView.findViewById(R.id.weather_day_wind);
            date = (TextView) itemView.findViewById(R.id.weather_day_date);
        }
    }
}
