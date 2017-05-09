package dong.lan.smarttrip.presentation.presenter;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dong.lan.model.bean.notice.Notice;
import dong.lan.model.bean.notice.NoticeReply;
import dong.lan.model.bean.user.Tourist;
import dong.lan.model.features.IGatherReply;
import dong.lan.smarttrip.presentation.presenter.features.INoticeInfoPresenter;
import dong.lan.smarttrip.presentation.viewfeatures.NoticeInfoView;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by 梁桂栋 on 17-3-28 ： 下午9:43.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class NoticeInfoPresenter implements INoticeInfoPresenter {

    private static final String TAG = NoticeInfoPresenter.class.getSimpleName();
    private NoticeInfoView view;
    private RealmResults<NoticeReply> noticeReplies;

    public NoticeInfoPresenter(NoticeInfoView view) {
        this.view = view;
    }
    private boolean first = true;

    private List<NoticeReply> replies;
    private HashMap<String, NoticeReply> replyMap = new HashMap<>();
    private int all = 0;

    @Override
    public void start(final String travelId, final String id) {
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Notice notice = realm.where(Notice.class).equalTo("id", id)
                        .findFirst();
                if (notice == null) {
                    view.initView(null);
                } else {
                    view.initView(realm.copyFromRealm(notice));
                }
            }
        });
        noticeReplies = Realm.getDefaultInstance().where(NoticeReply.class)
                .equalTo("notice.id", id).findAllAsync();
        view.initList(noticeReplies);
        noticeReplies.addChangeListener(new RealmChangeListener<RealmResults<NoticeReply>>() {
            @Override
            public void onChange(RealmResults<NoticeReply> noticeReplies) {
                for (NoticeReply r : noticeReplies) {
                    replyMap.put(r.getTourist().getIdentifier(), r);
                }
                if(first){
                    first = false;
                    loop(travelId);
                }
            }
        });
    }


    private void loop(final String travelId){
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (replies == null)
                    replies = new ArrayList<>();
                RealmResults<Tourist> tourists = realm.where(Tourist.class)
                        .equalTo("travel.id", travelId).findAll();
                if (tourists != null) {
                    all = tourists.size();
                    for (Tourist tourist :
                            tourists) {
                        if (replyMap.containsKey(tourist.getIdentifier()))
                            continue;
                        NoticeReply reply = new NoticeReply();
                        reply.tourist = realm.copyFromRealm(tourist);
                        reply.status = IGatherReply.STATUS_NO;
                        replies.add(reply);
                    }
                }
                view.initCacheList(replies);

            }
        });
    }

    @Override
    public void switcherShow(boolean showFlag, TextView switcherLtv) {
        if (showFlag) {
            switcherLtv.setText("未到人员 " + (replies == null ? 0 : replies.size()) + "/" + all);
        } else {
            switcherLtv.setText("已到人员 " + (replyMap == null ? 0 : replyMap.size()) + "/" + all);
        }

    }
}
