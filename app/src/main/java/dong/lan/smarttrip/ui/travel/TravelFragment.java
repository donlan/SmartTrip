package dong.lan.smarttrip.ui.travel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;

import dong.lan.model.features.ITravel;
import dong.lan.permission.Permission;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.base.BinderClickListener;
import dong.lan.model.Config;
import dong.lan.smarttrip.adapters.binder.TravelBinder;
import dong.lan.smarttrip.presentation.presenter.TravelPresenter;
import dong.lan.smarttrip.presentation.presenter.features.ITravelsDisplayMenu;
import dong.lan.smarttrip.presentation.viewfeatures.TravelView;
import com.tencent.qcloud.ui.base.BaseFragment;
import dong.lan.smarttrip.ui.customview.popupView.MenuPopup;
import dong.lan.smarttrip.ui.customview.RecycleViewDivider;

/**
 * Created by 梁桂栋 on 16-10-8 ： 下午4:15.
 * Email:       760625325@qqcom
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class TravelFragment extends BaseFragment implements TravelView, MenuPopup.ItemClickListener, BinderClickListener<ITravel> {


    private static final String TAG = TravelFragment.class.getSimpleName();

    public static BaseFragment newInstance(String tittle) {
        TravelFragment fragment = new TravelFragment();
        Bundle b = new Bundle();
        b.putString(KEY_TITTLE, tittle);
        fragment.setArguments(b);
        return fragment;
    }

    ITravelsDisplayMenu presenter;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    EditText searchEt;
    ImageButton travelMore;
    private MenuPopup menuPopup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (content == null) {
            content = inflater.inflate(R.layout.fragment_travels, container, false);
            start(null);
        }
        return content;
    }

    private void initView() {
        refreshLayout = (SwipeRefreshLayout) content.findViewById(R.id.travel_srl);
        recyclerView = (RecyclerView) content.findViewById(R.id.travels_list);
        searchEt = (EditText) content.findViewById(R.id.travel_search);
        travelMore = (ImageButton) content.findViewById(R.id.travel_menu_more);
        travelMore.setImageResource(R.drawable.more);
        travelMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuPopup == null) {
                    menuPopup = new MenuPopup(getContext());
                    menuPopup.addItem(new MenuPopup.ItemBean(R.drawable.new_trave, "创建旅行"))
                            .addItem(new MenuPopup.ItemBean(R.drawable.join_trave, "加入旅行"))
                            .addItem(new MenuPopup.ItemBean(R.drawable.scan, "扫一扫"))
                            .build().setClickListener(TravelFragment.this);
                }
                if (!menuPopup.dismiss())
                    menuPopup.show(travelMore, 0, 10, Gravity.CENTER);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorAccentDark,
                R.color.colorPrimaryDark
        );
        refreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });
        recyclerView.addOnScrollListener(scrollListener);
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL, R.drawable.recycle_divider));
        presenter = new TravelPresenter(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recyclerView != null)
            recyclerView.removeOnScrollListener(scrollListener);
        Permission.instance().onDestroy();
    }


    @Override
    public void onClick(int position) {
        switch (position) {
            case 0:
                presenter.createNewTravel();
                break;
            case 1:
                presenter.joinTravel();
                break;
            case 2:
                presenter.startQRScan();
                break;
        }
        menuPopup.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == Config.REQUEST_CODE_ZXING) {
            Bundle res = data.getExtras();
            String resStr = res.getString("result");
            Log.d(TAG, "onActivityResult: " + resStr);
            presenter.handleScanResult(resStr);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Permission.instance().handleRequestResult(getActivity(), requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void stopRefresh() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void initAdapter(List<? extends ITravel> travels) {
        TravelBinder binder = new TravelBinder();
        binder.init((List<ITravel>) travels);
        binder.setBinderClickListener(this);
        recyclerView.setAdapter(binder.build());
    }

    @Override
    public void refreshDisplayView(int pos) {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public Activity activity() {
        return getActivity();
    }

    @Override
    public void onClick(ITravel data, int position, int action) {
        Log.d(TAG, "onClick: "+data);
        if (action == 0) {
            Intent travelingIntent = new Intent(getContext(), TravelingActivity.class);
            travelingIntent.putExtra(Config.TRAVEL_ID, data.getId());
            startActivity(travelingIntent);
        } else {
            presenter.deleteTravel(data.getId());
        }
    }

    @Override
    public void start(Object data) {
        if (isAdded() && isStart) {
            initView();
            presenter.loadTravels();
        }
        Log.d(TAG, "start: "+isStart+","+isAdded());
        isStart = true;
    }
}
