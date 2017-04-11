package dong.lan.smarttrip.presentation.viewfeatures;

import java.util.List;

import dong.lan.model.bean.notice.Gather;
import dong.lan.model.bean.notice.Notice;
import dong.lan.model.bean.notice.NoticeReply;
import dong.lan.model.features.IBaseReply;
import dong.lan.model.features.IGatherReply;
import dong.lan.model.features.INoticeReply;
import dong.lan.smarttrip.presentation.presenter.features.IActivityFeature;
import io.realm.RealmResults;

/**
 * Created by 梁桂栋 on 17-3-28 ： 下午9:41.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface NoticeInfoView extends BaseView , IActivityFeature{

    void initView(Notice notice);

    void initList(RealmResults<NoticeReply> noticeReplies);

    void initCacheList(List<NoticeReply> replies);
}
