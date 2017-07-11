package dong.lan.weather.bean;

import dong.lan.weather.bean.DayWeather;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 梁桂栋 on 17-7-10 ： 下午9:13.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class WeatherDay extends RealmObject implements DayWeather {
    @PrimaryKey
    private String date;
    private String cond;
    private String tmp;
    private String wind;

    public WeatherDay() {
    }

    public WeatherDay(String date, String cond, String tmp, String wind) {
        this.date = date;
        this.cond = cond;
        this.tmp = tmp;
        this.wind = wind;
    }

    @Override
    public String date() {
        return date;
    }

    @Override
    public String cond() {
        return cond;
    }

    @Override
    public String tmp() {
        return tmp;
    }

    @Override
    public String wind() {
        return wind;
    }
}
