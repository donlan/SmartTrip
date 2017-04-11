package dong.lan.weather.bean.search;

/**
 * Author: dooze
 * Created by: ModelGenerator on 17-4-9
 */
public class Basic {
    public String city;
    public String cnty;
    public String id;
    public String lat;
    public String lon;
    public String prov;

    @Override
    public String toString() {
        return "Basic{" +
                "city='" + city + '\'' +
                ", cnty='" + cnty + '\'' +
                ", id='" + id + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", prov='" + prov + '\'' +
                '}';
    }
}