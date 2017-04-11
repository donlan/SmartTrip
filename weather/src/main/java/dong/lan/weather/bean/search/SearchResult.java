package dong.lan.weather.bean.search;

import java.util.List;

/**
 * Author: dooze
 * Created by: ModelGenerator on 17-4-9
 */
public class SearchResult {
    public List<HeWeatherItem> HeWeather5;

    @Override
    public String toString() {
        return "SearchResult{" +
                "HeWeather=" + HeWeather5 +
                '}';
    }
}