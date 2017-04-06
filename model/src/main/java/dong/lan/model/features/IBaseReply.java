package dong.lan.model.features;

import android.content.Context;

/**
 * Created by 梁桂栋 on 17-4-6 ： 下午6:32.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface IBaseReply {

    int STATUS_OK = 0;
    int STATUS_NO = 1;
    int STATUS_DELAY = 2;

    String faceUrl();

    String displayName();

    String tips();

    void call(Context context);

    void sms(Context context);

}
