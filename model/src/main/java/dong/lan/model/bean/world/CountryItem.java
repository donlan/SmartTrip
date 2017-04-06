package dong.lan.model.bean.world;

import java.util.List;

/**
 * Author: dooze
 * Created by: ModelGenerator on 17-3-5
 */
public class CountryItem {
    public String CountryID;
    public String CountryName;
    public List<StationItem> Station;

    @Override
    public String toString() {
        return "CountryItem{" +
                "CountryID='" + CountryID + '\'' +
                ", CountryName='" + CountryName + '\'' +
                ", Station=" + Station +
                '}';
    }
}