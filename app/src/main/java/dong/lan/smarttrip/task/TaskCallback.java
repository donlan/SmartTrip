package dong.lan.smarttrip.task;

/**
 * Created by 梁桂栋 on 17-3-23 ： 下午6:22.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface TaskCallback<T> {
    void onTackCallback(T data);
}
