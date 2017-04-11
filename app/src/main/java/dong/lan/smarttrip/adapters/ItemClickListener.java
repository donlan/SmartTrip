package dong.lan.smarttrip.adapters;

/**
 * Created by 梁桂栋 on 16-10-13 ： 下午9:46.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface ItemClickListener<T> {
    void onItemClick(T data);
    void onItemLongClick(T data);
}
