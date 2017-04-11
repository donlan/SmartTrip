package dong.lan.smarttrip.presentation.presenter;

import java.util.Observable;
import java.util.Observer;

import dong.lan.smarttrip.presentation.viewfeatures.TravelBagView;

/**
 * Created by 梁桂栋 on 16-10-8 ： 下午5:13.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class TravelBagPresenter implements Observer {

    TravelBagView view;

    public TravelBagPresenter(TravelBagView view) {
        this.view = view;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
