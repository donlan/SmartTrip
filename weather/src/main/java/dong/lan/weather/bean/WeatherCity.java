package dong.lan.weather.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 梁桂栋 on 17-7-8 ： 下午5:36.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class WeatherCity extends RealmObject{

    @PrimaryKey
    public String id;
    public String letter;
    public String name;

    public WeatherCity() {
    }

    public WeatherCity(String id, String letter, String name) {
        this.id = id;
        this.letter = letter;
        this.name = name;
    }
}
