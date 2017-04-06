package dong.lan.model.base;

/**
 * Created by 梁桂栋 on 17-3-24 ： 下午3:50.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface JsonType<T extends Data> {

    T toTarget();

    String toJson();
}
