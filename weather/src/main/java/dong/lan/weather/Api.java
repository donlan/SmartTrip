package dong.lan.weather;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import dong.lan.weather.api.ApiInterceptor;
import dong.lan.weather.api.QueryCity;
import dong.lan.weather.api.WeatherService;
import dong.lan.weather.bean.search.SearchResult;
import dong.lan.weather.bean.weather.WeatherResult;
import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 梁桂栋 on 17-4-9 ： 上午12:05.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public final class Api {
    public static final String API_KEY = "dea52fcd0f254f5d90e2691fc5f672da";
    private static final String TAG = Api.class.getSimpleName();

    private static Api api;
    private Retrofit retrofit;

    private Api() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new ApiInterceptor())
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.heweather.com/v5/")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Api get() {
        if (api == null)
            api = new Api();
        return api;
    }

    public void queryCityIdByLocation(String lnglat, Callback<SearchResult> callback) {
        QueryCity queryCity = retrofit.create(QueryCity.class);
        String s = queryCity.queryCityIdByLocation(lnglat).request().url().url().toString();
        Log.d(TAG, "queryCityIdByLocation: " + s);
        queryCity.queryCityIdByLocation(lnglat).enqueue(callback);
    }


    public void queryWeather(String city, Callback<WeatherResult> callback) {
        WeatherService weatherService = retrofit.create(WeatherService.class);
        weatherService.queryWeather(city).enqueue(callback);
    }
}
