package dong.lan.smarttrip.common.features;

import dong.lan.model.bean.user.User;

/**
 * Created by 梁桂栋 on 17-2-22 ： 下午12:52.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface IUserFunc {

    void initUser(User user);

    User currentUser();

    String identifier();

    String faceUrl();
}
