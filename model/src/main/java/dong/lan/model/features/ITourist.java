package dong.lan.model.features;

import dong.lan.model.permission.IRole;

/**
 * Created by 梁桂栋 on 17-2-28 ： 上午10:54.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface ITourist extends IRole {

    IUser getUser();

    int getRole();

    int getStatus();
}
