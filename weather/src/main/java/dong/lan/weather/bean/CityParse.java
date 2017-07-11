package dong.lan.weather.bean;

import android.content.Context;

import com.blankj.ALog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import dong.lan.weather.WeatherRealm;
import io.realm.Realm;

/**
 * Created by 梁桂栋 on 17-7-8 ： 下午10:16.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class CityParse {

    private CityParse() {
        //no instance
    }

    public static void parse(Context context)  {
        Realm realm = WeatherRealm.get().realm();
        try {
            InputStream is = context.getResources().getAssets().open("city-list.txt");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String s = "";
            realm.beginTransaction();
            while ((s = br.readLine()) != null) {
                String[] ss = s.split(",");
                ALog.d(s);
                WeatherCity weatherCity = new WeatherCity(ss[0], ss[1].trim(), ss[2].trim());
                realm.copyToRealm(weatherCity);
            }
            realm.commitTransaction();


            is = context.getResources().getAssets().open("scenic-list.txt");
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            realm.beginTransaction();
            while ((s = br.readLine()) != null) {
                String[] ss = s.split(",");
                WeatherCity weatherCity = new WeatherCity(ss[0], "", ss[1].trim());
                realm.copyToRealm(weatherCity);
            }
            realm.commitTransaction();

            is = context.getResources().getAssets().open("world-top-list.txt");
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            realm.beginTransaction();
            while ((s = br.readLine()) != null) {
                String[] ss = s.split(",");
                WeatherCity weatherCity = new WeatherCity(ss[0], ss[1].trim(), ss[2].trim());
                realm.copyToRealm(weatherCity);
            }
            realm.commitTransaction();
        }catch (Exception e){
            e.printStackTrace();
            if(realm.isInTransaction()){
                realm.cancelTransaction();
            }
            realm.close();
        }
        if(!realm.isClosed())
            realm.close();
    }
}
