package dong.lan.smarttrip.model;

import io.realm.RealmObject;

/**
 * Created by 梁桂栋 on 17-2-23 ： 下午2:10.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class Location extends RealmObject {
    String name;
    String addr;

    public Location() {
    }

    public Location(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
