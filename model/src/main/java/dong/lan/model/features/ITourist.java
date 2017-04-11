package dong.lan.model.features;

import dong.lan.model.permission.IRole;

/**
 * Created by 梁桂栋 on 17-2-28 ： 上午10:54.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface ITourist extends IRole {

    int STATUS_NORMAL = 0;
    int STATUS_OFFLINE =1;

    IUser getUser();

    int getRole();

    int getStatus();
}
