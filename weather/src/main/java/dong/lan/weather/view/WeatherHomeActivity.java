package dong.lan.weather.view;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import base.ListFragmentPagerAdapter;
import base.MyFragmentPagerAdapter;
import dong.lan.weather.R;
import dong.lan.weather.WeatherRealm;
import dong.lan.weather.bean.CityDeleteEvent;
import dong.lan.weather.bean.Weather;
import dong.lan.weather.bean.WeatherItem;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class WeatherHomeActivity extends AppCompatActivity {


    private ViewPager mViewPager;
    private ListFragmentPagerAdapter pagerAdapter;
    private List<Fragment> pagers = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_home);

        mViewPager = (ViewPager) findViewById(R.id.weather_home_pager);

        Realm realm = WeatherRealm.get().realm();
        realm.beginTransaction();
        RealmResults<WeatherItem> weatherItems = realm.where(WeatherItem.class)
                .findAllSorted("id", Sort.ASCENDING);
        realm.commitTransaction();
        if(weatherItems!=null){
            for(int i = 0;i<weatherItems.size();i++){
                pagers.add(WeatherPagerFragment.newInstance(weatherItems.get(i).id));
            }
        }
        realm.close();
        pagers.add(CityManagerFragment.newInstance());
        pagers.add(CityQueryFragment.newInstance());
        pagerAdapter = new ListFragmentPagerAdapter(getSupportFragmentManager(),pagers);
        mViewPager.setAdapter(pagerAdapter);

        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWeatherItemAdded(WeatherItem weatherItem){
        pagers.add(0,WeatherPagerFragment.newInstance(weatherItem.id));
        pagerAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCityDeleted(CityDeleteEvent event){
        for(int i = 0;i<pagers.size();i++){
            if(pagers.get(i).getArguments().getLong("id") == event.id){
                pagers.remove(i);
                pagerAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
