package dong.lan.smarttrip.ui.travel;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.tencent.TIMGroupEventListener;
import com.tencent.TIMGroupTipsElem;
import com.tencent.TIMGroupTipsElemGroupInfo;
import com.tencent.TIMGroupTipsGroupInfoType;
import com.tencent.TIMManager;
import com.tencent.qcloud.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dong.lan.map.service.LocationService;
import dong.lan.map.utils.MapHelper;
import dong.lan.model.Config;
import dong.lan.model.base.BaseJsonData;
import dong.lan.model.bean.notice.Code;
import dong.lan.model.bean.notice.GuideLocation;
import dong.lan.model.bean.travel.Node;
import dong.lan.model.features.ITravel;
import dong.lan.model.utils.TimeUtil;
import dong.lan.smarttrip.App;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.common.SPHelper;
import dong.lan.smarttrip.common.UserManager;
import dong.lan.smarttrip.presentation.presenter.TravelingMapPresenter;
import dong.lan.smarttrip.presentation.presenter.features.ITravelingMapFeature;
import dong.lan.smarttrip.presentation.viewfeatures.ITravelingMapView;
import dong.lan.smarttrip.ui.customview.LabelTextView;
import dong.lan.smarttrip.ui.customview.MapPinNumView;
import dong.lan.smarttrip.ui.notice.CreateGatherActivity;

