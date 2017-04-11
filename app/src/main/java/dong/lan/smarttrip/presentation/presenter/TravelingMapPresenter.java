package dong.lan.smarttrip.presentation.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.tencent.TIMCallBack;
import com.tencent.TIMGroupManager;
import com.tencent.qcloud.ui.Dialog;

import org.greenrobot.eventbus.EventBus;

import dong.lan.model.bean.notice.Code;
import dong.lan.model.bean.notice.GuideLocation;
import dong.lan.model.bean.travel.Travel;
import dong.lan.model.base.BaseJsonData;
import dong.lan.model.features.ITravel;
import dong.lan.smarttrip.presentation.presenter.features.ITravelingMapFeature;
import dong.lan.smarttrip.presentation.viewfeatures.ITravelingMapView;
import io.realm.Realm;

/**
 * Created by 梁桂栋 on 17-3-21 ： 下午9:14.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class TravelingMapPresenter implements ITravelingMapFeature {

    private static final String TAG = TravelingMapPresenter.class.getSimpleName();
    private ITravelingMapView view;
    private Realm realm;
    private Travel travel;
    private Dialog dialog;
    private String groupId;

    public TravelingMapPresenter(ITravelingMapView view) {
        this.view = view;
        realm = Realm.getDefaultInstance();
        dialog = new Dialog(view.activity());
    }

    @Override
    public void loadTravel(final String travelId) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                travel = realm.where(Travel.class).equalTo("id", travelId).findFirst();
                for (int i = 0, s = travel.getNodes().size(); i < s; i++)
                    travel.getNodes().get(i).setNo(i + 1);
                groupId = travelId;
                view.initView(travel);
            }
        });
    }

    @Override
    public ITravel getTravel() {
        return travel;
    }

    @Override
    public void startNav(LatLng startPoint, LatLng endPoint) {
        if (startPoint == null) {
            dialog.setMessageText("无法获取到你当前的位置").show();
            return;
        }
        if (endPoint == null) {
            dialog.setMessageText("无法获取到导航终点位置").show();
            return;
        }
        NaviParaOption para = new NaviParaOption()
                .startPoint(startPoint).endPoint(endPoint);
        try {
            BaiduMapNavigation.openBaiduMapNavi(para, view.activity());
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            dialog.setMessageText("您尚未安装百度地图app或app版本过低，点击确认安装？")
                    .setRightText("安装")
                    .setClickListener(new Dialog.DialogClickListener() {
                        @Override
                        public boolean onDialogClick(int which) {
                            if (which == Dialog.CLICK_RIGHT) {
                                OpenClientUtil.getLatestBaiduMapApp(view.activity());
                            }
                            return true;
                        }
                    });
        }

    }

    @Override
    public void updateGuideLocation(LatLng latLng) {
        BaseJsonData<GuideLocation> notice = new  BaseJsonData<>();
        notice.code = Code.CODE_GUIDE_LOCATION;
        notice.data = new GuideLocation(latLng.latitude,latLng.longitude);


        TIMGroupManager.getInstance().modifyGroupNotification(groupId,
                JSON.toJSONString(notice), new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        Log.d(TAG, "onError: " + i + "," + s);
                    }

                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: ");
                    }
                });
    }
}
