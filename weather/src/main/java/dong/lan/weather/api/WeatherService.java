package dong.lan.weather.api;

import dong.lan.weather.bean.weather.WeatherResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 梁桂栋 on 17-4-9 ： 下午3:14.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface WeatherService {

    @GET("weather")
    Call<WeatherResult> queryWeather(@Query("city") String city);
}
