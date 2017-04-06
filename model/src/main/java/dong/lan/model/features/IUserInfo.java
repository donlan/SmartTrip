package dong.lan.model.features;

/**
 * Created by 梁桂栋 on 17-3-22 ： 下午8:14.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface IUserInfo extends IUser, ITourist, Comparable<IUserInfo> {

    int getSex();

    String displayName();
}
