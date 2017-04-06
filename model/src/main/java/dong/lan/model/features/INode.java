package dong.lan.model.features;

import java.util.List;

import dong.lan.model.bean.travel.Transportation;

/**
 * Created by 梁桂栋 on 17-4-2 ： 下午2:45.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface INode {

    String getObjId();

    public String getTag();


    public long getCreateTime();


    public String getAddress();


    public int getNo();


    public String getCountry();


    public String getCity();


    public double getLatitude();


    public double getLongitude();


    public long getArrivedTime();

    public long getRemainTime();


    public long getStayTime();


    public boolean isRequired();

    public int getTeamType();


    public String getDescription();

    public List<Transportation> getTransportations();

}
