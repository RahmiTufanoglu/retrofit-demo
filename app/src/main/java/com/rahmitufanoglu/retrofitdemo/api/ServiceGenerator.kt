package com.rahmitufanoglu.retrofitdemo.api


import com.rahmitufanoglu.retrofitdemo.Constants

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ServiceGenerator {

    private var retrofit: Retrofit? = null

    private fun getClient(baseUrl: String): Retrofit? {
        if (retrofit == null) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
        }
        return retrofit
    }

    @JvmStatic fun <S> createService(serviceClass: Class<S>): S {
        return getClient(Constants.BASE_URL)!!.create(serviceClass)
    }

}
