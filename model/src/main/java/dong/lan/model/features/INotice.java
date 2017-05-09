package dong.lan.model.features;

/**
 * Created by 梁桂栋 on 17-4-6 ： 下午3:23.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface INotice {


    String getObjId();
    String getId();
     long getCreateTime();

     String getCreatorId();

     String getTravelId();

     String getContent();
}
