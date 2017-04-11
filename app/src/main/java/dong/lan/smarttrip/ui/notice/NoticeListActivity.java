package dong.lan.smarttrip.ui.notice;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import java.util.List;

import butterknife.BindView;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.base.BinderClickListener;
import dong.lan.smarttrip.adapters.binder.NoticeBinder;
import dong.lan.model.Config;
import dong.lan.model.bean.notice.NoticeShow;
import dong.lan.smarttrip.presentation.presenter.NoticeListPresenter;
import dong.lan.smarttrip.presentation.presenter.features.INoticeListPresenter;
import dong.lan.smarttrip.presentation.viewfeatures.NoticeListView;
import com.tencent.qcloud.ui.base.BaseBarActivity;

public class NoticeListActivity extends BaseBarActivity implements NoticeListView {


    @BindView(R.id.notice_list)
    RecyclerView noticeListView;


    private INoticeListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ntice_list);
        int type = getIntent().getIntExtra(Config.NOTICE_TYPE, -1);
        String tittle = getIntent().getStringExtra(Config.BAR_TITTLE);
        String identifier = getIntent().getStringExtra(Config.IDENTIFIER);
        String travelId = getIntent().getStringExtra(Config.TRAVEL_ID);
        bindView(tittle);
        if (TextUtils.isEmpty(identifier) || TextUtils.isEmpty(travelId)) {
            toast("无效的用户id");
            return;
        }
        presenter = new NoticeListPresenter(this);
        presenter.loadNotice(type, identifier, travelId);
        noticeListView.setLayoutManager(new GridLayoutManager(this, 1));
    }

    @Override
    public Activity activity() {
        return this;
    }


    private BinderClickListener<NoticeShow> listener = new BinderClickListener<NoticeShow>() {
        @Override
        public void onClick(NoticeShow data, int position, int action) {
            //删除通知
            if (action == 1) {
                noticeListView.getAdapter().notifyItemRemoved(position);
                data.delete();
            }
        }
    };

    @Override
    public void initView(final List<? extends NoticeShow> noticeShows) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (noticeShows == null || noticeShows.isEmpty()) {
                        toast("无通知");
                    } else {
                        NoticeBinder binder = new NoticeBinder();
                        binder.init((List<NoticeShow>) noticeShows);
                        binder.setBinderClickListener(listener);
                        noticeListView.setAdapter(binder.build());
                    }
                }
            });
    }
}
