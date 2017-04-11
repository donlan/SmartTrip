package dong.lan.smarttrip.presentation.presenter.features;

import java.util.Observer;

/**
 * Created by 梁桂栋 on 17-2-23 ： 下午5:06.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface ITravelsDisplayMenu extends Observer,ITravelMenuAction {

    void loadTravels();

    void loadFromLocal();

    void loadFromNet();

    void deleteTravel(String travelId);

    void refresh();

}
