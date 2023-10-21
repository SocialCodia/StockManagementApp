
package com.socialcodia.stockmanagement.apis;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.socialcodia.stockmanagement.SocialCodia;
import com.socialcodia.stockmanagement.helper.Helper;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.socialcodia.stockmanagement.configs.Config.BASE_URL;

public class ApiClient {

    public static final String HEADER_CACHE_CONTROL = "Cache-Control";

    public static final String HEADER_PRAGMA = "Pragma";
    private static ApiClient mInstance;

    private Retrofit retrofit;
    public static final long CACHE_SIZE = 5 * 1024 * 1024;

    private ApiClient()
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

   private static OkHttpClient okHttpClient(){
        return new OkHttpClient.Builder()
                .cache(cache())
                .addInterceptor(httpLoggingInterceptor()) //
                // used if network off OR on
                .addNetworkInterceptor(networkInterceptor()) // only used when network is on
                .addInterceptor(offlineInterceptor())
                .build();
    }

    private static Cache cache()
    {
        return new Cache(new File(SocialCodia.getInstance().getCacheDir(),"socialcodia"),CACHE_SIZE);
    }

    private static HttpLoggingInterceptor httpLoggingInterceptor()
    {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("mufazmi",message);
            }        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    private static Interceptor networkInterceptor()
    {
        return chain -> {
            Response response =chain.proceed(chain.request());
            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(2, TimeUnit.SECONDS)
                    .build();

            return response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL,cacheControl.toString())
                    .build();
        };
    }

    public static Interceptor offlineInterceptor()
    {
        return chain -> {
            Request request = chain.request();
            if (!Helper.isNetworkOk())
            {
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxStale(7,TimeUnit.DAYS)
                        .build();

                request = request.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .cacheControl(cacheControl)
                        .build();
            }
            return  chain.proceed(request);
        };
    }

    public static synchronized ApiClient getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new ApiClient();
        }
        return mInstance;
    }

    public Api getApi()
    {
        return retrofit.create(Api.class);
    }

}
