package dong.lan.smarttrip.presentation.presenter;

import android.util.Log;
import android.widget.TextView;

import com.tencent.TIMFriendshipManager;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberInfo;
import com.tencent.TIMManager;
import com.tencent.TIMValueCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dong.lan.model.bean.notice.Gather;
import dong.lan.model.bean.notice.GatherReply;
import dong.lan.model.bean.user.Tourist;
import dong.lan.model.features.IBaseReply;
import dong.lan.model.features.IGatherReply;
import dong.lan.smarttrip.presentation.presenter.features.IGatherInfoPresenter;
import dong.lan.smarttrip.presentation.viewfeatures.GatherInfoView;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by 梁桂栋 on 17-3-28 ： 下午9:43.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class GatherInfoPresenter implements IGatherInfoPresenter {

    private static final String TAG = GatherInfoPresenter.class.getSimpleName();
    private GatherInfoView view;
    private RealmResults<GatherReply> gatherReplies;

    public GatherInfoPresenter(GatherInfoView view) {
        this.view = view;
    }


    private List<GatherReply> replies;
    private HashMap<String, GatherReply> replyMap = new HashMap<>();
    private int all = 0;

    @Override
    public void start(final String travelId, final String id) {
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Gather gather = realm.where(Gather.class).equalTo("id", id)
                        .findFirst();
                if (gather == null) {
                    view.initView(null);
                } else {
                    view.initView(realm.copyFromRealm(gather));
                }
            }
        });
        gatherReplies = Realm.getDefaultInstance().where(GatherReply.class)
                .equalTo("gather.id", id).findAllAsync();
        view.initList(gatherReplies);


        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (gatherReplies != null) {
                    for (GatherReply r : gatherReplies) {
                        replyMap.put(r.getTourist().getIdentifier(), r);
                    }
                }
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
                        GatherReply reply = new GatherReply();
                        reply.tourist = tourist;
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
