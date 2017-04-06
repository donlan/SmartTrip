package dong.lan.avoscloud.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by 梁桂栋 on 17-4-2 ： 下午2:23.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */
@AVClassName("Transportation")
public class AVOTransportation extends AVObject {

    public String getInfo() {
        return getString("info");
    }

    public void setInfo(String info) {
        put("info",info);
    }

    public int getType() {
        return getInt("type");
    }

    public void setType(int type) {
        put("type",type);
    }

    public String getCode() {
        return getString("code");
    }

    public void setCode(String code) {
        put("code",code);
    }

    public String getStartCity() {
        return getString("startCity");
    }

    public void setStartCity(String startCity) {
        put("startCity",startCity);
    }

    public String getEndCity() {
        return getString("endCity");
    }

    public void setEndCity(String endCity) {
       put("endCity",endCity);
    }

    public long getStartTime() {
        return getLong("startTime");
    }

    public void setStartTime(long startTime) {
       put("startTime",startTime);
    }

    public long getEndTime() {
        return getLong("endTime");
    }

    public void setEndTime(long endTime) {
        put("endTime",endTime);
    }
}
