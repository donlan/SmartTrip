package dong.lan.smarttrip.task;

import android.os.AsyncTask;
import android.util.Log;

import com.tencent.TIMFriendGenderType;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;

import dong.lan.avoscloud.model.AVOUser;
import dong.lan.model.bean.user.User;
import dong.lan.model.permission.Permission;
import dong.lan.smarttrip.common.UserManager;
import io.realm.Realm;

/**
 * Created by 梁桂栋 on 17-3-23 ： 下午4:48.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class FirstConfigTask extends AsyncTask<String, Integer, Object> {
    private static final String TAG = FirstConfigTask.class.getSimpleName();
    private Realm realm;

    private TaskCallback<String> taskCallback;

    public FirstConfigTask(TaskCallback<String> taskCallback) {
        this.taskCallback = taskCallback;
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected Object doInBackground(String... params) {
        AVOUser avoUser = AVOUser.getCurrentUser(AVOUser.class);
        UserManager.instance().initAvoUser(avoUser);
        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "onError: " + i + "," + s);
                taskCallback.onTackCallback(s);
            }

            @Override
            public void onSuccess(final TIMUserProfile timUserProfile) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(User.class).equalTo("identifier", timUserProfile.getIdentifier()).findAll().deleteAllFromRealm();
                        User user = new User();
                        user.setIdentifier(timUserProfile.getIdentifier());
                        user.setAvatarUrl(timUserProfile.getFaceUrl());
                        user.setSex(timUserProfile.getGender() == TIMFriendGenderType.Male ? 1 : 0);
                        user.setUsername(timUserProfile.getIdentifier());
                        user.setNickname(timUserProfile.getNickName());
                        user.setRemark(timUserProfile.getRemark());
                        user.setRole(Permission.LEVEL_OTHER);
                        user.setType(User.TYPE_SELF);
                        realm.copyToRealmOrUpdate(user);
                        UserManager.instance().initUser(realm.copyFromRealm(user));
                    }
                });
                taskCallback.onTackCallback("加载成功");
            }
        });
        return null;
    }
}
