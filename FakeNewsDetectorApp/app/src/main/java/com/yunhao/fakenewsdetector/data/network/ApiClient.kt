package com.yunhao.fakenewsdetector.data.network

import android.net.TrafficStats
import com.yunhao.fakenewsdetector.utils.PreferencesManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // private const val BASE_URL = "http://10.0.2.2:8000/" // only for emulator
    private const val BASE_URL = "https://sharing-cool-sole.ngrok-free.app/"

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
            TrafficStats.setThreadStatsTag(10000)
            try{
                chain.proceed(request.build())
            }
            finally {
                TrafficStats.clearThreadStatsTag()
            }
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(csrfInterceptor)
            .build()


        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiInterface::class.java)
    }
}