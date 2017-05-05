package dong.lan.smarttrip.ui.travel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import butterknife.BindView;
import dong.lan.model.Config;
import dong.lan.model.bean.travel.Node;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.TimeLineAdapter;
import dong.lan.smarttrip.base.BaseActivity;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by 梁桂栋 on 2017/5/4.
 * Email: 760625325@qq.com
 * Github: github.com/donlan
 */

public class TravelTimeLineActivity extends BaseActivity {


    @BindView(R.id.time_line_list)
    RecyclerView timeLineView;

    private String travelId;

    private TimeLineAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_time_line);
        bindView();
        timeLineView.setLayoutManager(new GridLayoutManager(this, 1));
        travelId = getIntent().getStringExtra(Config.TRAVEL_ID);
        if (TextUtils.isEmpty(travelId)) {
            finish();
        } else {
            RealmResults<Node> nodes = Realm.getDefaultInstance()
                    .where(Node.class)
                    .equalTo("travel.id", travelId)
                    .findAllSortedAsync("arrivedTime", Sort.DESCENDING);
            adapter = new TimeLineAdapter(nodes);
            timeLineView.setAdapter(adapter);
        }
    }
}
