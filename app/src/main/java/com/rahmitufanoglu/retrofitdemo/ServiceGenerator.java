package com.rahmitufanoglu.retrofitdemo;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceGenerator {

    private static final String BASE_URL = "https://api.github.com/";

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    // logging for knowing which data is being send and received by Retrofit
    private static HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private ServiceGenerator() {}

    public static <S> S createService(Class<S> serviceClass) {
        // check if the logging interceptor is already present
        if (!okHttpClient.interceptors().contains(httpLoggingInterceptor)) {
            okHttpClient.newBuilder().addInterceptor(httpLoggingInterceptor);
            retrofit.newBuilder().client(okHttpClient).build();
        }
        return retrofit.create(serviceClass);
    }
}
