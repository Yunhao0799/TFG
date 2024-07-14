package com.yunhao.fakenewsdetector.data.network

import com.yunhao.fakenewsdetector.data.model.CreateUserDTO
import com.yunhao.fakenewsdetector.data.model.LoginResponseDTO
import com.yunhao.fakenewsdetector.data.model.LoginUserDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

interface ApiInterface {
    @GET("backend/getCsrf/")
    fun getCsrf() : Call<Void>

    @POST("api/users/")
    fun createUser(@Body userInfo: CreateUserDTO) : Call<CreateUserDTO>

    @POST("api/login/")
    fun login(@Body userInfo: LoginUserDTO) : Call<LoginResponseDTO>
}