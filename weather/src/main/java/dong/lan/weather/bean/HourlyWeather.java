package dong.lan.weather.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 梁桂栋 on 17-7-10 ： 下午8:26.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class HourlyWeather extends RealmObject implements WeatherHourly {

    @PrimaryKey
    private String time;
    private String tmp;
    private String cond;
    private String windDir;
    private String windInfo;

    public HourlyWeather() {
    }

    public HourlyWeather(String time, String tmp, String cond, String windDir, String windInfo) {
        this.time = time;
        this.tmp = tmp;
        this.cond = cond;
        this.windDir = windDir;
        this.windInfo = windInfo;
    }

    @Override
    public String tmp() {
        return tmp;
    }

    @Override
    public String cond() {
        return cond;
    }

    @Override
    public String windDir() {
        return windDir;
    }

    @Override
    public String windInfo() {
        return windInfo;
    }

    @Override
    public String time() {
        return time;
    }
}
