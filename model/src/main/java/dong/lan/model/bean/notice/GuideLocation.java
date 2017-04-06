package dong.lan.model.bean.notice;

import com.baidu.mapapi.model.LatLng;

import dong.lan.model.base.Data;

/**
 * Created by 梁桂栋 on 17-3-26 ： 上午12:32.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class GuideLocation implements Data {

    public  double latitude;
    public  double longitude;

    public GuideLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LatLng toLatLng(){
        return new LatLng(latitude,longitude);
    }
    @Override
    public String toJson() {
        return null;
    }
}
