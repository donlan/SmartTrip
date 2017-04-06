package dong.lan.model.features;

import dong.lan.model.bean.user.User;

/**
 * Created by 梁桂栋 on 17-4-6 ： 下午2:40.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface IReply {

    public int getType() ;

    public IUserInfo getUser() ;

    public long getTime() ;

    public int getStatus();

    public double getLatitude() ;

    public double getLongitude();
}
