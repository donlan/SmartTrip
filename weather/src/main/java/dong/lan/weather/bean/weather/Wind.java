package dong.lan.weather.bean.weather;

/**
 * Author: dooze
 * Created by: ModelGenerator on 17-4-9
 */
public class Wind {
    public String deg;
    public String dir;
    public String sc;
    public String spd;

    @Override
    public String toString() {
        return "Wind{" +
                "deg='" + deg + '\'' +
                ", dir='" + dir + '\'' +
                ", sc='" + sc + '\'' +
                ", spd='" + spd + '\'' +
                '}';
    }
}