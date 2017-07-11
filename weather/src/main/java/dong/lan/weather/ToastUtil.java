package dong.lan.weather;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 梁桂栋 on 17-7-9 ： 下午4:48.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class ToastUtil {
    private ToastUtil() {
        //no instance
    }

    public static void show(Context context ,String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }
}
