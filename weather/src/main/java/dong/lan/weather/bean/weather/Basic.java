package dong.lan.weather.bean.weather;

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
    public Update update;

    @Override
    public String toString() {
        return "Basic{" +
                "weatherCity='" + city + '\'' +
                ", cnty='" + cnty + '\'' +
                ", id='" + id + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", update=" + update +
                '}';
    }
}