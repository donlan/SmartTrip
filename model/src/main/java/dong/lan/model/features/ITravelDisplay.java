package dong.lan.model.features;

import android.content.Context;

/**
 * Created by 梁桂栋 on 17-3-31 ： 下午5:54.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface ITravelDisplay {

    String imageUrl();

    String name();

    void jumpToDetail(Context context);

    void delete();

    int unread();
}
