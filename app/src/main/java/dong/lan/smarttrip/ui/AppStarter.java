package dong.lan.smarttrip.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.MyPagerAdapter;
import dong.lan.smarttrip.common.UserManager;
import dong.lan.smarttrip.ui.customview.LabelTextView;

/**
 * Created by 梁桂栋 on 17-1-10 ： 下午11:10.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class AppStarter extends Activity {
    private static final String TAG = AppStarter.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isFirst = UserManager.isFirst();
        if (isFirst) {
            setContentView(R.layout.first_loading);
            firstLoadingInit();
        } else {
            startActivity(new Intent(this, SplashActivity.class));
            finish();
        }

    }

    //第一次使用时加载欢迎页面
    private void firstLoadingInit() {
        List<View> loadingViews = new ArrayList<>();
        View loadingOneView = getLayoutInflater().inflate(R.layout.loading_page_one, null);
        View loadingTwoView = getLayoutInflater().inflate(R.layout.loading_page_two, null);
        View loadingThreeView = getLayoutInflater().inflate(R.layout.loading_page_three, null);

        View loadingFourView = getLayoutInflater().inflate(R.layout.loading_page_four, null);
        final LabelTextView startLtv = (LabelTextView) loadingFourView.findViewById(R.id.start_app);
        startLtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppStarter.this, SplashActivity.class));
                finish();
            }
        });

        loadingViews.add(loadingOneView);
        loadingViews.add(loadingTwoView);
        loadingViews.add(loadingThreeView);
        loadingViews.add(loadingFourView);
        ViewPager loadingViewpager = (ViewPager) findViewById(R.id.loading_viewpager);
        loadingViewpager.setAdapter(new MyPagerAdapter(loadingViews));

    }
}
