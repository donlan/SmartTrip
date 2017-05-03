package dong.lan.smarttrip.common;

import com.tencent.TIMUserProfile;
import com.tencent.qcloud.presentation.event.FriendshipEvent;

import java.util.List;

import dong.lan.avoscloud.model.AVOUser;
import dong.lan.model.BeanConvert;
import dong.lan.model.bean.user.User;
import dong.lan.smarttrip.model.factory.UserFactory;
import io.realm.Realm;

/**
 * Created by 梁桂栋 on 2017/5/3.
 * Email: 760625325@qq.com
 * Github: github.com/donlan
 */

public final class FriendShipHandler {

    private static FriendShipHandler handler;

    private FriendShipHandler() {
        //no instance
    }

    public static FriendShipHandler getInstance() {
        if (handler == null) {
            handler = new FriendShipHandler();
        }
        return handler;
    }


    public void handler(final FriendshipEvent.NotifyCmd data) {
        FriendshipEvent.NotifyType type = data.type;
        if (type == FriendshipEvent.NotifyType.ADD) { //好友添加
            Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    List<TIMUserProfile> list = (List<TIMUserProfile>) data.data;
                    for (TIMUserProfile profile : list) {
                        User user = UserFactory.createRealmUser(profile);
                        realm.copyToRealmOrUpdate(user);
                        AVOUser avoUser = BeanConvert.toAvoUser(user);
                        UserManager.instance().currentAvoUser().addFriend(avoUser);
                    }
                }
            });

        } else if (type == FriendshipEvent.NotifyType.DEL) {//删除好友
            final List<String> ids = (List<String>) data.data;
            Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (String id : ids) {
                        User user = realm.where(User.class).equalTo("identifier", id).findFirst();
                        if (user != null) {
                            user.deleteFromRealm();
                            UserManager.instance().currentAvoUser().removeFriend(BeanConvert.toAvoUser(user));
                        }
                    }
                }
            });

        }
    }
}
