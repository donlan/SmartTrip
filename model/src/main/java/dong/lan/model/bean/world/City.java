package dong.lan.model.bean.world;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.github.promeg.pinyinhelper.Pinyin;

import dong.lan.model.features.ItemTextDisplay;
import io.realm.RealmObject;

/**
 * Created by 梁桂栋 on 17-3-6 ： 上午11:22.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class City extends RealmObject implements Comparable<City> ,ItemTextDisplay {
    String id;
    String origin;
    String name;
    String pinyin;


    public City() {
    }

    public City(String id, String origin, String name) {
        this.id = id;
        this.origin = origin;
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        pinyin = Pinyin.toPinyin(name,"").toUpperCase();
    }


    @Override
    public String toString() {
        return "City{" +
                "id='" + id + '\'' +
                ", origin='" + origin + '\'' +
                ", name='" + name + '\'' +
                ", pinyin='" + pinyin + '\'' +
                '}';
    }

    @Override
    public String display() {
        if(TextUtils.isEmpty(pinyin))
            return Pinyin.toPinyin(name,"").substring(0,1);
        return pinyin.substring(0,1);
    }

    @Override
    public int compareTo(@NonNull City o) {
        if (this == o)
            return 0;
        return this.pinyin.compareTo(o.pinyin);
    }
}
