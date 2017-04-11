package dong.lan.smarttrip.presentation.viewfeatures;

import dong.lan.model.bean.travel.Node;
import dong.lan.model.features.ITravel;
import dong.lan.smarttrip.presentation.presenter.features.IActivityFeature;

/**
 * Created by 梁桂栋 on 17-3-21 ： 下午9:10.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface ITravelingMapView extends IActivityFeature {

    /**
     * 显示旅行路线
     * @param travel
     */
    void showNodeLine(ITravel travel);

    /**
     * 显示节点信息
     * @param node
     */
    void showNodeInfo(Node node);

    /**
     * 根据旅行参数进行页面初始化
     * @param travel
     */
    void initView(ITravel travel);
}
