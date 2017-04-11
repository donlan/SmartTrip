package dong.lan.weather.bean.weather;

/**
 * Author: dooze
 * Created by: ModelGenerator on 17-4-9
 */
public class DailyForecastItem {
    public Astro astro;
    public Cond cond;
    public String date;
    public String hum;
    public String pcpn;
    public String pop;
    public String pres;
    public Tmp tmp;
    public String uv;
    public String vis;
    public Wind wind;

    @Override
    public String toString() {
        return "DailyForecastItem{" +
                "astro=" + astro +
                ", cond=" + cond +
                ", date='" + date + '\'' +
                ", hum='" + hum + '\'' +
                ", pcpn='" + pcpn + '\'' +
                ", pop='" + pop + '\'' +
                ", pres='" + pres + '\'' +
                ", tmp=" + tmp +
                ", uv='" + uv + '\'' +
                ", vis='" + vis + '\'' +
                ", wind=" + wind +
                '}';
    }
}