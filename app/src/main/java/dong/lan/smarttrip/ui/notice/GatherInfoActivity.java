package dong.lan.smarttrip.ui.notice;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dong.lan.model.bean.notice.Gather;
import dong.lan.model.bean.notice.GatherReply;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.binder.GatherReplyBinder;
import dong.lan.model.Config;
import dong.lan.smarttrip.presentation.presenter.GatherInfoPresenter;
import dong.lan.smarttrip.presentation.presenter.features.IGatherInfoPresenter;
import dong.lan.smarttrip.presentation.viewfeatures.GatherInfoView;
import dong.lan.smarttrip.base.BaseBarActivity;
import dong.lan.model.utils.TimeUtil;
import dong.lan.smarttrip.ui.customview.LabelTextView;
import io.realm.RealmResults;

public class GatherInfoActivity extends BaseBarActivity implements GatherInfoView {


    @BindView(R.id.gather_address_info)
    TextView addressInfoTv;
    @BindView(R.id.gather_detail)
    TextView gatherDetailTv;
    @BindView(R.id.gather_time_info)
    TextView timeInfoTv;
    @BindView(R.id.list_switcher)
    LabelTextView switcherLtv;
    @BindView(R.id.gather_users)
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
    private IGatherInfoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gather_info);
        bindView("集合详情");
        String id = getIntent().getStringExtra("id");
        String travelId = getIntent().getStringExtra(Config.TRAVEL_ID);
        String creatorId = getIntent().getStringExtra(Config.IDENTIFIER);
        presenter = new GatherInfoPresenter(this);
        presenter.start(travelId,id);
    }

    private GatherReplyBinder binder;

    @Override
    public void initView(final Gather gather) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (gather == null) {
                    toast("集合信息已失效");
                    return;
                }
                usersList.setLayoutManager(new GridLayoutManager(GatherInfoActivity.this, 1));
                addressInfoTv.setText("集合地点: " + gather.address);
                gatherDetailTv.setText(gather.getShowContent());
                timeInfoTv.setText("集合时间: " + TimeUtil.getTime(gather.time, "yyyy.MM.dd HH:mm"));

            }
        });
    }

    @Override
    public void initList(RealmResults<GatherReply> gatherReplies) {
        binder = new GatherReplyBinder();
        binder.init(gatherReplies);
        usersList.setAdapter(binder.build());

    }

    @Override
    public void initCacheList(List<GatherReply> replies) {
        if(binder!=null)
            binder.setCache(replies);
    }

    @Override
    public Activity activity() {
        return this;
    }
}
