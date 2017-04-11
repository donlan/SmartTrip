package dong.lan.weather.api;

import java.io.IOException;

import dong.lan.weather.Api;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 梁桂栋 on 17-4-9 ： 上午12:34.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class ApiInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl httpUrl = request.url().newBuilder()
                .addQueryParameter("key", Api.API_KEY)
                .build();

        return chain.proceed(request.newBuilder().url(httpUrl).build());
    }
}
