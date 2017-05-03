package dong.lan.smarttrip.presentation.presenter;

import android.Manifest;
import android.content.Intent;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.blankj.ALog;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.Collections;
import java.util.List;
import java.util.Observable;

import dong.lan.avoscloud.model.AVOTravel;
import dong.lan.avoscloud.model.AVOUser;
import dong.lan.model.BeanConvert;
import dong.lan.model.Config;
import dong.lan.model.bean.travel.Travel;
import dong.lan.model.features.IUser;
import dong.lan.permission.CallBack;
import dong.lan.permission.Permission;
import dong.lan.smarttrip.model.factory.UserFactory;
import dong.lan.smarttrip.presentation.presenter.features.ITravelsDisplayMenu;
import dong.lan.smarttrip.presentation.viewfeatures.TravelView;
import dong.lan.smarttrip.ui.im.AddFriendActivity;
import dong.lan.smarttrip.ui.im.ApplyGroupActivity;
import dong.lan.smarttrip.ui.im.SearchGroupActivity;
import dong.lan.smarttrip.ui.travel.AddTravelActivity;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by 梁桂栋 on 16-10-8 ： 下午4:10.
 * Email:       760625325@qqcom
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class TravelPresenter implements ITravelsDisplayMenu {


    private static final String TAG = TravelPresenter.class.getSimpleName();
    TravelView view;
    private Realm realm;

    public TravelPresenter(TravelView view) {
        this.view = view;
        realm = Realm.getDefaultInstance();
    }


    @Override
    public void loadTravels() {
        loadFromLocal();
    }

    @Override
    public void loadFromLocal() {

        RealmResults<Travel> travels = realm.where(Travel.class)
                .findAllSortedAsync("unread", Sort.DESCENDING, "createTime", Sort.DESCENDING);
        view.initAdapter(travels);
        travels.addChangeListener(new RealmChangeListener<RealmResults<Travel>>() {
            @Override
            public void onChange(RealmResults<Travel> element) {
                view.refreshDisplayView(0);
                if (element == null || element.isEmpty()) {
                    view.toast("本地数据为空,即将从网络加载");
                    loadFromNet();
                }
            }
        });

    }

    @Override
    public void loadFromNet() {
        AVQuery<AVOTravel> query = new AVQuery<>("Travel");
        query.include("creator");
        query.whereEqualTo("tourists", AVOUser.getCurrentUser());
        query.findInBackground(new FindCallback<AVOTravel>() {
            @Override
            public void done(final List<AVOTravel> list, AVException e) {
                ALog.d(TAG, "done: " + list + "," + e);
                if (e == null) {
                    if (list == null || list.isEmpty()) {
                        view.toast("无旅行数据");
                    } else {
                        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                for (AVOTravel travel : list) {
                                    Travel t = BeanConvert.toTravel(travel);
                                    realm.copyToRealmOrUpdate(t);
                                }
                            }
                        });
                        view.refreshDisplayView(0);
                    }
                } else {
                    view.toast("获取旅行数据失败,错误码:" + e.getCode());
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void deleteTravel(String travelId) {

    }

    @Override
    public void refresh() {
        view.stopRefresh();
    }

    @Override
    public void createNewTravel() {
        view.activity().startActivity(new Intent(view.activity(), AddTravelActivity.class));
    }

    @Override
    public void joinTravel() {
        view.activity().startActivity(new Intent(view.activity(), SearchGroupActivity.class));
    }

    @Override
    public void startQRScan() {
        Permission.instance()
                .check(new CallBack<List<String>>() {
                           @Override
                           public void onResult(List<String> result) {
                               if (result == null) {
                                   Intent qrIntent = new Intent(view.activity(), CaptureActivity.class);
                                   view.activity().startActivityForResult(qrIntent, Config.REQUEST_CODE_ZXING);
                               }
                           }
                       }, view.activity(),
                        Collections.singletonList(Manifest.permission.CAMERA));
    }

    @Override
    public void handleScanResult(String scanRes) {
        if (scanRes.contains(Config.USER_QRCODE_URL + "/add/")) {
            final String userId = scanRes.substring((Config.USER_QRCODE_URL + "/add/").length());
            TIMFriendshipManager.getInstance().getUsersProfile(Collections.singletonList(userId), new TIMValueCallBack<List<TIMUserProfile>>() {
                @Override
                public void onError(int i, String s) {
                    view.toast("无法获取用户信息:" + s);
                }

                @Override
                public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                    if (timUserProfiles != null && !timUserProfiles.isEmpty()) {
                        TIMUserProfile timUserProfile = timUserProfiles.get(0);
                        IUser user = UserFactory.createUser(timUserProfile);
                        Intent intent = new Intent(view.activity(), AddFriendActivity.class);
                        intent.putExtra("id", user.getIdentifier());
                        intent.putExtra("name", user.getUsername());
                        view.activity().startActivity(intent);
                    }
                }
            });

        } else if (scanRes.contains(Config.SHARE_BASE_URL + "?travelId=")) {
            int i = scanRes.indexOf("travelId=");
            if (scanRes.length() >= i + 9) {
                String id = scanRes.substring(i + 9);
                Intent intent = new Intent(view.activity(), ApplyGroupActivity.class);
                intent.putExtra("identifier", id);
                view.activity().startActivity(intent);
            }

        } else {
            view.toast("很抱歉 小路不能处理这个二维码的信息");
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
