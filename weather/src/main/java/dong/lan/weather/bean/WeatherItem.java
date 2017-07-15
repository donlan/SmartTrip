package dong.lan.weather.bean;


import android.text.TextUtils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import base.GsonHelper;
import base.TimeUtil;
import dong.lan.weather.bean.weather.Air;
import dong.lan.weather.bean.weather.Comf;
import dong.lan.weather.bean.weather.Cw;
import dong.lan.weather.bean.weather.DailyForecastItem;
import dong.lan.weather.bean.weather.Drsg;
import dong.lan.weather.bean.weather.Flu;
import dong.lan.weather.bean.weather.HeWeatherItem;
import dong.lan.weather.bean.weather.HourlyForecastItem;
import dong.lan.weather.bean.weather.Sport;
import dong.lan.weather.bean.weather.Trav;
import dong.lan.weather.bean.weather.Uv;
import dong.lan.weather.bean.weather.WeatherResult;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 梁桂栋 on 17-7-8 ： 下午10:05.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: 天气信息封装
 */

public class WeatherItem extends RealmObject implements Weather {

    @PrimaryKey
    public String id;                   //城市id
    public WeatherCity weatherCity;     //城市信息
    public long lastUpdateTime;         //最后一次更新的时间
    public String tmp;                  //当前温度
    public String tmpRang;              //温度范围
    public String windDir;              //风向
    public String windInfo;             //其他风信息（风力，速度）
    public String cond;                 //天气情况


    public String suggests;             //天气相关建议
    public String dayWeathers;          //天气日报
    public String weatherHourlies;      //天气小时报


    @Ignore
    private
    List<WeatherSuggest> weatherSuggets;
    @Ignore
    private
    List<DayWeather> dayWeather;
    @Ignore
    private
    List<WeatherHourly> weatherHourly;


    /**
     * 将网络请求到的天气信息进行解析
     * @param weatherResult
     */
    public void generate(WeatherResult weatherResult) {
        HeWeatherItem weatherItem = weatherResult.HeWeather5.get(0);
        id = weatherItem.basic.id;
        lastUpdateTime = TimeUtil.stringToLong(weatherItem.basic.update.loc, "yyyy-MM-dd HH:mm");
        tmp = weatherItem.now.tmp + "°";
        windDir = weatherItem.now.wind.dir;
        windInfo = weatherItem.now.wind.sc + "(" + weatherItem.now.wind.spd + "米/秒) ";
        cond = weatherItem.now.cond.txt;

        if (weatherSuggets == null)
            weatherSuggets = new ArrayList<>();
        weatherSuggets.clear();
        Air air = weatherItem.suggestion.air;
        weatherSuggets.add(new Suggest("空气指数", air.brf, air.txt));
        Comf comf = weatherItem.suggestion.comf;
        weatherSuggets.add(new Suggest("舒适指数", comf.brf, comf.txt));
        Cw cw = weatherItem.suggestion.cw;
        weatherSuggets.add(new Suggest("穿衣指数", cw.brf, cw.txt));
        Drsg drsg = weatherItem.suggestion.drsg;
        weatherSuggets.add(new Suggest("穿衣指数", drsg.brf, drsg.txt));
        Flu flu = weatherItem.suggestion.flu;
        weatherSuggets.add(new Suggest("流感指数", flu.brf, flu.txt));
        Sport sport = weatherItem.suggestion.sport;
        weatherSuggets.add(new Suggest("运动指数", sport.brf, sport.txt));
        Trav trav = weatherItem.suggestion.trav;
        weatherSuggets.add(new Suggest("出行指数", trav.brf, trav.txt));
        Uv uv = weatherItem.suggestion.uv;
        weatherSuggets.add(new Suggest("紫外线指数", uv.brf, uv.txt));
        if (weatherHourly == null)
            weatherHourly = new ArrayList<>();
        weatherHourly.clear();
        Collections.sort(weatherItem.hourly_forecast, new Comparator<HourlyForecastItem>() {
            @Override
            public int compare(HourlyForecastItem o1, HourlyForecastItem o2) {
                return o1.date.compareTo(o2.date);
            }
        });
        List<HourlyForecastItem> hs = weatherItem.hourly_forecast;
        for (int i = 0; i < hs.size(); i++) {
            HourlyForecastItem item = hs.get(i);
            weatherHourly.add(new HourlyWeather(item.date.substring(10), item.tmp, item.cond.txt, item.wind.dir, item.wind.sc));
        }


        Collections.sort(weatherItem.daily_forecast, new Comparator<DailyForecastItem>() {
            @Override
            public int compare(DailyForecastItem o1, DailyForecastItem o2) {
                return o1.date.compareTo(o2.date);
            }
        });
        if (dayWeather == null)
            dayWeather = new ArrayList<>();
        dayWeather.clear();

        List<DailyForecastItem> df = weatherItem.daily_forecast;
        for (int i = 0; i < df.size(); i++) {
            DailyForecastItem item = df.get(i);
            dayWeather.add(new WeatherDay(item.date.substring(5), item.cond.txt_d + " " + item.cond.txt_n,
                    item.tmp.min + "~" + item.tmp.max + "℃", item.wind.dir + " " + item.wind.sc));
        }

        tmpRang = dayWeather.get(0).tmp();

        suggests = GsonHelper.getInstance().toJson(weatherSuggets);
        weatherHourlies = GsonHelper.getInstance().toJson(weatherHourly);
        dayWeathers = GsonHelper.getInstance().toJson(dayWeather);
    }

    /**
     * 将日报，小时报，天气建议的json字符串重新解析成列表信息
     */
    public void parseJSON() {
        if (TextUtils.isEmpty(suggests) || TextUtils.isEmpty(weatherHourlies) || TextUtils.isEmpty(dayWeathers)) {
            return;
        }
        if (weatherSuggets == null)
            weatherSuggets = new ArrayList<>();
        if (weatherHourly == null)
            weatherHourly = new ArrayList<>();
        if (dayWeather == null)
            dayWeather = new ArrayList<>();
        weatherSuggets.clear();
        weatherHourly.clear();
        dayWeather.clear();
        weatherSuggets.addAll(GsonHelper.getInstance().toArray(suggests, Suggest[].class));
        weatherHourly.addAll(GsonHelper.getInstance().toArray(weatherHourlies, HourlyWeather[].class));
        dayWeather.addAll(GsonHelper.getInstance().toArray(dayWeathers, WeatherDay[].class));
        if (dayWeather != null) {
            tmpRang = dayWeather.get(0).tmp();
        }
    }


    @Override
    public String id() {
        return id;
    }

    @Override
    public long lastQueryTime() {
        return lastUpdateTime;
    }

    @Override
    public String currentTmp() {
        return tmp;
    }

    @Override
    public String tmpRang() {
        return tmpRang;
    }

    @Override
    public String cond() {
        return cond;
    }

    @Override
    public String windDir() {
        return windDir;
    }

    @Override
    public String windInfo() {
        return windInfo;
    }

    @Override
    public String city() {
        return weatherCity.name;
    }

    @Override
    public List<WeatherHourly> hourlyWeather() {
        return weatherHourly;
    }

    @Override
    public List<WeatherSuggest> suggestWeather() {
        return weatherSuggets;
    }

    @Override
    public List<DayWeather> dayWeather() {
        return dayWeather;
    }
}
