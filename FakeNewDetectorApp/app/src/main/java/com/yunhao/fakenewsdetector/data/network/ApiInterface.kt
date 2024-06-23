package com.yunhao.fakenewsdetector.data.network

import com.yunhao.fakenewsdetector.data.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Body

interface ApiInterface {
    @GET("backend/getCsrf/")
    fun getCsrf() : Call<Void>

    @POST("backend/createUser/")
    fun createUser(@Body userInfo: User) : Call<User>
}