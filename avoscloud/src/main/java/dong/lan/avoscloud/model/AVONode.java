package dong.lan.avoscloud.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;

import java.util.List;

/**
 * Created by 梁桂栋 on 17-4-2 ： 下午2:02.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

@AVClassName("Node")
public class AVONode extends AVObject {


    public void setTravel(AVOTravel travel){
        put("travel",travel);
    }

    public AVOTravel getTravel(){
        try {
            return getAVObject("travel",AVOTravel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public int getNo() {
        return getInt("no");
    }

    public void setNo(int no) {
       put("no",no);
    }

    public String getTag() {
        return getString("tag");
    }

    public void setTag(String tag) {
       put("tag",tag);
    }

    public long getCreateTime() {
        return getLong("createTime");
    }

    public void setCreateTime(long createTime) {
        put("createTime",createTime);
    }

    public String getCountry() {
        return getString("country");
    }

    public void setCountry(String country) {
        put("country",country);
    }

    public String getCity() {
        return getString("city");
    }

    public void setCity(String city) {
       put("city",city);
    }

    public String getAddress() {
        return getString("address");
    }

    public void setAddress(String address) {
      put("address",address);
    }

    public AVGeoPoint getLocation() {
        return getAVGeoPoint("location");
    }

    public void setLocation(AVGeoPoint point) {
        put("location", point);
    }

    public void setLocation(double lat, double lng) {
        put("location", new AVGeoPoint(lat, lng));
    }


    public long getArrivedTime() {
        return getLong("arrivedTime");
    }

    public void setArrivedTime(long arrivedTime) {
       put("arrivedTime",arrivedTime);
    }

    public long getRemainTime() {
        return getLong("remainTime");
    }

    public void setRemainTime(long remainTime) {
       put("remainTime",remainTime);
    }

    public long getStayTime() {
        return getLong("stayTime");
    }

    public void setStayTime(long stayTime) {
       put("stayTime",stayTime);
    }

    public boolean isRequired() {
        return getBoolean("isRequired");
    }

    public void setRequired(boolean required) {
        put("isRequired",required);
    }

    public int getTeamType() {
        return getInt("teamType");
    }

    public void setTeamType(int teamType) {
        put("teamType",teamType);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
       put("description",description);
    }

    public List<AVOTransportation> getTransportations() {
        return getList("transportations",AVOTransportation.class);
    }

    public void setTransportations(List<AVOTransportation> transportations) {
       put("transportations",transportations);
    }
}
