package dong.lan.smarttrip.presentation.viewfeatures;

import java.util.List;

import dong.lan.model.bean.notice.NoticeShow;
import dong.lan.smarttrip.presentation.presenter.features.IActivityFeature;

/**
 * Created by 梁桂栋 on 17-3-27 ： 下午2:14.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface NoticeListView extends IActivityFeature {

    void initView(List< ? extends NoticeShow> noticeShows);
}