/**
 * Created by 梁桂栋 on 16-11-2 ： 下午9:27.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class TravelingMapFragment extends BaseFragment implements ITravelingMapView, BaiduMap.OnMapLongClickListener, BaiduMap.OnMarkerClickListener {


    private static final String TAG = TravelingMapFragment.class.getSimpleName();


    public static TravelingMapFragment newInstance(String travelId) {
        TravelingMapFragment infoFragment = new TravelingMapFragment();
        Bundle data = new Bundle();
        data.putString(Config.TRAVEL_ID, travelId);
        infoFragment.setArguments(data);
        return infoFragment;
    }


    @BindView(R.id.traveling_map_leader_loc)
    LabelTextView leaderLocLtv;
    @BindView(R.id.traveling_map_nav_switcher)
    LabelTextView navSwitcher;
    @BindView(R.id.traveling_map_nav_guide)
    LabelTextView navGuider;
    @BindView(R.id.traveling_map_nav_node)
    LabelTextView navNode;
    @BindView(R.id.traveling_map_nav_pick)
    LabelTextView navPick;

    @BindView(R.id.team_line_map)
    MapView mapView;
    @BindView(R.id.traveling_map_guide_loc)
    LabelTextView guideLocationLtv;
    @BindView(R.id.traveling_map_me_loc)
    LabelTextView meLocationLtv;
    @BindView(R.id.traveling_map_new_gather)
    LabelTextView gatherLtv;

    @OnClick(R.id.traveling_map_new_gather)
    void createNewGather() {
        Intent intent = new Intent(getContext(), CreateGatherActivity.class);
        intent.putExtra(Config.TRAVEL_ID, getArguments().getString(Config.TRAVEL_ID));
        startActivity(intent);
    }

    @OnClick(R.id.traveling_map_leader_loc)
    void toLeaderLocation(){

    }

    @OnClick(R.id.traveling_map_guide_loc)
    void toGuideLocation() {
    }

    @OnClick(R.id.traveling_map_me_loc)
    void toSelfLocation() {
        BDLocation loc = App.myApp().locationService().getLastLocation();
        if (loc == null) {
            toast("无定位结果");
        } else {
            firstLoc = true;
            setMyLocation(loc);
        }
    }

    @OnClick(R.id.traveling_map_nav_guide)
    void navToGuide() {
        startNavAnim();
        navFlag = 0;
        if (markerGuide == null) {
            dialog("无法获取导游的位置");
            return;
        }
        presenter.startNav(markerGuide.getPosition(), markerMe.getPosition());
    }

    @OnClick(R.id.traveling_map_nav_node)
    void navToNode() {
        startNavAnim();
        navFlag = 1;
    }

    @OnClick(R.id.traveling_map_nav_pick)
    void navByPickPoint() {
        startNavAnim();
        navFlag = 2;
    }

    @OnClick(R.id.traveling_map_line)
    void toTeamMapLine() {
        showNodeLine(presenter.getTravel());
    }

    @OnClick(R.id.traveling_map_nav_switcher)
    void navSwitcherAnim() {
        startNavAnim();
    }

    @OnClick(R.id.traveling_map_hint_parent)
    void hideNodeInfoView() {
        hintParent.setVisibility(View.GONE);
    }

    @BindView(R.id.traveling_map_hint_parent)
    RelativeLayout hintParent;

    @BindView(R.id.travel_nav_number)
    LabelTextView navNumberLtv;
    @BindView(R.id.travel_nav_position)
    TextView navPositionTv;
    @BindView(R.id.travel_nav_time)
    TextView navTimeInfoTv;
    @BindView(R.id.travel_nav_info)
    TextView navInfoTv;


    @BindView(R.id.traveling_map_guide_loc_switcher)
    LabelTextView guideLocSwitcher;
    private boolean guideLocFlag = false;

    @OnClick(R.id.traveling_map_guide_loc_switcher)
    void toggleGuideLocation() {
        guideLocFlag = !guideLocFlag;
        guideLocSwitcher.setText("共享位置:" + (guideLocFlag ? "开" : "关"));
        SPHelper.instance().putBoolean(Config.GUIDE_LOCATION_FLAG, guideLocFlag);
    }

    private ITravelingMapFeature presenter;

    private int navFlag = 0; // 0:导航到导游的位置 1:导航到选择的节点位置 2:自己从地图上选择导航终点
    private boolean firstLoc = true;
    private BaiduMap baiduMap;
    private Marker markerMe;
    private Marker markerGuide;
    private Marker markerLeader;

    private BitmapDescriptor bmpGuide = BitmapDescriptorFactory.fromResource(R.drawable.location_guide);
    private BitmapDescriptor bmpMe = BitmapDescriptorFactory.fromResource(R.drawable.location_me);
    private LocationService.LocationCallback locationCallback = new LocationService.LocationCallback() {
        @Override
        public void onLocation(BDLocation location) {
            setMyLocation(location);
            firstLoc = false;
            MapHelper.setLocation(location, baiduMap, firstLoc);
            if (guideLocFlag)
                presenter.updateGuideLocation(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (content == null) {
            content = inflater.inflate(R.layout.fragment_team_map_line, container, false);
            bindView(content);
            baiduMap = mapView.getMap();
            baiduMap.setOnMapLongClickListener(this);
            baiduMap.setOnMarkerClickListener(this);
            hintParent.setVisibility(View.GONE);
            presenter = new TravelingMapPresenter(this);
            presenter.loadTravel(getArguments().getString(Config.TRAVEL_ID));
        }
        return content;
    }


    private boolean isNavAnimOpen = true;

    /**
     * 点击导航按钮动画
     */
    private void startNavAnim() {
        if (isNavAnimOpen) {
            ObjectAnimator.ofFloat(navGuider, "translationX", 0, -750).setDuration(400).start();
            ObjectAnimator.ofFloat(navNode, "translationX", 0, -500).setDuration(400).start();
            ObjectAnimator.ofFloat(navPick, "translationX", 0, -250).setDuration(400).start();
        } else {
            ObjectAnimator.ofFloat(navGuider, "translationX", 0).setDuration(400).start();
            ObjectAnimator.ofFloat(navNode, "translationX", 0).setDuration(400).start();
            ObjectAnimator.ofFloat(navPick, "translationX", 0).setDuration(400).start();
        }
        isNavAnimOpen = !isNavAnimOpen;
    }


    /**
     * 将地图定位到自己的位置
     *
     * @param loc
     */
    private void setMyLocation(BDLocation loc) {
        LatLng p = new LatLng(loc.getLatitude(), loc.getLongitude());
        if (markerMe == null) {
            markerMe = MapHelper.drawMarker(baiduMap, p, bmpMe, 0.5f, 0.5f);
        } else {
            markerMe.setPosition(p);
        }
        if (firstLoc) {
            MapHelper.setLocation(loc, baiduMap, true);
            firstLoc = false;
        }
    }

    /**
     * 在地图上显示导游的位置
     *
     * @param point
     */
    private void setGuideLocation(LatLng point) {
        if (markerGuide == null) {
            markerGuide = MapHelper.drawMarker(baiduMap, point, bmpGuide, 0.5f, 0);
        } else {
            markerGuide.setPosition(point);
        }
    }

    public void initView(ITravel travel) {

        if (travel.getTag().equals(UserManager.instance().identifier())) {
            guideLocSwitcher.setVisibility(View.VISIBLE);
            guideLocSwitcher.setText("共享位置:" + (guideLocFlag ? "开" : "关"));
            guideLocationLtv.setVisibility(View.GONE);
            gatherLtv.setVisibility(View.VISIBLE);
        } else {
            TIMManager.getInstance().setGroupEventListener(new TIMGroupEventListener() {
                @Override
                public void onGroupTipsEvent(TIMGroupTipsElem timGroupTipsElem) {
                    List<TIMGroupTipsElemGroupInfo> groupInfos = timGroupTipsElem.getGroupInfoList();
                    for (TIMGroupTipsElemGroupInfo info : groupInfos) {
                        if (info.getType() == TIMGroupTipsGroupInfoType.ModifyNotification) {
                            try {
                                BaseJsonData<GuideLocation> notice = JSON.parseObject(info.getContent(), new TypeReference<BaseJsonData<GuideLocation>>() {
                                });
                                LatLng p = notice.toTarget().toLatLng();
                                if (notice.code == Code.CODE_GUIDE_LOCATION) {
                                    setGuideLocation(p);
                                } else if (notice.code == Code.CODE_LEADER_LOCATION) {
                                    setLeaderLocation(p);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * 显示领队位置
     * @param p
     */
    private void setLeaderLocation(LatLng p) {
        if (markerLeader == null) {
            markerLeader = MapHelper.drawMarker(baiduMap, p, bmpGuide, 0.5f, 0);
        } else {
            markerLeader.setPosition(p);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        guideLocFlag = SPHelper.instance().getBoolean(Config.GUIDE_LOCATION_FLAG);
        guideLocSwitcher.setText("共享位置:" + (guideLocFlag ? "开" : "关"));
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        App.myApp().locationService().registerCallback(this, locationCallback);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
        mapView = null;
        baiduMap = null;
        bmpGuide.recycle();
        bmpMe.recycle();
        bmpMe = null;
        bmpGuide = null;
        App.myApp().locationService().unregisterCallback(this);
    }

    /**
     * 显示旅行路线
     *
     * @param travel
     */
    @Override
    public void showNodeLine(ITravel travel) {
        if (travel == null || travel.getNodes().isEmpty()) {
            toast("无节点...");
            return;
        }
        baiduMap.clear();
        List<LatLng> points = new ArrayList<>(travel.getNodes().size());
        for (Node node : travel.getNodes()) {
            points.add(new LatLng(node.getLatitude(), node.getLongitude()));
        }
        MapHelper.drawPolyLine(baiduMap, points);
        int n = 1;
        MapHelper.setLocation(points.get(0),baiduMap,true);
        for (LatLng p : points) {
            View dot = new MapPinNumView(getContext(), String.valueOf(n), 0xffFC993C, 20, Color.DKGRAY);
            Marker m = MapHelper.drawMarker(baiduMap, p, BitmapDescriptorFactory.fromView(dot), 0.5f, 0.7f);
            m.setTitle(String.valueOf(n));
            n++;
        }
    }

    /**
     * 显示节点信息
     *
     * @param node
     */
    @Override
    public void showNodeInfo(Node node) {
        if (hintParent.getVisibility() == View.GONE)
            hintParent.setVisibility(View.VISIBLE);
        navNumberLtv.setText(String.valueOf(node.getNo()));
        navPositionTv.setText(node.getAddress());
        navInfoTv.setText(node.getDescription());
        navTimeInfoTv.setText(TimeUtil.getTime(node.getArrivedTime(), "MM.dd HH:mm"));
    }

    @Override
    public Activity activity() {
        return getActivity();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (navFlag == 2)
            presenter.startNav(markerMe.getPosition(), latLng);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (navFlag == 1) {
            presenter.startNav(markerMe.getPosition(), marker.getPosition());
        } else {
            String t = marker.getTitle();
            if (TextUtils.isEmpty(t))
                return true;
            Node node = presenter.getTravel().getNodes().get(Integer.parseInt(t) - 1);
            showNodeInfo(node);
        }
        return true;
    }
}
