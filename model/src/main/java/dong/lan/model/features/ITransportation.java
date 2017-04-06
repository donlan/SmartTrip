package dong.lan.model.features;

/**
 * Created by 梁桂栋 on 17-4-2 ： 下午2:45.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface ITransportation {

    String getObjId();

    public String getInfo();


    public int getType();


    public String getCode();


    public String getStartCity();


    public String getEndCity();


    public long getStartTime();


    public long getEndTime();

}
