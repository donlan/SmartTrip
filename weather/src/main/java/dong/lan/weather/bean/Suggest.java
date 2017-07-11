package dong.lan.weather.bean;

import io.realm.RealmObject;

/**
 * Created by 梁桂栋 on 17-7-10 ： 下午2:10.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class Suggest extends RealmObject implements WeatherSuggest {

    private String type;
    private String level;
    private String desc;


    public Suggest() {
    }

    public Suggest(String type, String level, String desc) {
        this.type = type;
        this.level = level;
        this.desc = desc;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public String level() {
        return level;
    }

    @Override
    public String describer() {
        return desc;
    }
}
