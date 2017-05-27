package com.rahmitufanoglu.retrofitdemo.di;


import com.rahmitufanoglu.retrofitdemo.BuildConfig;
import com.rahmitufanoglu.retrofitdemo.Constants2;
import com.rahmitufanoglu.retrofitdemo.api.GitHubService2;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class AppModule {

    @Provides
    OkHttpClient provideLoggingCapableHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

    @Provides
    static Retrofit provideClient(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(Constants2.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    @Provides
    GitHubService2 provideGithubService(Retrofit retrofit) {
        return retrofit.create(GitHubService2.class);
    }

    /*public static <S> S createService(Class<S> serviceClass) {
        return provideClient(Constants2.BASE_URL).create(serviceClass);
    }*/
}
