package dong.lan.weather;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.annotations.RealmModule;

/**
 * Created by 梁桂栋 on 17-4-3 ： 下午11:56.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class WeatherRealm {

    private  Realm realm;
    private static WeatherRealm weatherRealm;
    private RealmConfiguration configuration;

    public static WeatherRealm get(){
        if(weatherRealm == null)
            weatherRealm = new WeatherRealm();
        return weatherRealm;
    }

    public void init(){
        configuration = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .name("weather")
                .modules(new MyWeatherModule())
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(configuration);
    }


    public Realm realm(){
        if(realm == null || realm.isClosed())
            realm = Realm.getInstance(configuration);
        return realm;
    }

}
