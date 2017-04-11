package dong.lan.smarttrip.ui.travel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;


import com.tencent.TIMConversationType;
import com.tencent.qcloud.ui.TabIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import dong.lan.smarttrip.R;
import dong.lan.model.Config;
import dong.lan.smarttrip.common.EasyFragmentManager;
import com.tencent.qcloud.ui.base.BaseActivity;
import dong.lan.smarttrip.ui.im.ChatFragment;

public class TravelingActivity extends BaseActivity implements TabIndicator.TabClickListener {

    private static final String TAG = TravelingActivity.class.getSimpleName();
    @BindView(R.id.traveling_tabs)
    TabIndicator tabIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveling);
        bindView();
        initView();
    }

    private EasyFragmentManager easyFragmentManager;

    private void initView() {

        String id = getIntent().getStringExtra(Config.TRAVEL_ID);
        tabIndicator.setTabClickListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();

        Log.d(TAG, "initView: "+id);
        ChatFragment chatFragment = ChatFragment.newInstance(id, TIMConversationType.Group);
        TravelingInfoFragment infoFragment = TravelingInfoFragment.newInstance(id);
        TravelingMapFragment mapFragment = TravelingMapFragment.newInstance(id);

        List<Fragment> fras = new ArrayList<>(3);
        fras.add(chatFragment);
        fras.add(mapFragment);
        fras.add(infoFragment);
        easyFragmentManager = new EasyFragmentManager(R.id.container, fragmentManager, fras);
    }


    private void checkPage(int pos) {
        easyFragmentManager.show(pos);
    }

    @Override
    public void onTabClick(int index) {
        checkPage(index);
    }
}
