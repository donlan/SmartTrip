package dong.lan.smarttrip.presentation.presenter.features;

import java.util.List;

import dong.lan.model.features.ITravel;

/**
 * Created by 梁桂栋 on 17-2-26 ： 下午11:26.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface ITravelAction {

    void actionSave(final String travelName, List<String> dest, final String travelIntro, final long travelStartTime, final long travelEndTime, final String imgPath);

    ITravel getTravel();
}
