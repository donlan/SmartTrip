package dong.lan.smarttrip.presentation.viewfeatures;

import android.app.Activity;

import com.tencent.qcloud.ui.base.ProgressView;

import dong.lan.model.features.ITravel;

/**
 * Created by 梁桂栋 on 17-2-2 ： 下午10:47.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface AddTravelView extends ProgressView {


    Activity activity();

    void initView(ITravel travel);
}
