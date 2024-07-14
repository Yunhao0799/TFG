package com.yunhao.fakenewsdetector.data.network

import com.yunhao.fakenewsdetector.utils.PreferencesManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:5001/"

    val instance by lazy {
        val csrfInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
            val csrfToken = PreferencesManager.default.get(PreferencesManager.Properties.CSRF_TOKEN, "")
            if (csrfToken != ""){
                request.addHeader("X-CSRFToken", csrfToken)
            }
//            ?.let {
//                request.addHeader("X-CSRFToken", it)
//            }
            chain.proceed(request.build())
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(csrfInterceptor)
            .addInterceptor(CsrfInterceptor())
            .build()


        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiInterface::class.java)
    }
}