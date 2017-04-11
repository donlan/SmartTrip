package dong.lan.filecloud;

/**
 * Created by 梁桂栋 on 17-2-4 ： 下午1:06.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface CallBack<T> {
    void callback(int status,T listener);
}
