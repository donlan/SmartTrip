package dong.lan.model;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by 梁桂栋 on 17-4-3 ： 下午11:56.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class RealmInit {

    public static void init(Context appContext){
        Realm.init(appContext);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .name("smarttrip")
                .modules(new MyTravelModule())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
    }
}
