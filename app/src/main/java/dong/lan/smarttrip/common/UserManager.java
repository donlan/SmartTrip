package dong.lan.smarttrip.common;

import com.avos.avoscloud.AVUser;
import com.tencent.TIMCallBack;
import com.tencent.qcloud.presentation.business.LoginBusiness;
import com.tencent.qcloud.tlslibrary.service.TlsBusiness;

import dong.lan.model.Config;
import dong.lan.avoscloud.Secure;
import dong.lan.smarttrip.common.features.IGroupFunc;
import dong.lan.smarttrip.common.features.IUserFunc;
import dong.lan.model.bean.user.User;
import com.tencent.qcloud.presentation.business.UserInfo;

/**
 * Created by 梁桂栋 on 17-1-10 ： 下午10:54.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class UserManager implements IGroupFunc, IUserFunc {
    private User user;
    private static UserManager manager;

    public static UserManager instance() {
        if (manager == null)
            manager = new UserManager();
        return manager;
    }


    public static boolean isFirst() {
        boolean isFirst = SPHelper.instance().getBoolean(Config.SP_IS_FIRST);
        return !isFirst;
    }

    public static void needLoadingPager(boolean need) {
        SPHelper.instance().putBoolean(Config.SP_IS_FIRST, !need);
    }

    @Override
    public String createGroupId(String username, String groupName) {

        String src = String.valueOf(username.hashCode() ^ groupName.hashCode() ^ System.currentTimeMillis());
        return Secure.MD5(src);
    }

    @Override
    public void initUser(User user) {
        this.user = null;
        this.user = user;
    }

    @Override
    public User currentUser() {
        return user;
    }

    @Override
    public String identifier() {
        return UserInfo.getInstance().getId();
    }

    @Override
    public String faceUrl() {
        return null;
    }

    public void logout() {
        LoginBusiness.logout(new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
            }

            @Override
            public void onSuccess() {
            }
        });
        TlsBusiness.logout(identifier());
        AVUser.logOut();
    }
}
