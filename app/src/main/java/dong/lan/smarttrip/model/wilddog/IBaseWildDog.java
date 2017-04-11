package dong.lan.smarttrip.model.wilddog;

import com.wilddog.client.SyncReference;

/**
 * Created by 梁桂栋 on 17-2-27 ： 下午4:01.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface IBaseWildDog {

    void save(SyncReference.CompletionListener callback);

    void delete(SyncReference.CompletionListener callback);
}
