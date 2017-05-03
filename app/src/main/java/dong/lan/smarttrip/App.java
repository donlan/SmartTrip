package dong.lan.smarttrip;

import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.SDKInitializer;
import com.tencent.TIMGroupReceiveMessageOpt;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMOfflinePushListener;
import com.tencent.TIMOfflinePushNotification;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.tencent.qcloud.presentation.event.FriendshipEvent;
import com.tencent.qcloud.presentation.event.GroupEvent;
import com.tencent.qcloud.presentation.event.MessageEvent;

import java.util.Observable;
import java.util.Observer;

import dong.lan.avoscloud.AVOConfig;
import dong.lan.map.service.LocationService;
import dong.lan.model.Config;
import dong.lan.model.RealmInit;
import dong.lan.smarttrip.base.DelayInitView;
import dong.lan.smarttrip.common.CustomMessageHandler;
import dong.lan.smarttrip.common.FriendShipHandler;
import dong.lan.smarttrip.common.GroupHandler;
import dong.lan.smarttrip.common.SPHelper;
import dong.lan.smarttrip.utils.Foreground;

/**
 * Created by 梁桂栋 on 2016/9/13.
 * Email: 760625325@qq.com
 * Github: github.com/donlan
 */
public class App extends MultiDexApplication implements DelayInitView<Integer>, Observer {

    private static final String TAG = "Application";
    private static App context;

    //百度地图定位服务
    private LocationService locationService;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        //阿里 ARoute
//        ARouter.openLog();
//        ARouter.openDebug();
//        ARouter.init(this);

        //LeanCode
        AVOConfig.init(this);

        //野狗
        //WildDog.instance().init(this);

        //腾讯云对象存储
        // COS.instance().init(this);

        //腾讯云通信
        Foreground.init(this);
        if (MsfSdkUtils.isMainProcess(this)) {
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification) {
                    if (notification.getGroupReceiveMsgOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify) {
                        notification.doNotify(context, R.drawable.logo);
                    }
                }
            });
        }

        //百度地图
        SDKInitializer.initialize(this);

        //Realm
        RealmInit.init(this);

        //SharePreference
        SPHelper.instance().init(this, Config.DEFAULT_SP);

    }


    public LocationService locationService() {
        if (locationService == null)
            locationService = new LocationService(this);
        return locationService;
    }

    public static App myApp() {
        return context;
    }

    @Override
    public void start(Integer data) {
        MessageEvent.getInstance().addObserver(this);
        GroupEvent.getInstance().addObserver(this);
        FriendshipEvent.getInstance().addObserver(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof MessageEvent) {
            TIMMessage msg = (TIMMessage) data;
            if (msg == null)
                return;
            //只对自定义消息进行处理
            CustomMessageHandler.instance().handler(msg);
        } else if (observable instanceof GroupEvent) {
            GroupHandler.instance().handler((GroupEvent.NotifyCmd) data);
        } else if (observable instanceof FriendshipEvent) {
            FriendShipHandler.getInstance().handler((FriendshipEvent.NotifyCmd) data);
        }
    }
}
