package dong.lan.smarttrip.ui.notice;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dong.lan.model.Config;
import dong.lan.model.bean.notice.Notice;
import dong.lan.model.bean.notice.NoticeReply;
import dong.lan.model.utils.TimeUtil;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.binder.NoticeReplyBinder;
import dong.lan.smarttrip.presentation.presenter.NoticeInfoPresenter;
import dong.lan.smarttrip.presentation.presenter.features.INoticeInfoPresenter;
import dong.lan.smarttrip.presentation.viewfeatures.NoticeInfoView;
import com.tencent.qcloud.ui.base.BaseBarActivity;
import dong.lan.smarttrip.ui.customview.LabelTextView;
import io.realm.RealmResults;

public class NoticeInfoActivity extends BaseBarActivity implements NoticeInfoView {


    @BindView(R.id.notice_detail)
    TextView noticeDetailTv;
    @BindView(R.id.notice_time_info)
    TextView timeInfoTv;
    @BindView(R.id.list_switcher)
    LabelTextView switcherLtv;
    @BindView(R.id.notice_users)
    RecyclerView usersList;

    @OnClick(R.id.list_switcher)
    void clickSwitcher() {
        if (binder == null)
            return;
        if (listFlag) {
            binder.showCache(true);
        } else {
            binder.showCache(false);
        }
        presenter.switcherShow(listFlag,switcherLtv);
        if (usersList.getAdapter() != null)
            usersList.getAdapter().notifyDataSetChanged();
        listFlag = !listFlag;
    }

    private boolean listFlag = true;
    private INoticeInfoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gather_info);
        bindView("通知详情");
        String id = getIntent().getStringExtra("id");
        String travelId = getIntent().getStringExtra(Config.TRAVEL_ID);
        String creatorId = getIntent().getStringExtra(Config.IDENTIFIER);
        presenter = new NoticeInfoPresenter(this);
        presenter.start(travelId,id);
    }

    private NoticeReplyBinder binder;

    @Override
    public void initView(final Notice notice) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (notice == null) {
                    toast("集合信息已失效");
                    return;
                }
                usersList.setLayoutManager(new GridLayoutManager(NoticeInfoActivity.this, 1));
                noticeDetailTv.setText(notice.getShowContent());
                timeInfoTv.setText("集合时间: " + TimeUtil.getTime(notice.time, "yyyy.MM.dd HH:mm"));

            }
        });
    }

    @Override
    public void initList(RealmResults<NoticeReply> noticeReplies) {
        binder = new NoticeReplyBinder();
        binder.init(noticeReplies);
        usersList.setAdapter(binder.build());

    }

    @Override
    public void initCacheList(List<NoticeReply> replies) {
        if(binder!=null)
            binder.setCache(replies);
    }

    @Override
    public Activity activity() {
        return this;
    }
}
