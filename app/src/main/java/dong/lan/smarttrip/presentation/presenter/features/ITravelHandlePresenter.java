package dong.lan.smarttrip.presentation.presenter.features;

import java.util.List;

/**
 * Created by 梁桂栋 on 17-2-26 ： 下午10:46.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface ITravelHandlePresenter {


    int TYPE_ADD = 0;
    int TYPE_EDIT = 1;


    void actionSave(final String travelName, List<String> dest, final String travelIntro, final long travelStartTime, final long travelEndTime, final String imgPath);

    void toTravelMembersAc();

    void toTravelDocAc();

    void toTravelLineAc();

    void toTravelOtherSetAc();


}
