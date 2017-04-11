package dong.lan.smarttrip.model.wilddog;

import com.wilddog.client.SyncReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dong.lan.model.Config;
import dong.lan.smarttrip.common.UserManager;
import dong.lan.smarttrip.common.WildDog;
import dong.lan.model.bean.travel.Node;
import dong.lan.model.bean.travel.Transportation;

/**
 * Created by 梁桂栋 on 17-3-8 ： 下午2:35.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class WDNode implements IBaseWildDog {

    int no;             //编号
    String tag;         //travel id
    long createTime;
    String country;         //国家
    String city;            //城市
    String address;
    double latitude;
    double longitude;
    long arrivedTime;
    long remainTime;
    long stayTime;
    boolean isRequired;     //是否必选
    int teamType;       //组队方式
    String description;     //节点描述信息
    List<Transportation> transportations;

    public WDNode(Node node) {
        no = node.getNo();
        tag = node.getTag();
        createTime = node.getCreateTime();
        country = node.getCountry();
        city = node.getCity();
        address = node.getAddress();
        latitude = node.getLatitude();
        longitude = node.getLongitude();
        arrivedTime = node.getArrivedTime();
        remainTime = node.getRemainTime();
        stayTime = node.getStayTime();
        isRequired = node.isRequired();
        teamType = node.getTeamType();
        description = node.getDescription();
        transportations = node.getTransportations();
    }

    public WDNode(Map<String, Object> map) {
        no = ((Number) map.get("no")).intValue();
        tag = map.get("tag").toString();
        createTime = ((Number) map.get("createTime")).longValue();
        country = map.get("country") == null ? "" : map.get("country").toString();
        city = map.get("city") == null ? "" : map.get("city").toString();
        address = map.get("address").toString();
        latitude = ((Number) map.get("latitude")).doubleValue();
        longitude = ((Number) map.get("longitude")).doubleValue();
        arrivedTime = ((Number) map.get("arrivedTime")).longValue();
        remainTime = ((Number) map.get("remainTime")).longValue();
        stayTime = ((Number) map.get("stayTime")).longValue();
        isRequired = ((Boolean) map.get("isRequired"));
        teamType = ((Number) map.get("teamType")).intValue();
        description = map.get("description").toString();
        Object object = map.get("transportations");
        List<Transportation> transportations = new ArrayList<>();
        if (object != null) {
            ArrayList<Object> list = (ArrayList<Object>) object;
            for(Object obj : list){
                transportations.add(new Transportation((Map<String, Object>) obj));
            }
        }
        this.transportations = transportations;
    }


    public int getNo() {
        return no;
    }

    public String getTag() {
        return tag;
    }

    public long getCreateTime() {
        return createTime;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getArrivedTime() {
        return arrivedTime;
    }

    public long getRemainTime() {
        return remainTime;
    }

    public long getStayTime() {
        return stayTime;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public int getTeamType() {
        return teamType;
    }

    public String getDescription() {
        return description;
    }

    public List<Transportation> getTransportations() {
        return transportations;
    }

    @Override
    public void save(SyncReference.CompletionListener callback) {
        WildDog.instance().reference("users/" + UserManager.instance().identifier() +
                "/travels/" + Config.checkId2Key(tag) + "/nodes/"
                + createTime).setValue(this);
    }

    @Override
    public void delete(SyncReference.CompletionListener callback) {
        WildDog.instance().reference("users/" + UserManager.instance().identifier() +
                "/travels/" + Config.checkId2Key(tag) + "/nodes/"
                + createTime).removeValue(callback);
    }
}
