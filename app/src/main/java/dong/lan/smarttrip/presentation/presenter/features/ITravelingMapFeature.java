package dong.lan.smarttrip.presentation.presenter.features;

import com.baidu.mapapi.model.LatLng;

import dong.lan.model.features.ITravel;

/**
 * Created by 梁桂栋 on 17-3-21 ： 下午9:09.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface ITravelingMapFeature {

    void loadTravel(String travelId);

    ITravel getTravel();

    void startNav(LatLng startPoint,LatLng endPoint);

    void updateGuideLocation(LatLng latLng);
}
