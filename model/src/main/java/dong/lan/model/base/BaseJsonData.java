package dong.lan.model.base;


/**
 * Created by 梁桂栋 on 17-3-25 ： 下午10:35.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class BaseJsonData<T extends Data> implements JsonType<T> {
    private static final String TAG = BaseJsonData.class.getSimpleName();
    public int code;
    public T data;

    public BaseJsonData() {
    }

    public BaseJsonData(int code, T data) {
        this.code = code;
        this.data = data;
    }


    @Override
    public String toString() {
        return "BaseJsonData{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }


    @Override
    public T toTarget() {
        return (T) GsonHelper.getInstance().toTarget(data.toJson(),data.getClass());
    }

    @Override
    public String toJson() {
        return GsonHelper.getInstance().toJson(this);
    }
}
