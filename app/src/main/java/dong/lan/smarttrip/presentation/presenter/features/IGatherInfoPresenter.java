package dong.lan.smarttrip.presentation.presenter.features;

import android.widget.TextView;

import dong.lan.smarttrip.ui.customview.LabelTextView;

/**
 * Created by 梁桂栋 on 17-3-28 ： 下午9:42.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface IGatherInfoPresenter {

    void start(String travelId,String id);

    void switcherShow(boolean showFlag,TextView switcherLtv);
}
