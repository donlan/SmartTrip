package dong.lan.smarttrip.presentation.presenter;

import dong.lan.model.bean.notice.Gather;
import dong.lan.model.bean.notice.Notice;
import dong.lan.model.bean.notice.NoticeShow;
import dong.lan.smarttrip.presentation.presenter.features.INoticeListPresenter;
import dong.lan.smarttrip.presentation.viewfeatures.NoticeListView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by 梁桂栋 on 17-3-27 ： 下午2:15.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class NoticeListPresenter implements INoticeListPresenter {

    private NoticeListView view;

    public NoticeListPresenter(NoticeListView view) {
        this.view = view;
    }

    @Override
    public void loadNotice(final int type, final String userId, final String travelId) {
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if(type == NoticeShow.TYPE_GATHER){
                    RealmResults<Gather> gathers = realm.where(Gather.class)
                            .equalTo("creatorId",userId)
                            .equalTo("travelId",travelId)
                            .findAllSorted("time", Sort.DESCENDING);
                    view.initView(realm.copyFromRealm(gathers));
                }else if(type == NoticeShow.TYPE_NOTICE){
                    RealmResults<Notice> gatherInfos = realm.where(Notice.class)
                            .equalTo("creatorId",userId)
                            .equalTo("travelId",travelId)
                            .findAllSorted("createTime", Sort.DESCENDING);
                    view.initView(realm.copyFromRealm(gatherInfos));
                }
            }
        });

    }
}
