package dong.lan.smarttrip.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.qcloud.ui.TabIndicator;

import dong.lan.smarttrip.R;
import com.tencent.qcloud.ui.base.BaseFragment;

/**
 * Created by 梁桂栋 on 17-1-28 ： 下午11:37.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class TripFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, TabIndicator.TabClickListener {


    public static BaseFragment newInstance(String tittle) {
        TripFragment fragment = new TripFragment();
        Bundle b = new Bundle();
        b.putString(KEY_TITTLE, tittle);
        fragment.setArguments(b);
        return fragment;
    }


    private SwipeRefreshLayout refresher;
    private RecyclerView tripsList;
    private TabIndicator tabIndicator;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (content == null) {
            content = inflater.inflate(R.layout.fragment_trips, container, false);

            tabIndicator = (TabIndicator) content.findViewById(R.id.trips_tabs);
            refresher = (SwipeRefreshLayout) content.findViewById(R.id.trips_refresh);
            tripsList = (RecyclerView) content.findViewById(R.id.trips_list);

            tabIndicator.setTabClickListener(this);
            refresher.setOnRefreshListener(this);
            tripsList.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        return content;
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onTabClick(int index) {
        toast("tab:" + index);
    }
}
