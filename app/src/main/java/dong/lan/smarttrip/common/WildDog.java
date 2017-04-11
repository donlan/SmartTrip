package dong.lan.smarttrip.common;

import android.content.Context;

import com.wilddog.client.SyncReference;
import com.wilddog.client.WilddogSync;
import com.wilddog.wilddogcore.WilddogApp;
import com.wilddog.wilddogcore.WilddogOptions;

/**
 * Created by 梁桂栋 on 17-1-31 ： 下午8:10.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class WildDog {
    private WildDog() {
    }

    private static WildDog wildDog;

    public static WildDog instance() {
        if (wildDog == null)
            wildDog = new WildDog();
        return wildDog;
    }

    public void init(Context appContext) {
        WilddogOptions options = new WilddogOptions.Builder()
                .setSyncUrl("https://smart01.wilddogio.com").build();
        WilddogApp.initializeApp(appContext, options);
        WilddogSync.getInstance().setPersistenceEnabled(true);
    }

    public SyncReference reference() {
        return WilddogSync.getInstance().getReference();
    }
    public SyncReference reference(String path) {
        return WilddogSync.getInstance().getReference(path);
    }


}
