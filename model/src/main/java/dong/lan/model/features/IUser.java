package dong.lan.model.features;

import android.content.Context;

import dong.lan.model.permission.IRole;

/**
 * Created by 梁桂栋 on 17-2-25 ： 下午1:38.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface IUser extends IRole ,ItemTextDisplay{

    String getObjId();

    String getUsername();

    String getIdentifier();

    String getAvatarUrl();

    String getPinYin();

    void call(Context context);

    void sms(Context context);
}
