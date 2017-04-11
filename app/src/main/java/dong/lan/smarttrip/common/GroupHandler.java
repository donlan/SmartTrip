package dong.lan.smarttrip.common;

import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberInfo;
import com.tencent.TIMManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.tencent.qcloud.presentation.event.FriendshipEvent;
import com.tencent.qcloud.presentation.event.GroupEvent;

import java.util.Collections;
import java.util.List;

import dong.lan.avoscloud.model.AVOTourist;
import dong.lan.avoscloud.model.AVOUser;
import dong.lan.model.BeanConvert;
import dong.lan.model.bean.travel.Travel;
import dong.lan.model.bean.user.Tourist;
import dong.lan.model.bean.user.User;
import dong.lan.model.features.ITourist;
import dong.lan.model.permission.Permission;
import dong.lan.smarttrip.model.factory.UserFactory;
import io.realm.Realm;

/**
 * Created by 梁桂栋 on 17-4-8 ： 下午7:01.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public final class GroupHandler {
    private GroupHandler() {

    }

    private static GroupHandler groupHandler;

    public static GroupHandler instance() {
        if (groupHandler == null)
            groupHandler = new GroupHandler();
        return groupHandler;
    }

    private void addTourist(final User user, final Travel travel) {
        Tourist tourist = new Tourist();
        tourist.setTravel(travel);
        tourist.setUser(user);
        tourist.setStatus(ITourist.STATUS_NORMAL);
        tourist.setRole(Permission.LEVEL_YOUKE);
        final AVOTourist avoTourist = BeanConvert.toAvoTourist(tourist);
        avoTourist.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Tourist tourist = realm.createObject(Tourist.class);
                            tourist.setTravel(travel);
                            tourist.setUser(user);
                            tourist.setObjId(avoTourist.getObjectId());
                            tourist.setStatus(ITourist.STATUS_NORMAL);
                            tourist.setRole(Permission.LEVEL_YOUKE);
                        }
                    });
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handlerMemberAdd(final Travel travel, List<TIMGroupMemberInfo> memberInfos) {
        if (memberInfos != null) {
            for (TIMGroupMemberInfo info : memberInfos) {
                final String identifier = info.getUser();
                TIMFriendshipManager.getInstance().getUsersProfile(Collections.singletonList(identifier), new TIMValueCallBack<List<TIMUserProfile>>() {
                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onSuccess(final List<TIMUserProfile> timUserProfiles) {
                        if (!timUserProfiles.isEmpty()) {
                            final User user = UserFactory.createRealmUser(timUserProfiles.get(0));
                            Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    User u = realm.copyToRealmOrUpdate(user);
                                    if (TextUtils.isEmpty(u.getObjId())) {
                                        final AVOUser avoUser = BeanConvert.toAvoUser(user);
                                        avoUser.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(AVException e) {
                                                if (e == null) {
                                                    Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                                                        @Override
                                                        public void execute(Realm realm) {
                                                            User user = BeanConvert.toUser(avoUser);
                                                            addTourist(realm.copyToRealmOrUpdate(user), travel);
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    } else {
                                        addTourist(u, travel);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    public void handler(GroupEvent.NotifyCmd data) {
        if (data.type == GroupEvent.NotifyType.MEMBER_ADD) {
            final List<TIMGroupMemberInfo> memberInfos = (List<TIMGroupMemberInfo>) data.data;
            final String groupId = data.id;
            Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Travel travel = realm.where(Travel.class).equalTo("id", groupId).findFirst();
                    handlerMemberAdd(travel, memberInfos);
                }
            });
        } else if (data.type == GroupEvent.NotifyType.MEMBER_QUIT) {
            final List<TIMGroupMemberInfo> memberInfos = (List<TIMGroupMemberInfo>) data.data;
            final String groupId = data.id;
            if (memberInfos == null)
                return;
            Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (TIMGroupMemberInfo info : memberInfos) {
                        Tourist tourist = realm.where(Tourist.class).equalTo("user.indentifer", info.getUser())
                                .equalTo("travel.id", groupId).findFirst();
                        BeanConvert.toAvoTourist(tourist).deleteEventually();
                        tourist.deleteFromRealm();
                    }
                }
            });
        }
    }
}
