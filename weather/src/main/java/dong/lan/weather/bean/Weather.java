package dong.lan.weather.bean;

import java.util.List;

/**
 * Created by 梁桂栋 on 17-7-8 ： 下午4:01.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface Weather {

    String id();

    long lastQueryTime();

    String currentTmp();

    String tmpRang();

    String cond();

    String windDir();

    String windInfo();

    String city();

    List<WeatherHourly> hourlyWeather();

    List<WeatherSuggest> suggestWeather();

    List<DayWeather> dayWeather();


}
