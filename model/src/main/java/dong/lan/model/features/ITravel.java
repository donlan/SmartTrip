package dong.lan.model.features;

import java.util.List;

import dong.lan.model.bean.travel.Document;
import dong.lan.model.bean.travel.Node;
import dong.lan.model.bean.user.Tourist;

/**
 * Created by 梁桂栋 on 17-2-27 ： 下午1:51.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */
public interface ITravel extends ITravelDisplay {


    String getObjId();
    String getTag();

    String getId();

    String getName();

    String getImageUrl();

    String getIntroduce();

    String getDestinations();

    int getUnread();

    long getCreateTime();

    long getStartTime();

    long getEndTime();


    int getPermission(IUserInfo tourist);

    List<Node> getNodes();


}
