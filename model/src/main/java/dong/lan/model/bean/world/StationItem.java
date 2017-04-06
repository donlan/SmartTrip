package dong.lan.model.bean.world;

import java.util.List;

/**
 * Author: dooze
 * Created by: ModelGenerator on 17-3-5
 */
public class StationItem {
    public String StationID;
    public String StationName;
    public List<CityItem> City;

    @Override
    public String toString() {
        return "StationItem{" +
                "StationID='" + StationID + '\'' +
                ", StationName='" + StationName + '\'' +
                ", City=" + City +
                '}';
    }
}