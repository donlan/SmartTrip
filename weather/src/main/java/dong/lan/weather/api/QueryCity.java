package dong.lan.weather.api;

import dong.lan.weather.bean.search.SearchResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 梁桂栋 on 17-4-9 ： 上午12:18.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface QueryCity {
    @GET("search")
    Call<SearchResult> queryCityIdByLocation(@Query("city") String lnglat);
}
