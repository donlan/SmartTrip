package dong.lan.smarttrip.presentation.presenter;

import android.content.Intent;
import android.util.Log;

import com.tencent.TIMManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMUserStatusListener;
import com.tencent.qcloud.presentation.event.FriendshipEvent;
import com.tencent.qcloud.presentation.event.MessageEvent;
import com.tencent.qcloud.tlslibrary.service.TlsBusiness;
import com.tencent.qcloud.ui.Dialog;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import dong.lan.model.Config;
import dong.lan.smarttrip.common.SPHelper;
import dong.lan.smarttrip.model.factory.UserFactory;
import dong.lan.smarttrip.model.im.FriendshipInfo;
import dong.lan.smarttrip.model.im.GroupInfo;
import dong.lan.model.bean.user.User;
import com.tencent.qcloud.presentation.business.UserInfo;
import dong.lan.smarttrip.presentation.viewfeatures.HomeView;
import dong.lan.smarttrip.ui.access.SplashActivity;
import dong.lan.smarttrip.ui.customview.DialogActivity;
import io.realm.Realm;

/**
 * Created by 梁桂栋 on 17-3-26 ： 下午11:41.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class HomePresenter implements Observer {

    private static final String TAG = HomePresenter.class.getSimpleName();
    private HomeView view;

    public HomePresenter(final HomeView view) {
        this.view = view;
        //互踢下线逻辑
        TIMManager.getInstance().setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                Log.d(TAG, "receive force offline message");
                Intent intent = new Intent(HomePresenter.this.view.activity(), DialogActivity.class);
                HomePresenter.this.view.activity().startActivity(intent);
            }

            @Override
            public void onUserSigExpired() {
                new Dialog(view.activity())
                        .setMessageText("票据过期，需要重新登录")
                        .setRightText("登录")
                        .setLeftText("退出")
                        .setClickListener(new Dialog.DialogClickListener() {
                            @Override
                            public boolean onDialogClick(int which) {
                                if (which == Dialog.CLICK_RIGHT) {
                                    logout();
                                } else {
                                    view.activity().finish();
                                }
                                return true;
                            }
                        });
            }
        });

    }

    private void logout() {
        TlsBusiness.logout(UserInfo.getInstance().getId());
        UserInfo.getInstance().setId(null);
        MessageEvent.getInstance().clear();
        FriendshipInfo.getInstance().clear();
        GroupInfo.getInstance().clear();
        SPHelper.instance().putString(Config.SP_KEY_USER, "");
        Intent intent = new Intent(view.activity(), SplashActivity.class);
        view.activity().finish();
        view.activity().startActivity(intent);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof FriendshipEvent) {
            FriendshipEvent.NotifyCmd cmd = (FriendshipEvent.NotifyCmd) arg;
            if (cmd.type == FriendshipEvent.NotifyType.ADD) {
                final List<TIMUserProfile> profiles = (List<TIMUserProfile>) cmd.data;
                Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for (TIMUserProfile profile : profiles) {
                            User user = UserFactory.createRealmUser(profile);
                            realm.copyToRealmOrUpdate(user);
                        }
                    }
                });

            } else if (cmd.type == FriendshipEvent.NotifyType.DEL) {
                final List<String> ids = (List<String>) cmd.data;
                Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for (String id : ids) {
                            realm.where(User.class).equalTo("identifier", id)
                                    .equalTo("type", User.TYPE_FRIEND).
                                    findAll().deleteAllFromRealm();
                        }
                    }
                });
            }
        }
    }
}
