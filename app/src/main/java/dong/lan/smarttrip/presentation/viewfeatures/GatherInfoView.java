package dong.lan.smarttrip.presentation.viewfeatures;

import java.util.List;

import dong.lan.model.bean.notice.Gather;
import dong.lan.model.bean.notice.GatherReply;
import dong.lan.model.features.IBaseReply;
import dong.lan.model.features.IGatherReply;
import dong.lan.smarttrip.presentation.presenter.features.IActivityFeature;
import io.realm.RealmResults;

/**
 * Created by 梁桂栋 on 17-3-28 ： 下午9:41.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface GatherInfoView extends BaseView , IActivityFeature{

    void initView(Gather gather);

    void initList(RealmResults<GatherReply> gatherReplies);

    void initCacheList(List<GatherReply> replies);
}
