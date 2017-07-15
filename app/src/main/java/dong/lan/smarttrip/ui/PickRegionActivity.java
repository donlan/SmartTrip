package dong.lan.smarttrip.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.TextView;

import com.baidu.location.BDLocation;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import dong.lan.map.service.LocationService;
import dong.lan.smarttrip.App;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.binder.CityBinder;
import dong.lan.model.Config;
import base.GsonHelper;
import dong.lan.model.bean.world.City;
import dong.lan.model.bean.world.CityItem;
import dong.lan.model.bean.world.CountryItem;
import dong.lan.model.bean.world.Earth;
import dong.lan.model.bean.world.StationItem;
import dong.lan.model.bean.world.World;
import dong.lan.smarttrip.base.BaseBarActivity;
import dong.lan.smarttrip.ui.customview.IndexRecycleView;
import dong.lan.smarttrip.ui.customview.RecycleViewDivider;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class PickRegionActivity extends BaseBarActivity {

    private static final String TAG = PickRegionActivity.class.getSimpleName();
    @BindView(R.id.pick_region_city_tv)
    TextView regionCity;
    @BindView(R.id.region_list)
    IndexRecycleView regionListView;

    private CityBinder cityBinder;
    private List<City> cities = new ArrayList<>();
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_region);
        bindView("选择城市");

        initView();
    }

    private void initView() {
        realm = Realm.getDefaultInstance();
        App.myApp().locationService().registerCallback(this, new LocationService.LocationCallback() {
            @Override
            public void onLocation(BDLocation location) {
                Log.d(TAG, "onLocation: " + location.getCity());
                regionCity.setText(location.getCity());
            }
        });

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<City> c = realm.where(City.class).findAllSorted("pinyin", Sort.ASCENDING);
                if (c == null || c.isEmpty()) {
                    try {
                        int count = 1;
                        World world = GsonHelper.getInstance().toTarget(new InputStreamReader(getResources().getAssets().open("earth.json")), World.class);
                        Earth earth = world.Earth;
                        int i = 1000;
                        for (CountryItem countryItem : earth.Country) {
                            i += 1000;
                            int j = 100;
                            if (countryItem.Station == null) {
                                addCity(count++, realm, (i + j) + "", countryItem.CountryID, countryItem.CountryName);
                            } else {
                                for (StationItem stationItem : countryItem.Station) {
                                    j++;
                                    int z = 1;
                                    if (stationItem.City == null) {
                                        addCity(count++, realm, (i + j + z) + "", stationItem.StationID, stationItem.StationName);
                                    } else {
                                        for (CityItem cityItem : stationItem.City) {
                                            addCity(count++, realm, (i + j + z++) + "", cityItem.CityID, cityItem.CityName);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    PickRegionActivity.this.cities = realm.copyFromRealm(c);
                }
                setUpCity();
            }
        });

        regionListView.calculateIndex(cities);
        regionListView.setIndexBarBgColor(0xffffffff);
        regionListView.innerRecycleView().addItemDecoration(new RecycleViewDivider(this,
                LinearLayoutManager.VERTICAL, 1, getResources().getColor(R.color.background_gray3)));
        regionListView.innerRecycleView().setLayoutManager(new GridLayoutManager(this, 1));
    }


    private void setUpCity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cityBinder = new CityBinder();
                cityBinder.init(cities);
                regionListView.innerRecycleView().setAdapter(cityBinder.build());
            }
        });
    }

    private void addCity(int count, Realm realm, String id, String origin, String name) {
        City city = realm.createObject(City.class);
        city.setId(id);
        city.setName(name);
        city.setOrigin(origin);
        PickRegionActivity.this.cities.add(realm.copyFromRealm(city));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.myApp().locationService().unregisterCallback(this);
    }

    @Override
    public void finish() {

        if (realm.isInTransaction())
            realm.cancelTransaction();
        realm.close();
        realm = null;

        City city = cityBinder.getCity();
        if (city == null) {
            toast("没有选择城市");
        }
        Intent intent = new Intent();
        intent.putExtra(Config.CITY_NAME, city == null ? "" : city.getName());
        setResult(Config.REGION_RESULT_CODE, intent);
        super.finish();
    }
}
