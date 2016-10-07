package dong.lan.smarttrip;

import android.app.Application;
import android.content.Context;

import com.tencent.TIMGroupReceiveMessageOpt;
import com.tencent.TIMManager;
import com.tencent.TIMOfflinePushListener;
import com.tencent.TIMOfflinePushNotification;
import com.tencent.qalsdk.sdk.MsfSdkUtils;

import dong.lan.smarttrip.utils.Foreground;

/**
 * Created by 梁桂栋 on 2016/9/13.
 * Email: 760625325@qq.com
 * Github: github.com/donlan
 */
public class MyApplication extends Application {

    /*

     */
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Foreground.init(this);
        if(MsfSdkUtils.isMainProcess(this)){
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification) {
                    if(notification.getGroupReceiveMsgOpt()== TIMGroupReceiveMessageOpt.ReceiveAndNotify){
                        notification.doNotify(context, R.mipmap.ic_app);
                    }
                }
            });
        }
    }
    public static Context getContext(){
        return context;
    }
}
