package dong.lan.model.features;



/**
 * Created by 梁桂栋 on 17-4-6 ： 下午2:38.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface IGather {

    String getObjId();
    public String getId() ;

    public long getCreateTime() ;

    public String getCreatorId() ;

    public String getTravelId() ;

    public String getAddress() ;

    public double getLatitude() ;

    public double getLongitude() ;

    public long getTime() ;

    public String getContent() ;

}
