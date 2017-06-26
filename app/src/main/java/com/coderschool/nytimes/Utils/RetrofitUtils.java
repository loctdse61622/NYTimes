package com.coderschool.nytimes.Utils;

import com.coderschool.nytimes.Model.ApiResponse;
import com.coderschool.nytimes.BuildConfig;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Admin on 6/21/2017.
 */

public class RetrofitUtils {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final Gson GSON = new Gson();
    private static final String BASE_URL = "http://api.nytimes.com/svc/search/v2/";

    public static Retrofit get(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static OkHttpClient client(){
        return new OkHttpClient.Builder()
                .addInterceptor(apiKeyInterceptor())
                .addInterceptor(responseInterceptor())
                .build();
    }

    private static Interceptor responseInterceptor() {
        return chain -> {
            Request request = chain.request();
            Response response = chain.proceed(request);
            ResponseBody body = response.body();
            ApiResponse apiResponse = GSON.fromJson(body.string(), ApiResponse.class);
            body.close();
            response = response.newBuilder()
                    .body(ResponseBody.create(JSON, GSON.toJson(apiResponse.getResponse())))
                    .build();
            return response;
        };
    }


    private static Interceptor apiKeyInterceptor() {
        return chain -> {
            Request request = chain.request();
            HttpUrl url = request.url()
                    .newBuilder()
                    .addQueryParameter("api_key", BuildConfig.API_KEY)
                    .build();
            request = request.newBuilder()
                    .url(url)
                    .build();
            return chain.proceed(request);
        };
    }
}
