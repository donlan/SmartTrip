package dong.lan.model.base;

import android.content.Context;

/**
 * Created by 梁桂栋 on 17-4-4 ： 上午12:33.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class App {
    private App(){}
    private static Context mAppContext;
    private static App app;
    public static App myApp(Context appContext){
        if(app == null)
            app = new App();
        mAppContext = appContext;
        return app;
    }
    public static Context myApp(){
        return mAppContext;
    }
}
