package dong.lan.model.bean.notice;

import android.content.Context;

/**
 * Created by 梁桂栋 on 17-3-26 ： 下午1:53.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface NoticeShow {

    int TYPE_GATHER = 1;
    int TYPE_NOTICE = 2;

    String getShowTittle();

    String getShowContent();

    void jump(Context context);

    String showExtras();

    int type();

    void delete();



}
