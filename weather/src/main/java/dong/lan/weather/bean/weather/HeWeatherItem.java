package dong.lan.weather.bean.weather;

import java.util.List;

/**
 * Author: dooze
 * Created by: ModelGenerator on 17-4-9
 */
public class HeWeatherItem {
    public Aqi aqi;
    public Basic basic;
    public List<DailyForecastItem> daily_forecast;
    public List<HourlyForecastItem> hourly_forecast;
    public Now now;
    public String status;
    public Suggestion suggestion;

    @Override
    public String toString() {
        return "HeWeatherItem{" +
                "aqi=" + aqi +
                ", basic=" + basic +
                ", dailyForecast=" + daily_forecast +
                ", hourlyForecast=" + hourly_forecast +
                ", now=" + now +
                ", status='" + status + '\'' +
                ", suggestion=" + suggestion +
                '}';
    }
}