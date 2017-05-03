package dong.lan.smarttrip.ui.travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.avos.avoscloud.AVObject;
import com.blankj.ALog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import dong.lan.model.Config;
import dong.lan.model.bean.travel.Node;
import dong.lan.model.utils.TimeUtil;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.TravelLinePagerAdapter;
import dong.lan.smarttrip.base.BaseBarActivity;
import dong.lan.smarttrip.event.NodeEvent;
import dong.lan.smarttrip.ui.customview.AddLineNodeListView;
import io.realm.Realm;

public class AddTravelLineActivity extends BaseBarActivity implements AddLineNodeListView.OnNodeClickListener {

    private static final String TAG = AddTravelLineActivity.class.getSimpleName();
    @BindView(R.id.pager_container)
    ViewPager pagerContainer;


    private AddLineNodeListView curNodeListView;
    private long startTime;
    private long endTime;
    private int days;
    private String travelId;
    private TravelLinePagerAdapter travelLinePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel_line);

        startTime = getIntent().getLongExtra(Config.TRAVEL_START_TIME, 0);
        endTime = getIntent().getLongExtra(Config.TRAVEL_END_TIME, 0);
        travelId = getIntent().getStringExtra(Config.TRAVEL_ID);
        days = TimeUtil.getTimeDays(startTime, endTime);
        bindView("旅行路线(共" + days + "天)");
        initView();
    }


    private void initView() {


        String travelId = getIntent().getStringExtra(Config.TRAVEL_ID);

        travelLinePagerAdapter =
                new TravelLinePagerAdapter(this, travelId, days, startTime, this);
        pagerContainer.setAdapter(travelLinePagerAdapter);
        pagerContainer.setCurrentItem(0);

        EventBus.getDefault().register(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNodeEvent(NodeEvent nodeEvent) {
        Node node = nodeEvent.getNode();
        node.setTag(travelId);
        travelLinePagerAdapter.addNode(node);
        curNodeListView.addNode(node);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onNodeClick(AddLineNodeListView view, final Node node, int position, int action) {
        if (action == AddLineNodeListView.ACTION_ADD) {
            curNodeListView = view;
            Intent nodeIntent = new Intent(AddTravelLineActivity.this, TravelNewNodeActivity.class);
            nodeIntent.putExtra(Config.NODE_DAY_TIME, curNodeListView.getNodeTime());
            startActivity(nodeIntent);

        } else if (action == AddLineNodeListView.ACTION_DELETE) {
            AVObject.createWithoutData("Node",node.getObjId()).deleteEventually();
            view.deleteNode(position);
            Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    if(node.isManaged())
                        node.deleteFromRealm();
                    else
                        realm.where(Node.class).equalTo("objId",node.getObjId())
                                .findAll().deleteAllFromRealm();
                }
            });
        } else if (action == AddLineNodeListView.ACTION_NODE) {
            ALog.d("AddTravelLineActivity", (node.toString()));
        }
    }

}
