package dong.lan.model.bean.travel;


import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import dong.lan.model.features.INode;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by 梁桂栋 on 16-10-12 ： 下午2:29.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: 旅行的一个节点
 */
@RealmClass
public class Node extends RealmObject implements Serializable, Comparator<Node> ,INode{

    Travel travel;      //节点对应的旅行
    String objId;       //后台Node表的objectId
    int no;             //编号,用于表明节点的顺序(1....n)
    String tag;         //实际保存的Travel 的 id
    long createTime;    //创建时间
    String country;     //国家(非必须)
    String city;        //城市(非必须)
    String address;     //节点地址描述信息
    double latitude;    //节点的维度(必须)
    double longitude;   //节点的经度(必须)
    long arrivedTime;   //到达时间
    long remainTime;    //停留时间
    long stayTime;      //预留时间


    boolean isRequired; //是否必选(目前没有涉及这个功能)
    int teamType;       //组队方式(目前没有涉及)
    String description; //节点描述信息

    RealmList<Transportation> transportations; //节点交通方式




    public Node() {
    }


    public Travel getTravel() {
        return travel;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }

    @Override
    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getArrivedTime() {
        return arrivedTime;
    }

    public void setArrivedTime(long arrivedTime) {
        this.arrivedTime = arrivedTime;
    }

    public long getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(long remainTime) {
        this.remainTime = remainTime;
    }

    public long getStayTime() {
        return stayTime;
    }

    public void setStayTime(long stayTime) {
        this.stayTime = stayTime;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public int getTeamType() {
        return teamType;
    }

    public void setTeamType(int teamType) {
        this.teamType = teamType;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Transportation> getTransportations() {
        return transportations;
    }

    public void setTransportations(RealmList<Transportation> transportations) {
        this.transportations = transportations;
    }

    @Override
    public int compare(Node o1, Node o2) {
        if (o1 == o2)
            return 0;
        if (o1.arrivedTime < o2.arrivedTime)
            return -1;
        if (o1.arrivedTime > o2.arrivedTime)
            return 1;
        return 0;
    }


    @Override
    public String toString() {
        return "Node{" +
                "no=" + no +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", arrivedTime=" + arrivedTime +
                ", remainTime=" + remainTime +
                ", stayTime=" + stayTime +
                ", isRequired=" + isRequired +
                ", teamType=" + teamType +
                ", description='" + description + '\'' +
                ", transportations=" + transportations +
                '}';
    }
}
