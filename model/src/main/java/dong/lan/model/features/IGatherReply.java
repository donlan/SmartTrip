package dong.lan.model.features;


/**
 * Created by 梁桂栋 on 17-4-6 ： 下午3:04.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface IGatherReply extends IBaseReply{

    String getObjId();

    IGather getGather();

    IUserInfo getTourist();

    long getReplyTime();

    int getStatus();

    double getLatitude();

    double getLongitude();
}
