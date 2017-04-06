package dong.lan.model.bean.travel;


import java.io.Serializable;
import java.util.Map;

import dong.lan.model.features.ITransportation;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by 梁桂栋 on 16-11-19 ： 下午8:36.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: 交通方式封装
 */

@RealmClass
public class Transportation extends RealmObject implements Serializable ,ITransportation{
    public static final int TYPE_WALK = 0;      //步行
    public static final int TYPE_CAR = 1;       //汽车
    public static final int TYPE_TRAIN = 2;     //火车
    public static final int TYPE_PLANE = 3;     //飞机
    public static final int TYPE_BOAT = 4;      //船
    public static final int TYPE_BUS = 5;        //公交车

    String objId;       //后台Transportation表的objectId
    String info;        //该交通方式的说明信息
    int type;           //交通方式(目前就三种:飞机,火车,自由行)
    String code;        //如果交通方式为飞机火车,则用来保存对应的航班号或者火车列次
    String startCity;   //起始城市
    String endCity;     //目的城市
    long startTime;     //出发时间
    long endTime;       //到达时间

    public Transportation() {
    }

    public Transportation(Map<String, Object> mapVal) {
        info = mapVal.get("info") == null ? "" : mapVal.get("info").toString();
        type = ((Number) mapVal.get("type")).intValue();
        code = mapVal.get("code") == null ? "" : mapVal.get("code").toString();
        startCity = mapVal.get("startCity").toString();
        endCity = mapVal.get("endCity").toString();
        startTime = ((Number) mapVal.get("startTime")).longValue();
        endTime = ((Number) mapVal.get("endTime")).longValue();
    }

    @Override
    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getEndCity() {
        return endCity;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
