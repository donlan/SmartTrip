package dong.lan.avoscloud;

import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;

import dong.lan.avoscloud.model.AVODocument;
import dong.lan.avoscloud.model.AVOGather;
import dong.lan.avoscloud.model.AVOGatherReply;
import dong.lan.avoscloud.model.AVONode;
import dong.lan.avoscloud.model.AVONotice;
import dong.lan.avoscloud.model.AVONoticeReply;
import dong.lan.avoscloud.model.AVOTourist;
import dong.lan.avoscloud.model.AVOUser;
import dong.lan.avoscloud.model.AVOTransportation;
import dong.lan.avoscloud.model.AVOTravel;

/**
 * Created by 梁桂栋 on 17-3-31 ： 下午10:26.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public final class AVOConfig {
    private static final String API_ID = "XoDjoDoOw5L9WWmBGWDlVQlw-9Nh9j0Va";
    private static final String API_KEY = "yuy2tUWq91IxCGU4U88oiamc";

    public static void init(Context appContext) {

        AVObject.registerSubclass(AVOTransportation.class);
        AVObject.registerSubclass(AVODocument.class);
        AVObject.registerSubclass(AVONode.class);
        AVObject.registerSubclass(AVOTravel.class);
        AVObject.registerSubclass(AVOTourist.class);
        AVObject.registerSubclass(AVOGather.class);
        AVObject.registerSubclass(AVONotice.class);
        AVObject.registerSubclass(AVOGatherReply.class);
        AVObject.registerSubclass(AVONoticeReply.class);
        AVUser.alwaysUseSubUserClass(AVOUser.class);

        AVOSCloud.initialize(appContext, API_ID, API_KEY);
        AVOSCloud.setDebugLogEnabled(true);
    }

    public static void signUp(String identifier, SignUpCallback callback) {
        AVOUser user = new AVOUser();
        user.setUsername(identifier);
        user.setPassword(identifier);
        user.setIdentifier(identifier);
        user.signUpInBackground(callback);
    }

    public static void login(String identifier, LogInCallback<AVUser> callback) {
        AVOUser.logInInBackground(identifier, identifier, callback);
    }
}
