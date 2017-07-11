package dong.lan.weather.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import base.RotateImageButton;
import dong.lan.weather.Api;
import dong.lan.weather.adapter.DayWeatherAdapter;
import dong.lan.weather.adapter.HourlyWeatherAdapter;
import dong.lan.weather.R;
import dong.lan.weather.ToastUtil;
import dong.lan.weather.WeatherRealm;
import dong.lan.weather.adapter.WeatherSuggestAdapter;
import dong.lan.weather.bean.Weather;
import dong.lan.weather.bean.WeatherCity;
import dong.lan.weather.bean.WeatherItem;
import dong.lan.weather.bean.weather.HeWeatherItem;
import dong.lan.weather.bean.weather.WeatherResult;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 梁桂栋 on 17-7-6 ： 下午12:01.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class WeatherPagerFragment extends Fragment {


    public static Fragment newInstance(String queryCode) {
        Bundle bundle = new Bundle();
        bundle.putString("code", queryCode);
        bundle.putLong("id",queryCode.hashCode());
        WeatherPagerFragment fragment = new WeatherPagerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private View content;


    private RotateImageButton refresher;
    private RecyclerView hourlyList;
    private RecyclerView sugList;
    private RecyclerView dayList;
    private TextView locationTv;
    private TextView curTmpTv;
    private TextView tmpRangTv;
    private TextView condTv;
    private TextView windDirTv;
    private TextView windInfoTv;

    private HourlyWeatherAdapter hourlyWeatherAdapter;
    private WeatherSuggestAdapter suggestAdapter;
    private DayWeatherAdapter dayWeatherAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (content == null) {
            content = inflater.inflate(R.layout.fragment_weather_pager, container, false);
            refresher = (RotateImageButton) content.findViewById(R.id.refresher);

            refresher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refresher.start();
                    queryNewWeatherInfo();
                }
            });

            hourlyList = (RecyclerView) content.findViewById(R.id.hourly_list);
            dayList = (RecyclerView) content.findViewById(R.id.day_list);
            sugList = (RecyclerView) content.findViewById(R.id.sug_list);

            curTmpTv = (TextView) content.findViewById(R.id.cur_tmp);
            tmpRangTv = (TextView) content.findViewById(R.id.tmp_rang);
            condTv = (TextView) content.findViewById(R.id.cur_cond);
            windDirTv = (TextView) content.findViewById(R.id.wind_dir);
            windInfoTv = (TextView) content.findViewById(R.id.wind_info);
            locationTv = (TextView) content.findViewById(R.id.location_address);

            hourlyList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            dayList.setLayoutManager(new GridLayoutManager(getContext(), 1));
            sugList.setLayoutManager(new GridLayoutManager(getContext(), 2));


            Realm realm = WeatherRealm.get().realm();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    WeatherItem weatherItem = realm.where(WeatherItem.class)
                            .equalTo("id", getArguments().getString("code"))
                            .findFirst();
                    if (weatherItem == null) {
                        queryNewWeatherInfo();
                    } else {
                        weatherItem.parseJSON();
                        showWeather(weatherItem);
                    }
                }
            });
        }
        return content;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    /**
     * 查询天气的回调
     */
    private Callback<WeatherResult> weatherResultCallback = new Callback<WeatherResult>() {
        @Override
        public void onResponse(Call<WeatherResult> call, Response<WeatherResult> response) {
            refresher.finish();
            if(response.body() == null || response.body().HeWeather5.isEmpty()){
                ToastUtil.show(getContext(),"获取天气信息失败");
            }else {
                HeWeatherItem heWeatherItem = response.body().HeWeather5.get(0);
                WeatherItem weatherItem = new WeatherItem();
                weatherItem.generate(response.body());
                Realm realm = WeatherRealm.get().realm();
                realm.beginTransaction();
                WeatherCity weatherCity = realm.where(WeatherCity.class)
                        .equalTo("id",heWeatherItem.basic.id).findFirst();
                weatherItem.weatherCity = realm.copyFromRealm(weatherCity);
                realm.copyToRealmOrUpdate(weatherItem);
                realm.commitTransaction();
                showWeather(weatherItem);
            }
        }

        @Override
        public void onFailure(Call<WeatherResult> call, Throwable t) {
            refresher.finish();
            ToastUtil.show(getContext(), "获取最新天气失败");
        }
    };

    /**
     * 获取最新的天气信息
     */
    private void queryNewWeatherInfo() {
        String queryCode = getArguments().getString("code");
        Api.get().queryWeather(queryCode, weatherResultCallback);
    }


    /**
     * 显示天气信息
     *
     * @param weather
     */
    private void showWeather(Weather weather) {
        curTmpTv.setText(weather.currentTmp());
        tmpRangTv.setText(weather.tmpRang());
        condTv.setText(weather.cond());
        locationTv.setText(weather.city());
        windDirTv.setText(weather.windDir());
        windInfoTv.setText(weather.windInfo());

        if (hourlyWeatherAdapter == null) {
            hourlyWeatherAdapter = new HourlyWeatherAdapter(weather.hourlyWeather());
            hourlyList.setAdapter(hourlyWeatherAdapter);
        } else {
            hourlyWeatherAdapter.reset(weather.hourlyWeather());
            hourlyWeatherAdapter.notifyDataSetChanged();
        }

        if (suggestAdapter == null) {
            suggestAdapter = new WeatherSuggestAdapter(weather.suggestWeather());
            sugList.setAdapter(suggestAdapter);
        } else {
            suggestAdapter.reset(weather.suggestWeather());
            suggestAdapter.notifyDataSetChanged();
        }

        if (dayWeatherAdapter == null) {
            dayWeatherAdapter = new DayWeatherAdapter(weather.dayWeather());
            dayList.setAdapter(dayWeatherAdapter);
        } else {
            dayWeatherAdapter.reset(weather.dayWeather());
            dayWeatherAdapter.notifyDataSetChanged();
        }
    }
}
