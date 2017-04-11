package dong.lan.weather.bean.weather;

/**
 * Author: dooze
 * Created by: ModelGenerator on 17-4-9
 */
public class Now {
    public Cond cond;
    public String fl;
    public String hum;
    public String pcpn;
    public String pres;
    public String tmp;
    public String vis;
    public Wind wind;

    @Override
    public String toString() {
        return "Now{" +
                "cond=" + cond +
                ", fl='" + fl + '\'' +
                ", hum='" + hum + '\'' +
                ", pcpn='" + pcpn + '\'' +
                ", pres='" + pres + '\'' +
                ", tmp='" + tmp + '\'' +
                ", vis='" + vis + '\'' +
                ", wind=" + wind +
                '}';
    }
}