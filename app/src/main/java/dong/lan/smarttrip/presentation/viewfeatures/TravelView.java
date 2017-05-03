package dong.lan.smarttrip.presentation.viewfeatures;

import dong.lan.smarttrip.base.DelayInitView;

import java.util.List;

import dong.lan.model.features.ITravel;
import dong.lan.smarttrip.presentation.presenter.features.IActivityFeature;

/**
 * Created by 梁桂栋 on 16-10-8 ： 下午4:09
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface TravelView extends IActivityFeature ,BaseView,DelayInitView {

    void stopRefresh();

    void initAdapter(List<? extends ITravel> travels);

    void refreshDisplayView(int pos);
}
