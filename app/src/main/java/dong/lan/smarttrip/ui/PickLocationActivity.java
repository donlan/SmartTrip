package dong.lan.smarttrip.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dong.lan.map.service.LocationService;
import dong.lan.map.utils.MapHelper;
import dong.lan.permission.CallBack;
import dong.lan.permission.Permission;
import dong.lan.smarttrip.App;
import dong.lan.smarttrip.R;
import dong.lan.model.Config;
import dong.lan.smarttrip.base.BaseActivity;
import dong.lan.smarttrip.utils.InputUtils;

public class PickLocationActivity extends BaseActivity {

    private static final String TAG = PickLocationActivity.class.getSimpleName();

    @OnClick(R.id.pick_location_back)
    void back() {
        double lat = 0;
        double lng = 0;
        if (pickMarker != null) {
            lat = pickMarker.getPosition().latitude;
            lng = pickMarker.getPosition().longitude;
        } else {
            toast("没有选择位置信息");
        }
        Intent locData = new Intent();
        locData.putExtra(Config.LATITUDE, lat);
        locData.putExtra(Config.LONGITUDE, lng);
        locData.putExtra(Config.LOC_ADDRESS, locationText.getText().toString());
        setResult(Config.RESULT_LOCATION, locData);
        finish();
    }

    @BindView(R.id.pick_location_input)
    EditText searchInput;

    @BindView(R.id.pick_location_input_city)
    EditText cityInput;

    @OnClick(R.id.pick_location_search)
    void search() {
        String searchText = searchInput.getText().toString();
        String city = cityInput.getText().toString();
        if (TextUtils.isEmpty(city)) {
            toast("请输入正确的城市名称");
            return;
        }
        if (TextUtils.isEmpty(searchText)) {
            toast("搜索内容不能为空");
        }
        InputUtils.hideInputKeyboard(searchInput);
        locationText.setVisibility(View.GONE);
        if (poiSearch == null) {
            poiSearch = PoiSearch.newInstance();
            poiSearch.setOnGetPoiSearchResultListener(poiSearchResultListener);
        }
        PoiCitySearchOption option = new PoiCitySearchOption();
        option.city(city);
        option.keyword(searchText);
        option.pageCapacity(10);
        option.pageNum(1);
        poiSearch.searchInCity(option);

    }

    @BindView(R.id.bdMap)
    MapView mapView;

    @BindView(R.id.pick_location_text)
    EditText locationText;
    private BaiduMap baiduMap;

    private BitmapDescriptor pickFlag;
    private Marker pickMarker;
    private GeoCoder geoSearch;
    private PoiSearch poiSearch;
    private LocationService.LocationCallback locationCallback = new LocationService.LocationCallback() {
        @Override
        public void onLocation(BDLocation bdLocation) {
            Log.d(TAG, "onLocation: " + bdLocation.getCity());
            baiduMap.setMyLocationEnabled(true);
            MyLocationConfiguration configuration = new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL, true,
                    BitmapDescriptorFactory.fromResource(R.drawable.location));
            baiduMap.setMyLocationConfigeration(configuration);
            MapHelper.setLocation(bdLocation, baiduMap, isFirstLocation);
            isFirstLocation = false;
            App.myApp().locationService().unregisterCallback(PickLocationActivity.this);
            setCityText(bdLocation.getCity());
        }
    };

    private void setCityText(String cityText) {
//        cityInput.setText(cityText);
    }

    private OnGetGeoCoderResultListener geoCoderResultListener = new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            Log.d(TAG, "onGetGeoCodeResult: " + geoCodeResult);
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            if (reverseGeoCodeResult != null && reverseGeoCodeResult.error == SearchResult.ERRORNO.NO_ERROR) {
                if (locationText.getVisibility() == View.GONE) {
                    locationText.setVisibility(View.VISIBLE);
                }
                String addr = "";
                if (!TextUtils.isEmpty(reverseGeoCodeResult.getAddress())) {
                    addr = reverseGeoCodeResult.getAddress();
                } else if (null != reverseGeoCodeResult.getAddressDetail()) {
                    ReverseGeoCodeResult.AddressComponent component = reverseGeoCodeResult.getAddressDetail();
                    addr = component.city + "-" + component.street;
                }
                if (reverseGeoCodeResult.getPoiList() == null && TextUtils.isEmpty(addr)) {
                    locationText.setText("");
                    toast("无法识别位置信息");
                } else {
                    for (PoiInfo p : reverseGeoCodeResult.getPoiList()) {
                        Log.d(TAG, "onGetReverseGeoCodeResult: " + p.address);
                    }
                    locationText.setText(addr);
                }
            } else {
                toast("没有搜索结果");
            }
            if (locationText.getText().length() <= 0)
                locationText.setHint("无识别结果,请手动输入");
        }
    };

    private OnGetPoiSearchResultListener poiSearchResultListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {

            if (poiResult == null || poiResult.getAllPoi() == null) {
                toast("无搜索结果");
                return;
            }
            for (int i = 0, s = poiResult.getAllPoi().size(); i < s; i++) {
                LatLng loc = poiResult.getAllPoi().get(i).location;
                MapHelper.drawMarker(baiduMap, loc,
                        BitmapDescriptorFactory.fromResource(R.drawable.location));
                if (i == 0) {
                    MapHelper.setLocation(loc, baiduMap, true);
                    locationText.setVisibility(View.VISIBLE);
                    locationText.setText(poiResult.getAllPoi().get(i).address);
                }
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            Log.d(TAG, "onGetPoiDetailResult: " + poiDetailResult);
        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
            Log.d(TAG, "onGetPoiIndoorResult: " + poiIndoorResult);
        }
    };

    private boolean isFirstLocation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_location);
        bindView();
        initView();
    }

    private void initView() {
        mapView.setLogoPosition(LogoPosition.logoPostionRightTop);
        baiduMap = mapView.getMap();
        baiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (geoSearch == null) {
                    geoSearch = GeoCoder.newInstance();
                    geoSearch.setOnGetGeoCodeResultListener(geoCoderResultListener);
                    pickFlag = BitmapDescriptorFactory.fromResource(R.drawable.location_flag);
                    pickMarker = MapHelper.drawMarker(baiduMap, latLng, pickFlag);
                } else {
                    pickMarker.setPosition(latLng);
                }
                ReverseGeoCodeOption option = new ReverseGeoCodeOption();
                option.location(latLng);
                geoSearch.reverseGeoCode(option);
            }
        });


        Permission.instance().check(new CallBack<List<String>>() {
            @Override
            public void onResult(List<String> result) {
                if (result == null) {
                    App.myApp().locationService().
                            registerCallback(PickLocationActivity.this, locationCallback);
                } else {
                    toast("缺少定位权限,无法获取位置信息");
                }
            }
        }, this, Collections.singletonList(Manifest.permission.ACCESS_FINE_LOCATION));

    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
        if (geoSearch != null)
            geoSearch.destroy();
        if (pickFlag != null)
            pickFlag.recycle();
        pickFlag = null;
        pickMarker = null;
        App.myApp().locationService().unregisterCallback(this);
    }
}
