package dong.lan.weather.bean.weather;

/**
 * Author: dooze
 * Created by: ModelGenerator on 17-4-9
 */
public class HourlyForecastItem {
    public Cond cond;
    public String date;
    public String hum;
    public String pop;
    public String pres;
    public String tmp;
    public Wind wind;

    @Override
    public String toString() {
        return "HourlyForecastItem{" +
                "cond=" + cond +
                ", date='" + date + '\'' +
                ", hum='" + hum + '\'' +
                ", pop='" + pop + '\'' +
                ", pres='" + pres + '\'' +
                ", tmp='" + tmp + '\'' +
                ", wind=" + wind +
                '}';
    }
}